package com.nkxgen.spring.jdbc.Exception;

public class UsernameNotFoundException extends RuntimeException {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UsernameNotFoundException(String message) {
		super();
		this.message = message;
	}

}
