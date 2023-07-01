package com.nkxgen.spring.jdbc.Exception;

public class InvalidLoanRepaymentException extends Exception {
	String message;

	public InvalidLoanRepaymentException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}