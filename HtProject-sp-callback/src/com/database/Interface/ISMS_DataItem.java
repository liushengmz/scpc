package com.database.Interface;

import java.util.Date;

public interface ISMS_DataItem {

	String get_imei();

	void set_imei(String value);

	String get_imsi();

	void set_imsi(String value);

	int get_trone_id();

	void set_trone_id(int value);

	String get_mobile();

	void set_mobile(String value);

	/** port */
	String get_ori_trone();

	/** port */
	void set_ori_trone(String value);

	/** msg */
	String get_ori_order();

	/** msg */
	void set_ori_order(String value);

	String get_service_code();

	void set_service_code(String value);

	String get_cp_param();

	void set_cp_param(String value);

	String get_linkid();

	void set_linkid(String value);

	/** 接收日期,MO对应mo_date,MR对应mr_date */
	Date get_recdate();

	/** 接收日期,MO对应mo_date,MR对应mr_date */
	void set_recdate(Date value);

	int get_price();

	void set_price(int value);

	String get_ip();

	void set_ip(String value);

	String get_mcc();

	void set_mcc(String value);

	/** 同步URLID */
	int get_sp_api_url_id();

	/** 同步URLID */
	void set_sp_api_url_id(int value);

	int get_sp_id();

	void set_sp_id(int value);

	int get_trone_type();

	void set_trone_type(int value);

}
