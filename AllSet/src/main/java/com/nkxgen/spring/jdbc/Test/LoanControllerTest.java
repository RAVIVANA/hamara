package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.LoanApplicationDaoInterface;
import com.nkxgen.spring.jdbc.Exception.AccountNotFound;
import com.nkxgen.spring.jdbc.Exception.ApplicationNotFound;
import com.nkxgen.spring.jdbc.InputModels.LoanApplicationInput;
import com.nkxgen.spring.jdbc.ViewModels.LoanAccountViewModel;
import com.nkxgen.spring.jdbc.ViewModels.LoanApplicationViewModel;
import com.nkxgen.spring.jdbc.ViewModels.LoanViewModel;
import com.nkxgen.spring.jdbc.controller.LoanController;
import com.nkxgen.spring.jdbc.events.LoanAppApprovalEvent;
import com.nkxgen.spring.jdbc.events.LoanAppRequestEvent;
import com.nkxgen.spring.jdbc.model.LoanApplication;

public class LoanControllerTest {

	@Mock
	private LoanApplicationDaoInterface loanApplicationDao;

	@Mock
	private CustomerDaoInterface customerDao;

	@Mock
	private ViewInterface viewInterface;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpSession session;

	@Mock
	private Model model;

	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private LoanController loanController;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testLoanNewApplicationForm() {
		// Arrange
		List<LoanViewModel> loans = new ArrayList<>();
		when(viewInterface.getAllLoans()).thenReturn(loans);

		// Act
		String result = loanController.loanNewApplicationForm(model);

		// Assert
		verify(model).addAttribute(eq("loan"), eq(loans));
		assert result.equals("loan-new-application-form");
	}

	@Test
	public void testRedirectForm() {
		// Arrange
		List<LoanApplicationViewModel> loanApplications = new ArrayList<>();
		when(viewInterface.getLoanApplicationsByStatus("redirecting")).thenReturn(loanApplications);

		// Act
		String result = loanController.redirectForm(model);

		// Assert
		verify(model).addAttribute(eq("loanApplications"), eq(loanApplications));
		assert result.equals("redirected-applications");
	}

	@Test
	public void testNewLoanApplication_Success() {
		// Arrange
		LoanApplicationInput loanApplicationInput = new LoanApplicationInput();
		loanApplicationInput.setLoanTypeId("Personal Loan");

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testUser");

		// Act
		String result = loanController.newLoanApplication(loanApplicationInput, model, request);

		// Assert
		verify(loanApplicationDao).saveLoanApplication(any(LoanApplication.class));
		verify(eventPublisher).publishEvent(any(LoanAppRequestEvent.class));
		assert result.equals("loan-new-application-form");
	}

	@Test
	public void testNewLoanApplication_Exception() {
		// Arrange
		LoanApplicationInput loanApplicationInput = new LoanApplicationInput();
		loanApplicationInput.setLoanTypeId("Personal Loan");
		loanApplicationInput.setTenureRequested(9);
		loanApplicationInput.setApplicationDate("2023-06-23");
		loanApplicationInput.setAmount(0);
		loanApplicationInput.setCreatedBy(1);
		loanApplicationInput.setCreatedDate("2023-06-23");
		loanApplicationInput.setCustId(2);
		loanApplicationInput.setEmiLimitFrom(3000);
		loanApplicationInput.setEmiLimitTo(5000);
		loanApplicationInput.setID(1);
		loanApplicationInput.setIntrest(3);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testUser");
		doThrow(new DataIntegrityViolationException("Foreign key violation occurred.")).when(loanApplicationDao)
				.saveLoanApplication(any(LoanApplication.class));

		// Act
		String result = loanController.newLoanApplication(loanApplicationInput, model, request);

		// Assert
		verify(model).addAttribute(eq("error"), eq("An error occurred while processing the new loan application."));
		assert result.equals("error-view");
	}

	@Test
	public void testUpdateLoanApplication_Success() {
		// Arrange
		LoanApplicationInput loanApplicationInput = new LoanApplicationInput();
		loanApplicationInput.setLoanTypeId("Personal Loan");

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testUser");

		// Act
		String result = loanController.updateLoanApplication(loanApplicationInput, request, model);

		// Assert
		verify(loanApplicationDao).updateLoanApplication(any(LoanApplicationInput.class));
		verify(eventPublisher).publishEvent(any(LoanAppApprovalEvent.class));
		assert result.equals("loan-approval");
	}

	@Test
	public void testUpdateLoanApplication_Exception() {
		// Arrange
		LoanApplicationInput loanApplicationInput = new LoanApplicationInput();
		loanApplicationInput.setLoanTypeId("PL");

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testUser");
		doThrow(new Exception()).when(loanApplicationDao).updateLoanApplication(any(LoanApplicationInput.class));

		// Act
		String result = loanController.updateLoanApplication(loanApplicationInput, request, model);

		// Assert
		verify(model).addAttribute(eq("error"), eq("An error occurred while updating the loan application."));
		assert result.equals("error-view");
	}

