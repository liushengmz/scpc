package com.dianfu.logical;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.database.Dao.tbl_cp_push_urlDao;
import com.database.Dao.tbl_mobile_white_listDao;
import com.database.Dao.tbl_troneDao;
import com.database.Interface.IDatabase;
import com.database.Interface.IHold_DataItem;
import com.database.LightModel.tbl_cp_mrItem;
import com.database.LightModel.tbl_cp_push_urlItem;
import com.database.LightModel.tbl_troneItem;
import com.database.LightModel.tbl_trone_orderItem;
import com.database.Logical.DBRuntimeException;
import com.dianfu.Interface.ICPPushModel;
import com.dianfu.Interface.ICPPushModel.EPushField;
import com.shotgun.Tools.Funcs;

public abstract class BasePusher {
	static Logger logger = Logger.getLogger("push");
	private tbl_trone_orderItem _config;
	private IDatabase _dbase;
	private tbl_cp_push_urlItem _cp_push_url;
	private tbl_troneItem _trone;
	private ICPPushModel _push;
	private String _err;

	/** 加载渠道指令集 */
	public abstract boolean LoadCPAPI(int troneId);

	public boolean doPush() {

		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date today = currentDate.getTime();

		IHold_DataItem holdCfg;
		if (_config.get_hold_is_Custom())
			holdCfg = _config;
		else
			holdCfg = _cp_push_url;

		synchronized (holdCfg) {// 以扣量条件，锁定处理,不同条，可以并行处理
			if (Math.abs(holdCfg.get_lastDate().getTime() - today.getTime()) > 10000) {// 重置昨日数据，为今日新数据
				holdCfg.set_lastDate(today);
				holdCfg.set_amount(new BigDecimal(0));
				holdCfg.set_push_count(0);
			}

			if (isCycHidde()) {// 扣量处理
				try {
					_push.SetHidden(_dbase, _config);
					_dbase.saveData(_config);
					_dbase.saveData(_cp_push_url);
				} catch (DBRuntimeException ex) {
					WriteLog(-1, String.format("扣量数据保存失败{0}", ex.getMessage()));
					return SetError(ex.getMessage());
				}
				WriteLog(-2, "扣量");
				return SetSuccess();
			}
			holdCfg.set_push_count(holdCfg.get_push_count() + 1);
			holdCfg.set_amount(holdCfg.get_amount().add(_trone.get_price()));
		}
		try {
			tbl_cp_mrItem cpmr = _push.SetPushed(_dbase, _config);

			cpmr.set_syn_status(_cp_push_url.get_is_realtime() ? 1 : 0);// 非实时同步时，CPMR
																		// 标记为0：未同步

			_dbase.saveData(cpmr);
			_dbase.saveData(_config);
			_dbase.saveData(_cp_push_url);
		} catch (DBRuntimeException ex) {
			WriteLog(-1, ex.getMessage());
			return SetError(ex.getMessage());
		}

		if (_cp_push_url.get_is_realtime())
			SendQuery();
		return SetSuccess();
	}

	public IDatabase getDBase() {
		return _dbase;
	}

	public void setDBase(IDatabase value) {
		_dbase = value;
	}

	/**
	 * 设置渠道通道信息,同步信息,SP通道信息
	 * 
	 * @param m
	 *            匹配的CP指令
	 */
	protected void SetConfig(tbl_trone_orderItem m) {
		_config = m;

		_cp_push_url = tbl_cp_push_urlDao.queryById(_dbase, m.get_push_url_id());

		// new tbl_cp_push_urlItem();
		// String sql = "select * from tbl_cp_push_url where id=" +
		// m.get_push_url_id();
		// _dbase.sqlToModel(_cp_push_url, sql);

		if (_trone != null && _trone.get_id() == m.get_trone_id())
			return;

		_trone = tbl_troneDao.queryById(_dbase, m.get_trone_id());

	}

	public void setTrone(tbl_troneItem value) {
		_trone = value;
	}

	public tbl_troneItem getTrone() {
		return _trone;
	}

	/** 推送数据 */
	public void setPushObject(ICPPushModel mr) {
		_push = mr;
	}

	/** 推送数据 */
	public ICPPushModel getPushObject() {
		return _push;
	}

	/** 给数据给CP */
	protected void SendQuery() {

		HashMap<String, String> ptrs = new HashMap<String, String>();
		ptrs.put("mobile", _push.GetValue(EPushField.Mobile));
		ptrs.put("servicecode", _push.GetValue(EPushField.ServiceCode));
		ptrs.put("linkid", _push.GetValue(EPushField.LinkID));
		ptrs.put("msg", _push.GetValue(EPushField.Msg));
		// ptrs.Add("status", PushObject.GetValue(Logical.EPushField.Status));
		ptrs.put("port", _push.GetValue(EPushField.port));

		ptrs.put("price", Integer.toString((int) (_trone.get_price().floatValue() * 100)));
		ptrs.put("cpparam", _push.GetValue(EPushField.cpParam));
		ptrs.put("provinceId", _push.GetValue(EPushField.province));

		asyncSendQuery(_cp_push_url.get_url(), ptrs);
	}

	protected void asyncSendQuery(String pfxUrl, HashMap<String, String> data) {
		new Thread(new HttpSendData(pfxUrl, data)).start();
	}

