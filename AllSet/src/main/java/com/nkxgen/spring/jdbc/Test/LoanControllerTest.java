package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.any;

import org.testng.Assert;
import org.testng.annotations.*;
import com.nkxgen.spring.jdbc.model.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.Dao.PermissionsDAO;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.LoanApplicationDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.PermissionsDAOInterface;
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

import customEception.LoanApplicationSaveException;

public class LoanControllerTest {

	@Mock
	private LoanApplicationDaoInterface loanApplicationDao;

	@Mock
	private CustomerDaoInterface customerDao;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
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
	    private PermissionsDAOInterface permissionsDAO;
	@Mock
	private BindingResult bindingResult;


	    @Mock
	    private HttpServletResponse response;
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
        loanApplicationInput.setAmount((long)1000.0);
        loanApplicationInput.setTenureRequested(12);
        loanApplicationInput.setCreatedDate("2023-01-10");

        // Mock the behavior of the dependencies
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("user123");

        // Act
        String result = loanController.newLoanApplication(loanApplicationInput, model, request);

        // Assert
        verify(loanApplicationDao).saveLoanApplication(any(LoanApplication.class));
        verify(session).getAttribute("username");
        Assert.assertEquals(result, "loan-new-application-form");
    }



    @Test
    public void testUpdateLoanApplication_Success() {
        // Arrange
        LoanApplicationInput loanApplicationInput = new LoanApplicationInput();
        loanApplicationInput.setLoanTypeId("personal");
        loanApplicationInput.setAmount((long)1000.0);

        // Mock the behavior of the dependencies
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("user123");

        // Act
        String result = loanController.updateLoanApplication(loanApplicationInput, request, model);

        // Assert
        verify(loanApplicationDao).updateLoanApplication(any(LoanApplicationInput.class));
        verify(session).getAttribute("username");
        Assert.assertEquals(result, "loan-approval");
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
	    public void testGetLoanApplication_PermissionGranted() {
	        // Arrange
	        String accountType = "Savings";
	        List<LoanApplicationViewModel> list = new ArrayList<>();
	        List<LoanApplicationViewModel> filteredList = new ArrayList<>();
	        String username = "1";
	        Permission permission = new Permission();
	        permission.setUserId((long)1);
	        permission.setApplication(true);

	        // Mock the behavior of the dependencies
	        when(viewInterface.getLoanApplicationByValue(accountType)).thenReturn(list);
	        when(request.getSession()).thenReturn(session);
	        when(session.getAttribute("username")).thenReturn(username);
	        when(permissionsDAO.getPermissions(Long.parseLong(username))).thenReturn(permission);

	        // Act
	        String result = loanController.GetLoanApplication(accountType, model, request, response);

	        // Assert
	        verify(viewInterface).getLoanApplicationByValue(accountType);
	        verify(permissionsDAO).getPermissions(Long.parseLong(username));
	        verify(model).addAttribute(eq("loanApplications"), any(List.class));
	        Assert.assertEquals(result, "loan-approval");
	    }
	 @Test
	    public void testGetLoanApplication_PermissionDenied() {
	        // Arrange
	        String accountType = "Savings";
	        String username = "123";
	        Permission permission = new Permission();
	        permission.setUserId((long)123);
	        permission.setApplication(false);

	        // Mock the behavior of the dependencies
	        when(request.getSession()).thenReturn(session);
	        when(session.getAttribute("username")).thenReturn(username);
	        when(permissionsDAO.getPermissions(Long.parseLong(username))).thenReturn(permission);

	        // Act
	        String result = loanController.GetLoanApplication(accountType, model, request, response);

	        // Assert
	        verify(permissionsDAO).getPermissions(Long.parseLong(username));
	        Assert.assertEquals(result, "not-permitted");
	    }
	    @Test
	    public void testGetLoanAccounts_PermissionGranted() {
	        // Arrange
	        String accountType = "type";
	        List<LoanAccountViewModel> loanAccounts = new ArrayList<>();
	        // Add some loan accounts to the list
	        
	        // Mock the behavior of the loanAccountService
	        when(viewInterface.getLoanAccountsByLoanType(accountType)).thenReturn(loanAccounts);
	        
	        HttpSession session = Mockito.mock(HttpSession.class);
	        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	        when(request.getSession()).thenReturn(session);
	        when(session.getAttribute("username")).thenReturn("123");
	        
	        Permission p = new Permission();
	        p.setLoans(true);
	        when(permissionsDAO.getPermissions(123L)).thenReturn(p);

	        // Act
	        String result = loanController.GetLoanAccounts(accountType, model, request, response);

	        // Assert
	        verify(viewInterface).getLoanAccountsByLoanType(accountType);
	        verify(model).addAttribute(eq("loanAccounts"), eq(loanAccounts));
	        Assert.assertEquals(result, "loan-account-details");
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
        Long customerId = 1L;
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(loanId); // Set the loanId for the loan application
        loanApplication.setProcessedStatus("Pending"); // Set the initial processed status
        loanApplication.setStatus("Pending"); // Set the initial status

        // Mock the necessary methods of dependencies
        Mockito.when(loanApplicationDao.getLoanApplicationByid(loanId)).thenReturn(loanApplication);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("username")).thenReturn("testUser");

        // Act
        String result = loanController.approveLoanApplication(loanId, customerId, model, request);

        // Assert
        Assert.assertEquals(result, "loan-approval"); // Verify the expected return value

        // Verify the behavior of the loanApplicationService and applicationEventPublisher
        Mockito.verify(loanApplicationDao).getLoanApplicationByid(loanId);
        Mockito.verify(loanApplicationDao).saveTheApprovedLoanApplication(loanApplication);
        Mockito.verify(loanApplicationDao).approveApplication(loanId, customerId);
        Mockito.verify(session).getAttribute("username");

        // Verify that the loanapp object was modified correctly
        Assert.assertEquals(loanApplication.getProcessedStatus(), "Approved");
        Assert.assertEquals(loanApplication.getStatus(), "Approved");
    }
	





    @Test
    public void testGetLoanApplicationById_Success() {
        // Arrange
        int accountId = 1;
        LoanApplicationViewModel loanApplication = new LoanApplicationViewModel();
        loanApplication.setId(1);
        
        // Mock the behavior of the loanApplicationService
        try {
			when(viewInterface.getLoanApplicationById(accountId)).thenReturn(loanApplication);
		} catch (ApplicationNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Act
        String result = loanController.getLoanApplicationById(accountId, model);

        // Assert
        try {
			verify(viewInterface).getLoanApplicationById(accountId);
		} catch (ApplicationNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        verify(model).addAttribute(eq("loanApplications"), ArgumentMatchers.<LoanApplicationViewModel>anyList());
        Assert.assertEquals(result, "loan-approval");
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
    public void testGetAccountById_Success() {
        // Arrange
        int accountId = 1;
        LoanAccountViewModel loanAccountViewModel = new LoanAccountViewModel();
        loanAccountViewModel.setLoanId((long)1);
        loanAccountViewModel.setLoanType("personal");

        // Mock the behavior of the loanAccountService
        try {
			when(viewInterface.getLoanAccountById(accountId)).thenReturn(loanAccountViewModel);
		} catch (AccountNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Act
        String result = loanController.getAccountById(accountId, model);

        // Assert
        try {
			verify(viewInterface).getLoanAccountById(accountId);
		} catch (AccountNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        verify(model).addAttribute(eq("loanAccounts"), ArgumentMatchers.<LoanAccountViewModel>anyList());
        Assert.assertEquals(result, "loan-account-details");
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
