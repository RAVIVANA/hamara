package com.nkxgen.spring.jdbc.DaoInterfaces;

import javax.security.auth.login.AccountNotFoundException;

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

public interface TransactionsInterface {

	// Retrieves an Account object by its ID
	public Account getAccountById(int id) throws AccountNotFoundException;

	// Retrieves a LoanAccount object by its ID
	public LoanAccount getLoanAccountById(long acnt_id) throws LoanAccountNotFoundException;

	public LoanApplication getLoanAccountApplicationById(long acnt_id) throws LoanAccountApplicationNotFoundException;

	// Performs a money deposit transaction
	public void moneyDeposit(transactioninfo tempacc) throws AccountNotFoundException;

	// Performs a loan repayment transaction
	public void loanRepayment(tempRepayment temprr) throws InvalidLoanRepaymentException;

	// Performs a money withdrawal transaction
	public void moneyWithdrawl(transactioninfo tempacc) throws InsufficientBalanceException;

	// Performs a loan withdrawal transaction
	public void loanWithdrawl(long id) throws LoanWithdrawalException;

	public Transaction transactionSave(transactioninfo tarn) throws TransactionWithdrawlSaveException;

	public void saveTransaction(Transaction t) throws TransactionSaveException;

	public Transaction transactionSave1(transactioninfo tarn) throws TransactionDepositSaveException;

	// Retrieves a Customertrail object by loan ID
	public Customertrail getCustomerByLoanID(Long loanId) throws CustomerNotFoundException;

	// Converts a LoanAccount object to EMIpay object
	public EMIpay changeToEMI(LoanAccount account) throws EMIpayConversionException;

	// Creates a LoanTransactions object based on repayment information
	public LoanTransactions loanTransactionRepay(tempRepayment tarn) throws LoanTransactionRepayException;

	// Saves a LoanTransactions object
	public void saveLoanTransaction(LoanTransactions t) throws LoanTransactionSaveException;

	public LoanTransactions loanTransactionWithdrawl(tempRepayment temp) throws LoanTransactionWithdrawlException;

}