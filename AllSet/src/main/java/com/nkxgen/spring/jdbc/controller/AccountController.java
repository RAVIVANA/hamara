package com.nkxgen.spring.jdbc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.AccountApplicationDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.Exception.AccountNotFound;
import com.nkxgen.spring.jdbc.Exception.ApplicationNotFound;
import com.nkxgen.spring.jdbc.InputModels.AccountApplicationInput;
import com.nkxgen.spring.jdbc.InputModels.AccountDocumentInput;
import com.nkxgen.spring.jdbc.InputModels.AccountInput;
import com.nkxgen.spring.jdbc.ViewModels.AccountApplicationViewModel;
import com.nkxgen.spring.jdbc.ViewModels.AccountViewModel;
import com.nkxgen.spring.jdbc.events.AccountAppApprovalEvent;
import com.nkxgen.spring.jdbc.events.AccountAppRequestEvent;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.AccountApplication;
import com.nkxgen.spring.jdbc.model.Accountdocument;
import com.nkxgen.spring.jdbc.model.Customertrail;

@Controller
// The @Controller annotation indicates that this class is a controller in the Spring MVC framework.
public class AccountController {
	Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private AccountApplicationDaoInterface ac;
	@Autowired
	ViewInterface v;

	AccountController(AccountApplicationDaoInterface ac) {
		this.ac = ac;
	}

	@Autowired
	private CustomerDaoInterface cd;

	// here @RequestMapping annotation maps the /master_account URL to the masterAccount method, which returns the view
	// "account_master_entry"
	@RequestMapping("/masterAccount")
	public String masterAccount(Model model) {
		return "account-master-entry";
	}

	@RequestMapping(value = "/getAccountById", method = RequestMethod.POST)
	public String getAccountById(@RequestParam("Data") int accountId, Model model) {
		try {
			AccountViewModel account = v.getAccountById(accountId);
			List<AccountViewModel> list1 = new ArrayList<AccountViewModel>();
			// Retrieve the Customertrail object based on the customerId of the account
			Customertrail customer = cd.getRealCustomerById(account.getCustomerId());
			List<Customertrail> list2 = new ArrayList<Customertrail>();
			// Add the retrieved customer to the list of Customertrail objects
			list1.add(account);
			list2.add(customer);
			LOGGER.info("AccountFound");
			model.addAttribute("list_of_account", list1);
			model.addAttribute("list_of_customer", list2);
			return "any-type-account-info";
		} catch (AccountNotFound e) {
			LOGGER.warn(e.getMessage());
			return "AccountNotFound";
		}

	}

	@RequestMapping(value = "/getApplicationById", method = RequestMethod.POST)
	public String getAccountApplicationById(@RequestParam("Data") int accountId, Model model) {
		try {
			AccountApplicationViewModel application = v.getAccountsappById(accountId);
			List<AccountApplicationViewModel> list1 = new ArrayList<AccountApplicationViewModel>();
			list1.add(application);
			model.addAttribute("listOfAccountApplications", list1);
			return "new-account-application";
		} catch (ApplicationNotFound e) {
			return "ApplicationNotFound";
		}

	}

	// The @RequestMapping annotation maps the /New_account_application URL to the getAccountApplicationByType method,
	// which accepts a Types object and a Model object as parameters
	@RequestMapping(value = "/newAccountApplication", method = RequestMethod.POST)
	public String getAccountApplicationByType(@RequestParam("Typevalue") String accountType, Model model) {
		String value = accountType;// get the account type value

		List<AccountApplicationViewModel> list1 = v.getAccountsappByType(value);

		// Check if the list is not empty before accessing the first object
		if (!list1.isEmpty()) {
			AccountApplicationViewModel firstAccount = list1.get(0);// get the first object of the list
			String acapActyId = firstAccount.getAcap_acty_id();
		}
		model.addAttribute("listOfAccountApplications", list1);

		return "new-account-application";
	}

