package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
import com.nkxgen.spring.jdbc.controller.AccountController;
import com.nkxgen.spring.jdbc.events.AccountAppApprovalEvent;
import com.nkxgen.spring.jdbc.events.AccountAppRequestEvent;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.AccountApplication;
import com.nkxgen.spring.jdbc.model.Accountdocument;
import com.nkxgen.spring.jdbc.model.Customertrail;

public class AccountControllerTest {
	@Mock
	private AccountApplicationDaoInterface accountApplicationDao;

	@Mock
	private CustomerDaoInterface customerDao;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpSession session;

	@Mock
	private Model model;

	@Mock
	private ViewInterface viewInterface;

	@InjectMocks
	private AccountController accountController;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAccountApplicationByType() {
		String accountType = "some-account-type";
		List<AccountApplicationViewModel> accountApplications = new ArrayList<>();
		when(viewInterface.getAccountsappByType(eq(accountType))).thenReturn(accountApplications);

		String result = accountController.getAccountApplicationByType(accountType, model, request, null);

		Assert.assertEquals("new-account-application", result);
		verify(model).addAttribute(eq("listOfAccountApplications"), eq(accountApplications));
	}

	@Test
	public void testViewAccounts() {
		String accountType = "some-account-type";
		List<AccountViewModel> accounts = new ArrayList<>();
		when(viewInterface.getAccountsByType(eq(accountType))).thenReturn(accounts);
		when(customerDao.getRealCustomerById(anyLong())).thenReturn(new Customertrail());

		String result = accountController.viewAccounts(accountType, model, request, null);

		Assert.assertEquals("any-type-account-info", result);
		verify(model).addAttribute(eq("list_of_account"), eq(accounts));
		verify(model).addAttribute(eq("list_of_customer"), any(List.class));
	}

	@Test
	public void testAccountApplicationSaveToDb() {
		AccountApplicationInput accountApplicationInput = new AccountApplicationInput();
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testuser");
		doThrow(new RuntimeException()).when(accountApplicationDao).save(any(AccountApplication.class)); // Simulating
																											// RuntimeException

		String result = accountController.accountApplicationSaveToDb(accountApplicationInput, request, model);

		Assert.assertEquals("error-view", result); // Updated assertion
		verify(accountApplicationDao).save(any(AccountApplication.class));
		verify(eventPublisher, never()).publishEvent(any(AccountAppRequestEvent.class));
		verify(model).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testAccountApplicationSaveToDbError() {
		AccountApplicationInput accountApplicationInput = new AccountApplicationInput();
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testuser");
		doThrow(new RuntimeException()).when(accountApplicationDao).save(any(AccountApplication.class));

		String result = accountController.accountApplicationSaveToDb(accountApplicationInput, request, model);

		Assert.assertEquals("error-view", result);
		verify(accountApplicationDao).save(any(AccountApplication.class));
		verify(eventPublisher, never()).publishEvent(any(AccountAppRequestEvent.class));
		verify(model).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testSaveToAccountDatabase() {
		AccountInput accountInput = new AccountInput();

		AccountApplication accountApplication = new AccountApplication();
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testuser");
		when(accountApplicationDao.getAccountApplicationById(anyLong())).thenReturn(accountApplication);
		doThrow(new RuntimeException()).when(accountApplicationDao).saveAccount(any(Account.class));

		String result = accountController.saveToAccountDatabase(accountInput, model, request);

		Assert.assertEquals("error-view", result); // Updated assertion
		verify(accountApplicationDao).saveAccount(any(Account.class));
		verify(eventPublisher, never()).publishEvent(any(AccountAppApprovalEvent.class));
		verify(accountApplicationDao, never()).savetheAccountapp(eq(accountApplication));
		verify(model).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testSaveToAccountDatabaseError() {
		AccountInput accountInput = new AccountInput();
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("username")).thenReturn("testuser");
		doThrow(new RuntimeException()).when(accountApplicationDao).saveAccount(any(Account.class));

		String result = accountController.saveToAccountDatabase(accountInput, model, request);

		Assert.assertEquals("error-view", result);
		verify(accountApplicationDao).saveAccount(any(Account.class));
		verify(eventPublisher, never()).publishEvent(any(AccountAppApprovalEvent.class));
		verify(model).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testSaveToAccountDocumentsDatabase() {
		AccountDocumentInput accountDocumentInput = new AccountDocumentInput();

		String result = accountController.saveToAccountDocumentsDatabase(accountDocumentInput, model);

		Assert.assertEquals("account-new-application-form", result);
		verify(accountApplicationDao).saveAccountdocument(any(Accountdocument.class));
		verify(model, never()).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testSaveToAccountDocumentsDatabaseError() {
		AccountDocumentInput accountDocumentInput = new AccountDocumentInput();
		doThrow(new RuntimeException()).when(accountApplicationDao).saveAccountdocument(any(Accountdocument.class));

		String result = accountController.saveToAccountDocumentsDatabase(accountDocumentInput, model);

		Assert.assertEquals("error-view", result);
		verify(accountApplicationDao).saveAccountdocument(any(Accountdocument.class));
		verify(model).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testGetAccountById() throws AccountNotFound {
		int accountId = 1;
		AccountViewModel accountViewModel = new AccountViewModel();
		Customertrail customerTrail = new Customertrail();
		when(viewInterface.getAccountById(eq(accountId))).thenReturn(accountViewModel);
		when(customerDao.getRealCustomerById(anyLong())).thenReturn(customerTrail);

		String result = accountController.getAccountById(accountId, model);

		Assert.assertEquals("any-type-account-info", result);
		verify(model).addAttribute(eq("list_of_account"), anyList());
		verify(model).addAttribute(eq("list_of_customer"), anyList());
		verify(model, never()).addAttribute(eq("error"), anyString());
	}

	@Test
	public void testGetAccountApplicationById() throws ApplicationNotFound {
		int accountId = 1;
		AccountApplicationViewModel accountApplicationViewModel = new AccountApplicationViewModel();
		when(viewInterface.getAccountsappById(eq(accountId))).thenReturn(accountApplicationViewModel);

		String result = accountController.getAccountApplicationById(accountId, model);

		Assert.assertEquals("new-account-application", result);
		verify(model).addAttribute(eq("listOfAccountApplications"), anyList());
		verify(model, never()).addAttribute(eq("error"), anyString());
	}

}
