package com.database.Interface;

import java.math.BigDecimal;
import java.util.Date;

public interface IHold_DataItem {
	/** 扣量比例 */
	int get_hold_percent();

	/** 单日最大同步金额 */
	BigDecimal get_hold_amount();

	/** 单日已经同步金额 */
	BigDecimal get_amount();

	/** 单日已经同步金额 */
	void set_amount(BigDecimal value);

	/** 扣量周期，已经处理多少条数据 */
	int get_hold_CycCount();

	/** 扣量周期，已经处理多少条数据 */
	void set_hold_CycCount(int value);

	/** 扣量周期，已经扣除量 */
	int get_hold_CycProc();

	/** 扣量周期，已经扣除量 */
	void set_hold_CycProc(int value);

	/** 已经推送条数 */
	int get_push_count();

	/** 已经推送条数 */
	void set_push_count(int value);

	/** 起扣条数 */
	int get_hold_start();

	Date get_lastDate();

	void set_lastDate(Date value);
}
