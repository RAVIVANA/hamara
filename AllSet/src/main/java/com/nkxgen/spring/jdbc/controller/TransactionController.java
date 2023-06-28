package com.nkxgen.spring.jdbc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nkxgen.spring.jdbc.Bal.CustomerSetter;
import com.nkxgen.spring.jdbc.DaoInterfaces.TransactionsInterface;
import com.nkxgen.spring.jdbc.events.TransactionEvent;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.Customertrail;
import com.nkxgen.spring.jdbc.model.EMIpay;
import com.nkxgen.spring.jdbc.model.LoanAccount;
import com.nkxgen.spring.jdbc.model.LoanTransactions;
import com.nkxgen.spring.jdbc.model.Transaction;
import com.nkxgen.spring.jdbc.model.tempRepayment;
import com.nkxgen.spring.jdbc.model.transactioninfo;;

@Controller
public class TransactionController {

	@Autowired
	TransactionsInterface ti;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private CustomerSetter s;

	// Mapping for money deposit form
	@RequestMapping(value = "/moneyDeposit", method = RequestMethod.GET)
	public String moneyDepositeForm(Model model) {
		return "money-deposit";
	}

	// Mapping for loan repayment form
	@RequestMapping(value = "/loanRepay", method = RequestMethod.GET)
	public String loanRepaymentForm(Model model) {
		return "loan-repayment";
	}

	// Mapping for money withdrawal form
	@RequestMapping(value = "/withdrawl", method = RequestMethod.GET)
	public String moneyWithdrawlForm(Model model) {
		return "money-withdrawl-form";
	}

	// Mapping for loan withdrawal form
	@RequestMapping(value = "/lowid", method = RequestMethod.GET)
	public String loWithdrawlForm(Model model) {
		return "loan-withdrawl-form";
	}

	// =================================================================================
	// money-deposit.html
	// Get account details for money deposit
	@RequestMapping(value = "/getAccountDetails", method = RequestMethod.POST)
	public String getAccountDetails(@RequestParam("accountNumber") int Acnt_id, Model model) {
		Account account = ti.getAccountById(Acnt_id); // Get the account details for the provided account number
		model.addAttribute("account", account); // Add the account object to the model

		return "sub-money-deposit"; // Return the name of the view to be rendered
	}

	// sub-money-deposit.html
	// Process money deposit
	@RequestMapping(value = "/moneyDepositUrl")
	public ResponseEntity<String> getDepositMoney(@Validated transactioninfo tarn, Model model,
			HttpServletRequest request) {
		ti.moneyDeposit(tarn); // Perform money deposit operation using the 'tarn' object
		Transaction t = ti.transactionSave1(tarn); // Save the transaction details using the 'tarn' object
		HttpSession session = request.getSession(); // Get the current session
		String username = (String) session.getAttribute("username"); // Get the username from the session
		// Publish a transaction event
		applicationEventPublisher.publishEvent(new TransactionEvent("Money Deposited ", username));
		ti.saveTransaction(t); // Save the transaction to the database

		return ResponseEntity.ok("deposit sucessfully"); // Return a response with a success message
	}

	// =================================================================================
	// money-withdrawl-form.html
	// Get account details for money withdrawal
	@RequestMapping(value = "/getAccountDetailsMoneyWithdrawl", method = RequestMethod.POST)
	public String getAccountDetailsForMoneyWithdrawl(@RequestParam("accountNumber") int Acnt_id, Model model) {
		Account account = ti.getAccountById(Acnt_id); // Get the account details for the provided account number
		model.addAttribute("account", account); // Add the account object to the model

		return "sub-money-withdrawl"; // Return the name of the view to be rendered
	}

	// sub-money-withdrawl.html
	// Process money withdrawal
	@RequestMapping(value = "/moneyWithDrawlUrl")
	public ResponseEntity<String> getMoneyWithdrawlAmount(@Validated transactioninfo tarn, Model model,
			HttpServletRequest request) {
		ti.moneyWithdrawl(tarn); // Perform money withdrawal based on the provided transaction info
		Transaction t = ti.transactionSave(tarn); // Save the transaction
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username"); // Retrieve the username from the session
		// Publish a transaction event
		applicationEventPublisher.publishEvent(new TransactionEvent("Money Withdrawed ", username));
		ti.saveTransaction(t); // Save the transaction to the database
		// Return a response entity with "Money withdrawal Successfully" message
		return ResponseEntity.ok("Money withdrawal Successfully");
	}

