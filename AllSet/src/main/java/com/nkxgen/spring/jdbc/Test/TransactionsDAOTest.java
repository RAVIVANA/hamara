package com.nkxgen.spring.jdbc.Test;

import java.time.LocalDate;

import org.testng.annotations.*;


import javax.persistence.EntityManager;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Bal.CustomerSetter;
import com.nkxgen.spring.jdbc.Dao.TransactionsDAO;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.TransactionsInterface;
import com.nkxgen.spring.jdbc.Exception.AccountNotFoundException;
import com.nkxgen.spring.jdbc.Exception.CustomerNotFoundException;
import com.nkxgen.spring.jdbc.Exception.InvalidLoanRepaymentException;
import com.nkxgen.spring.jdbc.Exception.LoanAccountApplicationNotFoundException;
import com.nkxgen.spring.jdbc.Exception.LoanAccountNotFoundException;
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

public class TransactionsDAOTest {
	private LoanAccount loanAccount;
	@Mock
	private CustomerDaoInterface cd;
	@Mock
	private EntityManager entityManager;
	@Mock
	private TransactionsInterface ti;
	@InjectMocks
	private TransactionsDAO transactionsDAO;

	@Mock
	private Transaction transactionService;
	@Mock
	private CustomerSetter s;
	@Mock
	EMIpay obj1;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMoneyDeposit() throws AccountNotFoundException {
		// Prepare test data
		long accountNumber = 12345;
		double amount = 100.0;
		Account account = Mockito.mock(Account.class);
		Mockito.when(account.getBalance()).thenReturn((long) 500.0);

		transactioninfo transaction = new transactioninfo();
		transaction.setAccountNumber((int) accountNumber);
		transaction.setAmount((int) amount);

		// Configure mock behavior
		Mockito.when(entityManager.find(Account.class, accountNumber)).thenReturn(account);

		// Perform the test
		try {
			transactionsDAO.moneyDeposit(transaction);
		} catch (javax.security.auth.login.AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Verify the results
		Mockito.verify(account).setBal(Mockito.eq((long) 600.0)); // Assert that the balance was updated correctly
	}

	@Test
	public void testGetAccountById() throws AccountNotFoundException {
		// Create a mock Account object
		Account account = Mockito.mock(Account.class);

		// Set up test data
		int accountId = 1;

		// Mock the behavior of the EntityManager
		Mockito.when(entityManager.find(Account.class, (long) accountId)).thenReturn(account);

		// Invoke the method being tested
		Account result;
		try {
			result = transactionsDAO.getAccountById(accountId);
			Assert.assertEquals(account, result);

		} catch (javax.security.auth.login.AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Verify that the EntityManager's find method was called with the correct arguments
		Mockito.verify(entityManager).find(Account.class, (long) accountId);
	}

	@Test
	public void testTransactionSave() throws TransactionWithdrawlSaveException {
		// Set up test data
		transactioninfo tarn = new transactioninfo();
		tarn.setMode("Online");

		// Create a mock Transaction object
		Transaction transaction = Mockito.mock(Transaction.class);

		// Mock the behavior of the TransactionService
		Mockito.when(s.transactionSet(tarn)).thenReturn(transaction);
		Mockito.when(transaction.getTran_mode()).thenReturn(tarn.getMode());

		// Perform the transaction save
		Transaction result = transactionsDAO.transactionSave(tarn);

		// Verify that the TransactionService method was called
		Mockito.verify(s).transactionSet(tarn);

		// Verify that the transaction type and mode were set correctly
		Mockito.verify(transaction).setTran_type("Withdrawl");
		Mockito.verify(transaction).setTran_mode(tarn.getMode());

		// Verify that the result is the same as the mocked transaction
		Assert.assertEquals(transaction, result);
	}

	@Test
	public void testSaveTransaction() throws TransactionSaveException {
		// Create a mock Transaction object
		Transaction transaction = Mockito.mock(Transaction.class);

		// Perform the transaction save
		transactionsDAO.saveTransaction(transaction);

		// Verify that the persist method of the entity manager was called with the transaction object
		Mockito.verify(entityManager).persist(transaction);
	}

	@Test
	public void testTransactionSave1() throws TransactionDepositSaveException {
		// Create a mock Transaction object
		Transaction transaction = Mockito.mock(Transaction.class);

		// Create a mock transactioninfo object
		transactioninfo tarn = Mockito.mock(transactioninfo.class);

		// Mock the behavior of the transactionService
		Mockito.when(s.transactionSet(tarn)).thenReturn(transaction);

		// Perform the transaction save
		Transaction result = transactionsDAO.transactionSave1(tarn);

		// Verify that the transactionSet method of the transactionService was called with the transactioninfo object
		Mockito.verify(s).transactionSet(tarn);

		// Verify that the transaction type was set to "deposit"
		Mockito.verify(transaction).setTran_type("deposit");

		// Verify that the returned Transaction object is the same as the mocked Transaction object
		Assert.assertEquals(result, transaction);
	}

	@Test(expectedExceptions = LoanAccountNotFoundException.class)
	public void testGetLoanAccountByIdNotFound() throws LoanAccountNotFoundException {
		// Mock the behavior of the EntityManager to return null
		Mockito.when(entityManager.find(LoanAccount.class, 1)).thenReturn(null);

		LoanAccount result = transactionsDAO.getLoanAccountById(1);
		LoanAccount loanaccount = new LoanAccount();
		Assert.assertEquals(result, loanaccount);
		// Perform the getLoanAccountById operation, expecting LoanAccountNotFoundException
	}

	@Test
	public void testGetLoanAccountApplicationById() {
		// Create a mock LoanApplication object
		LoanApplication loanApplication = Mockito.mock(LoanApplication.class);

		// Define the loan application ID for testing
		long loanApplicationId = 123L;

		// Mock the behavior of the EntityManager to return the mock LoanApplication object
		Mockito.when(entityManager.find(LoanApplication.class, loanApplicationId)).thenReturn(loanApplication);

		try {
			// Perform the getLoanAccountApplicationById operation
			LoanApplication result = transactionsDAO.getLoanAccountApplicationById(loanApplicationId);

			// Verify that the returned LoanApplication matches the mock object
			Assert.assertEquals(result, loanApplication);
		} catch (LoanAccountApplicationNotFoundException e) {
			// Handle the exception if needed
			e.printStackTrace();
		}
	}

	@Test
	public void testLoanWithdrawal() {
		// Create a mock LoanAccount object
		LoanAccount account = Mockito.mock(LoanAccount.class);

		// Define the loan account ID for testing
		long loanAccountId = 123L;

		// Define the loan amount for testing
		long loanAmount = 1000L;

		// Mock the behavior of the EntityManager to return the mock LoanAccount object
		Mockito.when(entityManager.find(LoanAccount.class, loanAccountId)).thenReturn(account);
		Mockito.when(account.getdeductionAmt()).thenReturn(0);
		Mockito.when(account.getLoanAmount()).thenReturn(loanAmount);

		try {
			// Perform the loanWithdrawl operation
			transactionsDAO.loanWithdrawl(loanAccountId);

			// Verify that the setdeductionAmt method was called with the loan amount
			Mockito.verify(account).setdeductionAmt(loanAmount);
		} catch (LoanWithdrawalException e) {
			// Handle the exception if needed
			e.printStackTrace();
		}
	}

	@Test
	public void testLoanRepayment() {
		// Create a mock LoanAccount object
		LoanAccount account = Mockito.mock(LoanAccount.class);

		// Define the loan ID for testing
		long loanId = 123L;

		// Create a tempRepayment object with the required values for testing
		tempRepayment repayment = new tempRepayment();
		repayment.setLoanid((int) loanId);
		repayment.setAmount((int) 1000.0);
		repayment.setTotal((int) 2000.0);
		repayment.setComplete((int) 2000.0);
		repayment.setPastdue((int) 1000.0);
		repayment.setEMI(500.0);

		// Mock the behavior of the EntityManager to return the mock LoanAccount object
		Mockito.when(entityManager.find(LoanAccount.class, loanId)).thenReturn(account);
		Mockito.when(account.getdeductionAmt()).thenReturn((int) 1000L);

		try {
			// Perform the loanRepayment operation
			transactionsDAO.loanRepayment(repayment);

			// Verify that the setdeductionAmt method was called with the updated due balance
			Mockito.verify(account).setdeductionAmt(Mockito.anyLong());
		} catch (InvalidLoanRepaymentException e) {
			// Handle the exception if needed
			e.printStackTrace();
		}
	}

	@Test
	public void testLoanTransactionWithdrawl() {
		// Create a mock LoanTransactions object
		LoanTransactions transaction = Mockito.mock(LoanTransactions.class);

		// Create a tempRepayment object with the required values for testing
		tempRepayment temp = new tempRepayment();
		temp.setLoanid((int) 123L);

		// Mock the behavior of the loantransactionSet method to return the mock LoanTransactions object
		Mockito.when(s.loantransactionSet(temp)).thenReturn(transaction);
		Mockito.when(transaction.getDate()).thenReturn(LocalDate.now().toString());
		Mockito.when(transaction.getType()).thenReturn("loan withdrawl");

		try {
			// Perform the loanTransactionWithdrawl operation
			LoanTransactions result = transactionsDAO.loanTransactionWithdrawl(temp);

			// Verify that the loantransactionSet method was called with the tempRepayment object
			Mockito.verify(s).loantransactionSet(temp);

			// Verify that the setDate and setType methods were called on the LoanTransactions object
			Mockito.verify(transaction).setDate(Mockito.anyString());
			Mockito.verify(transaction).setType(Mockito.anyString());

			// Verify that the returned LoanTransactions object matches the mock object
			Assert.assertEquals(result, transaction);
		} catch (LoanTransactionWithdrawlException e) {
			// Handle the exception if needed
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveLoanTransaction() {
		// Create a mock LoanTransactions object
		LoanTransactions loanTransaction = Mockito.mock(LoanTransactions.class);

		try {
			// Perform the saveLoanTransaction operation
			transactionsDAO.saveLoanTransaction(loanTransaction);

			// Verify that the persist method was called on the entity manager with the LoanTransactions object
			Mockito.verify(entityManager).persist(loanTransaction);
		} catch (LoanTransactionSaveException e) {
			// Handle the exception if needed
			e.printStackTrace();
		}
	}

	@Test
	public void testGetCustomerByLoanID() {
		try {
			// Set up test data
			long loanId = 12;
			Customertrail expectedCustomerTrail = new Customertrail(/* set necessary properties */);
			// Set up the behavior of the mock CustomerDaoInterface
			Mockito.when(cd.getCustomerById(loanId)).thenReturn(expectedCustomerTrail);

			// Call the getCustomerByLoanID method
			Customertrail result = transactionsDAO.getCustomerByLoanID(loanId);

			// Verify that the getCustomerById method was called on the mock CustomerDaoInterface with the loanId
			Mockito.verify(cd).getCustomerById(loanId);

			// Verify that the expected customer trail object is returned
			Assert.assertEquals(result, expectedCustomerTrail);
		} catch (CustomerNotFoundException e) {
			// Handle the exception if needed
			e.printStackTrace();
		}
	}

}
