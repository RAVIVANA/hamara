package com.nkxgen.spring.jdbc.Test;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nkxgen.spring.jdbc.Bal.CustomerSetter;
import com.nkxgen.spring.jdbc.Bal.ViewInterface;
import com.nkxgen.spring.jdbc.DaoInterfaces.CustomerDaoInterface;
import com.nkxgen.spring.jdbc.ViewModels.CustomerViewModel;
import com.nkxgen.spring.jdbc.controller.customercontroller;
import com.nkxgen.spring.jdbc.model.Customer;
import com.nkxgen.spring.jdbc.model.CustomerSub;
import com.nkxgen.spring.jdbc.model.Customertrail;

public class CustomerControllerTest {
	@Mock
	private CustomerDaoInterface customerDao;

	@Mock
	private ViewInterface viewInterface;

	@InjectMocks
	private customercontroller customerController;

	@Mock
	private Model model;

	@Mock
	private CustomerSetter s;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCustomerDataSaveToDb() {
		// Set up test data
		CustomerViewModel customer = new CustomerViewModel();
		// Set necessary properties of customer view model

		// Set up the behavior of the mock CustomerSetter
		Customertrail expectedCustomerTrail = new Customertrail();
		Mockito.when(s.dotheservice2(Mockito.any(CustomerViewModel.class))).thenReturn(expectedCustomerTrail);

		// Set up the behavior of the mock CustomerDaoInterface

		// Call the customerDataSaveToDb method
		String result = customerController.customerDataSaveToDb(customer, model);

		// Verify that the necessary methods were called on the mocks
		Mockito.verify(s).dotheservice2(customer);
		Mockito.verify(customerDao).saveCustomer(expectedCustomerTrail);

		// Verify the returned view name
		Assert.assertEquals(result, "account-new-application-form");
	}

	@Test
	public void testSaveToCustomerDatabase() {
		// Set up test data
		Long customerId = 123L;

		// Set up the behavior of the mock CustomerDaoInterface
		Customertrail expectedCustomerTrail = new Customertrail();
		Mockito.when(customerDao.getCustomerById(customerId)).thenReturn(expectedCustomerTrail);
		Customer customer = new Customer();

		Mockito.doNothing().when(s).dotheservice(expectedCustomerTrail);

		// Call the saveToCustomerDatabase method
		String result = customerController.saveToCustomerDatabase(customerId);

		// Verify that the necessary methods were called on the mocks
		Mockito.verify(customerDao).getCustomerById(customerId);
		Mockito.verify(s).dotheservice(expectedCustomerTrail);
		Mockito.verify(customerDao).saveCustomertoDb(customer);

		// Verify the returned view name
		Assert.assertEquals(result, "account-new-application-form");
	}

	@Test
	public void testGetAllCustomers() {
		// Set up the behavior of the mock ViewInterface
		List<Customer> customerList = new ArrayList<>();
		Mockito.when(viewInterface.getAllCustomers()).thenReturn(customerList);

		// Call the getAllCustomers method
		String result = customerController.getAllCustomers(model);

		// Verify that the necessary methods were called on the mocks
		Mockito.verify(viewInterface).getAllCustomers();

		// Verify the returned view name
		Assert.assertEquals(result, "customer-edit-details-form");

		// Verify that the customerList attribute was added to the model
		Mockito.verify(model).addAttribute("customerList", customerList);
	}

	@Test
	public void testCustomerDataUpdation() {
		// Set up test data
		Customertrail updatedCustomer = new Customertrail();

		// Call the CustomerDataUpdation method
		String result = customerController.CustomerDataUpdation(updatedCustomer);

		// Verify that the necessary methods were called on the mock CustomerDaoInterface
		Mockito.verify(customerDao).updateCustomerDataById(updatedCustomer);

		// Verify the returned view name
		Assert.assertEquals(result, "customer-edit-details-form");
	}

	@Test
	public void testSaveToCustomersubDatabase() {
		// Set up test data
		CustomerSub customerSub = new CustomerSub();
		// Set necessary properties of CustomerSub

		// Set up the behavior of the mock CustomerDaoInterface
		Long customerId = customerSub.getCustomerId();
		Customertrail customerTrail = new Customertrail();
		Mockito.when(customerDao.getRealCustomerById(customerId)).thenReturn(customerTrail);

		// Call the saveToCustomersubDatabase method
		String result = customerController.saveToCustomersubDatabase(customerSub, model);

		// Verify that the necessary methods were called on the mock CustomerDaoInterface
		Mockito.verify(customerDao).getRealCustomerById(customerId);
		Mockito.verify(customerDao).changethese(customerTrail, customerSub);

		// Verify the returned view name
		Assert.assertEquals(result, "any-type-account-info");
	}

	// Add more test methods for other methods in the customercontroller class

}
