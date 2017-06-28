/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xiangtone.report;

//import com.xiangtone.util.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.common.util.ConnectionService;
import org.common.util.ThreadPool;

import java.io.*;
//import java.lang.reflect.Method;
import java.net.*;

/**
 * @author Administrator
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ReportAll {
	private static Logger logger = Logger.getLogger(ReportAll.class);
	// Random ra=new Random();
	// int k=ra.nextInt(100);
	private String sql = "";
	// http://211.154.135.192:51414/ppgwp/szcshdcxmot.jsp

	public ReportAll() {
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
		Connection con = ConnectionService.getInstance().getConnectionForLocal();
		sql = "select league_id,reportUrl from cp_info where type=1 and stat=1 and reportUrl!='' order by league_id asc";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery(sql);
			// List<String> l = new ArrayList<String>();
			HashMap cpMap = new HashMap();
			while (rs.next()) {
				String leagueId = rs.getString("league_id");
				String reportUrl = rs.getString("reportUrl");
				cpMap.put(leagueId, reportUrl);
			}
			Set key = cpMap.keySet();
			for (Iterator it = key.iterator(); it.hasNext();) {
				String cpid = (String) it.next();
				String reportUrl = cpMap.get(cpid).toString();
				logger.debug("============report All====== begin");
				logger.debug("cpid===" + cpid);
				logger.debug("reportUrl===" + reportUrl);
				logger.debug("============report All=======end");
				int selectnums = 200;
				if ("1071".equals(cpid))
					selectnums = 200;
				sql = "select exchange_id,messageid,exchange_status,service_code,src_phone,dst_phone,exchange_time,success_time from mms_transaction_detail where exchange_status<>1 and report_status=0 and linkid!='' and league_id='"
						+ cpid + "' order by exchange_id desc limit " + selectnums;

				ResultSet rs2 = ps.executeQuery(sql);

				logger.debug("sql==" + sql);
				String updatesql = "";

				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\"  encoding=\"UTF-8\" ?><report>");
				while (rs2.next()) {
					int exchangeId = rs2.getInt("exchange_id");
					String messageId = rs2.getString("messageid");
					String statusText = rs2.getString("exchange_status");
					// String price=rs2.getString("price");
					String sender = rs2.getString("src_phone");
					String to = rs2.getString("dst_phone");
					String exchangeTime = rs2.getString("exchange_time");
					String successTime = rs2.getString("success_time");
					String serviceCode = rs2.getString("service_code");
					updatesql += " exchange_id=" + exchangeId + " or ";
					sb.append(getXmlStr(messageId, statusText, sender, to, exchangeTime, successTime, serviceCode));
				}
				sb.append("</report>");

				if (!updatesql.equals("")) {
					String res = send(reportUrl, sb);
					logger.debug(cpid + "Report send:" + sb.toString());
					logger.debug(cpid + "Report res:" + res);

					if (res.equals("0")) {
						updatesql += "exchange_id=1";
						sql = "update mms_transaction_detail set report_status=1 where " + updatesql;
						ps.executeUpdate(sql);
					}

				}

			}

		} catch (Exception e) {
			logger.error("", e);
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

		// Log.showLog("now begin Report"+cpid+"===",Log.reportLogPath);

		// http://223.202.18.60/cmsc/interface/xmxt/mms_mr_xmxt.jsp
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

	private String send(String url, StringBuffer sb) {
		SendThread st=new SendThread(url, sb);
		ThreadPool.tpx.execute(st);
		return st.getTt();
	}

	private class SendThread implements Runnable {
		private StringBuffer sb;
		private URL sendUrl;
		private String tt="";

		public String getTt() {
			return tt;
		}

		public SendThread(String url, StringBuffer sb) {
			this.sb = sb;
			try {
				sendUrl = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {

				URLConnection connection = sendUrl.openConnection();

				connection.setRequestProperty("Content-type", "text/xml");
				HttpURLConnection httpConn = (HttpURLConnection) connection;
				byte[] b = sb.toString().getBytes("UTF-8");
				logger.debug("len=====" + String.valueOf(b.length));

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
				// String tt="";

				while ((inputLine = in.readLine()) != null) {
					tt += inputLine;
				}

				logger.debug("tt:" + tt+"	sendUrl:"+sendUrl);
				in.close();
			} catch (Exception e) {
				logger.error(sendUrl.toString(), e);
			}
		}

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
