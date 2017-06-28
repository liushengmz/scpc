package com.xt.sms.mt;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.common.util.ConfigManager;

public class MessageSubmit {
	private static Logger myLogger = Logger.getLogger(MessageSubmit.class);
	public String destCpn;
	public String feeCpn;
	public String content;
	public String serverID;
	public String vcpID;
	public String spCode;
	public String provID;
	public String feeType;
	public String feeCode;
	public String mediaType;
	public String delivery;
	public String linkId;
	public int cpntype;
	public String gameCode;
	public String sendTime;
	public String msgId;
	public SmSubmit sub;
	public Message xtsms;
	public ConnDesc conn;

	public MessageSubmit() {
		this.xtsms = new Message();
		this.conn = new ConnDesc();
	}

	public void setDestCpn(String paramString) {
		this.destCpn = paramString;
	}

	public void setFeeCpn(String paramString) {
		this.feeCpn = paramString;
	}

	public void setCpnType(int paramInt) {
		this.cpntype = paramInt;
	}

	public void setContent(String paramString) {
		this.content = paramString;
	}

	public void setServerID(String paramString) {
		this.serverID = paramString;
	}

	public void setVcpID(String paramString) {
		this.vcpID = paramString;
	}

	public void setProvID(String paramString) {
		this.provID = paramString;
	}

	public void setSpCode(String paramString) {
		this.spCode = paramString;
	}

	public void setFeeType(String paramString) {
		this.feeType = paramString;
	}

	public void setFeeCode(String paramString) {
		this.feeCode = paramString;
	}

	public void setMediaType(String paramString) {
		this.mediaType = paramString;
	}

	public void setDelivery(String paramString) {
		this.delivery = paramString;
	}

	public void setLinkId(String paramString) {
		this.linkId = paramString;
	}

	public void setGameCode(String paramString) {
		this.gameCode = paramString;
	}

	public void setSendTime(String paramString) {
		this.sendTime = paramString;
	}

	public void setMsgId(String paramString) {
		this.msgId = paramString;
	}

	public void sendResultToSmsPlatform() {
		try {
			this.sub = new SmSubmit();
			this.sub.setVcpId(this.vcpID);
			this.sub.setServerCode(this.spCode);
			this.sub.setProvId(this.provID);
			this.sub.setServerType(this.serverID);
			this.sub.setDestCpn(this.destCpn);
			this.sub.setFeeType(this.feeType);
			this.sub.setFeeCpn(this.feeCpn);
			this.sub.setFeeCpntype(this.cpntype);
			this.sub.setFeeLinkId(this.linkId);
			this.sub.setFeeMsgId(this.msgId);

			this.sub.setMediaType(this.mediaType);
			this.sub.setContent(this.content);
			this.sub.setRegisteredDelivery(this.delivery);
			myLogger.debug("开始连接... 发送MT信息...");

			String smsServerIp = (String) ConfigManager.getConfigData("sms_server_ip",
					"xiangtoneServerip not found!");
			String smsServerPort = (String) ConfigManager.getConfigData("sms_server_port",
					"xiangtoneServerport not found!");
			myLogger.debug(smsServerIp+":"+smsServerPort);

			this.xtsms.connectToServer(smsServerIp, Integer.parseInt(smsServerPort), this.conn);
			myLogger.debug(this.conn.sock);
			this.xtsms.sendSmSubmit(this.conn, this.sub);
			this.xtsms.readPa(this.conn);
			myLogger.debug("提交成功...");
		} catch (Exception e) {
			myLogger.error("sendResultToSmsPlatform",e);
		}finally {
			this.xtsms.disconnectFromServer(this.conn);
		}
	}
}