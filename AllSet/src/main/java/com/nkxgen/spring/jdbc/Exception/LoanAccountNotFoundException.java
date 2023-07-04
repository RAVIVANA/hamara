package com.nkxgen.spring.jdbc.Exception;

public class LoanAccountNotFoundException extends Exception {
	String message;

	public LoanAccountNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}