	/** 白名单检查 */
	protected boolean isWhite() {
		String mobile = _push.GetValue(EPushField.Mobile);
		return tbl_mobile_white_listDao.isWhite(_dbase, mobile);
		// if (Funcs.isNullOrEmpty(mobile))
		// return false;
		// String sql = "select id from tbl_mobile_white_list where mobile='" +
		// _dbase.sqlEncode(mobile) + "'";
		// return _dbase.executeScalar(sql) != null;
	}

	/** 此次操作是否标记为扣量信息 */
	protected boolean isCycHidde() {
		if (_cp_push_url.get_cp_id() == 34)
			return true;// 未知CP的，直接隐藏
		if (!_cp_push_url.get_is_realtime())
			return false;// 非实时同步，不进行扣量操作

		IHold_DataItem holdCfg = null;
		if (_config.get_hold_is_Custom())
			holdCfg = _config;
		else
			holdCfg = _cp_push_url;

		if (holdCfg.get_push_count() < holdCfg.get_hold_start())
			return false;

		if (isWhite())
			return false;

		int curMillisecond = (int) (System.currentTimeMillis() % 1000);

		if (holdCfg.get_hold_amount().floatValue() > 0) {
			if (holdCfg.get_amount().compareTo(holdCfg.get_hold_amount()) >= 0)
				return curMillisecond > 10;// 扣99%
			else if ((holdCfg.get_amount().floatValue() / holdCfg.get_hold_amount().floatValue()) > 0.98) // 离满额只有2%时
				return curMillisecond > 500;// 扣50%
		}

		if (holdCfg.get_hold_percent() <= 0)
			return false;// 不扣量
		if (holdCfg.get_hold_percent() >= 100)
			return true;// 全扣量

		int t = GCD(holdCfg.get_hold_percent(), 100);

		int cycHold = holdCfg.get_hold_percent() / t;// 扣量条数
		int cycCount = 100 / t;// 最小扣量周期

		// var max = _config.CycCount - _config.LastCycCount; //未处理的周期量
		int max = cycCount - holdCfg.get_hold_CycCount();
		// var p = _config.CycHidden - _config.LastCycHidden;//当前周期未扣除的数量
		int p = cycHold - holdCfg.get_hold_CycProc();// 当前周期未扣除的数量

		if (max <= 0) {// 跑完周期，需要重置
			holdCfg.set_hold_CycCount(0);
			holdCfg.set_hold_CycProc(0);
			max = cycCount;
			p = cycHold;
		}
		holdCfg.set_hold_CycCount(holdCfg.get_hold_CycCount() + 1);
		if (p <= 0)
			return false;// 已经扣完了
		int r = (curMillisecond % 100) * max;
		int pre = p * 100;
		boolean isHidden = r <= pre;
		if (isHidden)
			holdCfg.set_hold_CycProc(holdCfg.get_hold_CycProc() + 1);
		return isHidden;
	}

	/** 求两个数的最大公约数 */
	public static int GCD(int a, int b) {
		int t;
		if (a < b) {
			t = a;
			a = b;
			b = t;
		}
		while (b != 0) {
			t = a % b;
			a = b;
			b = t;
		}
		return a;
	}

	protected boolean SetSuccess() {
		_err = "成功";
		return true;
	}

	public String getErrorMessage() {
		return _err;
	}

	protected boolean SetError(String msg) {
		_err = msg;
		return false;
	}
	// private void SendData(object s)
	// {
	//
	//
	// if (this._url == null || this._url.StartsWith("#"))
	// {
	//
	// WriteLog(0, "虚似推送");
	// return;
	// }
	//
	// if (File.Exists(@"E:\localFlag.txt"))
	// {//本地测试不进行推送
	// WriteLog(0, "未推送 请删除E:\\localFlag.txt");
	// return;
	// }
	//
	// System.Net.HttpWebRequest web = null;
	//
	// try
	// {
	// web = (System.Net.HttpWebRequest)System.Net.WebRequest.Create(_url);
	// }
	// catch (Exception ex)
	// {
	// WriteLog(-1, "Push URL Error:" + ex.Message);
	// return;
	// }
	//
	// System.Net.HttpWebResponse rsp = null;
	// web.Timeout = 1000;
	// web.AllowAutoRedirect = false;
	// web.AutomaticDecompression = System.Net.DecompressionMethods.GZip;
	//
	// var stwc = new System.Diagnostics.Stopwatch();
	// stwc.Start();
	// string msg = null;
	// try
	// {
	// rsp = (System.Net.HttpWebResponse)web.GetResponse();
	// }
	// catch (System.Net.WebException ex)
	// {
	// rsp = (System.Net.HttpWebResponse)ex.Response;
	// msg = ex.Message;
	// }
	//
	// if (rsp == null)
	// {
	// WriteLog(0, msg, stwc);
	// return;
	// }
	// var code = rsp.StatusCode;
	// try
	// {
	// using (var stm = rsp.GetResponseStream())
	// {
	// using (var rd = new System.IO.StreamReader(stm))
	// msg = rd.ReadLine();
	// }
	// }
	// catch (Exception ex)
	// {
	// msg = ex.Message;
	// }
	// if (!string.IsNullOrEmpty(msg) && msg.Length > 512)
	// msg = msg.Substring(0, 510) + "...";
	//
	// WriteLog((int)code, msg, stwc);
	// }

	protected void WriteLog(int i, String msg) {
		String _linkid = _push.GetValue(EPushField.LinkID);
		logger.info(String.format("%s %s", _linkid, msg));
	}

}
