package com.nkxgen.spring.jdbc.Exception;

public class AccountNotFoundException extends Exception {
	String message;

	public AccountNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}