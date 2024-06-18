package dao.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.TicketBookingDao;
import entity.TicketBookingHistory;
import entity.Train;
import entity.User;
import manager.TrainsManager;
import util.Constants;
import util.TicketBookingUtil;
import util.TrainsUtil;

@Repository
@Transactional
public class TicketBookingDaoImpl implements TicketBookingDao{

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private TrainsManager trainsManager;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	@Transactional
	public Integer generatePNR() {
		String query = "select max(pnrNumber) from TicketBookingHistory";
		List<Object> list = (List<Object>) hibernateTemplate.find(query);
		Integer maxPk = (Integer)list.get(0);
		if(maxPk == null)
			return 1;
		return maxPk + 1;
	}

	@Override
	@Transactional
	public Map<Boolean, Object> bookTicket(TicketBookingHistory ticketBookingHistory) {
		hibernateTemplate.saveOrUpdate(ticketBookingHistory);
		getHibernateSession().flush();
		Integer pnr = ticketBookingHistory.getPnrNumber();
		Map<Boolean, Object> map = new HashMap<>();
		map.put(true, pnr);
		return map;
	}

	@Override
	@Transactional
	public List<TicketBookingHistory> getOccupiedSeatsforDateTrainAndClass(Train train, String trainClass,
			LocalDate trainDepartureDate) {
		String query = "from TicketBookingHistory where train.number = :trainNumber and trainClass = :trainClass and trainDepartureDate = :trainDepartureDate and isCancelled = 0";
		Query<TicketBookingHistory> hql = getHibernateSession().createQuery(query);
		hql.setParameter("trainNumber", train.getNumber());
		hql.setParameter("trainClass", trainClass);
		hql.setParameter("trainDepartureDate", trainDepartureDate);
		List<TicketBookingHistory> seatOccupanyList = hql.list();
		return seatOccupanyList;
	}

