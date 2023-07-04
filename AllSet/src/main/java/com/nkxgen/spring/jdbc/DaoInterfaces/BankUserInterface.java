package com.nkxgen.spring.jdbc.DaoInterfaces;

import java.util.List;

import com.nkxgen.spring.jdbc.model.BankUser;

public interface BankUserInterface {
	BankUser getBankUserById(long busr_id);

	List<BankUser> getAllBankUsers();

	BankUser saveUser(BankUser bankUser);

	List<BankUser> getBankUsersByDesignation(String designation);

	List<BankUser> getBankUsersByDesignation(BankUser bankUser);

	BankUser saveBankUser(BankUser bankUser);

	void updatePassword(String newPassword, long userID);

	boolean getBankUserByEmail(String email, long userID);
}
