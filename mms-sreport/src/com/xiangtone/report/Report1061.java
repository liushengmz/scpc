/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xiangtone.report;

//import com.xiangtone.util.*;
import java.sql.*;
import java.io.*;
//import java.lang.reflect.Method;
import java.net.*;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Report1061 {
	private static Logger logger = Logger.getLogger(Report.class);

	private String sql = "";
	// http://211.154.135.192:51414/ppgwp/szcshdcxmot.jsp
	private String leagueid = "1061";
	private String leagueName = "¶«ÐÅÈÙ";
	private String leagueUrl = "http://118.144.74.46:8080/xtong/mms/chk.aspx";

	public Report1061(Statement db) {
		logger.debug("now begin Report" + leagueid + "===" + leagueName);
		checkReport(db);
		// http://223.202.18.60/cmsc/interface/xmxt/mms_mr_xmxt.jsp
	}

	private void checkReport(Statement db) {
		sql = "select exchange_id,messageid,exchange_status,service_code,src_phone,dst_phone,exchange_time,success_time   from mms_transaction_detail where exchange_status<>1 and report_status=0 and linkid!='' and league_id='"
				+ leagueid + "' order by exchange_id desc limit 200";

		try {
			ResultSet rs = db.executeQuery(sql);

			logger.debug("sql==" + sql);
			String updatesql = "";
			/*
			 * <?xml version=\"1.0\"  encoding=\"GBK\" ?> <report> <mms>
			 * <type>1</type> <seqid></seqid>
			 * <messageid>092116015591000027494</messageid>
			 * <send>13584051176</send> <to>13584051176</to> <code>105</code>
			 * <exchange_time>2004-09-21 15:29:25</exchange_time>
			 * <success_time>2004-09-21 15:44:47</success_time>
			 * <status>0</status> </mms>
			 */
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\"  encoding=\"UTF-8\" ?><report>");
			while (rs.next()) {
				int exchange_id = rs.getInt("exchange_id");
				String messageid = rs.getString("messageid");
				String statustext = rs.getString("exchange_status");
				// String price=rs.getString("price");
				String sender = rs.getString("src_phone");
				String to = rs.getString("dst_phone");
				String exchange_time = rs.getString("exchange_time");
				String success_time = rs.getString("success_time");
				String service_code = rs.getString("service_code");
				updatesql += " exchange_id=" + exchange_id + " or ";
				sb.append(getXmlStr(messageid, statustext, sender, to, exchange_time, success_time, service_code));
			}
			sb.append("</report>");
			if (!updatesql.equals("")) {
				try {
					logger.debug(sb.toString());
					// http://222.35.143.200:8666/hxweb/ChannelCshdSingleMMSMR
					String res = send(leagueUrl, sb);
					// http://119.147.24.103:8080/channel/cshd2
					logger.debug(leagueid + "Report res:" + res);
					if (res.equals("0")) {
						updatesql += "exchange_id=1";
						sql = "update mms_transaction_detail set report_status=1 where " + updatesql;
						db.executeUpdate(sql);
					}
				} catch (Exception e) {
					logger.error(sql, e);
				}

			}
		} catch (SQLException e) {
			logger.error(sql, e);
		}

	}

	private String getXmlStr(String messageid, String statustext, String sender, String to, String exchange_time,
			String success_time, String service_code) {
		/*
		 * <mms> <type>1</type> <seqid></seqid>
		 * <messageid>092116015591000027494</messageid> <send>13584051176</send>
		 * <to>13584051176</to> <code>105</code> <exchange_time>2004-09-21
		 * 15:29:25</exchange_time> <success_time>2004-09-21
		 * 15:44:47</success_time> <status>0</status> </mms>
		 */
		String str = "";
		str += "<mms><type>1</type><seqid></seqid>";
		str += "<messageid>" + messageid + "</messageid>";
		str += "<send>" + sender + "</send>";
		str += "<to>" + to + "</to>";
		str += "<code>" + service_code + "</code>";
		str += "<exchange_time>" + exchange_time + "</exchange_time>";
		str += "<success_time>" + success_time + "</success_time>";
		str += "<status>" + statustext + "</status>";
		str += "</mms>";
		return str;
	}

	private String send(String url, StringBuffer sb) throws Exception {

		URL send_url = new URL(url);
		URLConnection connection = send_url.openConnection();
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		connection.setRequestProperty("Content-type", "text/xml");
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		byte[] b = sb.toString().getBytes("UTF-8");
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
		}

		logger.debug("tt===" + tt);
		in.close();
		return tt;
	}

	public static void main(String[] args) {
		/*
		 * logger.debug("report start....!"); try { Report1039 rep=new
		 * Report1039(); rep.start(); } catch (Exception e) {
		 * logger.debug("eee"+e);
		 * 
		 * }
		 */
	}

}
