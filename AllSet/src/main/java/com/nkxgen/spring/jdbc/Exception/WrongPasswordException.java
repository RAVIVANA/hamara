package com.nkxgen.spring.jdbc.Exception;

public class WrongPasswordException extends RuntimeException {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public WrongPasswordException(String message) {
		super();
		this.message = message;
	}

}
