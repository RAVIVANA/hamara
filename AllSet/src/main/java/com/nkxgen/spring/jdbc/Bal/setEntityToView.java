package com.nkxgen.spring.jdbc.Bal;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nkxgen.spring.jdbc.DaoInterfaces.AccountApplicationDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.AccountTypeInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.BankUserInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.LoanApplicationDaoInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.LoanTypesInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.TransactionsInterface;
import com.nkxgen.spring.jdbc.Exception.AccountNotFound;
import com.nkxgen.spring.jdbc.Exception.ApplicationNotFound;
import com.nkxgen.spring.jdbc.InputModels.BankUserInput;
import com.nkxgen.spring.jdbc.ViewModels.AccountApplicationViewModel;
import com.nkxgen.spring.jdbc.ViewModels.AccountTypeView;
import com.nkxgen.spring.jdbc.ViewModels.AccountViewModel;
import com.nkxgen.spring.jdbc.ViewModels.BankUserViewModel;
import com.nkxgen.spring.jdbc.ViewModels.LoanAccountViewModel;
import com.nkxgen.spring.jdbc.ViewModels.LoanApplicationViewModel;
import com.nkxgen.spring.jdbc.ViewModels.LoanViewModel;
import com.nkxgen.spring.jdbc.model.Account;
import com.nkxgen.spring.jdbc.model.AccountApplication;
import com.nkxgen.spring.jdbc.model.BankUser;
import com.nkxgen.spring.jdbc.model.Customer;
import com.nkxgen.spring.jdbc.model.Customertrail;
import com.nkxgen.spring.jdbc.model.LoanAccount;
import com.nkxgen.spring.jdbc.model.LoanApplication;
import com.nkxgen.spring.jdbc.model.LoansTypes;
import com.nkxgen.spring.jdbc.model.accountTypes;

public class setEntityToView implements ViewInterface {
	@Autowired
	LoanApplicationDaoInterface ll;
	@Autowired
	LoanTypesInterface loan;
	@Autowired
	AccountApplicationDaoInterface ac;
	@Autowired
	BankUserInterface bankUserService;
	@Autowired
	private AccountTypeInterface account;
	@Autowired
	private CustomerDaoInterface cd;
	@Autowired
	TransactionsInterface ti;
	Logger logger = LoggerFactory.getLogger(setEntityToView.class);

	List<LoanAccountViewModel> viewlist = new ArrayList<>();
	List<LoanApplicationViewModel> viewlist1 = new ArrayList<>();
	List<AccountViewModel> viewlist2 = new ArrayList<>();
	List<AccountApplicationViewModel> viewlist4 = new ArrayList<>();
	List<LoanViewModel> viewlist5 = new ArrayList<>();
	List<BankUserViewModel> viewlist3 = new ArrayList<>();
	List<AccountTypeView> viewlist6 = new ArrayList<>();
	List<Account> viewlist7 = new ArrayList<>();
	List<Customer> viewlist8 = new ArrayList<>();

	@Override
	public List<LoanAccountViewModel> getLoanAccountsByLoanType(String typee) {
	    logger.info("Getting loan accounts by loan type: {}", typee);

	    viewlist.clear(); // Clear the viewlist to ensure it's empty

	    // Get the list of LoanAccounts by loan type from the ll object
	    List<LoanAccount> list = ll.getLoanAccountsByLoanType(typee);

	    for (LoanAccount l : list) {
	        LoanAccountViewModel la = new LoanAccountViewModel(); // Create a new LoanAccountViewModel object

	        // Set the values from the LoanAccount object to the LoanAccountViewModel object
	        la.setValuesFromLoanAccount(l);

	        viewlist.add(la); // Add the LoanAccountViewModel object to the viewlist
	    }

	    logger.info("Retrieved {} loan accounts", viewlist.size());
	    return viewlist; // Return the viewlist containing LoanAccountViewModel objects
	}


	@Override
	public List<AccountViewModel> getAccoutsByType(List<Account> typee) {
	    logger.info("Getting accounts by type");

	    viewlist2.clear(); // Clear the existing contents of the viewlist2 list

	    for (Account l : typee) {
	        AccountViewModel la = new AccountViewModel(); // Create a new AccountViewModel object
	        AccountViewModel la1 = la.mapToViewModel(l); // Map the values from the Account object to the AccountViewModel object
	        viewlist2.add(la1); // Add the AccountViewModel object to the viewlist2 list
	    }

	    logger.info("Retrieved {} accounts", viewlist2.size());
	    return viewlist2; // Return the list of AccountViewModel objects
	}


