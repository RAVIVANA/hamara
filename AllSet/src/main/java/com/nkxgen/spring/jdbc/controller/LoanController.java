package com.nkxgen.spring.jdbc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.LoanApplicationDaoInterface;
import com.nkxgen.spring.jdbc.Exception.AccountNotFound;
import com.nkxgen.spring.jdbc.Exception.ApplicationNotFound;
import com.nkxgen.spring.jdbc.InputModels.LoanApplicationInput;
import com.nkxgen.spring.jdbc.ViewModels.LoanAccountViewModel;
import com.nkxgen.spring.jdbc.ViewModels.LoanApplicationViewModel;
import com.nkxgen.spring.jdbc.events.LoanAppApprovalEvent;
import com.nkxgen.spring.jdbc.events.LoanAppRequestEvent;
import com.nkxgen.spring.jdbc.model.LoanApplication;

@Controller
public class LoanController {
	private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

	@Autowired
	LoanApplicationDaoInterface ll;
	@Autowired
	CustomerDaoInterface cd;
	@Autowired
	ViewInterface v;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@RequestMapping(value = "/loanNewApplicationForm", method = RequestMethod.GET)
	public String loanNewApplicationForm(Model model) {
		logger.info("Entering loanNewApplicationForm method");

		return "loan-new-application-form";
	}

	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	public String editForm(Model model) {
		logger.info("Entering editForm method");

		return "edit-form";
	}

	@RequestMapping(value = "/redirected", method = RequestMethod.GET)
	public String redirectForm(Model model) {
		logger.info("Entering redirectForm method");

		// Get a list of loan applications with the status "redirecting"
		List<LoanApplicationViewModel> list = v.getLoanApplicationsByStatus("redirecting");

		// Add the list of loan applications as a model attribute
		model.addAttribute("loanApplications", list);

		// Return the view name "redirected-applications" to render the page
		return "redirected-applications";
	}

	@RequestMapping(value = "/newLoanApplication", method = RequestMethod.POST)
	public String NewLoanApplication(@Validated LoanApplicationInput l, Model model, HttpServletRequest request) {
		// Create a new instance of LoanApplication
		LoanApplication loan = new LoanApplication();

		// Set the input model values of the loan application using the LoanApplicationInput object
		loan.LoanApplication(l);

		try {
			// Save the loan application to the database using the ll (LoanApplicationDaoInterface) object
			ll.saveLoanApplication(loan);
		} catch (DataIntegrityViolationException e) {
			// Handle the foreign key violation exception here
			// For example, you can log an error message or show an error to the user
			// Here's an example of logging an error message:
			logger.error("Foreign key violation when saving loan application: {}", e.getMessage());
			// You can perform additional error handling or return an error response to the user
		}

		// Get the current session
		HttpSession session = request.getSession();

		// Get the username from the session attribute
		String username = (String) session.getAttribute("username");

		// Publish a loan application request event with the event message and username
		applicationEventPublisher.publishEvent(new LoanAppRequestEvent("New Loan Application Filled", username));

		// Change the return to the view name "loan_new_application_form" to render the page
		return "loan-new-application-form";
	}

	@RequestMapping(value = "/updateApplication", method = RequestMethod.POST)
	public String updateLoanApplication(@Validated LoanApplicationInput loanApplication, HttpServletRequest request) {
		// Update the loan application using the ll (LoanApplicationDaoInterface) object
		try {
			ll.updateLoanApplication(loanApplication);

			// Get the current session
			HttpSession session = request.getSession();

			// Get the username from the session attribute
			String username = (String) session.getAttribute("username");

			// Publish a loan application approval event with the event message and username
			applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Updated", username));
		} catch (DataIntegrityViolationException e) {
			// Handle the foreign key violation exception here
			// For example, you can log an error message or show an error to the user
			// Here's an example of logging an error message:
			logger.error("Foreign key violation when saving loan application: {}", e.getMessage());
			// You can perform additional error handling or return an error response to the user
		}
		// Change the return to the view name "Application" to render the page
		return "loan-approval";
	}

