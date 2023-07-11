package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.anyString;
import org.testng.annotations.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Dao.LoanApplicationDao;
import com.nkxgen.spring.jdbc.model.LoanAccount;
import com.nkxgen.spring.jdbc.model.LoanApplication;

public class LoanApplicationDaoTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private TypedQuery<LoanApplication> query;
	@Mock
	private TypedQuery<LoanAccount> accountQuery;

	private LoanApplicationDao loanApplicationDao;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		entityManager = Mockito.mock(EntityManager.class);
		loanApplicationDao = new LoanApplicationDao();
		loanApplicationDao.setEntity(entityManager);

	}

	@Test
	public void testGetLoanApplicationByValue() {
		// Arrange
		String value = "ML";
		String status = "waiting";
		List<LoanApplication> expectedLoanApplications = new ArrayList<>();
		expectedLoanApplications.add(new LoanApplication());

		when(entityManager.createQuery(anyString(), eq(LoanApplication.class))).thenReturn(query);
		when(query.setParameter(eq("value"), eq(value))).thenReturn(query);
		when(query.setParameter(eq("val"), eq(status))).thenReturn(query);
		when(query.getResultList()).thenReturn(expectedLoanApplications);

		// Act
		List<LoanApplication> result = loanApplicationDao.getLoanApplicationByValue(value);

		// Assert
		Assert.assertEquals(expectedLoanApplications, result);
		verify(entityManager).createQuery(anyString(), eq(LoanApplication.class));
		verify(query).setParameter(eq("value"), eq(value));
		verify(query).setParameter(eq("val"), eq(status));
		verify(query).getResultList();
	}

	@Test
	public void testSaveLoanApplication() {
		// Arrange
		LoanApplication loanApplication = new LoanApplication();

		// Act
		loanApplicationDao.saveLoanApplication(loanApplication);

		// Assert
		verify(entityManager).persist(loanApplication);
	}

	@Test
	public void testGetLoanApplicationsByStatus() {
		// Arrange
		String status = "waiting";
		List<LoanApplication> expectedLoanApplications = new ArrayList<>();
		expectedLoanApplications.add(new LoanApplication());
		when(entityManager.createQuery(anyString(), eq(LoanApplication.class))).thenReturn(query);
		when(query.setParameter(eq("status"), eq(status))).thenReturn(query);
		when(query.getResultList()).thenReturn(expectedLoanApplications);

		// Act
		List<LoanApplication> result = loanApplicationDao.getLoanApplicationsByStatus(status);

		// Assert
		Assert.assertEquals(expectedLoanApplications, result);
		verify(entityManager).createQuery(anyString(), eq(LoanApplication.class));
		verify(query).setParameter(eq("status"), eq(status));
		verify(query).getResultList();
	}

	@Test
	public void testGetLoanAccountsByLoanType() {
		// Arrange
		String loanType = "personal";
		List<LoanAccount> expectedLoanAccounts = new ArrayList<>();
		TypedQuery<LoanAccount> accountQuery = Mockito.mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(LoanAccount.class))).thenReturn(accountQuery);
		when(accountQuery.setParameter(eq("loanType"), eq(loanType))).thenReturn(accountQuery);
		when(accountQuery.getResultList()).thenReturn(expectedLoanAccounts);

		// Act
		List<LoanAccount> result = loanApplicationDao.getLoanAccountsByLoanType(loanType);

		// Assert
		Assert.assertEquals(expectedLoanAccounts, result);
		verify(entityManager).createQuery(anyString(), eq(LoanAccount.class));
		verify(accountQuery).setParameter(eq("loanType"), eq(loanType));
		verify(accountQuery).getResultList();
	}

	@Test
	public void testDeleteApplication() {
		// Arrange
		int loanId = 1;
		LoanApplication loanApplication = new LoanApplication();
		when(entityManager.find(eq(LoanApplication.class), eq(loanId))).thenReturn(loanApplication);

		// Act
		loanApplicationDao.deleteApplication(loanId);

		// Assert
		verify(entityManager).find(eq(LoanApplication.class), eq(loanId));
		verify(entityManager).remove(eq(loanApplication));
	}

	@Test
	public void testGetAllLoans() {
		// Arrange
		List<LoanAccount> expectedLoanAccounts = new ArrayList<>();
		TypedQuery<LoanAccount> query = Mockito.mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(LoanAccount.class))).thenReturn(query);
		when(query.getResultList()).thenReturn(expectedLoanAccounts);

		// Act
		List<LoanAccount> result = loanApplicationDao.getAllLoans();

		// Assert
		Assert.assertEquals(expectedLoanAccounts, result);
		verify(entityManager).createQuery(anyString(), eq(LoanAccount.class));
		verify(query).getResultList();
	}

	@Test
	public void testGetLoanApplicationByid() {
		// Arrange
		long loanApplicationId = 1L;
		LoanApplication expectedLoanApplication = new LoanApplication();
		when(entityManager.find(eq(LoanApplication.class), eq(loanApplicationId))).thenReturn(expectedLoanApplication);

		// Act
		LoanApplication result = loanApplicationDao.getLoanApplicationByid(loanApplicationId);

		// Assert
		Assert.assertEquals(expectedLoanApplication, result);
		verify(entityManager).find(eq(LoanApplication.class), eq(loanApplicationId));
	}

	@Test
	public void testSaveTheApprovedLoanApplication() {
		// Arrange
		LoanApplication loanApplication = new LoanApplication();

		// Act
		loanApplicationDao.saveTheApprovedLoanApplication(loanApplication);

		// Assert
		verify(entityManager).merge(eq(loanApplication));
	}

	@Test
	public void testGetLoanApplicationById() {
		// Arrange
		int loanApplicationId = 130;
		LoanApplication expectedLoanApplication = new LoanApplication();
		when(entityManager.find(eq(LoanApplication.class), eq(loanApplicationId))).thenReturn(expectedLoanApplication);

		// Act
		LoanApplication result = loanApplicationDao.getLoanApplicationById(loanApplicationId);

		// Assert
		Assert.assertEquals(expectedLoanApplication, result);
		verify(entityManager).find(eq(LoanApplication.class), eq(loanApplicationId));
	}

	@Test
	public void testGetLoanAccountById() {
		// Arrange
		int accountNumber = 120;
		LoanAccount expectedLoanAccount = new LoanAccount();
		when(entityManager.find(eq(LoanAccount.class), eq((long) accountNumber))).thenReturn(expectedLoanAccount);

		// Act
		LoanAccount result = loanApplicationDao.getLoanAccountById(accountNumber);

		// Assert
		Assert.assertEquals(expectedLoanAccount, result);
		verify(entityManager).find(eq(LoanAccount.class), eq((long) accountNumber));
	}
}
