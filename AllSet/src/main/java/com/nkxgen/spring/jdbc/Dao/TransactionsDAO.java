package com.nkxgen.spring.jdbc.Dao;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nkxgen.spring.jdbc.Bal.CustomerSetter;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.TransactionsInterface;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.Customertrail;
import com.nkxgen.spring.jdbc.model.EMIpay;
import com.nkxgen.spring.jdbc.model.LoanAccount;
import com.nkxgen.spring.jdbc.model.LoanApplication;
import com.nkxgen.spring.jdbc.model.LoanTransactions;
import com.nkxgen.spring.jdbc.model.Transaction;
import com.nkxgen.spring.jdbc.model.tempRepayment;
import com.nkxgen.spring.jdbc.model.transactioninfo;

@Repository
@Transactional
public class TransactionsDAO implements TransactionsInterface {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CustomerSetter s;

	@Autowired
	private CustomerDaoInterface cd;

	@Override
	public void moneyDeposit(transactioninfo trans) {
		// Find the account object with the given account number using the entity manager
		Account account = entityManager.find(Account.class, (long) trans.getAccountNumber());

		// Calculate the new balance by adding the deposit amount to the current balance
		long balance = (long) (account.getBalance() + trans.getAmount());

		account.setBal(balance); // Update the balance of the account with the new balance

	}

	@Override
	public void moneyWithdrawl(transactioninfo trans) {

		// Find the account object with the given account number using the entity manager
		Account account = entityManager.find(Account.class, (long) trans.getAccountNumber());

		// Check if the account balance is sufficient for the withdrawal
		if (account.getBalance() >= (long) trans.getAmount()) {

			// Calculate the new balance by subtracting the withdrawal amount from the current balance
			long balance = (long) (account.getBalance() - (long) trans.getAmount());

			account.setBal(balance); // Update the balance of the account with the new balance
		} else {

			// Print a message indicating that there is no sufficient balance for the withdrawal
			System.out.println("no sufficient balance");
		}

	}

	public Account getAccountById(int id) {
		// Find the account object with the given ID using the entity manager
		Account account = entityManager.find(Account.class, (long) id);
		return account; // Return the found account object
	}

	@Override
	public Transaction transactionSave(transactioninfo tarn) {
		// Create a new Transaction object by calling a method 'transactionSet' from another class or service, passing a
		// transactioninfo object
		Transaction t = s.transactionSet(tarn);
		t.setTran_mode(tarn.getMode());
		t.setTran_type("Withdrawl"); // Set the transaction type to "Withdrawl"

		return t; // Return the created Transaction object
	}

	@Override
	public void saveTransaction(Transaction transaction) {
		// Persist the provided Transaction object by adding it to the entity manager, allowing it to be saved in the
		// data store
		entityManager.persist(transaction);
	}

	@Override
	// save deposit transactions
	public Transaction transactionSave1(transactioninfo tarn) {
		// Create a new Transaction object by calling a method 'transactionSet' from another class or service, passing a
		// transactioninfo object
		Transaction t = s.transactionSet(tarn);

		t.setTran_type("deposit"); // Set the transaction type to "deposit"
		return t; // Return the created Transaction object
	}

	// =================================================================
	@Override
	public LoanAccount getLoanAccountById(long id) {
		// Find the loan account object with the given ID using the entity manager
		LoanAccount account = entityManager.find(LoanAccount.class, id);
		return account; // Return the found loan account object
	}

	// =================================================================
	// =================================================================
	@Override
	public LoanApplication getLoanAccountApplicationById(long id) {
		LoanApplication account = entityManager.find(LoanApplication.class, (long) id);

		return account; // Return the found loan account object
	}
	// =================================================================
	// =================================================================

	@Override
	public void loanWithdrawl(long id) {

		// Find the loan account object with the given ID using the entity manager
		LoanAccount account = entityManager.find(LoanAccount.class, id);
		if (account.getdeductionAmt() == 0) {
			account.setdeductionAmt(account.getLoanAmount()); // Set the deduction amount to the loan amount
		} else {
			System.out.println("already withdrawal over");
		}
	}

	@Override
	public void loanRepayment(tempRepayment trans) {

		// Find the loan account object with the given loan ID using the entity manager
		LoanAccount account = entityManager.find(LoanAccount.class, (long) trans.getLoanid());

		int x = (int) Math.ceil(trans.getEMI()); // Calculate the EMI value and round it up to the nearest integer
		if (trans.getAmount() == trans.getTotal()) { // If the repayment amount is equal to the total amount

			account.setdeductionAmt(account.getdeductionAmt() - x); // Update the due balance by subtracting the EMI
																	// amount
		} else if (trans.getAmount() == trans.getComplete()) { // If the repayment amount is equal to the complete
																// amount
			account.setdeductionAmt(0); // Set the due balance to 0
		} else {
			System.out.println("Inavlid data"); // Print an error message for invalid data
		}
	}

	@Override
	public Customertrail getCustomerByLoanID(Long loanId) {
		Customertrail t = cd.getCustomerById(loanId); // Retrieve the customer trail object with the given loan ID using
														// a method 'getCustomerById' from another class or service
		return t; // Return the retrieved customer trail object
	}

	@Override
	public EMIpay changeToEMI(LoanAccount account) {
		EMIpay obj = s.changeToEmiObj(account); // Create a new EMIpay object by calling a method 'changeToEmiObj' from
												// another class or service, passing the loan account object
		return obj; // Return the created EMIpay object
	}

	@Override
	public LoanTransactions loanTransactionRepay(tempRepayment lt) {
		LoanTransactions t = s.loantransactionSet(lt); // Create a new LoanTransactions object by calling a method
														// 'loantransactionSet' from another class or service
		t.setDate(LocalDate.now().toString()); // Set the date of the loan transaction to the current date
		t.setType("emi pay"); // Set the type of the loan transaction to "emi pay"
		return t; // Return the created LoanTransactions object
	}

	@Override
	public void saveLoanTransaction(LoanTransactions lt) {
		entityManager.persist(lt); // Persist the provided LoanTransactions object by adding it to the entity manager
	}

	@Override
	public LoanTransactions loanTransactionWithdrawl(tempRepayment temp) {
		LoanTransactions t = s.loantransactionSet(temp); // Create a new LoanTransactions object by calling a method
															// 'loantransactionSet' from another class or service
		t.setDate(LocalDate.now().toString()); // Set the date of the loan transaction to the current date
		t.setType("loan withdrawl"); // Set the type of the loan transaction to "loan withdrawl"
		return t; // Return the created LoanTransactions object
	}

}