package com.xt.sms.mt;

public class SmPack {
	protected SmHeader pkHead;
	protected byte[] buf;

	public SmPack() {
		this.pkHead = new SmHeader();
		this.buf = new byte[512];
	}
}