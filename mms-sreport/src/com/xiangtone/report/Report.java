/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xiangtone.report;

//import com.xiangtone.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.io.*;
//import java.lang.reflect.Method;
import java.net.*;
import org.apache.log4j.Logger;
import org.common.util.ConnectionService;

/**
 * @author Administrator
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Report extends Thread {
	private static Logger logger = Logger.getLogger(Report.class);

	private String sql = "";

	public ArrayList urlList = new ArrayList();
	public ArrayList leagueList = new ArrayList();

	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public Report() {
		/*
		 * db=new MysqlDB("league"); ResultSet rs=null; sql=
		 * "select url,league_id from server_month_info where qx_sign=9 ";
		 * logger.debug("sql==="+sql); try { rs=db.execQuery(sql); while
		 * (rs.next()){ urlList.add(rs.getString("url"));
		 * leagueList.add(rs.getString("league_id")); } }
		 * 
		 * catch (Exception e){ logger.error("db exception- restart db--"+e); //
		 * Log.showLog(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+
		 * "db exception- restart db-"+e,path); db=new MysqlDB("league"); }
		 */

	}

	public void getList() {
		urlList.clear();
		leagueList.clear();
		sql = "select url,league_id from server_month_info where qx_sign=9 ";
		logger.debug("sql===" + sql);
		try {
			con = ConnectionService.getInstance().getConnectionForLocal();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				urlList.add(rs.getString("url"));
				leagueList.add(rs.getString("league_id"));
			}
		} catch (Exception e) {
			logger.error(sql, e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < urlList.size(); i++) {
			logger.debug(leagueList.get(i).toString() + "==getList=" + urlList.get(i).toString());
		}
	}

	public void run() {
		while (true) {
			if (urlList.isEmpty())
				getList();
			// againSendFaile();
			// againSendSucc();
			checkReport();
			logger.debug("begin report");
			try {
				logger.debug("sleep 2 min");
				logger.debug("sleep 2 min");
				Thread.sleep(2 * 60 * 1000);
			} catch (InterruptedException e1) {
				logger.error("e1" + e1.toString());
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}
		}
	}

	private void checkReport() {
		sql = "select exchange_id,src_phone,dst_phone,service_code,exchange_time,success_time,exchange_status,messageid,league_id from mms_transaction_detail where exchange_status<>1 and report_status=0 and (league_id='1111' or league_id='1008') order by exchange_id desc limit 200";
		logger.debug("sql===" + sql);
		con = ConnectionService.getInstance().getConnectionForLocal();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				logger.debug("sleep 0.5s");
				sleep(500);
				if (leagueList.indexOf(rs.getString("league_id")) < 0) {
					sql = "update mms_transaction_detail set report_status=1 where exchange_id="
							+ rs.getInt("exchange_id");
					logger.debug("sql==" + sql);
					ps.executeUpdate(sql);
					logger.debug("succ no change a send message because by no leagueid");
				} else if (rs.getString("league_id").equals("1111")) {
					logger.debug("leagueid=============1111");
					// url="http://222.76.210.154:8888/report?reportid=".$messageid."&status=".$status;
					URL u = new URL("http://222.76.210.142:8888/report?reportid=" + rs.getString("messageid")
							+ "&status=" + rs.getString("exchange_status") + "&code=" + rs.getString("service_code"));
					logger.debug("u======" + u);
					URLConnection uc = u.openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
					// logger.debug("send res===="+in.readLine());
					logger.info("leagueid=1111,messageid=" + rs.getString("messageid") + ",res=" + in.readLine());
					// uc.
					sql = "update mms_transaction_detail set report_status=1 where exchange_id="
							+ rs.getInt("exchange_id");
					ps.executeUpdate(sql);
				} else if (rs.getString("league_id").equals("1027")) {
					logger.debug("leagueid=============1027");
					// http://ip:port?messageid=xxxx&status=x&success_time=x&sender=xxxx
					// url="http://222.76.210.154:8888/report?reportid=".$messageid."&status=".$status;
					String mlURL = "http://59.33.38.61:8000/get_mms_2.aspx?messageid=" + rs.getString("messageid")
							+ "&status=" + rs.getString("exchange_status") + "&success_time="
							+ java.net.URLEncoder.encode(rs.getString("success_time")) + "&sender="
							+ rs.getString("src_phone");
					logger.debug("mlURL==" + mlURL);
					URL u = new URL(mlURL);
					URLConnection uc = u.openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
					// logger.debug("send res===="+in.readLine());
					logger.info("leagueid=1027,messageid=" + rs.getString("messageid") + ",res=" + in.readLine());
					// uc.
					sql = "update mms_transaction_detail set report_status=1 where exchange_id="
							+ rs.getInt("exchange_id");
					ps.executeUpdate(sql);
				} else {
					StringBuffer sb = getCheckReportXml(rs.getString("messageid"), rs.getString("src_phone"),
							rs.getString("dst_phone"), rs.getString("service_code"), rs.getString("exchange_time"),
							rs.getString("success_time"), rs.getString("exchange_status"));
					ReportSelf RSelf = new ReportSelf(
							urlList.get(leagueList.indexOf(rs.getString("league_id"))).toString(),
							rs.getString("league_id"), sb, false, "", "");
					new Thread(RSelf).start();

					logger.debug("send checkReport");

					sql = "update mms_transaction_detail set report_status=1 where exchange_id="
							+ rs.getInt("exchange_id");

					ps.executeUpdate(sql);
					logger.debug("legueid=" + rs.getString("league_id") + "    sql==" + sql);
					logger.debug("succ change a send message");
				}
				sleep(200);
			}
		} catch (Exception e) {
			logger.error(sql,e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private StringBuffer getCheckReportXml(String messageid, String src_phone, String dst_phone, String service_code,
			String exchange_time, String success_time, String exchange_status) {
		StringBuffer ss = new StringBuffer();

		ss.append("<report><mms>\r");
		ss.append("<type>1</type>\r");
		ss.append("<seqid></seqid>\r");
		ss.append("<messageid>" + messageid + "</messageid>\r");
		ss.append("<send>" + src_phone + "</send>\r");
		ss.append("<to>" + dst_phone + "</to>\r");
		ss.append("<code>" + service_code + "</code>\r");
		ss.append("<exchange_time>" + exchange_time + "</exchange_time>\r");
		ss.append("<success_time>" + success_time + "</success_time>\r");
		ss.append("<status>" + exchange_status + "</status>\r");
		ss.append("</mms>\r</report>");
		logger.debug(ss.toString());
		return ss;

	}
	/**
	 * 
	 */
	/*
	 * private void againSendSucc() { sql=
	 * "select id,seqid,src_phone,dst_phone,faile_time,service_code,messageid,league_id from again_send where  status=1 and league_status=0 and send_times<11 "
	 * ; ResultSet rs=db.execQuery(sql); try { while (rs.next()) { logger.debug(
	 * "sleep 0.5s"); sleep(500); if
	 * (leagueList.indexOf(rs.getString("league_id"))<0) { sql=
	 * "update again_send set league_status=1 where id="+rs.getInt("id");
	 * logger.debug("update by sendsucc by no leagueid"); db.execUpdate(sql); }
	 * else { StringBuffer
	 * sb=getAgainSendSuccXml(rs.getString("seqid"),rs.getString("messageid"),rs
	 * .getString("src_phone"),rs.getString("dst_phone"),rs.getString(
	 * "service_code"),rs.getString("faile_time")); ReportSelf RSelf=new
	 * ReportSelf(urlList.get(leagueList.indexOf(rs.getString("league_id"))).
	 * toString(),rs.getString("league_id"),sb,true,
	 * "delete from again_send where id="+rs.getInt("id"),
	 * "update again_send set send_times=send_times+10 where id="
	 * +rs.getInt("id")); new Thread(RSelf).start();
	 * 
	 * } sleep(200); } } catch (Exception e) { db=new MysqlDB("league"); // TODO
	 * Auto-generated catch block logger.error(
	 * "exception e  by send agagin succ"+e); // logger.debug(
	 * "exception e  by send agagin succ"+e); // e.printStackTrace(); }
	 * 
	 * }
	 */
	/**
	 * 
	 */

	/*
	 * private void againSendFaile() { sql=
	 * "select id,seqid,src_phone,dst_phone,faile_time,service_code,league_id from again_send where league_status=0 and status=0 and send_times>=10   order by id asc "
	 * ; ResultSet rs=db.execQuery(sql); String res="111"; try { while
	 * (rs.next()) { logger.debug("sleep 0.5s"); sleep(500); if
	 * (leagueList.indexOf(rs.getString("league_id"))<0) { sql=
	 * "update again_send set league_status=1 where id="+rs.getInt("id");
	 * db.execUpdate(sql); logger.debug(
	 * "update by againSendFaile by no leagueid"); } else { StringBuffer
	 * sb=getAgainSendFailXml(rs.getString("seqid"),rs.getString("src_phone"),rs
	 * .getString("dst_phone"),rs.getString("service_code"),rs.getString(
	 * "faile_time")); ReportSelf RSelf=new
	 * ReportSelf(urlList.get(leagueList.indexOf(rs.getString("league_id"))).
	 * toString(),rs.getString("league_id"),sb,true,
	 * "delete from again_send where id="+rs.getInt("id"),
	 * "update again_send set send_times=send_times+10 where id="
	 * +rs.getInt("id")); new Thread(RSelf).start(); /* try { Class
	 * toRun=Class.forName(classname); Method
	 * mainMethod=toRun.getDeclaredMethod(classmethod,new
	 * Class[]{String.class,String.class,StringBuffer.class});
	 * res=mainMethod.invoke(toRun.newInstance(), new
	 * Object[]{urlList.get(leagueList.indexOf(rs.getString("league_id"))),sb}).
	 * toString(); } catch (Exception e) { logger.debug("===e=="+e); } //String
	 * res=send((urlList.get(leagueList.indexOf(rs.getString("league_id")))).
	 * toString(),sb);
	 * 
	 * 
	 * if (res.substring(0,1).equals("0")) { sql=
	 * "delete from again_send where id="+rs.getInt("id"); // sql=
	 * "update again_send set league_status=1 where id="+rs.getInt("id");
	 * logger.debug("delete a message by send again faile"); db.execUpdate(sql);
	 * } else { sql="update again_send set send_times=send_times+10 where id="
	 * +rs.getInt("id"); db.execUpdate(sql); }
	 */
	/*
	 * } sleep(200); }
	 * 
	 * logger.debug("report again..."); } catch (Exception e1) { db=new
	 * MysqlDB("league"); logger.error("e by check faile send .."+e1); //
	 * logger.debug("e by check faile send .."+e1); // e1.printStackTrace(); }
	 * 
	 * }
	 */
	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 * @return
	 */
	private StringBuffer getAgainSendFailXml(String seqid, String src_phone, String dst_phone, String service_code,
			String faile_time) {
		StringBuffer ss = new StringBuffer();
		ss.append("<report>\r");
		// urlList.get(1);
		ss.append("<mms>\r");
		ss.append("<type>3</type>\r");
		ss.append("<seqid>" + seqid + "</seqid>\r");
		ss.append("<messageid></messageid>\r");
		ss.append("<send>");
		ss.append(src_phone);
		ss.append("</send>\r");
		ss.append("<to>");
		ss.append(dst_phone);
		ss.append("</to>\r");
		ss.append("<code>");
		ss.append(service_code);
		ss.append("</code>\r");
		ss.append("<exchange_time>");
		ss.append(faile_time);
		ss.append("</exchange_time>\r");
		ss.append("<success_time>0000-00-00 00:00:00</success_time>\r");
		ss.append("<status></status>\r");
		ss.append("</mms>\r");
		ss.append("</report>");
		return ss;
	}

	private StringBuffer getAgainSendSuccXml(String seqid, String messageid, String src_phone, String dst_phone,
			String service_code, String faile_time) {
		StringBuffer ss = new StringBuffer();
		ss.append("<report>\r");
		ss.append("<mms>\r");
		ss.append("<type>2</type>\r");
		ss.append("<seqid>" + seqid + "</seqid>\r");
		ss.append("<messageid>" + messageid + "</messageid>\r");
		ss.append("<send>" + src_phone + "</send>\r");
		ss.append("<to>" + dst_phone + "</to>\r");
		ss.append("<code>" + service_code + "</code>");
		ss.append("<exchange_time>" + faile_time + "</exchange_time>\r");
		ss.append("<success_time>0000-00-00 00:00:00</success_time>\r");
		ss.append("<status>1</status>\r");
		ss.append("</mms>\r");
		ss.append("</report>");
		return ss;
	}

	private String send(String url, StringBuffer sb) throws Exception {

		URL send_url = new URL(url);
		URLConnection connection = send_url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		byte[] b = sb.toString().getBytes();
		logger.debug("len=====" + String.valueOf(b.length));
		logger.debug("url====" + url);
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));

		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		OutputStream out = httpConn.getOutputStream();
		out.write(b);
		out.close();

		// Read the response and write it to standard out.

		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);
		String inputLine;
		String tt = "";

		while ((inputLine = in.readLine()) != null) {
			tt += inputLine;
			// logger.debug(inputLine);
		}
		in.close();
		return tt;

		// return "";
	}

	public static void main(String[] args) {
		logger.debug("report start....!");
		try {
			Report rep = new Report();
			// StringBuffer
			// sb=getCheckReportXml(rs.getString("messageid"),rs.getString("src_phone"),rs.getString("dst_phone"),rs.getString("service_code"),rs.getString("exchange_time"),rs.getString("success_time"),rs.getString("exchange_status"));
			/*
			 * StringBuffer
			 * sb=rep.getCheckReportXml("222","13950000050","13950000050",
			 * "130101","2009-09-09 10:10:00","2009-09-09 10:15:00","1000");
			 * String
			 * res=rep.send("http://mms.gw.kkfun.com/ReceiveReportServlet",sb);
			 * logger.debug("res report=="+res);
			 * 
			 */
			StringBuffer sb2 = new StringBuffer();
			sb2.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb2.append(
					"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"	 xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"	 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"		 xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"			 xmlns:ns2=\"http://www.chinatelecom.com.cn/schema/ctcc/sms/v2_1\" ");
			sb2.append(
					" xmlns:ns3=\"http://www.chinatelecom.com.cn/schema/ctcc/common/v2_1\"		 xmlns:ns7=\"http://www.chinatelecom.com.cn/wsdl/ctcc/sms/notification/v2_1/service\"  xmlns:ns1=\"http://www.chinatelecom.com.cn/schema/ctcc/sms/notification/v2_1/local\"	 xmlns:ns8=\"http://www.chinatelecom.com.cn/wsdl/ctcc/sms/receive/v2_1/service\"");
			sb2.append(" xmlns:ns4=\"http://www.chinatelecom.com.cn/schema/ctcc/sms/receive/v2_1/local\">");
			sb2.append("<SOAP-ENV:Header>");
			sb2.append("<ns3:NotifySOAPHeader>");
			sb2.append("<spRevId>35100123</spRevId>");
			sb2.append("<spRevpassword>12345</spRevpassword>");
			sb2.append("<spId>35100123</spId>");
			sb2.append("<SAN></SAN>");
			sb2.append("<transactionId></transactionId>");
			sb2.append("<linkId>12345678901234567890</linkId>");
			sb2.append("</ns3:NotifySOAPHeader>");
			sb2.append("</SOAP-ENV:Header>");
			sb2.append("<SOAP-ENV:Body>");
			sb2.append("<ns1:notifySmsReception>");
			sb2.append("<ns1:registrationIdentifier></ns1:registrationIdentifier>");
			sb2.append("<ns1:message>");
			sb2.append("<message>test msg</message>");
			sb2.append("<senderAddress>tel:18918912345</senderAddress>");
			sb2.append("<smsServiceActivationNumber>tel:10659823003</smsServiceActivationNumber>");
			sb2.append("</ns1:message>");
			sb2.append("</ns1:notifySmsReception>");
			sb2.append("</SOAP-ENV:Body>");
			sb2.append("</SOAP-ENV:Envelope>");
			String res = rep.send("http://211.147.7.242:8080/axis/services/SmsNotification", sb2);
			logger.debug("res report==" + res);
			/*
			 * URL u = new URL(
			 * "http://222.76.210.154:8888/report?reportid=12341111111&status=2"
			 * ); URLConnection uc=u.openConnection(); BufferedReader in =new
			 * BufferedReader(new InputStreamReader(uc.getInputStream()));
			 * logger.debug(in.readLine());
			 */
			// m.currentThread(1000);
			// ReportSelf Rself1=new
			// ReportSelf("http://218.104.134.136:10080/mmsReport","1008");
			// new Thread(Rself1).start();
			// Thread.currentThread().sleep(1000*60);
			// ReportSelf Rself2=new
			// ReportSelf("http://211.136.87.225/code/mms_hz/sso/report_1111.php","1111");
			// new Thread(Rself2).start();
			// Thread.currentThread().sleep(1000*60);
			// Report m=new Report();;
			// m.start();
			// Rself1.currentThread(1000*);
			// m.stop();
			/*
			 * Maininfo m1=new Maininfo(); m1.start(); Maininfo m2=new
			 * Maininfo(); m2.start();
			 */
		} catch (Exception e) {
			logger.debug("", e);

		}
		// StringBuffer ss=new StringBuffer("");
		// ss.append("....dd");
		// ss.
		// logger.debug("ss"+ss);
	}

}
