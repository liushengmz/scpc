package com.xt.sms.mt;

import java.io.PrintStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class SmDeliverResult extends SmResult {
	private static Logger myLogger = Logger.getLogger(SmDeliverResult.class);
	public static String mobileCode;
	public static String gameCode;
	public static String actionCode;
	public static String spCode;
	public static String ismgCode;
	public static String linkId;
	public static int cpnType;
	public static String stat;
	public static String msgId;
	public DeByteCode deByteCode;

	public void readInBytes(byte[] b) {
		try {
//			myLogger.debug(Arrays.toString(b));
			this.deByteCode = new DeByteCode(b);
			while (this.deByteCode.offset < b.length) {
				byte type = this.deByteCode.int8();
				short len = this.deByteCode.int16();
				int valueLen = len - 3;
				switch (type) {
				case 1:
					mobileCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("mobileCode:" + mobileCode);
					break;
				case 2:
					gameCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("gameCode:" + gameCode);
					break;
				case 3:
					actionCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("actionCode:" + actionCode);
					break;
				case 4:
					spCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("spCode:" + spCode);
					break;
				case 5:
					ismgCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("ismgCode:" + ismgCode);
					break;
				case 12:
					linkId = this.deByteCode.asciiz(valueLen);
					myLogger.debug("Linkid:" + linkId);
					break;
				case 13:
					cpnType = this.deByteCode.int8();
					myLogger.debug("cpnType:" + cpnType);
					break;
				case 14:
					msgId = this.deByteCode.asciiz(valueLen);
					myLogger.debug("msgId:" + msgId);
					break;
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				default:
					stat = "01";
					return;
				}
			}
			stat = "00";
		} catch (Exception e) {
//			myLogger.error(Arrays.toString(b), e);
		}
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public String getGameCode() {
		return gameCode;
	}

	public String getActionCode() {
		return actionCode;
	}

	public String getSpCode() {
		return spCode;
	}

	public String getIsmgCode() {
		return ismgCode;
	}

	public String getLinkId() {
		return linkId;
	}

	public int getCpntype() {
		return cpnType;
	}

	public String getMsgId() {
		return msgId;
	}

}