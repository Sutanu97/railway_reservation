package manager.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.TicketBookingDao;
import dto.ReservedTicketDto;
import dto.Traveller;
import dto.TravellerSeatBookingDto;
import entity.TicketBookingHistory;
import entity.Train;
import entity.User;
import jakarta.persistence.Query;
import manager.TicketBookingManager;
import manager.TrainsManager;
import util.Constants;
import util.TicketBookingUtil;
import util.TrainsUtil;

@Service
public class TicketBookingManagerImpl implements TicketBookingManager{

	@Autowired
	private TicketBookingDao ticketBookingDao;
	
	@Autowired
	private TrainsManager trainsManager;

	@Override
	public Integer generatePNR() {
		return ticketBookingDao.generatePNR();
	}

	public TicketBookingDao getTicketBookingDao() {
		return ticketBookingDao;
	}

	public void setTicketBookingDao(TicketBookingDao ticketBookingDao) {
		this.ticketBookingDao = ticketBookingDao;
	}

	@Override
	public Map<String, Object> bookTicket(User user, Train train, String fromStation, String toStation,
			String trainClass, String travelTime, String departureTime, String arrivalTime, LocalDate dateOfJourney, List<Traveller> travellers, List<String> stationsList, String recipientEmailId, Double fare) {
		Map<String, Object> bookingStatusMap = null;
//		LocalDate dateOfJourney = LocalDate.of(Integer.parseInt(doj.split("/")[2]), Integer.parseInt(doj.split("/")[1]), Integer.parseInt(doj.split("/")[0]));
		Map<String, Integer> stnVsDayMap = trainsManager.fetchDayForStations(train.getNumber());
		LocalDate trainDepartureDate = TrainsUtil.getTrainDepartureDate(fromStation, dateOfJourney, stnVsDayMap);
		LocalDate arrivalDate = TrainsUtil.getTrainArrivalDate(fromStation, toStation, dateOfJourney, stnVsDayMap);
		LocalTime travelTimeLT = TrainsUtil.getTimeInLocalTime(travelTime);
		LocalTime departureTimeLT =  TrainsUtil.getLocalTimeFromString(departureTime);
		LocalTime arrivalTimeLT =  TrainsUtil.getLocalTimeFromString(arrivalTime);
		List<TicketBookingHistory> seatOccupanyList =  getOccupiedSeatsforDateTrainAndClass(train, trainClass, trainDepartureDate);
		synchronized(this) {
			//check if the no. of travellers > no. of seats available. If yes, then inform via a new jsp
			if(travellers.size() > trainsManager.noOfTicketsAvlForDateTrainAndClass(fromStation, toStation, dateOfJourney, train.getNumber(), trainClass)){
				bookingStatusMap = new HashMap<>();
				bookingStatusMap.put(Constants.TRAVELLER_COUNT_EXCEEDED_LIMIT, true);
				return bookingStatusMap;
			}
			List<TravellerSeatBookingDto> seatBookingDtoList = getConfirmedSeats(travellers, train, trainClass, fromStation, toStation, dateOfJourney,seatOccupanyList,stationsList);
			bookingStatusMap = saveOccupiedSeats(user, train, trainDepartureDate, dateOfJourney, departureTimeLT, arrivalDate, arrivalTimeLT, trainClass, fromStation, toStation, travelTimeLT, seatBookingDtoList, travellers, recipientEmailId, fare);
			updateTicketAvailability(fromStation, toStation, dateOfJourney, train, trainClass, seatBookingDtoList.size(), stationsList, false);
		}
		return bookingStatusMap;
	}

	@Override
	public void updateTicketAvailability(String fromStation, String toStation, LocalDate dateOfJourney, Train train, String trainClass, int seatsOccupied, List<String> stationsList, boolean isCancellation) {
		this.ticketBookingDao.updateTicketAvailability(fromStation, toStation, dateOfJourney, train, trainClass, seatsOccupied, stationsList, isCancellation);
		
	}

	private Map<String, Object> saveOccupiedSeats(User user, Train train, LocalDate trainDepartureDate, LocalDate dateOfJourney, LocalTime departureTimeLT, LocalDate arrivalDate, LocalTime arrivalTimeLT, String trainClass, String fromStation, String toStation, LocalTime travelTimeLT, List<TravellerSeatBookingDto> seatBookingDtoList, List<Traveller> travellers, String recipientEmailId, Double fare) {
		List<TicketBookingHistory> seatOccupancies = new ArrayList<>();
		Integer pnr = this.ticketBookingDao.generatePNR();
		Map<Traveller, String> travellerVsBerthMap = new HashMap<>();
		for(TravellerSeatBookingDto dto : seatBookingDtoList) {
			TicketBookingHistory seatOccupancy = new TicketBookingHistory();
			seatOccupancies.add(seatOccupancy);
			seatOccupancy.setPnrNumber(pnr);
			seatOccupancy.setUser(user);
			seatOccupancy.setTrainDepartureDate(trainDepartureDate);
			seatOccupancy.setTrain(train);
			seatOccupancy.setArrivalDate(arrivalDate);
			seatOccupancy.setArrivalTime(arrivalTimeLT);
			seatOccupancy.setDateOfJourney(dateOfJourney);
			seatOccupancy.setDepartureTime(departureTimeLT);
			seatOccupancy.setSourceStation(fromStation);
			seatOccupancy.setDestinationStation(toStation);
			seatOccupancy.setTravelTime(travelTimeLT);
			seatOccupancy.setTrainClass(trainClass);
			seatOccupancy.setFare(fare);
			seatOccupancy.setConfirmationEmailId(recipientEmailId);
			seatOccupancy.setCoach(dto.getConfirmationStatus().split("-")[0]);
			seatOccupancy.setIsCancelled(false);
			seatOccupancy.setSeatNumber(Integer.parseInt(dto.getConfirmationStatus().split("-")[1]));
			ObjectMapper mapper = new ObjectMapper();
			try {
				seatOccupancy.setTravellerDetails(mapper.writeValueAsString(dto.getTraveller()));
			} catch (JsonProcessingException e) {
				System.out.println("Exception caught while setting traveller details : "+e.fillInStackTrace());
			}
			travellerVsBerthMap.put(dto.getTraveller(), TicketBookingUtil.getSeatKey(seatOccupancy.getCoach(), seatOccupancy.getSeatNumber()));
		}
		this.ticketBookingDao.saveOccupiedSeats(seatOccupancies);
		Map<String, Object> bookingStatusMap = new HashMap<>();
		bookingStatusMap.put("pnr",pnr);
		bookingStatusMap.put("travellerVsBerth", travellerVsBerthMap);
		return bookingStatusMap;
	}

