package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.anyString;
import org.testng.annotations.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Dao.AccountApplicationDAO;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.AccountApplication;
import com.nkxgen.spring.jdbc.model.Accountdocument;

public class AccountApplicationDAOTest {

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private AccountApplicationDAO accountApplicationDAO;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSave() {
		AccountApplication accountApplication = new AccountApplication();

		accountApplicationDAO.save(accountApplication);

		verify(entityManager).persist(accountApplication);
	}

	@Test
	public void testGetAccountsappByType() {
		String value = "type";

		TypedQuery<AccountApplication> query = mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(AccountApplication.class))).thenReturn(query);
		when(query.setParameter(eq("value"), eq(value))).thenReturn(query);

		List<AccountApplication> resultList = new ArrayList<>();
		when(query.getResultList()).thenReturn(resultList);

		List<AccountApplication> result = accountApplicationDAO.getAccountsappByType(value);

		assert result != null;
		assert result.size() == 0;
		verify(query).getResultList();
	}

	@Test
	public void testGetAccountsByType() {
		String accountTypeId = "type";

		TypedQuery<Account> query = mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(Account.class))).thenReturn(query);
		when(query.setParameter(eq("acnt_acty_id"), eq(accountTypeId))).thenReturn(query);

		List<Account> resultList = new ArrayList<>();
		when(query.getResultList()).thenReturn(resultList);

		List<Account> result = accountApplicationDAO.getAccountsByType(accountTypeId);

		assert result != null;
		assert result.size() == 0;
		verify(query).getResultList();
	}

	@Test
	public void testSaveAccount() {
		Account account = new Account();

		Account mergedAccount = new Account();
		when(entityManager.merge(eq(account))).thenReturn(mergedAccount);

		accountApplicationDAO.saveAccount(account);

		verify(entityManager).persist(mergedAccount);
	}

	@Test
	public void testSaveAccountdocument() {
		Accountdocument accountdocument = new Accountdocument();

		Accountdocument mergedAccountdocument = new Accountdocument();
		when(entityManager.merge(eq(accountdocument))).thenReturn(mergedAccountdocument);

		accountApplicationDAO.saveAccountdocument(accountdocument);

		verify(entityManager).persist(mergedAccountdocument);
	}

	@Test
	public void testGetAll() {
		TypedQuery<Account> query = mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(Account.class))).thenReturn(query);

		List<Account> resultList = new ArrayList<>();
		when(query.getResultList()).thenReturn(resultList);

		List<Account> result = accountApplicationDAO.getall();

		assert result != null;
		assert result.size() == 0;
		verify(query).getResultList();
	}

	@Test
	public void testMergeAccount() {
		Account account = new Account();

		Account mergedAccount = new Account();
		when(entityManager.merge(eq(account))).thenReturn(mergedAccount);

		Account result = accountApplicationDAO.mergeAccount(account);

		assert result != null;
		assert result == mergedAccount;
	}

	@Test
	public void testGetAccountApplicationById() {
		Long applicationId = 1L;
		AccountApplication accountApplication = new AccountApplication();
		when(entityManager.find(eq(AccountApplication.class), eq(applicationId))).thenReturn(accountApplication);

		AccountApplication result = accountApplicationDAO.getAccountApplicationById(applicationId);

		assert result != null;
		assert result == accountApplication;
	}

	@Test
	public void testSaveTheAccountapp() {
		AccountApplication accountApplication = new AccountApplication();

		AccountApplication mergedAccountApplication = new AccountApplication();
		when(entityManager.merge(eq(accountApplication))).thenReturn(mergedAccountApplication);

		accountApplicationDAO.savetheAccountapp(accountApplication);

		verify(entityManager).merge(mergedAccountApplication);
	}

	@Test
	public void testGetAccountById() {
		Long num = 1L;
		Account account = new Account();
		when(entityManager.find(eq(Account.class), eq(num))).thenReturn(account);

		Account result = accountApplicationDAO.getAccountById(num);

		assert result != null;
		assert result == account;
	}
}
