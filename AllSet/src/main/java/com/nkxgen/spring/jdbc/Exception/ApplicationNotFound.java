package com.nkxgen.spring.jdbc.Exception;

public class ApplicationNotFound extends Exception {
	String message;

	public ApplicationNotFound(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
