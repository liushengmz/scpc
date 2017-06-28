/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xiangtone.report;

//import com.xiangtone.util.*;
import java.sql.*;

import org.apache.log4j.Logger;

import java.io.*;
//import java.lang.reflect.Method;
import java.net.*;

//import org.apache.log4j.Logger;
/**
 * @author Administrator
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Report1045 {
	private static Logger logger = Logger.getLogger(Report.class);

	private String sql = "";
	private String leagueid = "1045";

	public Report1045(Statement db) {
		logger.debug("now begin Report1045===五巨");
		checkReport(db);

	}

	public Report1045() {
		logger.debug("now begin Report1045===五巨");
		// checkReport(db);

	}

	private void checkReport(Statement db) {
		sql = "select exchange_id,linkid,src_phone from mms_transaction_detail where exchange_status=0 and report_status=0 and linkid!='' and league_id='"
				+ leagueid + "' order by exchange_id desc limit 200";

		try {
			ResultSet rs = db.executeQuery(sql);

			logger.debug("sql==" + sql);
			String updateSql = "";

			while (rs.next()) {
				int exchangeId = rs.getInt("exchange_id");
				String linkId = rs.getString("linkid");
				String srcPhone = rs.getString("src_phone");

			}

		} catch (SQLException e) {
			logger.error(sql,e);
		}

	}

	private String send(String url, StringBuffer sb) throws Exception {

		// String
		// s1="http://210.22.13.18/dodomusic/get_cshd_mms_mo.jsp?sender=13950000050&to=106581673040&receive_time="+java.net.URLEncoder.encode("2010-01-14
		// 15:00:00")+"&subject="+java.net.URLEncoder.encode("test短信上行测试")+"&linkid=1111111111111&code=104&messageid=123123123123123123";
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// InputStream in=uc.getInputStream();

		InputStreamReader isr = new InputStreamReader(uc.getInputStream());
		BufferedReader in = new BufferedReader(isr);
		String inputLine;
		String tt = "";

		while ((inputLine = in.readLine()) != null) {
			tt += inputLine;
		}
		in.close();
		logger.debug("tt==" + tt);
		return tt;

		/*
		 * URL send_url=new URL(url); URLConnection connection =
		 * send_url.openConnection();
		 * System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		 * System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		 * connection.setRequestProperty("Content-type","text/xml");
		 * HttpURLConnection httpConn = (HttpURLConnection) connection; byte[] b
		 * = sb.toString().getBytes("UTF-8");
		 * logger.debug("len====="+String.valueOf( b.length ));
		 * logger.debug("url===="+url); httpConn.setRequestProperty(
		 * "Content-Length",String.valueOf( b.length ) );
		 * 
		 * httpConn.setRequestMethod( "POST" ); httpConn.setDoOutput(true);
		 * httpConn.setDoInput(true);
		 * 
		 * OutputStream out = httpConn.getOutputStream(); out.write( b );
		 * out.close();
		 * 
		 * InputStreamReader isr = new
		 * InputStreamReader(httpConn.getInputStream()); BufferedReader in = new
		 * BufferedReader(isr); String inputLine; String tt="";
		 * 
		 * while ((inputLine = in.readLine()) != null) { tt+=inputLine; }
		 * in.close();
		 */
	}

	public static void main(String[] args) {
		Report1045 r = new Report1045();
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<?xml version=\"1.0\"  encoding=\"GBK\" ?><receivesms><message><messageid></messageid><sender>13950000050</sender><to>106581673041</to><linkid>15212901915302242429</linkid><code>150101</code><subject>906</subject><receive_time>2010-09-19+09%3A12%3A20</receive_time></message></receivesms>");
		try {
			String res = r.send("http://119.147.26.2/solar-star/xmxtCxDianBoMo.jsp", sb);
			logger.debug("res===" + res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
