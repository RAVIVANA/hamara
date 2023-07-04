package com.nkxgen.spring.jdbc.Exception;

public class LoanAccountApplicationNotFoundException extends Exception {
	String message;

	public LoanAccountApplicationNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}