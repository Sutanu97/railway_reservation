package util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ReservedTicketDto;
import dto.Traveller;
import entity.TicketBookingHistory;

public class TrainsUtil {
	public static LocalDate getTrainDepartureDate(String fromStation, LocalDate dateOfJourney, Map<String, Integer> stationVsDay) {
		return dateOfJourney.minusDays(stationVsDay.get(fromStation) - 1);
	}
	
	public static String getfromToDateKey(LocalDate fromDate, LocalDate toDate) {
		return fromDate.toString()+"=>"+toDate.toString();
	}
	
	public static String getfromStnToStnKey(String fromStn, String toStn) {
		return fromStn+"=>"+toStn;
	}

	public static LocalDate getTrainArrivalDate(String fromStation, String toStation, LocalDate dateOfJourney,
			Map<String, Integer> stnVsDayMap) {
		Integer sourceDay = stnVsDayMap.get(fromStation);
		Integer targetDay = stnVsDayMap.get(toStation);
		return dateOfJourney.plusDays(targetDay-sourceDay);
	}

	public static LocalTime getTimeInLocalTime(String travelTime) {
		travelTime = travelTime.trim();
		int hrs = Integer.parseInt(travelTime.substring(0, travelTime.indexOf("h")));
		int mins = Integer.parseInt(travelTime.substring(travelTime.indexOf(" ")+1, travelTime.indexOf("m")));
		LocalTime localTime = LocalTime.of(hrs, mins);
		return localTime;
	}

	public static List<ReservedTicketDto> groupBookedTicketsByPnr(
			List<TicketBookingHistory> bookedTickets) {
		List<ReservedTicketDto> reservedTicketsGroupedbyPnr = new ArrayList<>();
		int start = 0, end = 0;
		for(int i=1;i<bookedTickets.size();i++) {
			if(bookedTickets.get(i).getPnrNumber() != bookedTickets.get(i-1).getPnrNumber()) {
				end = i-1;
				reservedTicketsGroupedbyPnr.add(TrainsUtil.getGroupedByPnrTicket(bookedTickets.subList(start, end+1)));
				start = i;
			}
		}
		if(bookedTickets.size() > 0)
			reservedTicketsGroupedbyPnr.add(TrainsUtil.getGroupedByPnrTicket(bookedTickets.subList(start, bookedTickets.size())));
		return reservedTicketsGroupedbyPnr;
	}

	private static ReservedTicketDto getGroupedByPnrTicket(List<TicketBookingHistory> bookedTickets) {
		ReservedTicketDto groupedByPnrTicket = TicketBookingUtil.getReservedTicketDto(bookedTickets.get(0));
		Map<Traveller, String> travellerVsBerth = new HashMap<>();
		bookedTickets.forEach(ticket ->{
			travellerVsBerth.put(TrainsUtil.getTravellerFromString(ticket.getTravellerDetails()), TicketBookingUtil.getSeatKey(ticket.getCoach(), ticket.getSeatNumber()));
		});
		groupedByPnrTicket.setTravellerVsBerth(travellerVsBerth);
		return groupedByPnrTicket;
	}

	private static Traveller getTravellerFromString(String travellerDetails) {
		ObjectMapper mapper = new ObjectMapper();
		Traveller traveller = null;
		try {
			traveller = mapper.readValue(travellerDetails, Traveller.class);
			return traveller;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return traveller;
	}

	public static LocalDate getLocalDateFromString(String doj) {
		LocalDate date = LocalDate.of(Integer.parseInt(doj.split("-")[0]), Integer.parseInt(doj.split("-")[1]), Integer.parseInt(doj.split("-")[2]));
		return date;
	}
	
	public static String getTimeFromLocalTime(LocalTime lt) {
		StringBuilder time = new StringBuilder();
		time.append(lt.getHour())
		.append("h")
		.append(" ")
		.append(lt.getMinute())
		.append("m");
		return time.toString();
		
	}
	
	public static LocalTime getLocalTimeFromString(String time) {
		DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("hh:mm a");
		LocalTime lt = LocalTime.parse(time, formatter);
		return lt;
	}
	
}