	// =====================================================================================
	//
	@RequestMapping(value = "/anyTypeAccountInfo", method = RequestMethod.POST)
	public String viewAccounts(@RequestParam("Typevalue") String accountType, Model model) {
		// Retrieve the value from the validated 'Types' object
		String value = accountType;

		// Print the retrieved value to the console
		System.out.println(value);

		// Retrieve a list of AccountViewModel objects based on the value
		List<AccountViewModel> list1 = v.getAccountsByType(value);

		// Create an empty list to store Customertrail objects
		List<Customertrail> list2 = new ArrayList<>();
		System.out.println("print the list values");
		// Iterate through each AccountViewModel object in the list
		for (AccountViewModel account : list1) {
			// Retrieve the Customertrail object based on the customerId of the account
			Customertrail customer = cd.getRealCustomerById(account.getCustomerId());

			// Add the retrieved customer to the list of Customertrail objects
			list2.add(customer);
		}

		// Add the list of AccountViewModel objects and the list of Customertrail objects to the model
		model.addAttribute("list_of_account", list1);
		model.addAttribute("list_of_customer", list2);

		// Return the view name "Any_Type_account_info" to render the page
		return "any-type-account-info";
	}

	// ===============================================================================================
	@RequestMapping("/result")
	public String ne(Model model) {
		return "new-account-application";
	}

	// ===========================================================================================
	@RequestMapping("/accountNewApplicationForm")
	public String accountNewApplicationForm(Model model) {
		return "account-new-application-form";
	}

	// ===========================================================================================
	@RequestMapping("/anyTypeAccountInfo")
	public String anyTypeAccountInfo(Model model) {
		return "any-type-account-info";
	}
	// ===========================================================================================

	@RequestMapping(value = "/accountApplicationSave", method = RequestMethod.POST)
	public String accountApplicationSaveToDb(@Validated AccountApplicationInput accountApplication,
			HttpServletRequest request) {
		// Create a new instance of AccountApplication
		AccountApplication account = new AccountApplication();

		// Set the input model values of the account using the accountApplication object
		account.setInputModelValues(accountApplication);

		// Save the account to the database using the accountApplicationDaoInterface
		ac.save(account);

		// Retrieve the HttpSession object from the request
		HttpSession session = request.getSession();

		// Retrieve the username attribute from the session
		String username = (String) session.getAttribute("username");

		// Publish an AccountAppRequestEvent with the event message and username
		applicationEventPublisher.publishEvent(new AccountAppRequestEvent("New Application Form Filled", username));

		// Return the view name "Account_new_application_form" to render the page
		return "account-new-application-form";
	}

	// ===========================================================================================

	// ===================================================================================================
	@RequestMapping(value = "/saveToAccountDatabase", method = RequestMethod.POST)
	public String saveToAccountDatabase(@Validated AccountInput account, Model model, HttpServletRequest request) {
		// Create a new instance of Account
		Account a = new Account();

		// Set the input model values of the account using the accountInput object
		a.setInputModelValues(account);

		// Save the account to the database using the accountApplicationDaoInterface
		ac.saveAccount(a);

		// Retrieve the HttpSession object from the request
		HttpSession session = request.getSession();

		// Retrieve the username attribute from the session
		String username = (String) session.getAttribute("username");

		// Publish an AccountAppApprovalEvent with the event message and username
		applicationEventPublisher.publishEvent(new AccountAppApprovalEvent("Account Application Approved", username));

		// Change the return to the view name "Account_new_application_form" to render the page
		return "account-new-application-form";
	}

	@RequestMapping(value = "/saveToAccountDocumentsDatabase", method = RequestMethod.POST)
	public String saveToAccountDocumentsDatabase(@Validated AccountDocumentInput accountdocument, Model model) {
		// Create a new instance of Accountdocument
		Accountdocument ad = new Accountdocument();

		// Set the input model values of the accountdocument using the accountdocumentInput object
		ad.setInputModelValues(accountdocument);

		// Save the accountdocument to the database using the accountApplicationDaoInterface
		ac.saveAccountdocument(ad);

		// Change the return to the view name "Account_new_application_form" to render the page
		return "account-new-application-form";
	}

	// // ======================================================================================================

}
