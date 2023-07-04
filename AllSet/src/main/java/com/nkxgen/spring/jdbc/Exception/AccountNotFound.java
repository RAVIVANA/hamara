package com.nkxgen.spring.jdbc.Exception;

public class AccountNotFound extends Exception {
	String message;

	public AccountNotFound(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
