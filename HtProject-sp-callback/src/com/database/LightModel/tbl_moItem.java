package com.database.LightModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.database.Interface.ISMS_DataItem;

public class tbl_moItem extends tbl_moItem_ori implements ISMS_DataItem {
	final static String pfxTable = "tbl_mo";
	public final static String pfxSchame = "daily_log";

	String _tabName;

	public tbl_moItem() {
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

	@Override
	public Date get_recdate() {
		return this.get_mo_date();
	}

	@Override
	public void set_recdate(Date value) {
		this.set_mo_date(value);
	}

}
