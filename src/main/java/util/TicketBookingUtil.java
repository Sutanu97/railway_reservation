package util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dto.ReservedTicketDto;
import dto.Traveller;
import dto.TravellerSeatBookingDto;
import entity.TicketBookingHistory;
import entity.Train;

public class TicketBookingUtil {

	public static List<TravellerSeatBookingDto> getConfirmedSeats(List<Traveller> travellers, Train train,
			String trainClass, String fromStation, String toStation, LocalDate dateOfJourney, List<TicketBookingHistory> seatOccupanyList, List<String> stationsList) {
		List<String> selectedSeats = new ArrayList<>();
		String shortname = getShortname(trainClass);
		if(seatOccupanyList.isEmpty()) {
			for(int i=0;i<travellers.size();i++) {
				selectedSeats.add(shortname+"-"+(i+1));
			}
		}else {
			List<String> possibleSeats = new ArrayList<>();
			int maxSeatNoTraversed = -1;
			Set<String> rejectedSeats = new HashSet<>();
			for(TicketBookingHistory occupancy : seatOccupanyList) {
				if(rejectedSeats.contains(getSeatKey(occupancy.getCoach(), occupancy.getSeatNumber())))
					continue;
				maxSeatNoTraversed = occupancy.getSeatNumber() > maxSeatNoTraversed ? occupancy.getSeatNumber() : maxSeatNoTraversed; 
				String occupiedSource = occupancy.getSourceStation();
				String occupiedDestination = occupancy.getDestinationStation();
				if(stationsList.indexOf(occupiedDestination) <= stationsList.indexOf(fromStation) || 
						stationsList.indexOf(occupiedSource) >= stationsList.indexOf(toStation)) {
					possibleSeats.add(getSeatKey(occupancy.getCoach(), occupancy.getSeatNumber()));
				}else {
					rejectedSeats.add(getSeatKey(occupancy.getCoach(), occupancy.getSeatNumber()));
				}
				
			}
			List<String> cancelledSeats = new ArrayList<>();
			for(int i=1;i<maxSeatNoTraversed;i++) {
				if(!possibleSeats.contains((shortname + "-" + i)) && 
						!rejectedSeats.contains((shortname + "-" + i))){
					cancelledSeats.add((shortname + "-" + i));
				}
			}
			possibleSeats.addAll(cancelledSeats);
			
			int maxSeatNumber = getMaxSeatingCapacity(trainClass);
			for(int i=maxSeatNoTraversed+1; i<=maxSeatNumber; i++) {
				possibleSeats.add(shortname+"-"+i);
			}
			
			selectedSeats = getSuitableSeats(shortname,travellers.size(),possibleSeats);
			
		}
		return prepareSeatBookingDTOs(travellers, selectedSeats);
	}

	private static List<String> getSuitableSeats(String shortname, Integer countOfTravellers, List<String> possibleSeats) {
		List<String> selectedSeats = null;
		List<Integer> seatNumbers = possibleSeats.stream().map(e -> Integer.parseInt(e.split("-")[1])).toList();
		int currentStreak = 1;
		int start = 0;
		int end = 0;
		for(int i=1;i<seatNumbers.size();i++) {
			if(seatNumbers.get(i) == seatNumbers.get(i-1)+1) {
				currentStreak++;
				if(currentStreak >= countOfTravellers) {
					selectedSeats = new ArrayList<>();
					for(int j=start; j <= i; j++) {
						selectedSeats.add(shortname + "-" + seatNumbers.get(j));
					}
					break;
				}
			}else {
				end = i-1;
				currentStreak = 1;
				start = i;
			}
		}
		//handling the scenario where no consecutive seat could be found for the passenger count
		if(selectedSeats == null) {
			selectedSeats = new ArrayList<>();
			for(int i=0;i<seatNumbers.size();i++) {
				selectedSeats.add(getSeatKey(shortname, seatNumbers.get(i)));
			}
		}
		return selectedSeats;
	}

