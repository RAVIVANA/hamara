package com.nkxgen.spring.jdbc.Test;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class LoanControllerTestRunner {
    public static void main(String[] args) {
        // Create an instance of TestNG
        TestNG testNG = new TestNG();
        
        // Create a TestListenerAdapter to listen to test events
        TestListenerAdapter listenerAdapter = new TestListenerAdapter();
        
        // Add the listener adapter to the TestNG instance
        testNG.addListener(listenerAdapter);
        
        // Set the test classes to be run
        testNG.setTestClasses(new Class[]{LoanControllerTest.class});
        
        // Run the tests
        testNG.run();
    }
}
