package com.nkxgen.spring.jdbc.Exception;

public class LoanWithdrawalException extends Exception {
	String message;

	public LoanWithdrawalException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}