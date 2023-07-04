package com.nkxgen.spring.jdbc.Exception;

public class LoanTransactionSaveException extends Exception {
	String message;

	public LoanTransactionSaveException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}