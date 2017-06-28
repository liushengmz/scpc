package com.database.LightModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class tbl_api_orderItem extends tbl_api_order_ori {

	final static String pfxTable = "tbl_api_order";
	public final static String pfxSchame = "daily_log";

	String _tabName;

	public tbl_api_orderItem() {
		this.SetTabelDate(new Date());
	}

	@Override
	public String TableName() {
		return _tabName;
	}

	public void SetTabelDate(Date date) {
		_tabName = String.format("%s_%s", pfxTable, new SimpleDateFormat("YYYYMM").format(date));
	}

	@Override
	public String Schema() {
		return pfxSchame;
	}
}
