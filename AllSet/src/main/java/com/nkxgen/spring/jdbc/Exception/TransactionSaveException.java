package com.nkxgen.spring.jdbc.Exception;

public class TransactionSaveException extends Exception {
	String message;

	public TransactionSaveException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}