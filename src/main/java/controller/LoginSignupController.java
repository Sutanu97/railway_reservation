package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import manager.TicketBookingManager;
import manager.UserManager;

@Controller
public class LoginSignupController {
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private TicketBookingManager ticketBookingManager;
	
	@RequestMapping("/getLoginPage")
	public String getLoginPage() {
		return "loginPage";
	}
	
	@RequestMapping("/getSignupPage")
	public String getSignupPage(Model model) {
		model.addAttribute("user", new User());
		return "signupPage";
	}
	
	
	@RequestMapping("/login")
	public String loginUser(@RequestParam(name = "irctc_ID", required = false) String irctcId, @RequestParam(name="irctc_password",  required = false) String irctcPassword, Model model, HttpSession session) {
		User loggedInUser = (User)session.getAttribute("CurrentUser");
		if(irctcId != null && irctcPassword != null) {
			loggedInUser = userManager.loginUser(irctcId, irctcPassword);
			if(loggedInUser != null)
				session.setAttribute("CurrentUser", loggedInUser);
		}
		if(loggedInUser != null) {
			session.setAttribute("isAuthRequired",false);
			return "profile";
		}else {
			return null;
		}
			
		
	}
	
	@PostMapping("/signupAlternative")
	public String newUserSignup(@ModelAttribute("user") User user, BindingResult errors, Model model, HttpSession session) throws Exception {
		model.addAttribute("user", new User());
		if(errors.hasErrors()) {
			model.addAttribute("signupFormBindingError",true);
			return "getSignupPage";
		}
		boolean signupStatus = userManager.newUserSignup(user);
		if(!signupStatus) {
			model.addAttribute("signupFormSubmissionError",true);
			return "getSignupPage";
		}
//		session.setAttribute("CurrentUser", user);
//		model.addAttribute("isAuthRequired",false);
		return "home";
	}
	
	
	@PostMapping(path = "/signup"
			)
	//@ModelAttribute User user could have been used
	public String newUserSignup(@RequestParam(name="age") Integer age,
			@RequestParam(name="name") String name,
			@RequestParam(name="gender") String gender,
			@RequestParam(name="emailId") String emailId,
			@RequestParam(name="contactNumber") Long contactNumber,
			@RequestParam(name="irctcId") String irctcId,
			@RequestParam(name="irctcPassword") String irctcPassword,
			Model model, HttpSession session, HttpServletRequest request) throws Exception {
		boolean signupStatus;
		User user = new User();
		user.setAge(age);
		user.setContactNumber(contactNumber);
		user.setEmailId(emailId);
		user.setGender(gender);
		user.setIrctcId(irctcId);
		user.setIrctcPassword(irctcPassword);
		user.setName(name);
		signupStatus = userManager.newUserSignup(user);

		if(!signupStatus) {
			throw new Exception();
		}
//		session.setAttribute("CurrentUser", user);
//		model.addAttribute("isAuthRequired",false);
		return "home";
	}
}
