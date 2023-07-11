package com.nkxgen.spring.jdbc.Test;

import static org.mockito.ArgumentMatchers.any;
import org.testng.annotations.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Dao.AccountProcessingDAOimp;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.LoanTransactions;
import com.nkxgen.spring.jdbc.model.Transaction;

public class AccountProcessingDAOimpTest {

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private AccountProcessingDAOimp accountProcessingDAOimp;

	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetSavAcc() {
		// Create a list of mock Account objects
		List<Account> mockAccounts = new ArrayList<>();
		mockAccounts.add(mock(Account.class));
		mockAccounts.add(mock(Account.class));

		// Create a mock Query object
		Query mockQuery = mock(Query.class);

		// Mock the necessary method invocations
		when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
		when(mockQuery.executeUpdate()).thenReturn(1);

		// Call the method under test
		accountProcessingDAOimp.getSavAcc(mockAccounts);

		// Verify the expected method invocations
		verify(entityManager, times(2)).createNativeQuery(anyString());
		verify(mockQuery, times(2)).setParameter(anyString(), anyLong());
		verify(mockQuery, times(2)).executeUpdate();
	}

	@Test
	public void testExecuteProcedure() {
		// Create a mock StoredProcedureQuery object
		StoredProcedureQuery mockQuery = mock(StoredProcedureQuery.class);

		// Mock the necessary method invocations
		when(entityManager.createStoredProcedureQuery(anyString())).thenReturn(mockQuery);

		// Call the method under test
		accountProcessingDAOimp.executeProcedure(123);

		// Verify the expected method invocations
		verify(entityManager).createStoredProcedureQuery(anyString());
		verify(mockQuery).registerStoredProcedureParameter(anyInt(), any(Class.class), any());
		verify(mockQuery).setParameter(anyInt(), anyInt());
		verify(mockQuery).execute();
	}

	@Test
	public void testAccountTransactionStatementGeneration() {
		// Create a mock TypedQuery<Transaction> object
		TypedQuery<Transaction> mockQuery = mock(TypedQuery.class);

		// Create a mock Transaction object
		Transaction mockTransaction = mock(Transaction.class);

		// Mock the necessary method invocations
		when(entityManager.createQuery(anyString(), eq(Transaction.class))).thenReturn(mockQuery);
		when(mockQuery.setParameter(anyString(), anyInt())).thenReturn(mockQuery);
		when(mockQuery.getResultList()).thenReturn(Collections.singletonList(mockTransaction));

		// Call the method under test
		List<Transaction> result = accountProcessingDAOimp.AccountTransactionStatementGeneration(456);

		// Verify the expected method invocations
		verify(entityManager).createQuery(anyString(), eq(Transaction.class));
		verify(mockQuery).setParameter(anyString(), anyInt());
		verify(mockQuery).getResultList();

		// Perform assertions on the result
		assert result != null;
		assert result.size() == 1;
		assert result.get(0) == mockTransaction;
	}

	@Test
	public void testLoanTransactionStatementGeneration() {
		// Create a mock TypedQuery<Object> object
		TypedQuery<Object> mockQuery = mock(TypedQuery.class);

		// Create a mock LoanTransactions object
		LoanTransactions mockLoanTransaction = mock(LoanTransactions.class);

		// Mock the necessary method invocations
		when(entityManager.createQuery(anyString(), any())).thenReturn(mockQuery);
		when(mockQuery.setParameter(anyString(), anyInt())).thenReturn(mockQuery);
		when(mockQuery.getResultList()).thenReturn(Collections.singletonList(mockLoanTransaction));

		// Call the method under test
		List<LoanTransactions> result = accountProcessingDAOimp.LoanTransactionStatementGeneration(789);

		// Verify the expected method invocations
		verify(entityManager).createQuery(anyString(), any());
		verify(mockQuery).setParameter(anyString(), anyInt());
		verify(mockQuery).getResultList();

		// Perform assertions on the result
		assert result != null;
		assert result.size() == 1;
		assert result.get(0) == mockLoanTransaction;
	}

	// Add more test methods for the remaining methods in AccountProcessingDAOimp

}
