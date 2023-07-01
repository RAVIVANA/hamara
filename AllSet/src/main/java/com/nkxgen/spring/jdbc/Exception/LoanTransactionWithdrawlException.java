package com.nkxgen.spring.jdbc.Exception;

public class LoanTransactionWithdrawlException extends Exception {
	String message;

	public LoanTransactionWithdrawlException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}