	private static String getShortname(String trainClass) {
		String classShortname = null;
		switch(trainClass) {
		case Constants.FIRST_AC : 
			classShortname = Constants.FIRST_AC_SHORTNAME; 
			break;
		case Constants.SECOND_AC : 
			classShortname = Constants.SECOND_AC_SHORTNAME;
			break;
		case Constants.THIRD_AC : 
			classShortname = Constants.THIRD_AC_SHORTNAME;
			break;
		case Constants.SLEEPER : 
			classShortname = Constants.SLEEPER_SHORTNAME;
			break;
		case Constants.AC_CHAIR_CAR : 
			classShortname = Constants.AC_CHAIR_CAR_SHORTNAME;
			break;
		case Constants.EXECUTIVE_CHAIR_CAR : 
			classShortname = Constants.EXECUTIVE_CHAIR_CAR_SHORTNAME;
			break;	
		}
		return classShortname;
	}
	
	private static Integer getMaxSeatingCapacity(String trainClass) {
		Integer maxSeatNumber = null;
		switch(trainClass) {
		case Constants.FIRST_AC : 
			maxSeatNumber = Constants.H_MAX;
			break;
		case Constants.SECOND_AC : 
			maxSeatNumber = Constants.A_MAX;
			break;
		case Constants.THIRD_AC : 
			maxSeatNumber = Constants.B_MAX;
			break;
		case Constants.SLEEPER : 
			maxSeatNumber = Constants.S_MAX;
			break;
		case Constants.AC_CHAIR_CAR :
			maxSeatNumber = Constants.C_MAX;
			break;
		case Constants.EXECUTIVE_CHAIR_CAR :
			maxSeatNumber = Constants.E_MAX;
			break;	
		}
		return maxSeatNumber;
	}

	private static List<TravellerSeatBookingDto> prepareSeatBookingDTOs(List<Traveller> travellers,
			List<String> seatList) {
		List<TravellerSeatBookingDto> list = new ArrayList<>();
		int count = 0;
		for(int i=0;i<travellers.size();i++) { 
			TravellerSeatBookingDto dto = new TravellerSeatBookingDto();
			dto.setTraveller(travellers.get(i));
			dto.setConfirmationStatus(seatList.get(i));
			list.add(dto);
		}
		return list;
	}

	public static List<String> getStationsForTicketAvailabilityReduction(String fromStation, String toStation, List<String> stationsList) {
		List<String> list = new ArrayList<>();
		int start = stationsList.indexOf(fromStation);
		int end = stationsList.indexOf(toStation);
		for(int i=0;i<end;i++) {
			for(int j = start+1; j<=stationsList.size() -1; j++) {
				if(i < j)
					list.add(stationsList.get(i) + "=>" + stationsList.get(j));
			}
		}
		return list;
	}
	
	public static String getSeatKey(String coach, Integer seatNumber) {
		return coach+"-"+seatNumber;
	}

	public static ReservedTicketDto getReservedTicketDto(TicketBookingHistory ticket) {
		ReservedTicketDto reservedTicketDto = new ReservedTicketDto();
		reservedTicketDto.setPnr(ticket.getPnrNumber());
		reservedTicketDto.setFromStation(ticket.getSourceStation());
		reservedTicketDto.setToStation(ticket.getDestinationStation());
		reservedTicketDto.setTrainNumber(ticket.getTrain().getNumber());
		reservedTicketDto.setTrainName(ticket.getTrain().getName());
		reservedTicketDto.setTrainClass(ticket.getTrainClass());
		reservedTicketDto.setDateOfJourney(ticket.getDateOfJourney());
		reservedTicketDto.setFormattedDepartureDate(TrainInfoDisplay.getFormattedDate(ticket.getDateOfJourney(), "MMMM dd") + " " + 
				TrainInfoDisplay.getDayFromDate(ticket.getDateOfJourney()));
		reservedTicketDto.setFormattedArrivalDate(TrainInfoDisplay.getFormattedDate(ticket.getArrivalDate(), "MMMM dd") + " " + 
				TrainInfoDisplay.getDayFromDate(ticket.getArrivalDate()));
		reservedTicketDto.setDepartureTime(TrainInfoDisplay.getFormattedTime(ticket.getDepartureTime()));
		reservedTicketDto.setArrivalTime(TrainInfoDisplay.getFormattedTime(ticket.getArrivalTime()));
		reservedTicketDto.setTravelTime(TrainsUtil.getTimeFromLocalTime(ticket.getTravelTime()));
		reservedTicketDto.setIsCancelled(ticket.getIsCancelled());
		return reservedTicketDto;
	}

}