	@Override
	public List<LoanApplicationViewModel> getLoanApplicationsByStatus(String typee) {
	    logger.info("Getting loan applications by status: {}", typee);

	    viewlist1.clear(); // Clear the existing contents of the viewlist1 list

	    List<LoanApplication> list = ll.getLoanApplicationsByStatus(typee); // Retrieve the list of LoanApplication objects by status

	    for (LoanApplication l : list) {
	        LoanApplicationViewModel la = new LoanApplicationViewModel(); // Create a new LoanApplicationViewModel object
	        Customertrail c = cd.getCustomerById(l.getCustId().getId()); // Fetch the Customertrail association eagerly

	        la.copyFromEntity(l, c); // Copy the values from the LoanApplication object to the LoanApplicationViewModel object
	        viewlist1.add(la); // Add the LoanApplicationViewModel object to the viewlist1 list
	    }

	    logger.info("Retrieved {} loan applications", viewlist1.size());
	    return viewlist1; // Return the list of LoanApplicationViewModel objects
	}


	@Override
	public List<AccountApplicationViewModel> getAccountsappByType(String typee) {
	    logger.info("Getting account applications by type: {}", typee);

	    viewlist4.clear(); // Clear the existing contents of the viewlist4 list

	    List<AccountApplication> list = ac.getAccountsappByType(typee); // Retrieve the list of AccountApplication objects by type

	    for (AccountApplication l : list) {
	        AccountApplicationViewModel la = new AccountApplicationViewModel(); // Create a new AccountApplicationViewModel object
	        la.setEntityModelValues(l); // Set the values from the AccountApplication object to the AccountApplicationViewModel object
	        viewlist4.add(la); // Add the AccountApplicationViewModel object to the viewlist4 list
	    }

	    logger.info("Retrieved {} account applications", viewlist4.size());
	    return viewlist4; // Return the list of AccountApplicationViewModel objects
	}


	@Override
	public List<LoanApplicationViewModel> getLoanApplicationByValue(String typee) {
	    logger.info("Getting loan applications by value: {}", typee);

	    viewlist1.clear(); // Clear the existing contents of the viewlist1 list

	    List<LoanApplication> list = ll.getLoanApplicationByValue(typee); // Retrieve the list of LoanApplication objects by value

	    for (LoanApplication l : list) {
	        LoanApplicationViewModel la = new LoanApplicationViewModel(); // Create a new LoanApplicationViewModel object
	        // Fetch the Customertrail association eagerly
	        Customertrail c = cd.getCustomerById(l.getCustId().getId());

	        // Access the required properties without triggering lazy loading

	        la.copyFromEntity(l, c); // Copy the values from the LoanApplication object to the LoanApplicationViewModel object
	        viewlist1.add(la); // Add the LoanApplicationViewModel object to the viewlist1 list
	    }

	    logger.info("Retrieved {} loan applications", viewlist1.size());
	    return viewlist1;
	}





	@Override
	public List<AccountViewModel> getAccountsByType(String typee) {
	    logger.info("Getting accounts by type: {}", typee);

	    viewlist2.clear(); // Clear the existing contents of the viewlist2 list

	    List<Account> list = ac.getAccountsByType(typee); // Retrieve the list of Account objects by typee

	    for (Account l : list) {
	        AccountViewModel la = new AccountViewModel(); // Create a new AccountViewModel object
	        AccountViewModel la1 = la.mapToViewModel(l); // Map the values from the Account object to the AccountViewModel object
	        viewlist2.add(la1); // Add the AccountViewModel object to the viewlist2 list
	    }

	    logger.info("Retrieved {} accounts", viewlist2.size());
	    return viewlist2; // Return the list of AccountViewModel objects
	}


	@Override
	public List<BankUserViewModel> getAllBankUsers() {
	    logger.info("Getting all bank users");

	    viewlist3.clear(); // Clear the existing contents of the viewlist3 list

	    List<BankUser> list = bankUserService.getAllBankUsers(); // Retrieve the list of BankUser objects

	    for (BankUser l : list) {
	        BankUserViewModel la = new BankUserViewModel(); // Create a new BankUserViewModel object
	        la.setFromEntity(l); // Set the values in the BankUserViewModel object based on the BankUser entity
	        viewlist3.add(la); // Add the BankUserViewModel object to the viewlist3 list
	    }

	    logger.info("Retrieved {} bank users", viewlist3.size());
	    return viewlist3; // Return the list of BankUserViewModel objects
	}


	@Override
	public BankUser getBankUserById(BankUserInput status) {
	    logger.info("Getting bank user by ID: {}", status.getBusr_id());

	    BankUser b = bankUserService.getBankUserById(status.getBusr_id()); // Retrieve the BankUser object by its busr_id

	    logger.info("Bank user retrieved successfully");
	    return b; // Return the BankUser object
	}


