package com.database.LightModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class tbl_cp_mrItem extends tbl_cp_mr_ori {
	final static String pfxTable = "tbl_cp_mr";
	public final static String pfxSchame = "daily_log";

	String _tabName;

	public tbl_cp_mrItem() {
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
