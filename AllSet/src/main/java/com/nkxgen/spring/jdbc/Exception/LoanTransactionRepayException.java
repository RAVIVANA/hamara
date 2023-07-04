package com.nkxgen.spring.jdbc.Exception;

public class LoanTransactionRepayException extends Exception {
	String message;

	public LoanTransactionRepayException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}