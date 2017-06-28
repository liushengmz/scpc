/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xiangtone.report;

//import com.xiangtone.util.*;
//import com.xiangtone.sql.MysqlDB;
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
public class Report1034 {
	private static Logger logger = Logger.getLogger(Report.class);

	private String sql = "";

	public Report1034(Statement db) {
		// Log.show
		logger.debug("now begin Report1034===摩通");

		checkReport(db);

	}

	private void checkReport(Statement db) {
		sql = "select service_code,exchange_id,messageid,src_phone,dst_phone,exchange_time,success_time,exchange_status  from mms_transaction_detail where exchange_status<>1 and report_status=0 and linkid!='' and league_id='1034' order by exchange_id desc limit 200";
		try {
			ResultSet rs = db.executeQuery(sql);
			logger.debug("sql==" + sql);
			String updatesql = "";
			int i = 0;

			StringBuffer sb = new StringBuffer();
			sb.append("<report>");
			while (rs.next()) {
				i++;
				int exchange_id = rs.getInt("exchange_id");
				String messageid = rs.getString("messageid");
				String src_phone = rs.getString("src_phone");
				String dst_phone = rs.getString("dst_phone");
				String exchange_time = rs.getString("exchange_time");
				String success_time = rs.getString("success_time");
				String exchange_status = rs.getString("exchange_status");
				String service_code = rs.getString("service_code");
				updatesql += " exchange_id=" + exchange_id + " or ";
				sb.append(getXmlStr(messageid, src_phone, dst_phone, exchange_time, success_time, exchange_status,
						service_code));

			}
			sb.append("</report>");
			if (!updatesql.equals("")) {
				try {
					logger.debug(sb.toString());
					String res = send("http://210.22.13.18/dodomusic/get_cshd_mms_mr.jsp", sb);
					logger.debug("1034Report res:" + res);
					if (res.equals("0")) {
						updatesql += "exchange_id=1";
						sql = "update mms_transaction_detail set report_status=1 where " + updatesql;
						db.executeUpdate(sql);
						logger.debug("一共发送  " + i + "  条记录");
					}
				} catch (Exception e) {
					logger.error(sql, e);
				}

			}
		} catch (SQLException e) {
			logger.error(sql, e);
		}

	}

	private String getXmlStr(String messageid, String sender, String to, String ex_time, String su_time, String status,
			String code) {
		String str = "";
		str += "<mms>";
		str += "<type>1</type>";
		str += "<seqid></seqid>";
		str += "<messageid>" + messageid + "</messageid>";
		str += "<send>" + sender + "</send>";
		str += "<to>" + to + "</to>";
		str += "<code>" + code + "</code>";
		str += "<exchange_time>" + ex_time + "</exchange_time>";
		str += "<success_time>" + su_time + "</success_time>";
		str += "<status>" + status + "</status>";
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
		in.close();
		return tt;
	}

	public static void main(String[] args) {
		logger.debug("report start....!");
		/*
		 * try { Report1034 rep=new Report1034(); // rep.start(); } catch
		 * (Exception e) { logger.debug("eee"+e);
		 * 
		 * }
		 */
	}

}
