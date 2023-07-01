package com.nkxgen.spring.jdbc.Exception;

public class TransactionDepositSaveException extends Exception {
	String message;

	public TransactionDepositSaveException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}