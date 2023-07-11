package com.nkxgen.spring.jdbc.Test;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        TestListenerAdapter listenerAdapter = new TestListenerAdapter();
        TestNG testng = new TestNG();
        
        // Add the listener adapter to the TestNG instance
        testng.addListener(listenerAdapter);
        
        // Set the test classes to be run
        testng.setTestClasses(new Class[]{MasterControllerTest.class});
        
        // Run the tests
        testng.run();
    }
}