	@Override
	@Transactional
	public void saveOccupiedSeats(List<TicketBookingHistory> seatOccupancies) {
		seatOccupancies.forEach(occ ->{
			this.hibernateTemplate.persist(occ);
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void updateTicketAvailability(String fromStation, String toStation, LocalDate dateOfJourney, Train train, String trainClass,
			int seatsOccupied, List<String> stationsList, boolean isCancellation) {

		int qCount = 0;
		StringBuffer querySb = new StringBuffer();
		if(isCancellation)
			querySb = new StringBuffer("update TicketAvailability ta\n"
				+ "set ticketsAvailable = ticketsAvailable + ?"+qCount+++"\n"
				+ "WHERE ta.train.number = ?"+qCount+++"\n"
				+ "and category= ?"+qCount+++" and (\n");
		else
			querySb = new StringBuffer("update TicketAvailability ta\n"
				+ "set ticketsAvailable = ticketsAvailable - ?"+qCount+++"\n"
				+ "WHERE ta.train.number = ?"+qCount+++"\n"
				+ "and category= ?"+qCount+++" and (\n");
		
		List<String> sourceToTargetStationsList = TicketBookingUtil.getStationsForTicketAvailabilityReduction(fromStation, toStation, stationsList); 
		Map<String, Integer> stationVsDayMap = trainsManager.fetchDayForStations(train.getNumber());
		Map<String, String> fromToStationVsFromToDateMap = new HashMap<>();
		LocalDate trainDepartureDate = TrainsUtil.getTrainDepartureDate(fromStation, dateOfJourney, stationVsDayMap);
		for(String fromToStation : sourceToTargetStationsList) {
			if(sourceToTargetStationsList.get(0).equals(fromToStation))
				querySb.append(" sourceStation = ?"+qCount+++" and targetStation = ?"+qCount+++" and departureDate = ?"+qCount+++" and arrivalDate = ?"+qCount+++"\n");
			else
				querySb.append(" or sourceStation = ?"+qCount+++" and targetStation = ?"+qCount+++" and departureDate = ?"+qCount+++" and arrivalDate = ?"+qCount+++"\n");
		}
		
		for(String fromToStn : sourceToTargetStationsList) {
			String pointA = fromToStn.split("=>")[0];
			String pointB = fromToStn.split("=>")[1];
			Integer dayOfPointA = stationVsDayMap.get(pointA);
			Integer dayOfPointB = stationVsDayMap.get(pointB);
			LocalDate dateOfPointA = trainDepartureDate.plusDays(dayOfPointA -1 );
			LocalDate dateOfPointB = trainDepartureDate.plusDays(dayOfPointB -1 );
			fromToStationVsFromToDateMap.put(TrainsUtil.getfromStnToStnKey(pointA, pointB), TrainsUtil.getfromToDateKey(dateOfPointA, dateOfPointB));
		}
		
		querySb.append(")");
		String query = querySb.toString();
		int c = 0;
		try {
			Query hql = getHibernateSession().createQuery(query);
			hql.setParameter(c++, seatsOccupied);
			hql.setParameter(c++, train.getNumber());
			hql.setParameter(c++, trainClass);
			for(String fromToStations : fromToStationVsFromToDateMap.keySet()) {
				hql.setParameter(c++, fromToStations.split("=>")[0]);
				hql.setParameter(c++, fromToStations.split("=>")[1]);
				hql.setParameter(c++, LocalDate.parse(fromToStationVsFromToDateMap.get(fromToStations).split("=>")[0]));
				hql.setParameter(c++, LocalDate.parse(fromToStationVsFromToDateMap.get(fromToStations).split("=>")[1]));
			}
			hql.executeUpdate();
//			getHibernateSession().flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Session getHibernateSession() {
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	}

	@Override
	public Map<String, List<TicketBookingHistory>> fetchBookingsForUser(User loggedInUser) {
		String query = "from TicketBookingHistory where user = :user order by isCancelled, pnrNumber";
		Map<String, List<TicketBookingHistory>> typeVsbookingsMap = new HashMap<>();
		typeVsbookingsMap.put(Constants.CANCELLED_BOOKINGS, new ArrayList<>());
		typeVsbookingsMap.put(Constants.ACTIVE_BOOKINGS, new ArrayList<>());
		Query hql = getHibernateSession().createQuery(query);
		hql.setParameter("user", loggedInUser);
		List<TicketBookingHistory> bookingHistoryList = hql.list();
		bookingHistoryList.forEach(e ->{
			if(e.getIsCancelled()) {
				List<TicketBookingHistory> list = typeVsbookingsMap.get(Constants.CANCELLED_BOOKINGS);
				list.add(e);
				typeVsbookingsMap.put(Constants.CANCELLED_BOOKINGS,list);
			}else {
				List<TicketBookingHistory> list = typeVsbookingsMap.get(Constants.ACTIVE_BOOKINGS);
				list.add(e);
				typeVsbookingsMap.put(Constants.ACTIVE_BOOKINGS,list);
			}
		});
		return typeVsbookingsMap;
	}

	@Override
	@Transactional
	public boolean cancelTicket(Integer pnr, List<String> travellerArr) {
		StringBuilder query = new StringBuilder("update ticket_booking_history\n"
				+ "set is_cancelled = 1\n"
				+ "where pnr_number = ?0 and\n");
		int q=1;
		
		try {
			
			for(int i=0;i<travellerArr.size();i++) {
				String jsonContainsQuery = "JSON_CONTAINS(traveller_details, ?"+q+++") \n";	
				query.append(jsonContainsQuery);
				if(i != travellerArr.size()-1 )
					query.append("or \n");
			}
			query.append(";");
			int c = 0;
			Query hql = getHibernateSession().createNativeQuery(query.toString());
			hql.setParameter(c++, pnr);
			for(String traveller : travellerArr) {
				hql.setParameter(c++, traveller);
			}
			hql.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getRecipientEmail(int pnr) {
		String toEmail = null;
		String query = "select confirmationEmailId from TicketBookingHistory where pnrNumber = :pnr";
		Query hql = getHibernateSession().createQuery(query);
		hql.setParameter("pnr", pnr);
		List<String> rs = hql.list();
		toEmail = rs.get(0);
		return toEmail;
	}

	@Override
	public List<TicketBookingHistory> getBookingFromPnr(Integer pnr) {
		String query = "from TicketBookingHistory where pnrNumber= :pnr and isCancelled = :isCancelled ";
		Query hql = getHibernateSession().createQuery(query);
		hql.setParameter("pnr", pnr);
		hql.setParameter("isCancelled", false);
		return hql.list();
	}
	
}
