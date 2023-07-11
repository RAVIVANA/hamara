package com.nkxgen.spring.jdbc.Bal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nkxgen.spring.jdbc.DaoInterfaces.AccountProcessingDAO;
import com.nkxgen.spring.jdbc.controller.LoanController;
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
private final	Logger LOGGER = LoggerFactory.getLogger(Intrestcaluclation.class);

	@Override
	public List<Account> calcIntrst(List<Account> amnt) {
		LOGGER.info("Calculating interest for accounts");

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

	            LOGGER.debug("Interest calculated for account - Account ID: {}, Balance: {}, Interest Rate: {}, Updated Balance: {}",
	                    a.getAccountTypeId(), amount, FDintrstRate, a.getBalance());
	        } else {
	            a.setIntrest((long) FDintrstRate); // Set the interest for the account
	            a.setCount(1); // Increment the count
	            LocalDate currentDate = LocalDate.now();
	            String dateString1 = currentDate.toString();
	            a.setLastUpdate(dateString1); // Set the last update date to the current date

	            LOGGER.debug("Interest calculated for account - Account ID: {}, Balance: {}, Interest Rate: {}, Count: {}",
	                    a.getAccountTypeId(), amount, FDintrstRate, a.getCount());
	        }

	        newlist.add(a); // Add the updated account to the new list
	    }

	    LOGGER.info("Interest calculation completed");
	    return newlist; // Return the new list with updated accounts
	}


	public void setcashChest(cashChest c) {
		LOGGER.info("Setting cashChest account interest");

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

	    LOGGER.debug("Calculated total interest for this month: {}", thismonthintrest);

	    c.setaccountinterest(thismonthintrest); // Set the account interest for the cashChest object
	    thismonthintrest = 0; // Reset the thismonthintrest variable to 0

	    LOGGER.info("CashChest account interest set");
	}


}