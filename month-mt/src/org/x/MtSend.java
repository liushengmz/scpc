package org.x;

import com.xt.sms.mt.MessageSubmit;
import com.xt.util.DBForLocal;
import com.xt.util.DBForRead;
import com.xt.util.DateTimeTool;
import java.io.File;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.common.util.ConfigManager;

public class MtSend implements Runnable {
	private static Logger logger = Logger.getLogger(MtSend.class);
	private static boolean ifsend930 = true;
	private static boolean ifsend = true;

	private DBForLocal dbLocal = new DBForLocal();
	private DBForRead dbLog = new DBForRead();
	private long sleep = Long.valueOf(ConfigManager.getConfigData("sleep", "10000"));

	private Map<String, Map<String, String>> serviceMap = new HashMap();
	private Map<String, List<String>> messagesMap = new HashMap();
	private Map<String, List<String>> messagesSpecialMap = new HashMap();

	public static void main(String[] args) {
		MtSend ms = new MtSend();
		logger.debug("start");
		Thread t = new Thread(ms);
		t.start();
	}

	public void run() {
		while (true) {
			try {
				int hourOfDay = DateTimeTool.getHourOfDay();
				int min = DateTimeTool.getMinute();
				if ((hourOfDay >= 9) && (hourOfDay < 19)) {
					if ((hourOfDay > 9) || (min >= 30)) {
						loadServicePrice();
						loadMessages();
						loadMessagesSpecial();
						sendRetainedUser();
						sendNewUser();
						ifsend = true;
						ifsend930 = true;
					} else {
						if (ifsend930) {
							logger.debug("9:00-9:30 not send...");
							ifsend930 = false;
						}
					}
				} else {
					if (ifsend) {
						logger.debug("out of range 9:00-19:00 not send...");
						ifsend = false;
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			// logger.debug("sleep "+sleep+"s");
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadServicePrice() {
		if (this.serviceMap.size() > 0) {
			this.serviceMap.clear();
		}
		String sql = "select gameid,gamecode,spcode,gamename,price from companygames";
		logger.debug(sql);
		try {
			ResultSet rs = dbLog.executeQuery(sql);
			while (rs.next()) {
				String gamecode = rs.getString("gamecode");
				String gameid = rs.getString("gameid");
				String spcode = rs.getString("spcode");
				String gamename = rs.getString("gamename");
				int price = rs.getInt("price") / 100;
				Map map = new HashMap();
				map.put("gameid", gameid);
				map.put("spcode", spcode);
				map.put("gamename", gamename);
				map.put("price", String.valueOf(price));
				map.put("gameid", gameid);
				this.serviceMap.put(gamecode, map);
			}
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLog.close();
		}
	}

	private void loadMessages() {
		if (this.messagesMap.size() > 0) {
			this.messagesMap.clear();
		}
		String sql = "select serverid,msg from messages";
		logger.debug(sql);
		try {
			ResultSet rs = dbLog.executeQuery(sql);
			String msg;
			while (rs.next()) {
				String serverid = rs.getString("serverid");
				msg = rs.getString("msg");
				List list = (List) this.messagesMap.get(serverid);
				if (list == null) {
					list = new ArrayList();
					list.add(msg);
					this.messagesMap.put(serverid, list);
				} else {
					list.add(msg);
				}
			}
			for (String serverid : this.messagesMap.keySet())
				logger.debug("'" + serverid + "' : " + ((List) this.messagesMap.get(serverid)).size());
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLog.close();
		}

	}

	private void loadMessagesSpecial() {
		if (this.messagesSpecialMap.size() > 0) {
			this.messagesSpecialMap.clear();
		}
		String sql = "select serverid,msg from messages_special";
		logger.debug(sql);
		try {
			ResultSet rs = dbLog.executeQuery(sql);
			String msg;
			while (rs.next()) {
				String serverid = rs.getString("serverid");
				msg = rs.getString("msg");
				List list = (List) this.messagesSpecialMap.get(serverid);
				if (list == null) {
					list = new ArrayList();
					list.add(msg);
					this.messagesSpecialMap.put(serverid, list);
				} else {
					list.add(msg);
				}
			}
			for (String serverid : this.messagesSpecialMap.keySet())
				logger.debug("'" + serverid + "' : " + ((List) this.messagesSpecialMap.get(serverid)).size());
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLog.close();
		}

	}

	private int getSuccCount(String cpn, String serverid) {
		int count = 0;

		String sql = "select count(*) as c from sms_platform.sms_mtlogbackup where feetype='03' and reptstat='DELIVRD' and destcpn = '"
				+ cpn + "' and serverid = '" + serverid + "'";
		logger.debug(sql);
		try {
			ResultSet rs = dbLog.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt("c");
			}
			return count;
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLog.close();
		}
		return count;

	}

	private int updateCompanysUser(String id, String msgid, String sendate) {
		String sql = "update companys_user set msgid = " + msgid + "," + "sendate = '" + sendate + "',"
				+ "firstsend = '1'," + "sendflag = '1'," + "sendedtime = now() " + "where id = " + id;
		logger.debug(sql);
		try {
			return this.dbLocal.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLocal.close();
		}
		return 0;
	}

	private int updateCompanysUser(String id, String sendate) {
		String sql = "update companys_user set sendate = '" + sendate + "' where id = " + id;
		logger.debug(sql);
		try {
			return this.dbLocal.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLocal.close();
		}
		return 0;
	}

	private int insertSendRecord(Map<String, String> map) {
		String sql = "insert into sendrecord_month(cpn, serviceid, senddate, provid, sendtime, msg) values('"
				+ (String) map.get("cpn") + "', " + "'" + (String) map.get("serviceid") + "', " + "'"
				+ DateTimeTool.getToday() + "', " + "'" + (String) map.get("provid") + "', " + "now(), " + "'"
				+ (String) map.get("msg") + "')";
		logger.debug(sql);
		try {
			return this.dbLocal.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(sql, e);
		} finally {
			this.dbLocal.close();
		}
		return 0;
	}

	private void getMessage(Map<String, String> map) {
		String serviceid = (String) map.get("serviceid");
		int msgid = Integer.valueOf((String) map.get("msgid")).intValue();
		String msg = "谢谢您的关注，精彩内容稍后奉上，敬请期待";
		List list = (List) this.messagesSpecialMap.get(serviceid);
		if (list == null) {
			list = (List) this.messagesMap.get(serviceid);
		}
		if (msgid >= list.size()) {
			msgid = 0;
		}
		msg = (String) list.get(msgid);
		map.put("msgid", String.valueOf(msgid + 1));
		map.put("msg", msg);
	}

	private void getFirstSendMessage(Map<String, String> map) {
		String serviceid = (String) map.get("serviceid");
		int msgid = Integer.valueOf((String) map.get("msgid")).intValue();
		String msg = "谢谢您的关注，精彩内容稍后奉上，敬请期待";
		List list = (List) this.messagesMap.get(serviceid);
		if (list != null) {
			if (msgid >= list.size()) {
				msgid = 0;
			}
			msg = (String) list.get(msgid);
		}
		map.put("msgid", String.valueOf(msgid + 1));
		map.put("msg", msg);
	}

	private void filterSendList(List<Map<String, String>> list) {
		int sendCount = Integer.valueOf(ConfigManager.getConfigData("send_count", "15"));
		long millis = System.currentTimeMillis();
		int enoughCount = 0;
		int notEnoughCount = 0;
		int sendMtCount = 0;
		int errorCount = 0;
		int sendMtTmpCount = 0;
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String id = (String) map.get("id");
			String company = (String) map.get("company");
			String cpn = (String) map.get("cpn");
			String serviceid = (String) map.get("serviceid");

			logger.debug("RetainedUser filter " + (i + 1) + " count.");
			try {
				Map service = (Map) this.serviceMap.get(serviceid);
				if (service != null) {
					String serverid = (String) service.get("gameid");

					int succCount = getSuccCount(cpn, serverid);
					int price = Integer.valueOf((String) service.get("price")).intValue();
					int feeCount = price / 2 + 1;

					if (succCount < feeCount) {
						logger.debug("price : " + price + " fee_count : " + feeCount + " succ_count : " + succCount
								+ " not_enough.");
						notEnoughCount++;
						getMessage(map);
						updateCompanysUser(id, (String) map.get("msgid"), DateTimeTool.getTomorrow());
						insertSendRecord(map);
						String[] msg = splitConent((String) map.get("msg"));
						sendMT(map, msg);
						sendMtCount += msg.length;
						sendMtTmpCount += msg.length;
						if (sendMtTmpCount >= sendCount) {
							long curMillis = System.currentTimeMillis();
							long sendMillis = curMillis - millis;
							logger.debug("RetainedUser send " + sendCount + " time " + sendMillis + " ms.");
							if (sendMillis < 1000L) {
								long sleepMillis = 1000L - sendMillis;
								logger.debug("RetainedUser sleep " + sleepMillis + " ms.");
								Thread.sleep(sleepMillis);
							}
							sendMtTmpCount = 0;
							millis = System.currentTimeMillis();
						}
					} else {
						logger.debug("price : " + price + " fee_count : " + feeCount + " succ_count : " + succCount
								+ " enough.");
						enoughCount++;
						updateCompanysUser(id, DateTimeTool.getNextMonthFirstday());
					}
				} else {
					errorCount++;
					logger.warn("error service : " + id + ", " + company + ", " + cpn + ", " + serviceid);
				}
			} catch (Exception e) {
				errorCount++;
				logger.error("error service : " + id + ", " + company + ", " + cpn + ", " + serviceid, e);
			}
		}
		logger.debug("total :  " + list.size());
		logger.debug("enoughCount : " + enoughCount);
		logger.debug("sendMtCount : " + sendMtCount);
		logger.debug("notEnoughCount : " + notEnoughCount);
		logger.debug("errorCount : " + errorCount);
	}

	private void sendMT(Map<String, String> map, String[] msg) {
		String serviceId = (String) map.get("serviceid");
		Map service = (Map) this.serviceMap.get(serviceId);
		if (service == null) {
			return;
		}
		String destcpn = (String) map.get("cpn");
		String feecpn = (String) map.get("cpn");
		int tempCpntype = 0;
		String cpnlinkid = "";

		String serverID = (String) service.get("gameid");
		String vcpID = "1";

		String provID = "01";

		String spCode = (String) service.get("spcode");
		String strFeeType = "0";
		String mediaType = "1";
		String delivery = "0";

		String gameCode = serviceId.substring(1);
		String msgId = "";
		for (int i = 0; i < msg.length; i++) {
			MessageSubmit ms = new MessageSubmit();
			ms.setDestCpn(destcpn);
			ms.setFeeCpn(feecpn);
			ms.setCpnType(tempCpntype);
			ms.setLinkId(cpnlinkid);
			ms.setContent(msg[i]);
			ms.setServerID(serverID);
			ms.setVcpID(vcpID);
			ms.setProvID(provID);
			ms.setSpCode(spCode);
			ms.setFeeType(strFeeType);

			ms.setMediaType(mediaType);
			ms.setDelivery(delivery);
			ms.setGameCode(gameCode);
			ms.setMsgId(msgId);
			ms.setSendTime(DateTimeTool.getCurrentTime());

			logger.debug("destCpn : '" + ms.destCpn + "', " + "feeCpn : '" + ms.feeCpn + "', " + "cpnType : '"
					+ ms.cpntype + "', " + "linkid : '" + ms.linkId + "', " + "content : '" + ms.content + "', "
					+ "serverID : '" + ms.serverID + "', " + "vcpID : '" + ms.vcpID + "', " + "provID : '" + ms.provID
					+ "', " + "spCode : '" + ms.spCode + "', " + "feeType : '" + ms.feeType + "', " + "mediaType : '"
					+ ms.mediaType + "', " + "delivery : '" + ms.delivery + "', " + "gameCode : '" + ms.gameCode + "', "
					+ "msgId : '" + ms.msgId + "'");
			ms.sendResultToSmsPlatform();
		}
	}

	private String[] splitConent(String smContent) {
		int nLen = smContent.length();
		int n1 = (nLen + 69) / 70;
		logger.debug("短信条数:" + n1);
		String[] str1 = new String[n1];
		for (int j = 0; j < n1; j++) {
			int j1 = j * 70;
			int j2 = (j + 1) * 70;
			if (j < n1 - 1)
				str1[j] = smContent.substring(j1, j2);
			else {
				str1[j] = smContent.substring(j1);
			}
		}
		return str1;
	}

	private void sendRetainedUser() {
		int id = 0;
		List sendList = new ArrayList();
		String limit = ConfigManager.getConfigData("retained_user_limit", "30");
		do {
			sendList.clear();
			String sql = "select id,company,cpn,serviceid,msgid,provid from companys_user where (state = '1' or state='3') and firstsend = 1 and sendate <= '"
					+ DateTimeTool.getToday() + "' " + "and (UNIX_TIMESTAMP(now())-UNIX_TIMESTAMP(addate))>=3600*72 "
					+ "and id > " + id + " " + "order by id limit " + limit;
			logger.debug(sql);
			try {
				ResultSet rs = dbLog.executeQuery(sql);
				while (rs.next()) {
					id = rs.getInt("id");
					String company = rs.getString("company");
					String cpn = rs.getString("cpn");
					String serviceid = rs.getString("serviceid");
					int msgid = rs.getInt("msgid");
					String provid = rs.getString("provid");

					Map map = new HashMap();
					map.put("id", String.valueOf(id));
					map.put("company", company);
					map.put("cpn", cpn);
					map.put("serviceid", serviceid);
					map.put("msgid", String.valueOf(msgid));
					map.put("provid", provid);

					sendList.add(map);
				}
			} catch (SQLException e) {
				logger.error(sql, e);
			} finally {
				this.dbLog.close();
			}

			logger.debug("retained sendList : " + sendList.size());

			filterSendList(sendList);
		} while (sendList.size() > 0);
	}

	private void sendNewUser() {
		List sendList = new ArrayList();
		String limit = ConfigManager.getConfigData("new_user_limit", "30");
		int sendCount = Integer.valueOf(ConfigManager.getConfigData("send_count", "15"));
		String sql = "select id,company,cpn,serviceid,msgid,provid from companys_user where (state = '1' or state='3') and firstsend = 0 order by id limit "
				+ limit;
		logger.debug(sql);

		try {
			ResultSet rs = dbLog.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String company = rs.getString("company");
				String cpn = rs.getString("cpn");
				String serviceid = rs.getString("serviceid");
				int msgid = rs.getInt("msgid");
				String provid = rs.getString("provid");

				Map map = new HashMap();
				map.put("id", String.valueOf(id));
				map.put("company", company);
				map.put("cpn", cpn);
				map.put("serviceid", serviceid);
				map.put("msgid", String.valueOf(msgid));
				map.put("provid", provid);

				sendList.add(map);
			}
		} catch (SQLException e1) {
			logger.error(sql, e1);
		} finally {
			this.dbLog.close();
		}

		logger.debug("new user sendList : " + sendList.size());

		long millis = System.currentTimeMillis();
		int sendMtCount = 0;
		int errorCount = 0;
		int sendMtTmpCount = 0;

		for (int i = 0; i < sendList.size(); i++) {
			Map map = (Map) sendList.get(i);
			String id = (String) map.get("id");
			String company = (String) map.get("company");
			String cpn = (String) map.get("cpn");
			String serviceid = (String) map.get("serviceid");
			logger.debug("NewUser send " + (i + 1) + " count.");
			try {
				Map service = (Map) this.serviceMap.get(serviceid);
				if (service != null) {
					getFirstSendMessage(map);
					updateCompanysUser(id, (String) map.get("msgid"), DateTimeTool.getThreeDaysLater());
					insertSendRecord(map);
					String[] msg = splitConent((String) map.get("msg"));
					sendMT(map, msg);
					sendMtCount += msg.length;
					sendMtTmpCount += msg.length;
					if (sendMtTmpCount >= sendCount) {
						long curMillis = System.currentTimeMillis();
						long sendMillis = curMillis - millis;
						logger.debug("NewUser send " + sendCount + " time " + sendMillis + " ms.");
						if (sendMillis < 1000L) {
							long sleepMillis = 1000L - sendMillis;
							logger.debug("NewUser sleep " + sleepMillis + " ms.");
							Thread.sleep(sleepMillis);
						}
						sendMtTmpCount = 0;
						millis = System.currentTimeMillis();
					}
				} else {
					errorCount++;
					logger.debug("error service : " + id + ", " + company + ", " + cpn + ", " + serviceid);
				}
			} catch (Exception e) {
				errorCount++;
				logger.error("error service : " + id + ", " + company + ", " + cpn + ", " + serviceid, e);
			}
		}
		logger.debug("total :  " + sendList.size());
		logger.debug("sendMtCount : " + sendMtCount);
		logger.debug("errorCount : " + errorCount);
	}
}