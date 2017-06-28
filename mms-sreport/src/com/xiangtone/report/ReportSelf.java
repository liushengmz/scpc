/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xiangtone.report;

import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.common.util.ConnectionService;

import java.io.*;
import java.net.*;

/**
 * @author Administrator
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ReportSelf extends Thread {
	private String sql = "";
	// private StringBuffer ss=new StringBuffer();
	private String strsql = "";
	private String strsql2 = "";
	private String url = "";
	private String league_id = "";
	private boolean boolean_sql = false;
	private String true_sql = "";
	private String false_sql = "";
	private StringBuffer sb = new StringBuffer();
	private int times = 1;
	private Connection con = null;
	private PreparedStatement ps = null;
	private static Logger logger = Logger.getLogger(ReportSelf.class);

	/*
	 * 
	 * if bsql is true and return first string is '0' then Tsql
	 *
	 * if bsql is true and return first string isnot '0' then Fsql
	 * 
	 * if bsql is false then donot use Tsql and Fsql;
	 *
	 */
	public ReportSelf(String iurl, String ileague_id, StringBuffer isb, boolean bsql, String Tsql, String Fsql) {

		url = iurl;
		league_id = ileague_id;
		sb = isb;
		boolean_sql = bsql;
		true_sql = Tsql;
		false_sql = Fsql;
		// dealy();
		logger.debug("====boolean_sql===" + boolean_sql);
		logger.debug("====false_sql===" + false_sql);
		logger.debug("====true_sql===" + true_sql);
		logger.debug("====league_id===" + league_id);
	}

	public void run() {
		dealy();
	}

	public String dealy() {

		String res = "0";
		logger.debug("times===" + times);
		if (times < 10) {
			try {
				res = send(url, sb);
				if (res.substring(0, 1).equals("0")) {

					if (boolean_sql) {
						con = ConnectionService.getInstance().getConnectionForLocal();
						ps = con.prepareStatement(true_sql);
						ps.executeUpdate(true_sql);
						logger.debug("true_sql==" + true_sql);
					}
				} else {
					logger.debug(league_id + "==res==" + res);
					times++;
					sleep(2000);
					dealy();
				}

			} catch (Exception e) {
				times++;
				try {
					sleep(2000);
				} catch (InterruptedException e1) {

				}
				dealy();
				logger.error(true_sql, e);
			} finally {

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
			return res;
		} else {
			if (boolean_sql) {
				try {
					con = ConnectionService.getInstance().getConnectionForLocal();
					ps = con.prepareStatement(false_sql);
					ps.executeUpdate(false_sql);
					logger.debug("false_sql==" + false_sql);
				} catch (SQLException e) {
					logger.error(false_sql, e);
				} finally {

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
			logger.debug(league_id + "=========" + sb.toString());
			return "1";
		}
	}

	/*
	 * public void run () { while (true) { db=new MysqlDB("league");
	 * againSendFaile(); againSendSucc(); checkReport();
	 * 
	 * try { db.close(); } catch (Exception e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } try { logger.debug("sleep 5 min");
	 * logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "=========",path); Thread.sleep(5*60*1000);
	 * 
	 * } catch (InterruptedException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } } }
	 */
	// private void
	/**
	 * 
	 */
	/*
	 * private void checkReport() { sql=
	 * "select exchange_id,src_phone,dst_phone,service_code,exchange_time,success_time,exchange_status,messageid,league_id from mms_transaction_detail where exchange_status<>1 and report_status=0 and league_id="
	 * +league_id+"  order by exchange_id desc limit 50";
	 * logger.debug("sql==="+sql); ResultSet rs=db.execQuery(sql); try { while
	 * (rs.next()) { logger.debug("checkReport=="+league_id); StringBuffer
	 * sb=getCheckReportXml(rs.getString("messageid"),rs.getString("src_phone"),
	 * rs.getString("dst_phone"),rs.getString("service_code"),rs.getString(
	 * "exchange_time"),rs.getString("success_time"),rs.getString(
	 * "exchange_status")); try { String res=send(url,sb); } catch (Exception
	 * e2) { logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "===exception checkReport======"+e2,path); // TODO Auto-generated catch
	 * block e2.printStackTrace();
	 * 
	 * try { logger.debug("sleep 2min by checkReport,exception");
	 * Thread.sleep(60*1000*2); } catch (InterruptedException e3) { // TODO
	 * Auto-generated catch block e3.printStackTrace(); } continue; } sql=
	 * "update mms_transaction_detail set report_status=1 where exchange_id="
	 * +rs.getInt("exchange_id"); db.execUpdate(sql);
	 * logger.debug("legueid="+rs.getString("league_id")+"    sql=="+sql);
	 * logger.debug("succ change a send message"); try { sleep(200); } catch
	 * (InterruptedException e1) { // TODO Auto-generated catch block //
	 * e1.printStackTrace(); } } } catch (SQLException e) { // TODO
	 * Auto-generated catch block db=new MysqlDB("league");
	 * logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "=====exception ===="+e,path); // logger.debug(
	 * "exception by checkreprot  "+e); // e.printStackTrace(); } } private
	 * StringBuffer getCheckReportXml(String messageid,String src_phone,String
	 * dst_phone,String service_code,String exchange_time,String
	 * success_time,String exchange_status) { StringBuffer ss=new
	 * StringBuffer();
	 * 
	 * ss.append("<report><mms>\r"); ss.append("<type>1</type>\r");
	 * ss.append("<seqid></seqid>\r");
	 * ss.append("<messageid>"+messageid+"</messageid>\r");
	 * ss.append("<send>"+src_phone+"</send>\r");
	 * ss.append("<to>"+dst_phone+"</to>\r");
	 * ss.append("<code>"+service_code+"</code>\r");
	 * ss.append("<exchange_time>"+exchange_time+"</exchange_time>\r");
	 * ss.append("<success_time>"+success_time+"</success_time>\r");
	 * ss.append("<status>"+exchange_status+"</status>\r");
	 * ss.append("</mms>\r</report>"); return ss; }
	 */
	/**
	 * 
	 */
	/*
	 * private void againSendSucc() { sql=
	 * "select id,seqid,src_phone,dst_phone,faile_time,service_code,messageid,league_id from again_send where  status=1 and league_status=0 and send_times<11 and league_id='"
	 * +league_id+"'"; ResultSet rs=db.execQuery(sql); try { while (rs.next()) {
	 * logger.debug("againSendSucc"+league_id); StringBuffer
	 * sb=getAgainSendSuccXml(rs.getString("seqid"),rs.getString("messageid"),rs
	 * .getString("src_phone"),rs.getString("dst_phone"),rs.getString(
	 * "service_code"),rs.getString("faile_time")); String res=""; try { res =
	 * send(url,sb); } catch (Exception e2) {
	 * logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "===againSendSucc======"+e2,path); // TODO Auto-generated catch block
	 * e2.printStackTrace(); try { logger.debug(
	 * "sleep 2min by againsendsucc,exception"); Thread.sleep(60*1000*2); }
	 * catch (InterruptedException e3) { // TODO Auto-generated catch block
	 * e3.printStackTrace(); } continue; } if (res.substring(0,1).equals("0")) {
	 * sql="delete from again_send where id="+rs.getInt("id"); logger.debug(
	 * "delete a message by send again succ"); db.execUpdate(sql); } else { sql=
	 * "update again_send set send_times=send_times+1 where id="
	 * +rs.getInt("id"); db.execUpdate(sql); logger.debug(
	 * "send a message by again succ    fail....send_time++"); }
	 * 
	 * try { Thread.sleep(200); } catch (InterruptedException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); } } } catch
	 * (SQLException e) { db=new MysqlDB("league"); // TODO Auto-generated catch
	 * block logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "=====againSendSucc mysql===="+e,path); // logger.debug(
	 * "exception e  by send agagin succ"+e); // e.printStackTrace(); }
	 * 
	 * }
	 */
	/**
	 * 
	 */

	/*
	 * private void againSendFaile() { sql=
	 * "select id,seqid,src_phone,dst_phone,faile_time,service_code,league_id from again_send where league_status=0 and status=0 and send_times>=10  and league_id='"
	 * +league_id+"' order by id asc "; ResultSet rs=db.execQuery(sql); try {
	 * while (rs.next()) { logger.debug("againSendFaile"+league_id);
	 * StringBuffer
	 * sb=getAgainSendFailXml(rs.getString("seqid"),rs.getString("src_phone"),rs
	 * .getString("dst_phone"),rs.getString("service_code"),rs.getString(
	 * "faile_time")); String res=""; try { res = send(url,sb); } catch
	 * (Exception e2) {
	 * logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "===againSendFaile======"+e2,path); // TODO Auto-generated catch block
	 * e2.printStackTrace(); try { logger.debug(
	 * "sleep 2min by againSendFaile,exception"); Thread.sleep(60*1000*2); }
	 * catch (InterruptedException e3) { // TODO Auto-generated catch block
	 * e3.printStackTrace(); } continue; } if (res.substring(0,1).equals("0")) {
	 * sql="delete from again_send where id="+rs.getInt("id"); logger.debug(
	 * "delete a message by send again faile"); db.execUpdate(sql); } else {
	 * sql= "update again_send set send_times=send_times+1 where id="
	 * +rs.getInt("id"); db.execUpdate(sql); }
	 * 
	 * try { sleep(200); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * logger.debug("report again..."); } catch (SQLException e1) { db=new
	 * MysqlDB("league");
	 * logger.debug(Log.getTime("yyyy-MM-dd'T'HH:mm:ss")+"===="+league_id+
	 * "====againSendFaile mysql====="+e1,path); logger.debug(
	 * "e by check faile send .."+e1); // e1.printStackTrace(); }
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
	/*
	 * private StringBuffer getAgainSendFailXml(String seqid, String src_phone,
	 * String dst_phone, String service_code, String faile_time) { StringBuffer
	 * ss=new StringBuffer(); ss.append("<report>\r"); ss.append("<mms>\r");
	 * ss.append("<type>3</type>\r"); ss.append("<seqid>"+seqid+"</seqid>\r");
	 * ss.append("<messageid></messageid>\r"); ss.append("<send>");
	 * ss.append(src_phone); ss.append("</send>\r"); ss.append("<to>");
	 * ss.append(dst_phone); ss.append("</to>\r"); ss.append("<code>");
	 * ss.append(service_code); ss.append("</code>\r");
	 * ss.append("<exchange_time>"); ss.append(faile_time);
	 * ss.append("</exchange_time>\r"); ss.append(
	 * "<success_time>0000-00-00 00:00:00</success_time>\r");
	 * ss.append("<status></status>\r"); ss.append("</mms>\r");
	 * ss.append("</report>"); return ss; }
	 * 
	 * private StringBuffer getAgainSendSuccXml(String seqid,String
	 * messageid,String src_phone,String dst_phone,String service_code,String
	 * faile_time) { StringBuffer ss=new StringBuffer();
	 * ss.append("<report>\r"); ss.append("<mms>\r");
	 * ss.append("<type>2</type>\r"); ss.append("<seqid>"+seqid+"</seqid>\r");
	 * ss.append("<messageid>"+messageid+"</messageid>\r");
	 * ss.append("<send>"+src_phone+"</send>\r");
	 * ss.append("<to>"+dst_phone+"</to>\r");
	 * ss.append("<code>"+service_code+"</code>");
	 * ss.append("<exchange_time>"+faile_time+"</exchange_time>\r"); ss.append(
	 * "<success_time>0000-00-00 00:00:00</success_time>\r");
	 * ss.append("<status>1</status>\r"); ss.append("</mms>\r");
	 * ss.append("</report>"); return ss; }
	 * 
	 * 
	 */

	private String send(String url, StringBuffer sb) throws Exception {

		URL send_url = new URL(url);
		URLConnection connection = send_url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		byte[] b = sb.toString().getBytes();
		// logger.debug("len====="+String.valueOf( b.length ));
		// logger.debug("url===="+url);
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
			// ReportSelf m=new ReportSelf("http://localhost","99999");;
			// m.start();

		} catch (Exception e) {
			logger.debug("eee" + e);

		}
		// StringBuffer ss=new StringBuffer("");
		// ss.append("....dd");
		// ss.
		// logger.debug("ss"+ss);
	}

}