	@Override
	public List<BankUserViewModel> getBankUsersByDesignation(BankUser status) {
	    logger.info("Getting bank users by designation: {}", status);

	    viewlist3.clear(); // Clear the viewlist3 to start with an empty list

	    // Retrieve a list of BankUser objects based on the designation
	    List<BankUser> list = bankUserService.getBankUsersByDesignation(status);

	    for (BankUser l : list) {
	        BankUserViewModel la = new BankUserViewModel();
	        la.setFromEntity(l); // Set the values of BankUserViewModel based on the BankUser object
	        viewlist3.add(la); // Add the BankUserViewModel object to the viewlist3
	    }

	    logger.info("Retrieved {} bank users", viewlist3.size());
	    return viewlist3; // Return the list of BankUserViewModel objects
	}


	@Override
	public List<BankUserViewModel> getBankUsersByDesignation(String status) {
	    logger.info("Getting bank users by designation: {}", status);

	    viewlist3.clear(); // Clear the viewlist3 to start with an empty list

	    // Retrieve a list of BankUser objects based on the designation
	    List<BankUser> list = bankUserService.getBankUsersByDesignation(status);

	    for (BankUser l : list) {
	        BankUserViewModel la = new BankUserViewModel();
	        la.setFromEntity(l); // Set the values of BankUserViewModel based on the BankUser object
	        viewlist3.add(la); // Add the BankUserViewModel object to the viewlist3
	    }

	    logger.info("Retrieved {} bank users", viewlist3.size());
	    return viewlist3; // Return the list of BankUserViewModel objects
	}

	@Override
	public LoanViewModel getSelectedLoanDetails(int status) {
	    logger.info("Getting selected loan details for status: {}", status);

	    LoansTypes loank = loan.getSelectedLoanDetails(status); // Retrieve the selected loan details based on the status

	    LoanViewModel la = new LoanViewModel();
	    LoanViewModel la1 = la.mapEntityToViewModel(loank); // Map the LoansTypes object to a LoanViewModel object

	    logger.info("Selected loan details retrieved successfully");
	    return la1; // Return the LoanViewModel object
	}

	@Override
	public List<LoanViewModel> getAllLoans() {
	    logger.info("Getting all loans");

	    viewlist5.clear(); // Clear the existing list

	    List<LoansTypes> loank = loan.getAllLoans(); // Retrieve all loans

	    for (LoansTypes l : loank) {
	        LoanViewModel la = new LoanViewModel();
	        LoanViewModel la1 = la.mapEntityToViewModel(l); // Map each LoansTypes object to a LoanViewModel object
	        viewlist5.add(la1); // Add the LoanViewModel object to the viewlist5 list
	    }

	    logger.info("Retrieved {} loans", viewlist5.size());
	    return viewlist5; // Return the list of LoanViewModel objects
	}


	@Override
	public AccountTypeView getSelectedAccountDetails(int status) {
	    logger.info("Getting selected account details for status: {}", status);

	    accountTypes loank = account.getSelectedAccountDetails(status); // Retrieve selected account details

	    AccountTypeView la = new AccountTypeView();
	    AccountTypeView la1 = la.mapEntityToViewModel(loank); // Map the account details to an AccountTypeView object

	    logger.info("Selected account details retrieved successfully");
	    return la1; // Return the AccountTypeView object
	}


	@Override
	public List<AccountTypeView> set11() {
	    logger.info("Setting account types");

	    viewlist6.clear(); // Clear the existing list

	    List<accountTypes> loank = account.getAllAccounts(); // Retrieve all the accounts

	    for (accountTypes l : loank) {
	        AccountTypeView la = new AccountTypeView();
	        AccountTypeView la1 = la.mapEntityToViewModel(l); // Map each account to an AccountTypeView object
	        viewlist6.add(la1); // Add the mapped object to the list
	    }

	    logger.info("Account types set. Total account types: {}", viewlist6.size());
	    return viewlist6; // Return the list of AccountTypeView objects
	}


	@Override
	public List<Account> checkdate(List<Account> l) {
	    logger.info("Checking account update date");

	    viewlist7.clear(); // Clear the existing list

	    LocalDate currentDate = LocalDate.now(); // Get the current date
	    LocalDate oneMonthBackDate = currentDate.minusMonths(1); // Calculate the date one month ago

	    for (Account a : l) {
	        LocalDate lastUpdateDate = LocalDate.parse(a.getLastUpdate()); // Parse the last update date of the account

	        // Compare the last update date with one month ago
	        if (oneMonthBackDate.equals(lastUpdateDate)) {
	            viewlist7.add(a); // Add the account to the filtered list
	        }
	    }

	    logger.info("Filtered accounts based on update date. Total accounts: {}", viewlist7.size());
	    return viewlist7; // Return the filtered list of accounts
	}


