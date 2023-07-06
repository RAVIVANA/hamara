package com.nkxgen.spring.jdbc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.nkxgen.spring.jdbc.ViewModels.LoanViewModel;
import com.nkxgen.spring.jdbc.events.LoanAppApprovalEvent;
import com.nkxgen.spring.jdbc.events.LoanAppRequestEvent;
import com.nkxgen.spring.jdbc.model.LoanApplication;

@Controller
public class LoanController {

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
		List<LoanViewModel> list = v.getAllLoans();

		// Add the list of loans to the model attribute "loans"
		model.addAttribute("loan", list);
		return "loan-new-application-form";
	}

	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	public String editForm(Model model) {
		return "edit-form";
	}

	@RequestMapping(value = "/redirected", method = RequestMethod.GET)
	public String redirectForm(Model model) {
		try {
			// Get a list of loan applications with the status "redirecting"
			List<LoanApplicationViewModel> list = v.getLoanApplicationsByStatus("redirecting");

			// Add the list of loan applications as a model attribute
			model.addAttribute("loanApplications", list);

			// Return the view name "Application1" to render the page
			return "redirected-applications";
		} catch (Exception e) {
			throw new DataIntegrityViolationException("Foreign key violation occurred.", e);
		}
	}

	@RequestMapping(value = "/newLoanApplication", method = RequestMethod.POST)
	public String newLoanApplication(@Validated LoanApplicationInput l, Model model, HttpServletRequest request) {
		try {
			// Create a new instance of LoanApplication
			LoanApplication loan = new LoanApplication();

			// Set the input model values of the loan application using the LoanApplicationInput object
			loan.LoanApplication(l);

			// Save the loan application to the database using the ll (LoanApplicationDaoInterface) object
			ll.saveLoanApplication(loan);

			// Get the current session
			HttpSession session = request.getSession();

			// Get the username from the session attribute
			String username = (String) session.getAttribute("username");

			// Publish a loan application request event with the event message and username
			applicationEventPublisher.publishEvent(new LoanAppRequestEvent("New Loan Application Filled", username));

			// Change the return to the view name "loan_new_application_form" to render the page
			return "loan-new-application-form";
		} catch (Exception e) {
			model.addAttribute("error", "An error occurred while processing the new loan application.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/updateApplication", method = RequestMethod.POST)
	public String updateLoanApplication(@Validated LoanApplicationInput loanApplication, HttpServletRequest request,
			Model model) {
		try {
			// Update the loan application using the ll (LoanApplicationDaoInterface) object
			ll.updateLoanApplication(loanApplication);

			// Get the current session
			HttpSession session = request.getSession();

			// Get the username from the session attribute
			String username = (String) session.getAttribute("username");

			// Publish a loan application approval event with the event message and username
			applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Updated", username));

			// Change the return to the view name "loan-approval" to render the page
			return "loan-approval";
		} catch (Exception e) {
			model.addAttribute("error", "An error occurred while updating the loan application.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/update_application", method = RequestMethod.POST)
	public String updateLoanApplication1(@Validated LoanApplicationInput loanApplication, HttpServletRequest request,
			Model model) {
		try {
			// Update the loan application using the ll (LoanApplicationDaoInterface) object
			ll.updateLoanApplication(loanApplication);

			// Get the current session
			HttpSession session = request.getSession();

			// Get the username from the session attribute
			String username = (String) session.getAttribute("username");

			// Publish a loan application approval event with the event message and username
			applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Updated", username));

			// Change the return to the view name "loan-approval" to render the page
			return "loan-approval";
		} catch (Exception e) {
			model.addAttribute("error", "An error occurred while updating the loan application.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/getApplications", method = RequestMethod.POST)
	public String GetLoanApplication(@RequestParam("Typevalue") String accountType, Model model) {
		// Get the loan applications based on the value from the Types object
		List<LoanApplicationViewModel> list = v.getLoanApplicationByValue(accountType);
        
		// Add the loan applications to the model attribute
		model.addAttribute("loanApplications", list);

		// Change the return to the view name "Application" to render the page
		return "loan-approval";
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST)
	public String GetLoanAccounts(@RequestParam("Typevalue") String accountType, Model model) {
		// Get the loan accounts based on the loan type value from the Types object
		List<LoanAccountViewModel> list = v.getLoanAccountsByLoanType(accountType);

		// Add the loan accounts to the model attribute
		model.addAttribute("loanAccounts", list);

		// Change the return to the view name "Application3" to render the page
		return "loan-account-details";
	}

	@RequestMapping(value = "/deleteLoan", method = RequestMethod.POST)
	public String deleteLoanApplication(@RequestParam("loanId") int loanId, Model model) {
		try {
			// Delete the loan application based on the loanId parameter
			ll.deleteApplication(loanId);

			// Change the return to the view name "loan-approval" to render the page
			return "loan-approval";
		} catch (Exception e) {
			model.addAttribute("error", "An error occurred while deleting the loan application.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/approveLoan", method = RequestMethod.POST)
	public String approveLoanApplication(@RequestParam("loanId") int loanId,
			@RequestParam("customerId") Long customerId, Model model, HttpServletRequest request) {
		try {
			ll.approveApplication(loanId, customerId);
			// Approve the loan application based on the loanId and customerId parameters
			System.out.println("the loan application id is: " + loanId);
			LoanApplication loanapp = ll.getLoanApplicationByid(loanId);
			System.out.println("the acquired loan id is: " + loanapp.getId());
			loanapp.setProcessedStatus("Approved");
			loanapp.setStatus("Approved");
			ll.saveTheApprovedLoanApplication(loanapp);

			// Get the session object from the request
			HttpSession session = request.getSession();

			// Get the username attribute from the session
			String username = (String) session.getAttribute("username");

			// Publish a LoanAppApprovalEvent with the appropriate message and username
			applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Approved", username));

			// Change the return to the view name "loan-approval" to render the page
			return "loan-approval";
		} catch (Exception e) {
			model.addAttribute("error", "An error occurred while approving the loan application.");
			return "error-view";
		}
	}

	@RequestMapping(value = "/getLoanApplicationsById", method = RequestMethod.POST)
	public String getLoanApplicationById(@RequestParam("Data") int accountType, Model model) {
		// Get the loan applications based on the value from the Types object
		try {
			LoanApplicationViewModel list = v.getLoanApplicationById(accountType);
           List<LoanApplicationViewModel> list1=new ArrayList<>();
           list1.add(list);
			// Add the loan applications to the model attribute
			model.addAttribute("loanAccounts", list1);

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

}