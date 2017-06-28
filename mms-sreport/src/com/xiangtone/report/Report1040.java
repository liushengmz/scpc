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

import org.apache.log4j.Logger;

import java.io.*;
//import java.lang.reflect.Method;
import java.net.*;

/**
 * @author Administrator
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Report1040 {
	private static Logger logger = Logger.getLogger(Report.class);

	private String sql = "";

	public Report1040(Statement db) {
		logger.debug("Report 1040===øÏ¿÷∑Á");
		checkReport(db);
	}

	private void checkReport(Statement db) {
		sql = "select exchange_id,linkid,exchange_status,price,msgid from mms_transaction_detail where exchange_status<>1 and report_status=0 and linkid!='' and league_id='1040' order by exchange_id desc limit 200";
		try {
			ResultSet rs = db.executeQuery(sql);
			logger.debug("sql==" + sql);
			String updatesql = "";

			StringBuffer sb = new StringBuffer();
			sb.append(
					"<?xml version=\"1.0\"  encoding=\"UTF-8\" ?><pwd_message_list version=\"3.0\"><message_type>MMS_PT</message_type><data>");
			while (rs.next()) {
				int exchange_id = rs.getInt("exchange_id");
				String linkid = rs.getString("linkid");
				String statustext = rs.getString("exchange_status");
				String price = rs.getString("price");
				String msgid = rs.getString("msgid");
				updatesql += " exchange_id=" + exchange_id + " or ";
				sb.append(getXmlStr(linkid, statustext, price, msgid));
			}
			sb.append("</data></pwd_message_list>");
			// Sys
			if (!updatesql.equals("")) {
				try {
					logger.debug(sb.toString());
					String res = send("http://smsgw.kkfun.com/SMGatewayServlet", sb);
					logger.debug("1040Report res:" + res);
					if (res.equals("0")) {
						updatesql += "exchange_id=1";
						sql = "update mms_transaction_detail set report_status=1 where " + updatesql;
						db.executeUpdate(sql);
					}
				} catch (Exception e) {
					logger.error(sql,e);
				}

			}
		} catch (SQLException e) {
			logger.error(sql,e);
		}

	}

	private String getXmlStr(String linkid, String statustext, String price, String msgid) {
		String str = "";
		str += "<message><msgid>";
		str += msgid;
		str += "</msgid><feecode>";
		str += price;
		str += "</feecode>";
		str += "<reportstat>";
		str += statustext;
		str += "</reportstat>";
		str += "</message>";
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
		/*
		 * logger.debug("report start....!"); try { Report1040 rep=new
		 * Report1040(); rep.start(); } catch (Exception e) {
		 * logger.debug("eee"+e);
		 * 
		 * }
		 */
	}

}
