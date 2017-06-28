package com.xt.sms.mt;

public class UnknownPackException extends Exception {
	String details;

	public UnknownPackException() {
		this.details = "unknown packet is received";
	}
}