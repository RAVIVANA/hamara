package com.nkxgen.spring.jdbc.DaoInterfaces;

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
	public Account getAccountById(int id);

	// Retrieves a LoanAccount object by its ID
	public LoanAccount getLoanAccountById(long acnt_id);

	public LoanApplication getLoanAccountApplicationById(long acnt_id);

	// Performs a money deposit transaction
	public void moneyDeposit(transactioninfo tempacc);

	// Performs a loan repayment transaction
	public void loanRepayment(tempRepayment temprr);

	// Performs a money withdrawal transaction
	public void moneyWithdrawl(transactioninfo tempacc);

	// Performs a loan withdrawal transaction
	public void loanWithdrawl(long id);

	public Transaction transactionSave(transactioninfo tarn);

	public void saveTransaction(Transaction t);

	public Transaction transactionSave1(transactioninfo tarn);

	// Retrieves a Customertrail object by loan ID
	public Customertrail getCustomerByLoanID(Long loanId);

	// Converts a LoanAccount object to EMIpay object
	public EMIpay changeToEMI(LoanAccount account);

	// Creates a LoanTransactions object based on repayment information
	public LoanTransactions loanTransactionRepay(tempRepayment tarn);

	// Saves a LoanTransactions object
	public void saveLoanTransaction(LoanTransactions t);

	public LoanTransactions loanTransactionWithdrawl(tempRepayment temp);

}