	@Test
	public void testGetLoanApplication_Success() {
		// Arrange
		List<LoanApplicationViewModel> loanApplications = new ArrayList<>();
		when(viewInterface.getLoanApplicationByValue(toString())).thenReturn(loanApplications);

		// Act
		String result = loanController.GetLoanApplication("Personal Loan", model);

		// Assert
		verify(model).addAttribute(eq("loanApplications"), eq(loanApplications));
		assert result.equals("loan-approval");
	}

	@Test
	public void testGetLoanAccounts_Success() {
		// Arrange
		List<LoanAccountViewModel> loanAccounts = new ArrayList<>();
		when(viewInterface.getLoanAccountsByLoanType(toString())).thenReturn(loanAccounts);

		// Act
		String result = loanController.GetLoanAccounts("Personal Loan", model);

		// Assert
		verify(model).addAttribute(eq("loanAccounts"), eq(loanAccounts));
		assert result.equals("loan-account-details");
	}

	@Test
	public void testDeleteLoanApplication_Success() {
		// Arrange
		int loanId = 1;

		// Act
		String result = loanController.deleteLoanApplication(loanId, model);

		// Assert
		verify(loanApplicationDao).deleteApplication(eq(loanId));
		assert result.equals("loan-approval");
	}

	@Test
	public void testDeleteLoanApplication_Exception() {
		// Arrange
		int loanId = 1;
		doThrow(new Exception()).when(loanApplicationDao).deleteApplication(eq(loanId));

		// Act
		String result = loanController.deleteLoanApplication(loanId, model);

		// Assert
		verify(model).addAttribute(eq("error"), eq("An error occurred while deleting the loan application."));
		assert result.equals("error-view");
	}

	@Test
	public void testApproveLoanApplication_Success() throws Exception {
		// Arrange
		int loanId = 1;
		Long customerId = 12345L;

		when(loanApplicationDao.getLoanApplicationByid(eq(loanId))).thenReturn(new LoanApplication());

		// Act
		String result = loanController.approveLoanApplication(loanId, customerId, model, request);

		// Assert
		verify(loanApplicationDao).getLoanApplicationByid(eq(loanId));
		verify(loanApplicationDao).saveTheApprovedLoanApplication(any(LoanApplication.class));
		verify(eventPublisher).publishEvent(any(LoanAppApprovalEvent.class));
		assert result.equals("loan-approval");
	}

	@Test
	public void testApproveLoanApplication_Exception() {
		// Arrange
		int loanId = 1;
		Long customerId = 120L;

		LoanApplication loanApplication = new LoanApplication();
		when(loanApplicationDao.getLoanApplicationByid(eq(loanId))).thenReturn(loanApplication);
		Mockito.doThrow(new Exception()).when(loanApplicationDao)
				.saveTheApprovedLoanApplication(any(LoanApplication.class));

		// Act
		String result = loanController.approveLoanApplication(loanId, customerId, model, request);

		// Assert
		verify(model).addAttribute(eq("error"), eq("An error occurred while approving the loan application."));
		assert result.equals("error-view");
	}

	@Test
	public void testGetLoanApplicationById_Success() throws ApplicationNotFound {
		// Arrange
		int loanId = 1;
		LoanApplicationViewModel loanApplication = new LoanApplicationViewModel();
		when(viewInterface.getLoanApplicationById(eq(loanId))).thenReturn(loanApplication);

		// Act
		String result = loanController.getLoanApplicationById(loanId, model);

		// Assert
		verify(model).addAttribute(eq("loanAccounts"), eq(loanApplication));
		assert result.equals("loan-approval");
	}

	@Test
	public void testGetLoanApplicationById_ApplicationNotFound() throws ApplicationNotFound {
		// Arrange
		int loanId = 1;
		when(viewInterface.getLoanApplicationById(eq(loanId))).thenThrow(new ApplicationNotFound(null));

		// Act
		String result = loanController.getLoanApplicationById(loanId, model);

		// Assert
		assert result.equals("AccountNotFound");
	}

	@Test
	public void testGetAccountById_Success() throws AccountNotFound {
		// Arrange
		int accountId = 1;
		LoanAccountViewModel loanAccount = new LoanAccountViewModel();
		when(viewInterface.getLoanAccountById(eq(accountId))).thenReturn(loanAccount);

		// Act
		String result = loanController.getAccountById(accountId, model);

		// Assert
		verify(model).addAttribute(eq("loanApplications"), eq(Collections.singletonList(loanAccount)));
		assert result.equals("loan-account-details");
	}

	@Test
	public void testGetAccountById_AccountNotFound() throws AccountNotFound {
		// Arrange
		int accountId = 1;
		when(viewInterface.getLoanAccountById(eq(accountId))).thenThrow(new AccountNotFound(null));

		// Act
		String result = loanController.getAccountById(accountId, model);

		// Assert
		assert result.equals("AccountNotFound");
	}
}
