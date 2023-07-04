package com.nkxgen.spring.jdbc.Exception;

public class EMIpayConversionException extends Exception {
	String message;

	public EMIpayConversionException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}