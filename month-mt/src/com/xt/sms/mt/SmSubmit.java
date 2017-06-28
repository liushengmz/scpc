package com.xt.sms.mt;

import org.apache.log4j.Logger;

public class SmSubmit {
	private static Logger myLogger = Logger.getLogger(SmSubmit.class);
	public ByteCode bc;

	public SmSubmit() {
		this.bc = new ByteCode(1);
	}

	public void setVcpId(String vcpId) throws Exception {
		this.bc.AddByte((byte) 1);
		this.bc.AddShort((short) (3 + vcpId.getBytes().length));
		this.bc.AddBytes(vcpId.getBytes());
	}

	public void setServerCode(String serverCode) throws Exception {
		this.bc.AddByte((byte) 2);
		this.bc.AddShort((short) (3 + serverCode.getBytes().length));
		this.bc.AddBytes(serverCode.getBytes());
	}

	public void setMediaType(String mediaType) throws Exception {
		this.bc.AddByte((byte) 9);
		this.bc.AddShort((short) (3 + mediaType.getBytes().length));
		this.bc.AddBytes(mediaType.getBytes());
	}

	public void setServerType(String serverType) throws Exception {
		this.bc.AddByte((byte) 4);
		this.bc.AddShort((short) (3 + serverType.getBytes().length));
		this.bc.AddBytes(serverType.getBytes());
	}

	public void setDestCpn(String destCpn) throws Exception {
		this.bc.AddByte((byte) 5);
		this.bc.AddShort((short) (3 + destCpn.getBytes().length));
		this.bc.AddBytes(destCpn.getBytes());
	}

	public void setFeeCpn(String feeCpn) throws Exception {
		this.bc.AddByte((byte) 6);
		this.bc.AddShort((short) (3 + feeCpn.getBytes().length));
		this.bc.AddBytes(feeCpn.getBytes());
	}

	public void setFeeType(String feeType) throws Exception {
		this.bc.AddByte((byte) 7);
		this.bc.AddShort((short) (3 + feeType.getBytes().length));
		this.bc.AddBytes(feeType.getBytes());
	}

	public void setFeeCode(String feeCode) throws Exception {
		this.bc.AddByte((byte) 8);
		this.bc.AddShort((short) (3 + feeCode.getBytes().length));
		this.bc.AddBytes(feeCode.getBytes());
	}

	public void setContent(String content) throws Exception {
		this.bc.AddByte((byte) 10);
		this.bc.AddShort((short) (3 + content.getBytes().length));
		this.bc.AddBytes(content.getBytes());
	}

	public void setProvId(String provId) throws Exception {
		this.bc.AddByte((byte) 3);
		this.bc.AddShort((short) (3 + provId.getBytes().length));
		this.bc.AddBytes(provId.getBytes());
	}

	public void setRegisteredDelivery(String registeredDelivery) throws Exception {
		this.bc.AddByte((byte) 11);
		this.bc.AddShort((short) (3 + registeredDelivery.getBytes().length));
		this.bc.AddBytes(registeredDelivery.getBytes());
	}

	public void setFeeLinkId(String linkid) throws Exception {
		if (linkid == null) {
			linkid = "";
		}
		this.bc.AddByte((byte) 12);
		this.bc.AddShort((short) (3 + linkid.getBytes().length));
		this.bc.AddBytes(linkid.getBytes());
	}

	public void setFeeCpntype(int cpntype) {
		this.bc.AddByte((byte) 13);
		this.bc.AddShort((short) 4);
		this.bc.AddByte((byte) cpntype);
	}

	public void setFeeMsgId(String msgId) {
		if (msgId == null)
			msgId = "";
		try {
			this.bc.AddByte((byte) 14);
			this.bc.AddShort((short) (3 + msgId.getBytes().length));
			this.bc.AddBytes(msgId.getBytes());
		} catch (Exception e) {
			myLogger.error("setFeeMsgId",e);
		}
	}

	public byte[] getBytes() {
		return this.bc.getBytes();
	}
}