	// =====================================================================================================================
	// loan-withdrawl-form.html
	// Get loan account details for loan withdrawal
	@RequestMapping(value = "/getLoanDetails", method = RequestMethod.POST)
	public String getLoandetails(@RequestParam("accountNumber") long loan_id, Model model) {
		long acnt_id = loan_id; // Assign the loan_id to the acnt_id variable
		LoanAccount account = ti.getLoanAccountById(acnt_id); // Retrieve the LoanAccount object by its ID
		// Retrieve the Customertrail object associated with the loan account
		Customertrail customer = ti.getCustomerByLoanID(account.getCustomerId().getId());
		System.out.println("the value is" + account.getdeductionAmt());
		if (account.getdeductionAmt() == 0) { // Check if the deductionAmt property of the account is 0
			model.addAttribute("account", account); // Add the account object to the model attribute
			// Add the customer object to the model attribute with the name "customerss"
			model.addAttribute("customerss", customer);
			// Return the view name "sub_loan_withdrawl" to display the loan withdrawal form
			return "sub-loan-withdrawl";
		} else {
			return null; // Return null if the deductionAmt is not 0
		}
	}

	// sub-loan-withdrawl.html
	// Process loan withdrawal
	@RequestMapping(value = "/loanWithDrawlUrl", method = RequestMethod.POST)
	public ResponseEntity<String> getLoanmoneyWithdrawlAmount(@Validated transactioninfo tarn, Model model,
			HttpServletRequest request) {
		ti.loanWithdrawl(tarn.getAccountNumber()); // Perform the loan withdrawal operation based on the account number
		tempRepayment temp = s.setthistarn(tarn); // Set temporary repayment information using the tarn object
		// Create a LoanTransactions object based on the temprepayment
		LoanTransactions t = ti.LoanTransactionw(temp);
		HttpSession session = request.getSession(); // Get the HttpSession object from the request
		String username = (String) session.getAttribute("username"); // Get the username from the session attribute
		// Publish a TransactionEvent for loan withdrawal
		applicationEventPublisher.publishEvent(new TransactionEvent("Loan Withdrawed ", username));
		ti.saveLoanTransaction(t); // Save the LoanTransactions object
		return ResponseEntity.ok("Loan withdrawl Successfully"); // Return a ResponseEntity with a success message
	}

	// =====================================================================================================================
	// loan_repayment
	// Get loan account details for loan repayment
	@RequestMapping(value = "/getLoanRepaytDetails", method = RequestMethod.POST)
	public String getloanrepaytdetails(@RequestParam("accountNumber") long loan_id, Model model) {

		LoanAccount account = ti.getLoanAccountById(loan_id); // Retrieve the LoanAccount object based on the loan_id
		System.out.println("print the value: " + account.getInterestRate()); // Print the interest rate of the account
		if (account.getdeductionAmt() != 0) { // Check if the deduction amount is not zero
			EMIpay emiobj = ti.changeToEMI(account); // Convert the account to EMIpay object
			model.addAttribute("account", emiobj); // Add the emiobj to the model attribute
			return "sub-loan-repayment.html"; // Return the view for loan repayment
		} else {
			return null; // Return null if the deduction amount is zero
		}
	}

	// sub_loan_repayment
	// Process loan repayment
	@RequestMapping(value = "/loanRepaymentUrl")
	public ResponseEntity<String> getloanrepaymenAmount(@Validated tempRepayment tarn, Model model,
			HttpServletRequest request) {

		System.out.println("the value of tar complete :" + tarn.getComplete());
		ti.loanRepayment(tarn); // Perform the loan repayment based on the tempRepayment object
		// Create a LoanTransactions object based on the tempRepayment object
		LoanTransactions t = ti.LoanTransaction(tarn);
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		// Publish a Loan repayed event
		applicationEventPublisher.publishEvent(new TransactionEvent("Loan repayed ", username));
		ti.saveLoanTransaction(t); // Save the LoanTransactions object
		// Return a response entity indicating successful loan repayment
		return ResponseEntity.ok("Loan Repayed Successfully ");
	}

}