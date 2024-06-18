package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import dto.BookTicketDto;
import dto.Traveller;
import entity.Train;
import entity.User;
import jakarta.servlet.http.HttpSession;
import manager.TicketBookingManager;
import manager.TrainsManager;
import manager.UserManager;
import util.Constants;
import util.NotificationUtil;

@Controller
public class ReviewAndConfirmController {
	
	@Autowired
	private TicketBookingManager ticketBookingManager;
	
	@Autowired
	private TrainsManager trainsManager;
	
	@Autowired
	private UserManager userManager;
	
	@RequestMapping("/review")
	public String reviewTicket(@ModelAttribute BookTicketDto bookTicketDto, HttpSession session, Model model ) {
		System.out.println(bookTicketDto);
		User user = (User) session.getAttribute("CurrentUser");
		Boolean isAuthRequired = (Boolean) session.getAttribute("isAuthRequired");
		if(user == null || Boolean.TRUE.equals(isAuthRequired)) {
			return "home";
		}
//		String id = user.getIrctcId();
//		String pwd = bookTicketDto.getPassword();
//		if(!validateUser(id, pwd, user)) {
//			return "bookTicket";
//		}
		session.setAttribute("recipientEmailId",bookTicketDto.getEmailId());
		session.setAttribute("recipientPhoneNumber",bookTicketDto.getPhoneNumber());
		List<Traveller> travellers = new ArrayList<>();
		for(int i=0;i<bookTicketDto.getTravellerName().size();i++) {
			String name = bookTicketDto.getTravellerName().get(i);
			Integer age = bookTicketDto.getTravellerAge().get(i);
			String gender = bookTicketDto.getTravellerGender().get(i);
			travellers.add(new Traveller(name, age, gender));
		}
		model.addAttribute("travellers",travellers);
		session.setAttribute("travellers", travellers);
		return "reviewAndBook";
	}
	
//	private boolean validateUser(String id, String pwd, User user) {
//		User userFetched = userManager.loginUser(id, pwd);
//		if(userFetched == null)
//			return false;
//		if(user.getPkUserId() == userFetched.getPkUserId()) {
//			return true;
//		}
//		return false;
//	}

	@RequestMapping("/sendConfirmation")
	private String sendBookingConfirmation(Model model, HttpSession session) {
		User user = (User) session.getAttribute("CurrentUser");
		Train train = (Train)session.getAttribute("train");
		String fromStation = (String)session.getAttribute("fromStation");
		String toStation = (String)session.getAttribute("toStation");
		LocalDate doj = (LocalDate)session.getAttribute("doj");
		String trainClass = (String)session.getAttribute("trainClass");
		String travelTime = ((Map<Integer, String>) session.getAttribute("trainNumberVsTravelTime")).get(train.getNumber());
		String departureTime = ((Map<Integer, String>) session.getAttribute("trainNumberVsFormattedDepartureTime")).get(train.getNumber());
		String arrivalTime = ((Map<Integer, String>) session.getAttribute("trainNumberVsFormattedArrivalTime")).get(train.getNumber());
		List<Traveller> travellers = (List<Traveller>) session.getAttribute("travellers");
		Double totalFare = (Double)session.getAttribute("totalFarePerPassenger")*travellers.size();
		String recipientEmailId = session.getAttribute("recipientEmailId").toString();
		List<String> stationsList = trainsManager.fetchStationsForTrain(train);
		Map<String, Object> bookingStatus = ticketBookingManager.bookTicket(user,train,fromStation,toStation,trainClass,travelTime, departureTime, arrivalTime, doj, travellers, stationsList, recipientEmailId, totalFare);
		if(bookingStatus.containsKey(Constants.TRAVELLER_COUNT_EXCEEDED_LIMIT)
				&& (Boolean)bookingStatus.get(Constants.TRAVELLER_COUNT_EXCEEDED_LIMIT)) {
			model.addAttribute("isCountMore", true);
			return "home";
		}else {
			model.addAttribute("isCountMore", false);
		}
		model.addAttribute("bookingStatus", bookingStatus);
		session.setAttribute("bookingStatus", bookingStatus);
		
		Runnable thread = () -> NotificationUtil.sendEmailConfirmation(train, fromStation, toStation, doj,recipientEmailId, bookingStatus);
		new Thread(thread).start();
		session.setAttribute("newTicketBooked", true);
		return "showBookingStatus";
	}
	
	
}
