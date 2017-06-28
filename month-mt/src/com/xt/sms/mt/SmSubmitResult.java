package com.xt.sms.mt;

import java.io.PrintStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class SmSubmitResult extends SmResult {
	private static Logger myLogger = Logger.getLogger(SmSubmitResult.class);
	public static String vcpId;
	public static String serverCode = "08887";
	public static String provCode = "01";
	public static String serverType;
	public static String destCpn;
	public static String feeCpn;
	public static String feeType;
	public static String feeCode;
	public static String mediaType;
	public static byte[] content;
	public static String registeredDelivery;
	public static String vcpPwd;
	public static String stat = "09";
	public DeByteCode deByteCode;

	public void readInBytes(byte[] b) {
		try {
//			myLogger.debug(Arrays.toString(b));
			this.deByteCode = new DeByteCode(b);
			while (this.deByteCode.offset < b.length) {
				byte type = this.deByteCode.int8();
				short len = this.deByteCode.int16();
				int valueLen = len - 3;
				myLogger.debug("---type:" + type);
				myLogger.debug("---valueLen:  " + valueLen);
				switch (type) {
				case 1:
					vcpId = this.deByteCode.asciiz(valueLen);
					myLogger.debug("vcpId:" + vcpId);
					break;
				case 2:
					serverCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("serverCode:" + serverCode);
					break;
				case 3:
					provCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("provId:" + provCode);
					break;
				case 4:
					serverType = this.deByteCode.asciiz(valueLen);
					myLogger.debug("serverType:" + serverType);
					break;
				case 5:
					destCpn = this.deByteCode.asciiz(valueLen);
					myLogger.debug("destCpn:" + destCpn);
					break;
				case 6:
					feeCpn = this.deByteCode.asciiz(valueLen);
					myLogger.debug("feeCpn:" + feeCpn);
					break;
				case 7:
					feeType = this.deByteCode.asciiz(valueLen);
					myLogger.debug("feeType:" + feeType);
					break;
				case 8:
					feeCode = this.deByteCode.asciiz(valueLen);
					myLogger.debug("feeCode:" + feeCode);
					break;
				case 9:
					mediaType = this.deByteCode.asciiz(valueLen);
					myLogger.debug("mediaType:" + mediaType);
					break;
				case 10:
					content = this.deByteCode.getBytes(valueLen);
					myLogger.debug("content£º" + new String(content));
					break;
				case 11:
					registeredDelivery = this.deByteCode.asciiz(valueLen);
					myLogger.debug("registeredDelivery:" + registeredDelivery);
					break;
				case 12:
					vcpPwd = this.deByteCode.asciiz(valueLen);
					myLogger.debug("vcpPwd:" + vcpPwd);
				default:
					stat = "01";
					return;
				}
			}
			stat = "00";
		} catch (Exception e) {
			myLogger.error(Arrays.toString(b),e);
		}
	}

	public String getVcpId() {
		return vcpId;
	}

	public String getServerCode() {
		return serverCode;
	}

	public String getMediaType() {
		return mediaType;
	}

	public String getFeeType() {
		return feeType;
	}

	public String getServerType() {
		return serverType;
	}

	public String getDestCpn() {
		return destCpn;
	}

	public String getFeeCpn() {
		return feeCpn;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public byte[] getContent() {
		return content;
	}

	public String getStat() {
		return stat;
	}

	public String getProvCode() {
		return provCode;
	}

	public String getDelivery() {
		return registeredDelivery;
	}

	public String getVcpPwd() {
		return vcpPwd;
	}
}