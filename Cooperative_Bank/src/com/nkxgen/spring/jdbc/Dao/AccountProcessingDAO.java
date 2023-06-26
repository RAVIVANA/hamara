package com.nkxgen.spring.jdbc.Dao;

import java.util.List;

import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.LoanTransactions;
import com.nkxgen.spring.jdbc.model.Transaction;

public interface AccountProcessingDAO {

	public List<Account> getSavAcc(List<Account> acctype);

	public void executeProcedure(int accno);

	public List<Transaction> stgen(int accno);
	public List<LoanTransactions> stgen1(int accno);
}
