package com.xt.sms.mt;

import java.io.PrintStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class SmSubmitAckResult extends SmResult {
	private static Logger myLogger = Logger.getLogger(SmSubmitAckResult.class);
	public DeByteCode deByteCode;
	public String stat = "00";
	public String errorCode;

	public void readInBytes(byte[] b) {
		try {
//			myLogger.debug(Arrays.toString(b));
			this.deByteCode = new DeByteCode(b);
			while (this.deByteCode.offset < b.length) {
				byte type = this.deByteCode.int8();
				short len = this.deByteCode.int16();
				int valueLen = len - 3;
				myLogger.debug("type:" + type);
				switch (type) {
				case 100:
					this.errorCode = this.deByteCode.asciiz(valueLen);
					this.stat = this.errorCode;
					myLogger.debug("stat:" + this.stat);
					break;
				default:
					this.stat = "-1";
					return;
				}
			}
		} catch (Exception e) {
			myLogger.error("",e);
		}
	}
}