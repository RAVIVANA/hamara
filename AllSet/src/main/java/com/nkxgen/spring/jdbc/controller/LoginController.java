package com.nkxgen.spring.jdbc.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nkxgen.spring.jdbc.DaoInterfaces.BankUserInterface;
import com.nkxgen.spring.jdbc.Exception.EmailNotFoundException;
import com.nkxgen.spring.jdbc.ViewModels.GraphModel;
import com.nkxgen.spring.jdbc.events.LoginEvent;
import com.nkxgen.spring.jdbc.events.LogoutEvent;
import com.nkxgen.spring.jdbc.model.LoginModel;
import com.nkxgen.spring.jdbc.service.ChartService;
import com.nkxgen.spring.jdbc.validation.MailSender;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	private MailSender mailSender;
	private HttpSession httpSession;
	private ChartService chartService;
	private BankUserInterface bankUserService;
	private ApplicationEventPublisher applicationEventPublisher;
	LoginModel login = new LoginModel();

	@Autowired
	public LoginController(ApplicationEventPublisher applicationEventPublisher, HttpSession httpSession,
			MailSender mailSender, BankUserInterface bankUserService, ChartService chartService) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.httpSession = httpSession;
		this.mailSender = mailSender;
		this.bankUserService = bankUserService;
		this.chartService = chartService;
	}

	@RequestMapping(value = "/graphs", method = RequestMethod.GET)
	public String graphs(Locale locale, Model model) {
		logger.info("Entering graphs method");

		return "graphs";
	}

	@RequestMapping(value = "/getGraphData", method = RequestMethod.POST)
	@ResponseBody
	public String getGraphData(Locale locale, Model model) {
		GraphModel graphModel = new GraphModel();
		graphModel.setAccountData(chartService.getAccountData());
		graphModel.setLoanData(chartService.getLoanData());
		graphModel.setAccountLabels(chartService.getAccountLabels());
		graphModel.setLoanLabels(chartService.getLoanLabels());

		Gson gson = new Gson();
		String jsonData = gson.toJson(graphModel);

		return jsonData;

	}

	// =====================================================================================================
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		logger.info("Login page requested");
		return "login-page";
	}

	@RequestMapping(value = "/updatedPassword", method = RequestMethod.POST)
	public String updatedPassword(@RequestParam("password") String newPassword, HttpSession session) {
		logger.info("Updating password");
		bankUserService.updatePassword(newPassword, login.getUserID());
		session.setAttribute("errorMessage", "Successfully Updated Password");
		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(Locale locale, Model model) {
		logger.info("Login request submitted");
		return "redirect:/";
	}

	@RequestMapping(value = "/Test", method = RequestMethod.POST)
	public String homePage() {
		logger.info("Home page requested");
		return "redirect:/home";
	}

	@RequestMapping(value = "/enterOTP", method = RequestMethod.GET)
	public String enterOTP(Locale locale, Model model) {
		logger.info("Enter OTP page requested");
		login.setOtp(mailSender.send(login.getMail()));
		return "enter-otp";
	}

	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public String login2(HttpServletRequest request, HttpServletResponse response) {
		// Get the session object from the request
		HttpSession session = request.getSession();

		// Get the username attribute from the session
		String username = (String) session.getAttribute("username");

		logger.info("User '{}' logged out", username);

		// Publish a LogoutEvent with the appropriate message and username
		applicationEventPublisher.publishEvent(new LogoutEvent("Logged Out", username));

		// Invalidate the session to remove all session attributes
		session.invalidate();

		// Set Cache-Control headers to prevent caching of the page
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		logger.info("Redirecting to login page");

		// Change the return to the view name "LoginPage" to render the page
		return "redirect:/";
	}

	@RequestMapping(value = "/enterOtp", method = RequestMethod.POST)
	public String enterOtp(@RequestParam("email") String email, @RequestParam("userID") String userid,
			HttpSession session) throws EmailNotFoundException {
		try {
			login.setUserID(Long.parseLong(userid));

			logger.info("Received OTP entry request for email: {}, userID: {}", email, login.getUserID());

			if (bankUserService.getBankUserByEmail(email, login.getUserID())) {
				logger.info("User with email {} and userID {} exists", email, login.getUserID());

				login.setMail(email);
				// Call the "send" method on the "mailSender" object to send the OTP to the specified email

				logger.info("Redirecting to enterOTP page");

				// Change the return to the view name "enterOTP" to render the page
				return "redirect:/enterOTP";
			}
		} catch (EmailNotFoundException e) {
			String message = e.getMessage();
			session.setAttribute("message", message);
			logger.error("EmailNotFoundException: {}", message);
			return "redirect:/enterEmail";
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/enterEmail")
	public String sendOtp() {
		logger.info("Enter email page requested");
		return "enter-email";
	}

	@RequestMapping(value = "/confirmPass", method = RequestMethod.POST)
	public String confirmpass(@RequestParam("otp") String otp1, HttpSession session) {
		logger.info("Confirm password request received");

		// Check if the entered OTP matches the sent OTP
		if (otp1.equals(login.getOtp())) {
			logger.info("OTP verification successful");
			return "confirm-pass"; // If OTP is correct, return the view name "confirmPass"
		} else {
			session.setAttribute("errorMessage", "Invalid OTP"); // Add an error message to the session
			logger.warn("Invalid OTP entered");
			return "redirect:/"; // If OTP is incorrect, return the view name "EnterOtp"
		}
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String main_page(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Home page requested");

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		if (username == null) {
			logger.warn("User not logged in, redirecting to login page");
			// User is not logged in, redirect to a login page
			return "redirect:/";
		}

		// Add the username as an attribute to the model
		model.addAttribute("username", username);

		// Publish a LoginEvent with the username
		logger.info("Publishing LoginEvent for user: {}", username);
		applicationEventPublisher.publishEvent(new LoginEvent("Logged In", username));

		return "bank-home-page"; // Return the view name "bank-home-page" to render the page
	}

}