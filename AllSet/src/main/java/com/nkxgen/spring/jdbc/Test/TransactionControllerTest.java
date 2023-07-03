package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.anyInt; // Import anyInt() method
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.DaoInterfaces.TransactionsInterface;
import com.nkxgen.spring.jdbc.Exception.AccountNotFoundException;
import com.nkxgen.spring.jdbc.controller.TransactionController;
import com.nkxgen.spring.jdbc.events.TransactionEvent;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.Transaction;
import com.nkxgen.spring.jdbc.model.transactioninfo;

public class TransactionControllerTest {

	@Mock
	private Model model;
	@Mock
	private TransactionsInterface ti;
	@InjectMocks
	private TransactionController transactionController;

	private HttpSession session;
	private HttpServletRequest request;
	private ApplicationEventPublisher eventPublisher;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		session = Mockito.mock(HttpSession.class);
		request = Mockito.mock(HttpServletRequest.class);
		eventPublisher = Mockito.mock(ApplicationEventPublisher.class);

	}

	@Test
	public void moneyDepositeForm_ValidModel_ReturnsMoneyDepositView() {
		// Act
		String viewName = transactionController.moneyDepositeForm(model);

		// Assert
		assertEquals("money-deposit", viewName);
	}

	@Test
	public void loanRepaymentForm_ValidModel_ReturnsLoanRepaymentView() {
		// Act
		String viewName = transactionController.loanRepaymentForm(model);

		// Assert
		assertEquals("loan-repayment", viewName);
	}

	@Test
	public void moneyWithdrawlForm_ValidModel_ReturnsMoneyWithdrawlFormView() {
		// Act
		String viewName = transactionController.moneyWithdrawlForm(model);

		// Assert
		assertEquals("money-withdrawl-form", viewName);
	}

	@Test
	public void loWithdrawlForm_ValidModel_ReturnsLoanWithdrawlFormView() {
		// Act
		String viewName = transactionController.loWithdrawlForm(model);

		// Assert
		assertEquals("loan-withdrawl-form", viewName);
	}

	@Test
	public void getAccountDetails_ValidAccount_ReturnsSubMoneyDepositView() throws AccountNotFoundException {
		// Arrange
		int accountNumber = 12345;
		Account account = new Account(); // Create a mock Account object

		// Mock the behavior of the TransactionsInterface to return the mock Account object
		try {
			when(ti.getAccountById(anyInt())).thenReturn(account);
		} catch (javax.security.auth.login.AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Act
		String viewName = transactionController.getAccountDetails(accountNumber, model);

		// Assert
		assertEquals("sub-money-deposit", viewName);
		// Verify that the Account object was added to the model
		verify(model).addAttribute("account", account);
	}

	@Test
	public void testGetDepositMoney_Success() throws Exception {
		// Arrange
		TransactionsInterface ti = Mockito.mock(TransactionsInterface.class);
		TransactionController controller = new TransactionController();
		controller.setTi(ti);
		Model model = Mockito.mock(Model.class);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(session.getAttribute("username")).thenReturn("testuser");

		transactioninfo tarn = new transactioninfo(); // Create the transaction info object
		ResponseEntity<String> expectedResponse = ResponseEntity.ok("deposit successfully");

		Mockito.doNothing().when(ti).moneyDeposit(tarn);
		Mockito.when(ti.transactionSave1(tarn)).thenReturn(new Transaction());
		Mockito.doNothing().when(ti).saveTransaction(Mockito.any(Transaction.class));
		Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(TransactionEvent.class));

		// Act
		ResponseEntity<String> response = controller.getDepositMoney(tarn, model, request);
		System.out.println("Actual response: " + response.getBody()); // Print actual response

		// Assert
		Assert.assertEquals(response.getBody(), expectedResponse.getBody());
	}

}
