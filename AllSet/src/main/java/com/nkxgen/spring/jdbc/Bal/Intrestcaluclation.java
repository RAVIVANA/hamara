package com.nkxgen.spring.jdbc.Bal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nkxgen.spring.jdbc.DaoInterfaces.AccountProcessingDAO;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.cashChest;

@Component
public class Intrestcaluclation implements Accounts {
	long FDintrstRate;
	double FDAmnt;
	int period;
	int age;
	double Gen;
	double intrstRate;
	List<Account> newlist = new ArrayList<>();
	long thismonthintrest = 0;
	@Autowired
	private AccountProcessingDAO interestCalDao;

	@Override
	public List<Account> calcIntrst(List<Account> amnt) {
		int period = 1;
		List<Account> newlist = new ArrayList<>();

		for (Account a : amnt) {
			String type = a.getAccountTypeId();
			long amount = a.getBalance();

			double FDintrstRate = (amount * 4 / 12) / 100; // Calculate the interest rate

			if (a.getCount() == 4) {
				a.setCount(0);
				a.setBalance(a.getIntrest() + (long) FDintrstRate); // Add the interest to the account balance
				a.setIntrest(0);

				LocalDate currentDate = LocalDate.now();
				String dateString1 = currentDate.toString();
				a.setLastUpdate(dateString1); // Set the last update date to the current date
			} else {
				a.setIntrest((long) FDintrstRate); // Set the interest for the account
				a.setCount(a.getCount()); // Increment the count
				LocalDate currentDate = LocalDate.now();
				String dateString1 = currentDate.toString();
				a.setLastUpdate(dateString1); // Set the last update date to the current date
			}

			newlist.add(a); // Add the updated account to the new list
		}

		return newlist; // Return the new list with updated accounts
	}

	public void setcashChest(cashChest c) {
		List<Account> l = interestCalDao.getthisMonthIntrest();
		LocalDate currentDate = LocalDate.now(); // Get the current date

		for (Account a : l) {
			if (a.getLastUpdate().equals("")) {
				continue;
			} else {
				LocalDate ld = LocalDate.parse(a.getLastUpdate());
				if (currentDate.getYear() == ld.getYear() && currentDate.getMonthValue() == ld.getMonthValue()) {
					thismonthintrest = thismonthintrest + a.getIntrest();
				}
			}
		}
		c.setaccountinterest(thismonthintrest); // Set the account interest for the cashChest object
		thismonthintrest = 0; // Reset the thismonthintrest variable to 0
	}

}