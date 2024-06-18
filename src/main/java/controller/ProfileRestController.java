package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ReservedTicketDto;
import dto.Traveller;
import entity.TicketBookingHistory;
import entity.Train;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import manager.TicketBookingManager;
import manager.TrainsManager;
import util.Constants;
import util.NotificationUtil;
import util.TicketBookingUtil;

@RestController
public class ProfileRestController {
	
	@Autowired
	private TicketBookingManager ticketBookingManager;
	
	@Autowired
	private TrainsManager trainsManager;

	private Map<String, List<ReservedTicketDto>> journeyTypeVsbookedTickets;
	List<ReservedTicketDto> upcomingJourneys;
	List<ReservedTicketDto> pastJourneys; 
	List<ReservedTicketDto> cancelledBookings; 



	@RequestMapping(path = "/profileData")
	public List<ReservedTicketDto> getJourneys(HttpSession session, @RequestParam(name = "type", required = false, defaultValue = Constants.UPCOMING_JOURNEYS) String type) {
		User loggedInUser = (User)session.getAttribute("CurrentUser");
		if(Boolean.TRUE.equals(session.getAttribute("newTicketBooked"))) {
			journeyTypeVsbookedTickets = null;	
		}
		if(journeyTypeVsbookedTickets == null) {
			journeyTypeVsbookedTickets = ticketBookingManager.fetchBookingsForUser(loggedInUser);
			upcomingJourneys = journeyTypeVsbookedTickets.get(Constants.UPCOMING_JOURNEYS);
			pastJourneys = journeyTypeVsbookedTickets.get(Constants.PAST_JOURNEYS);
			cancelledBookings = journeyTypeVsbookedTickets.get(Constants.CANCELLED_BOOKINGS);
			journeyTypeVsbookedTickets.clear();
		}
		
		if(type.equals(Constants.PAST_JOURNEYS)) {
			return pastJourneys;
		}else if (type.equals(Constants.UPCOMING_JOURNEYS)) {
			return upcomingJourneys;
		}else if(type.equals(Constants.CANCELLED_BOOKINGS)) {
			return cancelledBookings;
		}
		return null;
	}
	
	@RequestMapping("/fetchTravellers")
	public List<Traveller> fetchTravellers(@RequestParam("pnr") String pnr) {
		List<ReservedTicketDto> dtoList = upcomingJourneys.
				stream().filter(e -> e.getPnr() == Integer.parseInt(pnr))
				.toList();
		
		ArrayList<Traveller> travellers = new ArrayList<Traveller>(dtoList.get(0).getTravellerVsBerth().keySet());
		return travellers;
	}
	
	@PostMapping("/cancelTicket")
	public String cancelTicket( @RequestParam("pnr") String pnr, @RequestParam("travellers") String travellers) {
		System.out.println("pnr : "+pnr);
		System.out.println("names : "+travellers);
		String[] travellerArr = travellers.split(":");
		List<TicketBookingHistory> bookingDetails = ticketBookingManager.getBookingFromPnr(Integer.parseInt(pnr));  
		String fromStation = bookingDetails.get(0).getSourceStation();
		String toStation = bookingDetails.get(0).getDestinationStation();
		LocalDate dateOfJourney = bookingDetails.get(0).getDateOfJourney();
		Train train = bookingDetails.get(0).getTrain();
		String trainClass = bookingDetails.get(0).getTrainClass();
		List<String> stationsList = trainsManager.fetchStationsForTrain(train);
		boolean cancellationStatus = ticketBookingManager.cancelTicket(Integer.parseInt(pnr), travellerArr);
		
		//changing ticket availability
		ticketBookingManager.updateTicketAvailability(fromStation, toStation, dateOfJourney, train, trainClass, travellerArr.length, stationsList, true);
		journeyTypeVsbookedTickets = null;
		return cancellationStatus ? "success-"+bookingDetails.get(0).getFare()*Constants.CANCELLATION_PERCENT : "failed";
	}
	
	@RequestMapping("/sendCancellationConfirmation")
	public void sendCancellationConfirmation(@RequestParam("pnr") Integer pnr,  @RequestParam("travellers") String travellers) {
		String toEmail = ticketBookingManager.getRecipientEmail(pnr);
		List<String> travellersList = Arrays.asList(travellers.split(":"));
		Runnable thread = () ->  NotificationUtil.sendCancellationEmailConfiguration(pnr, travellersList, toEmail);
		new Thread(thread).start();
	}
	
	@RequestMapping("/resendConfirmation")
	private void resendBookingConfirmation(@RequestParam("pnr") Integer pnr) {
		List<TicketBookingHistory> ticketBookingHistoryList = ticketBookingManager.getBookingFromPnr(pnr);
		Train train = ticketBookingHistoryList.get(0).getTrain();
		String fromStation = ticketBookingHistoryList.get(0).getSourceStation();
		String toStation = ticketBookingHistoryList.get(0).getDestinationStation();
		LocalDate doj = ticketBookingHistoryList.get(0).getDateOfJourney();
		String recipientEmailId = ticketBookingHistoryList.get(0).getConfirmationEmailId();
		Map<String, Object> bookingStatus = new HashMap<>();
		Map<Traveller, String> travellerVsBerthMap = new HashMap<>();
		bookingStatus.put("pnr", pnr);
		ObjectMapper mapper = new ObjectMapper();
		ticketBookingHistoryList.stream().forEach(ticket ->{
			try {
				Traveller traveller = mapper.readValue(ticket.getTravellerDetails(), Traveller.class);
				travellerVsBerthMap.put(traveller, TicketBookingUtil.getSeatKey(ticket.getCoach(), ticket.getSeatNumber()));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
		bookingStatus.put("travellerVsBerth", travellerVsBerthMap);
		NotificationUtil.sendEmailConfirmation(train, fromStation, toStation, doj, recipientEmailId, bookingStatus);
	}

}
