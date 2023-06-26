package com.nkxgen.spring.jdbc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.Dao.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.Dao.LoanApplicationDaoInterface;
import com.nkxgen.spring.jdbc.events.LoanAppApprovalEvent;
import com.nkxgen.spring.jdbc.events.LoanAppRequestEvent;
import com.nkxgen.spring.jdbc.model.LoanAccountViewModel;
import com.nkxgen.spring.jdbc.model.LoanApplication;
import com.nkxgen.spring.jdbc.model.LoanApplicationInput;
import com.nkxgen.spring.jdbc.model.LoanApplicationViewModel;
import com.nkxgen.spring.jdbc.model.Types;

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

	@RequestMapping(value = "/loan_new_application_form", method = RequestMethod.GET)
	public String loan_new_application_form(Model model) {
		return "loan_new_application_form";
	}

	@RequestMapping(value = "/edit_form", method = RequestMethod.GET)
	public String edit_form(Model model) {
		return "edit_form";
	}

	@RequestMapping(value = "/redirected", method = RequestMethod.GET)
	public String redirect_form(Model model) {
		List<LoanApplicationViewModel> list = v.set1("redirecting");
		model.addAttribute("loanApplications", list);
		return "Application1";
	}

	@RequestMapping(value = "/New_loan_Application", method = RequestMethod.POST)
	public String New_loan_application(@Validated LoanApplicationInput l, Model model,HttpServletRequest request) {
		LoanApplication loan = new LoanApplication();
		loan.LoanApplication(l);
		ll.saveLoanApplication(loan);
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		applicationEventPublisher.publishEvent(new LoanAppRequestEvent("New Loan Application Filled", username));
		return "loan_new_application_form";
	}

	@RequestMapping(value = "/update_application", method = RequestMethod.POST)
	public String updateLoanApplication(@Validated LoanApplicationInput loanApplication,HttpServletRequest request) {
		ll.updateLoanApplication(loanApplication);
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Updated", username));
		return "Application";

	}

	@RequestMapping(value = "/get_applications", method = RequestMethod.POST)
	public String Get_loan_application(@Validated Types l, Model model) {

		List<LoanApplicationViewModel> list = v.set6(l.gethrefvalue());
		model.addAttribute("loanApplications", list);
		return "Application";

	}

	@RequestMapping(value = "/Account", method = RequestMethod.POST)
	public String Get_loan_accounts(@Validated Types l, Model model) {
		List<LoanAccountViewModel> list = v.set(l.gethrefvalue());

		model.addAttribute("loanAccounts", list);
		return "Application3";

	}

	@RequestMapping(value = "/delete_loan", method = RequestMethod.POST)
	public String delete_loan_application(@RequestParam("loanId") int accountType, Model model) {
		ll.delete_Application(accountType);
		return "Application";

	}

	@RequestMapping(value = "/approve_loan", method = RequestMethod.POST)
	public String approve_loan_application(@RequestParam("loanId") int accountType,
			@RequestParam("customerId") Long custid, Model model,HttpServletRequest request) {
		ll.approve_Application(accountType, custid);
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		applicationEventPublisher.publishEvent(new LoanAppApprovalEvent("Loan Application Approved", "No user"));
		return "Application";

	}
}
