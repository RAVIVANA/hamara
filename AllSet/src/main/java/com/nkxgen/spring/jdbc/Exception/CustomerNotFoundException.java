package com.nkxgen.spring.jdbc.Exception;

public class CustomerNotFoundException extends Exception {
	String message;

	public CustomerNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}