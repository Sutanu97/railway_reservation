package dao.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.TrainsDao;
import dto.ClassFareDto;
import dto.TrainStationTimingDto;
import entity.Station;
import entity.TicketAvailability;
import entity.Train;
import entity.TrainStationTiming;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import util.Constants;
import util.TrainInfoDisplay;

@Repository
public class TrainsDaoImpl implements TrainsDao{

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Session getSession() {
		return hibernateTemplate.getSessionFactory().openSession();
	}
	
	@Override
	public List<Station> fetchStations() {
		List<Station> stations = hibernateTemplate.loadAll(Station.class);
		return stations;
	}

	@Override
	@Transactional
	public List<Train> fetchTrainsBetweenStations(String fromStation, String toStation, String day) {
		String query = "select * from lu_train\n"
				+ "where train_number in (\n"
				+ "select lstF.fk_train_number from train_station_timing lstF join train_station_timing lstT\n"
				+ "on lstF.fk_train_number = lstT.fk_train_number\n"
				+ "where lstF.station_code = ?\n"
				+ "and lstT.station_code = ?\n"
				+ "and (lstF.day < lstT.day\n"
				+ "or  \n"
				+ "lstF.day = lstT.day and (lstF.arrival_time is null or lstF.arrival_time < lstT.arrival_time)\n"
				+ "));";
		NativeQuery q = getSession().createSQLQuery(query);
		q.setParameter(1, fromStation);
		q.setParameter(2, toStation);
		List<Object[]> listOfTrains = q.list();
		List<Train> trains = new ArrayList<>();
		for(Object[] row : listOfTrains) {
			Train train = new Train();
			try {
				train.setNumber(Integer.parseInt(row[0].toString()));
				train.setName(row[1].toString());
				train.setCategory(row[2].toString());
				train.setAverageSpeed(Integer.parseInt(row[3].toString()));
				train.setMaxSpeed(Integer.parseInt(row[4].toString()));
				train.setIsPremium(Integer.parseInt(row[5].toString()) == 1 ? true : false);
				train.setOperatesOn(row[6].toString());
				if(checkAvailabilityOnDoj(day, train.getOperatesOn()))
					trains.add(train);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return trains;
	}
	
	private static boolean checkAvailabilityOnDoj(String day, String operatesOn) {
		if(operatesOn.contains(day))
			return true;
		return false;
	}

	@Override
	@Transactional
	public List<TrainStationTimingDto> fetchTrainTimings(String sourceStation, String destinationStation) {
		List<TrainStationTimingDto> timingDtos = new ArrayList<>();
		String query = "select fk_train_number as train_number, `day` as day, ifnull(arrival_time, departure_time) as time from train_station_timing\n"
				+ "where station_code in (?,?)\n"
				+ "order by fk_train_number,day, time;";
		NativeQuery q = getSession().createSQLQuery(query);
		q.setParameter(1, sourceStation);
		q.setParameter(2, destinationStation);
		List<Object[]> list = q.getResultList();
		
		if(list != null) {
			for(int i=0;i<list.size()-1;) {
				TrainStationTimingDto dto = new TrainStationTimingDto();
				Object[] timing = list.get(i);
				Object[] timingNext = list.get(i+1);
				int trNo = Integer.parseInt(timing[0].toString().trim());
				int nextTrNo = Integer.parseInt(timingNext[0].toString().trim());
				if(trNo == nextTrNo) {
					dto.setTrainNumber(trNo);
					dto.setSourceDay(Integer.parseInt(timing[1].toString()));
					dto.setDestinationDay(Integer.parseInt(timingNext[1].toString()));
					dto.setSourceStationCode(sourceStation);
					dto.setDestinationStationCode(destinationStation);
					dto.setSourceTime(LocalTime.parse(timing[2].toString()));
					dto.setDestinationTime(LocalTime.parse(timingNext[2].toString()));
					dto.setTravelTime(TrainInfoDisplay.getTravelTime(dto, sourceStation, destinationStation));
					timingDtos.add(dto);
					i+=2;
				}else {
					i++;
				}
				
			}
		}
		return timingDtos;
	}

	@Override
	@Transactional
	public Map<Integer, LinkedHashMap<String, Integer>> fetchNoOfTicketsAvailable(LocalDate doj, String fromStation, String toStation) {

		Map<Integer, LinkedHashMap<String, Integer>> trainVsClassVsAvailablility = new HashMap<>();
		Map<Integer, Map<String, Integer>> trainVsClassVsAvailablilityUnordered = new HashMap<>();
		String query = "from TicketAvailability where departureDate = :date and sourceStation = :sourceStation and targetStation = :targetStation";
		Query<TicketAvailability> hql = getSession().createQuery(query, TicketAvailability.class);
		hql.setParameter("date", doj);
		hql.setParameter("sourceStation", fromStation);
		hql.setParameter("targetStation", toStation);
		List<TicketAvailability> ticketAvailabilityList = hql.list();
		for(TicketAvailability ob : ticketAvailabilityList) {
			Map<String, Integer> classVsAvlMap = null;
			if(trainVsClassVsAvailablilityUnordered.get(ob.getTrain().getNumber()) != null) {
				classVsAvlMap = trainVsClassVsAvailablilityUnordered.get(ob.getTrain().getNumber());
			}else {
				classVsAvlMap  = new HashMap<>();
			}
			classVsAvlMap.put(ob.getCategory(), ob.getTicketsAvailable());
			trainVsClassVsAvailablilityUnordered.put(ob.getTrain().getNumber(), classVsAvlMap);
		}
		for(Integer trno : trainVsClassVsAvailablilityUnordered.keySet()) {
			Map<String, Integer> classVsTickets = trainVsClassVsAvailablilityUnordered.get(trno);
			LinkedHashMap<String, Integer> classVsTicketsOrdered = new LinkedHashMap<>();
			if(classVsTickets.containsKey(Constants.FIRST_AC)) {
				classVsTicketsOrdered.put(Constants.FIRST_AC,classVsTickets.get(Constants.FIRST_AC));
			}
			if(classVsTickets.containsKey(Constants.SECOND_AC)) {
				classVsTicketsOrdered.put(Constants.SECOND_AC,classVsTickets.get(Constants.SECOND_AC));
			}
			if(classVsTickets.containsKey(Constants.THIRD_AC)) {
				classVsTicketsOrdered.put(Constants.THIRD_AC,classVsTickets.get(Constants.THIRD_AC));
			}
			if(classVsTickets.containsKey(Constants.SLEEPER)) {
				classVsTicketsOrdered.put(Constants.SLEEPER,classVsTickets.get(Constants.SLEEPER));
			}
			if(classVsTickets.containsKey(Constants.EXECUTIVE_CHAIR_CAR)) {
				classVsTicketsOrdered.put(Constants.EXECUTIVE_CHAIR_CAR,classVsTickets.get(Constants.EXECUTIVE_CHAIR_CAR));
			}
			if(classVsTickets.containsKey(Constants.AC_CHAIR_CAR)) {
				classVsTicketsOrdered.put(Constants.AC_CHAIR_CAR,classVsTickets.get(Constants.AC_CHAIR_CAR));
			}
			trainVsClassVsAvailablility.put(trno, classVsTicketsOrdered);
		}
		
		return trainVsClassVsAvailablility;
	}

	@Override
	@Transactional
	public ClassFareDto fetchBaseFareBetweenStations(String fromStation, String toStation) {
		String query1 = "select stn.pkStationId from Station stn where \r\n"
				+ "stn.stationCode in (:fromStation,:toStation)";
		
		Query hql = getSession().createQuery(query1);
		hql.setParameter("fromStation", fromStation);
		hql.setParameter("toStation", toStation);
		List<Integer> stations = hql.list();
		
		ClassFareDto dto = fetchBaseFareBetweenStations(stations.get(0), stations.get(1));
		return dto;
	}

	@Override
	@Transactional
	public ClassFareDto fetchBaseFareBetweenStations(Integer station1, Integer station2) {
		ClassFareDto dto = null;
		String query = "select cost.fare from TicketCost cost \r\n"
				+ "where cost.stationId1.pkStationId = :station1 and cost.stationId2.pkStationId = :station2 \r\n"
				+ "or cost.stationId1.pkStationId = :station2 and cost.stationId2.pkStationId = :station1";
		Query hql = getSession().createQuery(query);
		hql.setParameter("station1", station1);
		hql.setParameter("station2", station2);
		List<String> list = hql.list();
		String cost = list.get(0);
		if(cost != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				dto = mapper.readValue(cost, ClassFareDto.class);
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return dto;
	}

	@Override
	@Transactional
	public Train getTrainUsingNumber(Integer trainNo) {
		String q = "from Train where number = :trainNo";
		Query<Train> hql = getSession().createQuery(q);
		hql.setParameter("trainNo", trainNo);
		List<Train> trains = hql.list();
		return trains.get(0);
	}

	@Override
	@Transactional
	public List<String> fetchStationsForTrain(Train train) {
		String q = "select stationCode from TrainStationTiming\n"
				+ "                where fkTrainNumber = :trainNumber\n"
				+ "                order by day, arrivalTime";
		Query query = getSession().createQuery(q);
		query.setParameter("trainNumber", train.getNumber());
		List<String> stations= query.list();
		return stations;
	}

	@Override
	@Transactional
	public LocalDate fetchMinDepartureDateFromTicketAvailability() {
		String q = "select min(departureDate) from TicketAvailability";
		Query hql = getSession().createQuery(q);
		return  LocalDate.parse(hql.list().get(0).toString());
	}

	@Override
	@Transactional
	public Boolean updateDepartureDates(int dayDiff) {
		String q = "update ticket_availability set departure_date = departure_date + interval :interval day";
		entityManager.createNativeQuery(q).setParameter("interval", dayDiff).executeUpdate();
		return true;
	}

	@Override
	@Transactional
	public Boolean updateDepartureAndArrivalDates(int dayDiff) {
		String q = "update ticket_availability set departure_date = departure_date + interval :interval day, arrival_date = arrival_date + interval :interval day";
		entityManager.createNativeQuery(q).setParameter("interval", dayDiff).executeUpdate();
		return true;
	}

	@Override
	@Transactional
	public Boolean updateArrivalDates() {
		String query = "update ticket_availability ta\n"
				+ "join train_station_timing tst\n"
				+ "on ta.fk_train_number = tst.fk_train_number\n"
				+ "set arrival_date = departure_date + interval (\n"
				+ "select tst_arr.day - tst_dep.day\n"
				+ "from train_station_timing tst_arr join \n"
				+ "train_station_timing tst_dep\n"
				+ "on tst_arr.fk_train_number = tst_dep.fk_train_number\n"
				+ "where ta.fk_train_number = tst_arr.fk_train_number \n"
				+ "and ta.fk_train_number = tst_dep.fk_train_number \n"
				+ "and tst_dep.station_code = ta.source_station_code\n"
				+ "and tst_arr.station_code = ta.target_station_code\n"
				+ ")\n"
				+ "day;";
		
		entityManager.createNativeQuery(query).executeUpdate();
		return true;
	}
	
	
	


	@Override
	public LocalDate getArrivalDateFromTicketAvailability(Integer trainNumber, String toStation, LocalDate dateOfJourney) {
		String query = "select arrivalDate from TicketAvailability where train.number =:trainNumber and targetStation =:toStation and departureDate =:departureDate";
		Query hql = getSession().createQuery(query);
		hql.setParameter("trainNumber", trainNumber);
		hql.setParameter("toStation", toStation);
		hql.setParameter("departureDate", dateOfJourney.toString());
		return LocalDate.parse(hql.list().get(0).toString());
	}

	@Override
	public Map<String, Integer> fetchDayForStations(Integer trainNumber) {
		Map<String, Integer> stationVsDay = new HashMap<>();
		String query = "from TrainStationTiming where fkTrainNumber =:trainNumber";
		Query hql = getSession().createQuery(query, TrainStationTiming.class);
		hql.setParameter("trainNumber", trainNumber);
		List<TrainStationTiming> timings = hql.list();
		timings.forEach(timing->{
			stationVsDay.put(timing.getStationCode(), timing.getDay());
		});
		return stationVsDay;
	}

	@Override
	public Integer noOfTicketsAvlForDateTrainAndClass(String sourceStation, String destinationStation, LocalDate doj,
			Integer trainNumber, String trainClass) {
		String query = "select ticketsAvailable from TicketAvailability avl "
				+ "where departureDate = :date and sourceStation = :sourceStation and targetStation = :targetStation "
				+ "and train.number = :trainNumber and category = :category";
		
		Query hql = getSession().createQuery(query);
		hql.setParameter("date", doj);
		hql.setParameter("sourceStation", sourceStation);
		hql.setParameter("targetStation", destinationStation);
		hql.setParameter("trainNumber", trainNumber);
		hql.setParameter("category", trainClass);
		List<Integer> count = hql.list();
		return count.get(0);
		
	}
}
