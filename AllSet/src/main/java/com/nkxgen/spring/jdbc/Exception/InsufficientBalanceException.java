package com.nkxgen.spring.jdbc.Exception;

public class InsufficientBalanceException extends Exception {
	String message;

	public InsufficientBalanceException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}