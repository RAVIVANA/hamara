package com.nkxgen.spring.jdbc.Dao;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nkxgen.spring.jdbc.Bal.CustomerSetter;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.TransactionsInterface;
import com.nkxgen.spring.jdbc.Exception.CustomerNotFoundException;
import com.nkxgen.spring.jdbc.Exception.EMIpayConversionException;
import com.nkxgen.spring.jdbc.Exception.InsufficientBalanceException;
import com.nkxgen.spring.jdbc.Exception.InvalidLoanRepaymentException;
import com.nkxgen.spring.jdbc.Exception.LoanAccountApplicationNotFoundException;
import com.nkxgen.spring.jdbc.Exception.LoanAccountNotFoundException;
import com.nkxgen.spring.jdbc.Exception.LoanTransactionRepayException;
import com.nkxgen.spring.jdbc.Exception.LoanTransactionSaveException;
import com.nkxgen.spring.jdbc.Exception.LoanTransactionWithdrawlException;
import com.nkxgen.spring.jdbc.Exception.LoanWithdrawalException;
import com.nkxgen.spring.jdbc.Exception.TransactionDepositSaveException;
import com.nkxgen.spring.jdbc.Exception.TransactionSaveException;
import com.nkxgen.spring.jdbc.Exception.TransactionWithdrawlSaveException;
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
	private TransactionsInterface ti;

	@Autowired
	private CustomerDaoInterface cd;

	@Override
	public void moneyDeposit(transactioninfo trans) throws AccountNotFoundException {
		// Find the account object with the given account number using the entity manager
		Account account = entityManager.find(Account.class, (long) trans.getAccountNumber());

		if (account == null) {
			throw new AccountNotFoundException("Account not found with ID: " + trans.getAccountNumber());
		}
		// Calculate the new balance by adding the deposit amount to the current balance
		long balance = (long) (account.getBalance() + trans.getAmount());

		account.setBal(balance); // Update the balance of the account with the new balance

	}

	@Override
	public void moneyWithdrawl(transactioninfo trans) throws InsufficientBalanceException {

		// Find the account object with the given account number using the entity manager
		Account account = entityManager.find(Account.class, (long) trans.getAccountNumber());

		// Check if the account balance is sufficient for the withdrawal
		if (account.getBalance() >= (long) trans.getAmount()) {

			// Calculate the new balance by subtracting the withdrawal amount from the current balance
			long balance = (long) (account.getBalance() - (long) trans.getAmount());

			account.setBal(balance); // Update the balance of the account with the new balance
		} else {
			// Throw an InsufficientBalanceException indicating insufficient balance for the withdrawal
			throw new InsufficientBalanceException("Insufficient balance for withdrawal");
		}

	}

	@Override
	public Account getAccountById(int id) throws AccountNotFoundException {
		// Find the account object with the given ID using the entity manager
		Account account = entityManager.find(Account.class, (long) id);
		if (account == null) {
			throw new AccountNotFoundException("Account not found with ID: " + id);
		}
		return account; // Return the found account object
	}

	@Override
	public Transaction transactionSave(transactioninfo tarn) throws TransactionWithdrawlSaveException {
		// Create a new Transaction object by calling a method 'transactionSet' from another class or service, passing a
		// transactioninfo object
		try {
			Transaction t = s.transactionSet(tarn);
			t.setTran_mode(tarn.getMode());
			t.setTran_type("Withdrawl"); // Set the transaction type to "Withdrawl"

			return t; // Return the created Transaction object
		} catch (Exception e) {
			throw new TransactionWithdrawlSaveException("Failed to save transaction" + e);
		}
	}

	@Override
	public void saveTransaction(Transaction transaction) throws TransactionSaveException {
		// Persist the provided Transaction object by adding it to the entity manager, allowing it to be saved in the
		// data store
		try {
			entityManager.persist(transaction);
		} catch (Exception e) {
			throw new TransactionSaveException("Failed to save transaction" + e);
		}
	}

	@Override
	// save deposit transactions
	public Transaction transactionSave1(transactioninfo tarn) throws TransactionDepositSaveException {
		// Create a new Transaction object by calling a method 'transactionSet' from another class or service, passing a
		// transactioninfo object
		try {
			Transaction t = s.transactionSet(tarn);

			t.setTran_type("deposit"); // Set the transaction type to "deposit"
			return t; // Return the created Transaction object
		} catch (Exception e) {
			throw new TransactionDepositSaveException("Failed to save transaction" + e);
		}
	}

	// =================================================================
	@Override
	public LoanAccount getLoanAccountById(long id) throws LoanAccountNotFoundException {
		// Find the loan account object with the given ID using the entity manager
		LoanAccount account = entityManager.find(LoanAccount.class, id);
		if (account == null) {
			throw new LoanAccountNotFoundException("Loan account not found with ID: " + id);
		}
		return account; // Return the found loan account object
	}

	// =================================================================
	@Override
	public LoanApplication getLoanAccountApplicationById(long id) throws LoanAccountApplicationNotFoundException {
		LoanApplication account = entityManager.find(LoanApplication.class, (long) id);
		if (account == null) {
			throw new LoanAccountApplicationNotFoundException("Loan account not found with ID: " + id);
		}
		return account; // Return the found loan account object
	}
	// =================================================================

	@Override
	public void loanWithdrawl(long id) throws LoanWithdrawalException {

		// Find the loan account object with the given ID using the entity manager
		LoanAccount account = entityManager.find(LoanAccount.class, id);
		if (account.getdeductionAmt() == 0) {
			account.setdeductionAmt(account.getLoanAmount()); // Set the deduction amount to the loan amount
		} else {
			throw new LoanWithdrawalException("Loan withdrawal has already been made");
		}

	}

	@Override
	public void loanRepayment(tempRepayment trans) throws InvalidLoanRepaymentException {

		// Find the loan account object with the given loan ID using the entity manager
		LoanAccount account = entityManager.find(LoanAccount.class, (long) trans.getLoanid());

		int x = (int) Math.ceil(trans.getEMI()); // Calculate the EMI value and round it up to the nearest integer
		if (trans.getAmount() == trans.getTotal()) { // If the repayment amount is equal to the total amount

			account.setdeductionAmt(account.getdeductionAmt() - x); // Update the due balance by subtracting the EMI
																	// amount
		} else if (trans.getAmount() == trans.getComplete()) { // If the repayment amount is equal to the complete
																// amount
			account.setdeductionAmt(0); // Set the due balance to 0
		} else if (trans.getAmount() == trans.getPastdue()) {
			double y = (double) trans.getPastdue() / trans.getTotal();
			double z = y * trans.getEMI();
			int xy = (int) z;
			account.setdeductionAmt(account.getdeductionAmt() - xy);
		} else {
			throw new InvalidLoanRepaymentException("Invalid loan repayment amount");
		}
	}

	@Override
	public Customertrail getCustomerByLoanID(Long loanId) throws CustomerNotFoundException {
		Customertrail t = cd.getCustomerById(loanId); // Retrieve the customer trail object with the given loan ID using
														// a method 'getCustomerById' from another class or service
		if (t == null) {
			throw new CustomerNotFoundException("Customer trail not found for loan ID: " + loanId);
		}
		return t; // Return the retrieved customer trail object
	}

	@Override
	public EMIpay changeToEMI(LoanAccount account) throws EMIpayConversionException {
		EMIpay obj = s.changeToEmiObj(account,ti); // Create a new EMIpay object by calling a method 'changeToEmiObj' from
												// another class or service, passing the loan account object
		if (obj == null) {
			throw new EMIpayConversionException("Failed to convert LoanAccount to EMIpay");
		}
		return obj; // Return the created EMIpay object
	}

	@Override
	public LoanTransactions loanTransactionRepay(tempRepayment lt) throws LoanTransactionRepayException {
		try {
			LoanTransactions t = s.loantransactionSet(lt); // Create a new LoanTransactions object by calling a method
															// 'loantransactionSet' from another class or service
			t.setDate(LocalDate.now().toString()); // Set the date of the loan transaction to the current date
			t.setType("emi pay"); // Set the type of the loan transaction to "emi pay"
			return t; // Return the created LoanTransactions object
		} catch (Exception e) {
			throw new LoanTransactionRepayException("Failed to perform loan transaction repayment" + e);
		}
	}

	@Override
	public void saveLoanTransaction(LoanTransactions lt) throws LoanTransactionSaveException {
		try {
			entityManager.persist(lt); // Persist the provided LoanTransactions object by adding it to the entity
										// manager
		} catch (Exception e) {
			throw new LoanTransactionSaveException("Failed to save LoanTransactions" + e);
		}
	}

	@Override
	public LoanTransactions loanTransactionWithdrawl(tempRepayment temp) throws LoanTransactionWithdrawlException {
		try {
			LoanTransactions t = s.loantransactionSet(temp); // Create a new LoanTransactions object by calling a method
			// 'loantransactionSet' from another class or service
			t.setDate(LocalDate.now().toString()); // Set the date of the loan transaction to the current date
			t.setType("loan withdrawl"); // Set the type of the loan transaction to "loan withdrawl"
			return t; // Return the created LoanTransactions object
		} catch (Exception e) {
			throw new LoanTransactionWithdrawlException("Failed to perform loan transaction withdrawal" + e);
		}

	}

}