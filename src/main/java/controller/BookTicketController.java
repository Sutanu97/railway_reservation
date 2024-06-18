package controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import entity.Train;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import manager.TrainsManager;
import manager.UserManager;

@Controller
public class BookTicketController {
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private TrainsManager trainsManager;
	
	
	
	@RequestMapping(path = "/bookTicket", method = {RequestMethod.GET, RequestMethod.POST})
	public String bookTicket(
			@RequestParam(name = "trainNo", required = false) Integer trainNo, 
			@RequestParam(name = "trainClass", required = false ) String trainClass, 
			@RequestParam(name = "ticketsAvl", required = false) String ticketsAvl, 
			@RequestParam(name = "irctc_ID", required = false) String irctcId,
			@RequestParam(name = "irctc_password", required = false) String irctcPassword,
			@ModelAttribute User newUser,
			Model model, HttpSession session, HttpServletRequest request) throws Exception {
		Train train = null;
		
		User user = (User)session.getAttribute("CurrentUser");
		if(user == null) {
			model.addAttribute("isAuthRequired",true);
		}
		if(request.getMethod().equals(RequestMethod.GET.toString())) {
			train = trainsManager.getTrainUsingNumber(trainNo);
			model.addAttribute("train",train);
			model.addAttribute("trainClass", trainClass);
			model.addAttribute("ticketsAvl", ticketsAvl);
			addAttributesToSession(session, train, trainClass, ticketsAvl);
		}else if(request.getMethod().equals(RequestMethod.POST.toString())) {
			user = userManager.loginUser(irctcId, irctcPassword);
			if(user == null) {
				System.out.println("Could not login");
				throw new Exception("Unsuccessful login!");
			}
			// else if(Boolean.TRUE.equals(Boolean.parseBoolean(request.getParameter("signup")))) {
			// 	boolean signupStatus = userManager.newUserSignup(newUser);
			// 	if(!signupStatus) {
			// 		System.out.println("Could not sign up");
			// 		return "findTrains";
			// 	}
			// 	user = newUser;
			// }
			session.setAttribute("CurrentUser", user);
			session.setAttribute("isAuthRequired",false);
			train = (Train)session.getAttribute("train");
			trainClass = (String)session.getAttribute("trainClass");
			ticketsAvl = (String)session.getAttribute("ticketsAvl");
			
			
			model.addAttribute("train", train);
			model.addAttribute("trainClass", trainClass);
			model.addAttribute("ticketsAvl", ticketsAvl);
			model.addAttribute("isAuthRequired",false);
		}
		
		
		return "bookTicket";
	}
	
	

	private void addAttributesToSession(HttpSession session, Train train, String trainClass, String ticketsAvl) {
		session.setAttribute("train", train);
		session.setAttribute("trainClass", trainClass);
		session.setAttribute("ticketsAvl", ticketsAvl);
		
	}
}
