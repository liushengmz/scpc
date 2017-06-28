package com.xtone.test;

import java.math.BigDecimal;
import java.util.HashMap;

import com.database.Logical.MysqlDatabase;
import com.dianfu.logical.SPApiConfig;
import com.dianfu.logical.SPCallbackProces;
import com.dianfu.logical.model.SPDataModel;
import com.shotgun.Tools.ConfigManager;

public class Test {

	public static void main11(String[] args) {
		String pfxUrl = "http://baidu.com/";
		HashMap<String, String> ptrs = new HashMap<String, String>();
		ptrs.put("mobile", "13800138000");
		ptrs.put("servicecode", "111");
		ptrs.put("linkid", "testLinkid");
		ptrs.put("msg", "M5");
		// ptrs.Add("status", PushObject.GetValue(Logical.EPushField.Status));
		ptrs.put("port", "1008611");

		ptrs.put("price", "100");
		ptrs.put("cpparam", "透传传传传");
		ptrs.put("provinceId", "1");
		// new Thread(new HttpSendData(pfxUrl, ptrs)).start();

		MysqlDatabase mysql = new MysqlDatabase();
		try {
			mysql.executeScalar("select abc from def");
		} finally {
			mysql.close();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done！");
	}

	public static void main(String[] args) {

		// BasicDataSource bds= new BasicDataSource();

		// HttpSendData at= new HttpSendData();
		// at.setUrl("http://192.168.1.21:83");
		// at.run();
		// at.setUrl("http://192.168.1.21:83/aaac");
		// at.run();
		MysqlDatabase sql= new MysqlDatabase();
	System.out.println(sql.executeScalar("select * from tbl_cp"));

		System.out.println(ConfigManager.getConfigData("demo-forwarding.url"));

	}

	public static void main3(String[] args) {

		SPDataModel m = new SPDataModel(null);
		MysqlDatabase dbase = new MysqlDatabase();
		SPCallbackProces sproc = new SPCallbackProces();
		try {
			sproc.SetDBase(dbase);
			if (!sproc.InitSpCfg(null)) {
				System.out.println("config not found");
				return;
			}
			SPApiConfig cfg = sproc.GetApiConfig();
			cfg.switchToMrMode();
			m.put("linkid", "test124");// + Integer.toString(new
										// Random().nextInt()));
			m.put("mobile", "13800138000");
			m.put("service_code", "1230");
			m.put("ori_trone", null);
			m.put("ori_order", "M5");
			m.put("status", "ok");
			m.setPrice(cfg.convertPrice("200"));
			sproc.StartProcess(m);
			System.out.println(sproc.getErrCode().toString());

		} finally {
			dbase.close();
		}

	}

	public static void main2(String[] args) {
		BigDecimal d = new BigDecimal(123450.999999999);
		double v = d.doubleValue();
		System.out.println((int) v);
	}
}
