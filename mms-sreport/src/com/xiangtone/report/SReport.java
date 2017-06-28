package com.xiangtone.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.common.util.ConnectionService;
import org.common.util.ThreadPool;

public class SReport {
	private static Logger logger = Logger.getLogger(SReport.class);

	public SReport() {
		Connection conn = null;
		PreparedStatement ps = null;
		while (true) {
			try {
				
				conn = ConnectionService.getInstance().getConnectionForLocal();
				logger.debug("the conn is:" + conn);
				ps = conn.prepareStatement("");
				long startTime = System.currentTimeMillis();

				logger.debug("System.currentTimeMillis():" + System.currentTimeMillis());
				// MysqlDB db=new MysqlDB("league");
				try {
					ReportAll rAll = new ReportAll();
					// ReportAllCopy rAll=new ReportAllCopy();
				} catch (Exception e) {
					logger.error("now exception ReportAll", e);
				}
				// try {
				// Report1073 r1073=new Report1073(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1073===X-深口袋",e);
				// }
				// try {
				// Report1072 r1072=new Report1072(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1072===X-智科无线",e);
				// }
				// try {
				// Report1071 r1071=new Report1071(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1071===X-天晨时代",e);
				// }
				// try {
				// Report1069 r1069=new Report1069(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1069===X-雪鲤鱼",e);
				// }
				// try {
				// Report1067 r1067=new Report1067(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1067===酷耘软件",e);
				// }
				// try {
				// Report1066 r1066=new Report1066(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1066===视讯中国",e);
				// }
				// try {
				// Report1065 r1065=new Report1065(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1056===信达艾瑞",e);
				// }
				// try {
				// Report1064 r1064=new Report1064(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1064===茂碧信息",e);
				// }
				// try {
				// Report1063 r1063=new Report1063(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1063===X-卓易恺达",e);
				// }
				// try {
				// Report1062 r1062=new Report1062(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1062===卓意万联",e);
				// }
				// try {
				// Report1061 r1061=new Report1061(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1061===东信荣",e);
				// }
				// try {
				// Report1060 r1060=new Report1060(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1060===掌中浩阅",e);
				// }
				// try {
				// Report1059 r1059=new Report1059(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1059===绩鼎网络",e);
				// }
				// try {
				// Report1057 r1057=new Report1057(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1057===X-上海品酷",e);
				// }
				try {
					Report1043 r1043 = new Report1043(ps);
				} catch (Exception e) {
					logger.error("now exception Report1043===X-掌游时代", e);
				}
				// int k=0;
				// try {
				// Report1056 r1056=new Report1056(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1056===图龙信息",e);
				// }
				// try {
				// Report1054 r1054=new Report1054(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1054===真石信息",e);
				// }

				// try {
				// Report1051 r1051=new Report1051(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1051===龙讯锋达",e);
				// }
				//
				// try {
				// Report1052 r1052=new Report1052(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1052===北京智通联合",e);
				// }
				// try {
				// Report1047 r1047=new Report1047(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1047===X-瓦尔雷思",e);
				// }
				// try {
				// Report1048 r1048=new Report1048(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1048===盛博特",e);
				// }
				// try {
				// Report1049 r1049=new Report1049(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1049===X-华夏卓杰",e);
				// }

				try {
					Report1045 r1045 = new Report1045(ps);
				} catch (Exception e) {
					logger.error("now exception Report1045===五巨", e);
				}

				// logger.debug("now begin 1027===深圳梦龙");
				// Report1027 r1027=new Report1027(db);
				//
				/*
				 * logger.debug("now begin 1028===普威德"); Thread.sleep(10000);
				 * Report1028 r1028=new Report1028(db);
				 */
				// logger.debug("now begin 1034===摩通科技");
				// Thread.sleep(10000);
				// try {
				// Report1034 r1034=new Report1034(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1034===摩通科技",e);
				// }

				// logger.debug("now begin 1035===上海通势");
				// Thread.sleep(10000);
				// try {
				// Report1035 r1035=new Report1035(db);
				// }catch (Exception e) {
				// logger.debug("now exception Report1035===上海通势",e);
				// }

				// logger.debug("now begin 1036===斯美通");
				// Thread.sleep(10000);
				// try {
				// Report1036 r1036=new Report1036(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1036===斯美通",e);
				// }
				//
				Thread.sleep(10000);
				try {
					Report1039 r1039 = new Report1039(ps);
				} catch (Exception e) {
					logger.error("now exception Report1039===新极地", e);
				}
				Thread.sleep(10000);
				try {
					Report1040 r1040 = new Report1040(ps);
				} catch (Exception e) {
					logger.error("now exception Report1040===快乐风", e);
				}
				// logger.debug("now begin 1041===斯美通D");
				// Thread.sleep(10000);
				// try {
				// Report1041 r1041=new Report1041(db);
				// }catch (Exception e) {
				// logger.error("now exception Report1041===斯美通D",e);
				// }
				/*
				 * try { Report1042 r1042=new Report1042(db); }catch (Exception
				 * e) { logger.error("now exception Report1042===X-深圳傲光",e); }
				 * // db.close(); Thread.sleep(10000);
				 */

				// db.close();
				Thread.sleep(10000);
				long endTime = System.currentTimeMillis();

				long bTime = endTime - startTime;
				if (bTime < 60 * 5 * 1000) {
					logger.debug("sleep(endTime - startTime):" + bTime);
					Thread.sleep(bTime);
				}
				/*
				 * Thread tr1027=new Thread(r1027); tr1027.start();
				 * 
				 * 
				 * while (!tr1027.isAlive()&&k<100) { Thread.sleep(100); k++;
				 * logger.debug("kk-=="+k); }
				 * logger.debug("Thread.activeCount()="+Thread.activeCount());
				 * if (k>=100) { tr1027.stop(); }
				 * 
				 * k=0;
				 * 
				 * logger.debug("now Sleep 10s"); Thread.sleep(10000);
				 * logger.debug("now begin 1040===快乐风"); Thread.sleep(3000);
				 * 
				 * Report1040 r1040=new Report1040(); Thread tr1040=new
				 * Thread(r1040); tr1040.start();
				 * 
				 * while (!tr1040.isAlive()&&k<100) { Thread.sleep(100); k++;
				 * logger.debug("kk-=="+k); }
				 * logger.debug("Thread.activeCount()="+Thread.activeCount());
				 * if (k>=100) { tr1040.stop(); } k=0;
				 * 
				 * //new Thread(r1040).start(); logger.debug("now Sleep 10s");
				 * Thread.sleep(10000);
				 * 
				 * 
				 * logger.debug("now begin 1028===普威德"); Thread.sleep(3000);
				 * Report1028 r1028=new Report1028(); Thread tr1028=new
				 * Thread(r1028); tr1028.start(); while
				 * (!tr1028.isAlive()&&k<100) { Thread.sleep(100); k++;
				 * logger.debug("kk-=="+k); }
				 * logger.debug("Thread.activeCount()="+Thread.activeCount());
				 * if (k>=100) { tr1028.stop(); } k=0; //new
				 * Thread(r1028).start(); logger.debug("now Sleep 10s");
				 * Thread.sleep(10000);
				 * 
				 * logger.debug("now begin 1034===摩通科技"); Thread.sleep(3000);
				 * Report1034 r1034=new Report1034(); Thread tr1034=new
				 * Thread(r1034); tr1034.start(); while
				 * (!tr1034.isAlive()&&k<100) { Thread.sleep(100); k++;
				 * logger.debug("kk-=="+k); }
				 * logger.debug("Thread.activeCount()="+Thread.activeCount());
				 * if (k>=100) { tr1034.stop(); } k=0; // new
				 * Thread(r1034).start(); logger.debug("now Sleep 10s");
				 * Thread.sleep(10000);
				 * 
				 * 
				 * logger.debug("now begin 1035===上海通势"); Thread.sleep(3000);
				 * Report1035 r1035=new Report1035(); Thread tr1035=new
				 * Thread(r1035); tr1035.start(); while
				 * (!tr1035.isAlive()&&k<100) { Thread.sleep(100); k++;
				 * logger.debug("kk-=="+k); }
				 * logger.debug("Thread.activeCount()="+Thread.activeCount());
				 * if (k>=100) { tr1035.stop(); } k=0; // new
				 * Thread(r1034).start(); logger.debug("now Sleep 10s");
				 * Thread.sleep(10000);
				 * 
				 * 
				 * logger.debug("now begin 1039===新极地"); Thread.sleep(3000);
				 * Report1039 r1039=new Report1039(); Thread tr1039=new
				 * Thread(r1039); tr1039.start(); while
				 * (!tr1039.isAlive()&&k<100) { Thread.sleep(100); k++;
				 * logger.debug("kk-=="+k); }
				 * logger.debug("Thread.activeCount()="+Thread.activeCount());
				 * if (k>=100) { tr1039.stop(); } k=0; // new
				 * Thread(r1034).start(); logger.debug("now Sleep 10s");
				 * Thread.sleep(10000);
				 */

			} catch (Exception e) {
				logger.error("", e);
			} finally {

				try {
					if (ps != null)
						ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public static void main(String args[]) {
		new SReport();
	}
}