	public String GetLoanApplication(@RequestParam("Typevalue") String accountType, Model model) {
		try {
			// Log the start of the method
			logger.info("Starting GetLoanApplication method");

			// Get the loan applications based on the value from the Types object
			List<LoanApplicationViewModel> list = v.getLoanApplicationByValue(accountType);

			// Add the loan applications to the model attribute
			model.addAttribute("loanApplications", list);

			// Log the successful execution of the method
			logger.info("GetLoanApplication method executed successfully");

			// Change the return to the view name "Application" to render the page
			return "loan-approval";
		} catch (Exception e) {
			// Log the exception
			logger.error("An error occurred in GetLoanApplication method", e);

			// Handle the exception and return an appropriate error view
			model.addAttribute("error", "An error occurred. Please try again later.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/getLoanApplicationsById", method = RequestMethod.POST)
	public String getLoanApplicationById(@RequestParam("Data") int accountType, Model model) {
		// Get the loan applications based on the value from the Types object
		try {
			LoanApplicationViewModel list = v.getLoanApplicationById(accountType);

			// Add the loan applications to the model attribute
			model.addAttribute("loanAccounts", list);

			// Change the return to the view name "Application" to render the page
			return "loan-approval";
		} catch (ApplicationNotFound e) {
			return "AccountNotFound";
		}
	}

	@RequestMapping(value = "/getLoanAccountById", method = RequestMethod.POST)
	public String getAccountById(@RequestParam("Data") int accountType, Model model) {
		// Get the loan applications based on the value from the Types object
		List<LoanAccountViewModel> list = new ArrayList<LoanAccountViewModel>();
		try {
			LoanAccountViewModel l = v.getLoanAccountById(accountType);
			list.add(l);
			// Add the loan applications to the model attribute
			model.addAttribute("loanApplications", list);

			// Change the return to the view name "Application" to render the page
			return "loan-account-details";
		} catch (AccountNotFound e) {
			return "AccountNotFound";
		}
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST)
	public String GetLoanAccounts(@RequestParam("Typevalue") String accountType, Model model) {
		// Log the start of the method
		logger.info("GetLoanAccounts method started.");

		try {
			// Get the loan accounts based on the loan type value from the Types object
			List<LoanAccountViewModel> list = v.getLoanAccountsByLoanType(accountType);

			// Add the loan accounts to the model attribute
			model.addAttribute("loanAccounts", list);

			// Log a message indicating the successful execution of the method
			logger.info("GetLoanAccounts method executed successfully.");

			// Change the return to the view name "loan-account-details" to render the page
			return "loan-account-details";
		} catch (Exception e) {
			// Log the exception
			logger.error("An error occurred in GetLoanAccounts method", e);

			// Handle the exception and return the error view
			model.addAttribute("error", "An error occurred. Please try again later.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/deleteLoan", method = RequestMethod.POST)
	public String deleteLoanApplication(@RequestParam("loanId") int accountType, Model model) {
		// Log the start of the method
		logger.info("deleteLoanApplication method started.");

		try {
			// Delete the loan application based on the loanId parameter
			ll.deleteApplication(accountType);

			// Log a message indicating the successful execution of the method
			logger.info("Loan application deleted successfully.");

			// Change the return to the view name "loan-approval" to render the page
			return "loan-approval";
		} catch (Exception e) {
			// Log the exception
			logger.error("An error occurred in deleteLoanApplication method", e);

			// Handle the exception and return the error view
			model.addAttribute("error", "An error occurred. Please try again later.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/approveLoan", method = RequestMethod.POST)
	public String approveLoanApplication(@RequestParam("loanId") int accountType,
			@RequestParam("customerId") Long custid, Model model, HttpServletRequest request) {
		try {
			// Approve the loan application based on the loanId and customerId parameters
			ll.approveApplication(accountType, custid);

			// Get the session object from the request
			HttpSession session = request.getSession();

			// Get the username attribute from the session
			String username = (String) session.getAttribute("username");

			// Publish a LoanAppApprovalEvent with the appropriate message and username
			applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Approved", "No user"));

			// Change the return to the view name "Application" to render the page
			return "loan-approval";
		} catch (DataIntegrityViolationException e) {
			// Handle the foreign key violation exception
			model.addAttribute("error", "Foreign key violation occurred.");

			// Return the view name for error handling
			return "error-view"; // Replace "error-view" with the appropriate error handling view
		}
	}

}