	@Override
	public List<Account> checkdates(List<Account> l) {
	    logger.info("Checking account update dates");

	    viewlist7.clear(); // Clear the existing list

	    LocalDate currentDate = LocalDate.now(); // Get the current date
	    LocalDate sixMonthsAgoDate = currentDate.minusMonths(6); // Calculate the date 6 months ago

	    for (Account a : l) {
	        LocalDate lastUpdateDate = LocalDate.parse(a.getLastUpdate()); // Parse the last update date of the account

	        // Compare the last update date with 6 months ago
	        if (sixMonthsAgoDate.compareTo(lastUpdateDate) > 0 ||sixMonthsAgoDate.compareTo(lastUpdateDate) == 0) {
	            viewlist7.add(a); // Add the account to the filtered list
	        }
	    }

	    logger.info("Filtered accounts based on update dates. Total accounts: {}", viewlist7.size());
	    return viewlist7; // Return the filtered list of accounts
	}


	@Override
	public List<Customer> getAllCustomers() {
	    logger.info("Getting all customers");

	    viewlist8.clear(); // Clear the existing list

	    List<Customertrail> loank = cd.getAllCustomers(); // Retrieve all customers from the Customertrail table

	    for (Customertrail l : loank) {
	        Customer la = new Customer();
	        Customer la1 = la.dotheservice1(l); // Convert Customertrail object to Customer object
	        viewlist8.add(la1); // Add the converted Customer object to the list
	    }

	    logger.info("Retrieved {} customers", viewlist8.size());
	    return viewlist8; // Return the list of customers
	}


	@Override
	public LoanAccountViewModel getLoanAccountById(int accountnumber) throws AccountNotFound {
	    logger.info("Getting loan account by account number: {}", accountnumber);

	    LoanAccountViewModel la2 = new LoanAccountViewModel();
	    LoanAccount la = ll.getLoanAccountById(accountnumber);

	    if (la == null) {
	        logger.error("Loan account not found for account number: {}", accountnumber);
	        throw new AccountNotFound("Account not found");
	    } else {
	        la2.setValuesFromLoanAccount(la);
	    }

	    logger.info("Loan account retrieved successfully");
	    return la2;
	}

	@Override
	public AccountViewModel getAccountById(int act) throws AccountNotFound {
	    logger.info("Getting account by ID: {}", act);

	    AccountViewModel a2 = new AccountViewModel();
	    Account a;

	    try {
	        a = ac.getAccountById((long) act);
	    } catch (Exception e) {
	        logger.error("Error retrieving account by ID: {}", act);
	        throw new AccountNotFound("Account not found");
	    }

	    if (a == null) {
	        logger.error("Account not found for ID: {}", act);
	        throw new AccountNotFound("Account not found");
	    } else {
	        AccountViewModel a1 = new AccountViewModel();
	        a2 = a1.mapToViewModel(a);
	    }

	    logger.info("Account retrieved successfully");
	    return a2;
	}


	@Override
	public AccountApplicationViewModel getAccountsappById(int typee) throws ApplicationNotFound {
	    logger.info("Getting account application by ID: {}", typee);

	    AccountApplicationViewModel la = new AccountApplicationViewModel();
	    AccountApplication list = ac.getAccountsappById((long) typee); // Retrieve the list of AccountApplication

	    if (list == null) {
	        logger.error("Account application not found for ID: {}", typee);
	        throw new ApplicationNotFound("ApplicationNotFound");
	    } else {
	        la.setEntityModelValues(list); // Set the values from the AccountApplication object to the AccountApplicationViewModel
	    }

	    logger.info("Account application retrieved successfully");
	    return la; // Return the AccountApplicationViewModel object
	}


	@Override
	public LoanApplicationViewModel getLoanApplicationById(int typee) throws ApplicationNotFound {
	    logger.info("Getting loan application by ID: {}", typee);

	    LoanApplicationViewModel la = new LoanApplicationViewModel();
	    LoanApplication list = ll.getLoanApplicationById((long) typee); // Retrieve the list of LoanApplication objects by value

	    if (list == null) {
	        logger.error("Loan application not found for ID: {}", typee);
	        throw new ApplicationNotFound("Application not found");
	    } else {
	        Customertrail c = cd.getCustomerById(list.getCustId().getId());
	        logger.debug("Retrieved customer details for loan application");

	        la.copyFromEntity(list, c); // Copy the values from the LoanApplication object to the LoanApplicationViewModel
	    }

	    logger.info("Loan application retrieved successfully");
	    return la; // Return the LoanApplicationViewModel object
	}


}
