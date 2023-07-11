package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.any;

import org.testng.Assert;
import org.testng.annotations.*;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Bal.Accounts;
import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.AccountTypeInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.LoanTypesInterface;
import com.nkxgen.spring.jdbc.ViewModels.AccountTypeView;
import com.nkxgen.spring.jdbc.ViewModels.LoanViewModel;
import com.nkxgen.spring.jdbc.controller.masterController;
import com.nkxgen.spring.jdbc.model.cashChest;

public class MasterControllerTest {
	@Mock
	private AccountTypeInterface accountTypeInterface;
	@Mock
	private ViewInterface viewInterface;
	@Mock
	private Accounts accounts;
	@Mock
	private LoanTypesInterface loanTypesInterface;

	@InjectMocks
	private masterController controller;
    @Mock
    private com.nkxgen.spring.jdbc.model.cashChest cashChest;

	@Mock
	private Model model;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAccounts() {
		List<AccountTypeView> accountTypeViewList = new ArrayList<>();
		accountTypeViewList.add(new AccountTypeView());

		when(viewInterface.set11()).thenReturn(accountTypeViewList);

		String result = controller.getAccounts(model);

		verify(viewInterface).set11();
		verify(model).addAttribute(eq("accountTypes"), anyList());
		assert result.equals("get-accounts");
	}

	@Test
	public void testGetSelectedAccountDetails() {
		int accountType = 1;
		AccountTypeView accountTypeView = new AccountTypeView();

		when(viewInterface.getSelectedAccountDetails(accountType)).thenReturn(accountTypeView);

		String result = controller.getSelectedAccountDetails(accountType, model);

		verify(viewInterface).getSelectedAccountDetails(accountType);
		verify(model).addAttribute(eq("accounts"), any(AccountTypeView.class));
		assert result.equals("account-details");
	}

	@Test
	public void testGetLoans() {
		List<LoanViewModel> loanViewModelList = new ArrayList<>();
		loanViewModelList.add(new LoanViewModel());

		when(viewInterface.getAllLoans()).thenReturn(loanViewModelList);

		String result = controller.getLoans(model);

		verify(viewInterface).getAllLoans();
		verify(model).addAttribute(eq("loans"), anyList());
		assert result.equals("get-loans");
	}

	@Test
	public void testGetSelectedLoanDetails() {
		int loanType = 1;
		LoanViewModel loanViewModel = new LoanViewModel();

		when(viewInterface.getSelectedLoanDetails(loanType)).thenReturn(loanViewModel);

		String result = controller.getSelectedLoanDetails(loanType, model);

		verify(viewInterface).getSelectedLoanDetails(loanType);
		verify(model).addAttribute(eq("loans"), any(LoanViewModel.class));
		assert result.equals("loan-details");
	}

	@Test
	public void testMoney() {
		cashChest cashChest = new cashChest();

		when(accountTypeInterface.getallamount()).thenReturn(cashChest);

		String result = controller.money(model);

		verify(accountTypeInterface).getallamount();
		verify(model).addAttribute(eq("cashChest"), any(cashChest.class));
		assert result.equals("cash-chest");
	}

	  @Test
	    public void testProfitloss() {
	        // Arrange
	        when(accountTypeInterface.getallamount()).thenReturn(cashChest);

	        // Act
	        String result = controller.Profitloss(model);

	        // Assert
	        verify(model).addAttribute(eq("cashChest"), eq(cashChest));
	        Assert.assertEquals(result, "profitLoss");
	    }
	





}
