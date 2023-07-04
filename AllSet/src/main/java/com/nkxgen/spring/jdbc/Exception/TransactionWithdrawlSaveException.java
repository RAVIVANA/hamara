package com.nkxgen.spring.jdbc.Exception;

public class TransactionWithdrawlSaveException extends Exception {
	String message;

	public TransactionWithdrawlSaveException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}