	private List<TravellerSeatBookingDto> getConfirmedSeats(List<Traveller> travellers, Train train, String trainClass,
			String fromStation, String toStation, LocalDate dateOfJourney, List<TicketBookingHistory> seatOccupanyList, List<String> stationsList) {
		List<TravellerSeatBookingDto> seatBookingDtoList = null;
		seatBookingDtoList = TicketBookingUtil.getConfirmedSeats(travellers, train, trainClass, fromStation, toStation, dateOfJourney, seatOccupanyList, stationsList);
		return seatBookingDtoList;
	}

	public List<TicketBookingHistory> getOccupiedSeatsforDateTrainAndClass(Train train, String trainClass,
			LocalDate trainDepartureDate) {
		return this.ticketBookingDao.getOccupiedSeatsforDateTrainAndClass(train, trainClass, trainDepartureDate);
	}

	@Override
	public Map<String, List<ReservedTicketDto>> fetchBookingsForUser(User loggedInUser) {
		Map<String, List<TicketBookingHistory>> bookedTickets = this.ticketBookingDao.fetchBookingsForUser(loggedInUser);
		Map<String, List<ReservedTicketDto>> journeyTypeVsBookingList = new HashMap<>();
		journeyTypeVsBookingList.put(Constants.PAST_JOURNEYS, new ArrayList<>());
		journeyTypeVsBookingList.put(Constants.UPCOMING_JOURNEYS, new ArrayList<>());
		journeyTypeVsBookingList.put(Constants.CANCELLED_BOOKINGS, new ArrayList<>());
		List<ReservedTicketDto> reservedTicketsGroupedByPnr = TrainsUtil.groupBookedTicketsByPnr(bookedTickets.get(Constants.ACTIVE_BOOKINGS));
		reservedTicketsGroupedByPnr.forEach(ticket ->{
				if(ticket.getDateOfJourney().isAfter(LocalDate.now())) {
					List<ReservedTicketDto> dtos = journeyTypeVsBookingList.get(Constants.UPCOMING_JOURNEYS);
					dtos.add(ticket);
					journeyTypeVsBookingList.put(Constants.UPCOMING_JOURNEYS, dtos);
				}else if(ticket.getDateOfJourney().isBefore(LocalDate.now())) {
					List<ReservedTicketDto> dtos = journeyTypeVsBookingList.get(Constants.PAST_JOURNEYS);
					dtos.add(ticket);
					journeyTypeVsBookingList.put(Constants.PAST_JOURNEYS, dtos);
				}
			
		});
		reservedTicketsGroupedByPnr = TrainsUtil.groupBookedTicketsByPnr(bookedTickets.get(Constants.CANCELLED_BOOKINGS));
		reservedTicketsGroupedByPnr.forEach(ticket ->{
				if(ticket.getDateOfJourney().isAfter(LocalDate.now())) {
					List<ReservedTicketDto> dtos = journeyTypeVsBookingList.get(Constants.CANCELLED_BOOKINGS);
					dtos.add(ticket);
					journeyTypeVsBookingList.put(Constants.CANCELLED_BOOKINGS, dtos);
				}
		});
		return journeyTypeVsBookingList;
	}

	@Override
	public boolean cancelTicket(Integer pnr, String[] travellerArr) {
		List<String> jsonConvertedStringArr = new ArrayList<>();  
		System.out.println(travellerArr);
		Arrays.stream(travellerArr).forEach(t ->{
			System.out.println("t"+t);
			String name = t.substring(0, t.indexOf("("));
			
System.out.println(name);
			String rem = t.substring(t.indexOf("(")+1, t.length()-1);
			System.out.println("t"+t);
			String gender = rem.split(",")[0];
			int age = Integer.parseInt(rem.split(",")[1].strip());
			Traveller traveller = new Traveller(name, age, String.valueOf(gender));
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				jsonConvertedStringArr.add(objectMapper.writeValueAsString(traveller));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
		return ticketBookingDao.cancelTicket(pnr, jsonConvertedStringArr);
	}

	@Override
	public String getRecipientEmail(int pnr) {
		return ticketBookingDao.getRecipientEmail(pnr);
	}

	@Override
	public List<TicketBookingHistory> getBookingFromPnr(Integer pnr) {
		return ticketBookingDao.getBookingFromPnr(pnr);
	}

}
