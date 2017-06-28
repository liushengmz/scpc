package com.dianfu.logical;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import com.database.Dao.tbl_phone_locateDao;
import com.database.Dao.tbl_sp_api_urlDao;
import com.database.Dao.tbl_troneDao;
import com.database.Interface.IDatabase;
import com.database.Interface.ISMS_DataItem;
import com.database.LightModel.tbl_cityItem;
import com.database.LightModel.tbl_moItem;
import com.database.LightModel.tbl_mrItem;
import com.database.LightModel.tbl_sp_api_urlItem;
import com.database.LightModel.tbl_troneItem;
import com.database.Logical.DBRuntimeException;
import com.dianfu.logical.model.SPDataModel;
import com.dianfu.logical.model.SPDataModel.DataType;
import com.shotgun.Tools.Funcs;

/** SP回调处理机 */
public class SPCallbackProces {
	public static final String C_VIRTUAL_PORT = "virtualport";
	public static final String C_VIRTUAL_MSG = "virtualmsg";
	/** 状态关键字检测 */
	private static final String C_STATUS_KEYWORD = "status|DELIVRD|fail|success|succ|ok";
	private ErrCode _errcode;
	private IDatabase _dBase;
	static ArrayList<String> _linkidProcing = new ArrayList<String>();

	private tbl_moItem _moItem;
	private tbl_mrItem _mrItem;
	private SPApiConfig _cfgItem;
	private String _clientIp;
	private tbl_troneItem _lastTrone;
	private SPDataModel _lastSpDataModel;
	private Boolean _lastStatusChecked;

	public enum ErrCode {
		ok, linkid_existed, linkid_not_found, /** 失败的状态同步 */
		fail_mr, database_busy, init_fail
	}

	public com.database.Interface.IDatabase GetDBase() {
		return this._dBase;
	}

	public void SetDBase(com.database.Interface.IDatabase value) {
		this._dBase = value;
	}

	/** 初始化SP回调配置信息 */
	public boolean InitSpCfg(HttpServletRequest request) {
		String url = null;
		if (request == null) {// for test
			_clientIp = "127.0.0.1";
			url = "/demo-interface/bjxt_MM_dqmzr.jsp";
		} else {
			_clientIp = request.getRemoteAddr();
			url = request.getRequestURI();
		}
		Pattern rx = Pattern.compile("(/(\\d+)){0,1}/([^/]+?).\\w+$");
		Matcher m = rx.matcher(url);
		if (!m.find())
			return false;
		// String sql;

		tbl_sp_api_urlItem item;
		if (!Funcs.isNullOrEmpty(m.group(2))) {
			item = tbl_sp_api_urlDao.queryById(_dBase, Integer.parseInt(m.group(2)));
			// System.out.println(item);
		} else {
			item = tbl_sp_api_urlDao.queryByVirtualpage(_dBase, m.group(3));
			// System.out.println(m.group(3));
		}
		// sql = "select * from tbl_sp_api_url where virtual_page='" +
		// _dBase.sqlEncode(m.group(3)) + "'";
		// System.out.println(sql);
		// tbl_sp_api_urlItem item = new tbl_sp_api_urlItem();
		// if (!_dBase.sqlToModel(item, sql)) {
		// return false;
		// }
		if (item == null)
			return false;
		_cfgItem = new SPApiConfig(item);
		return true;
	}

	/** SP回调配置信息 */
	public SPApiConfig GetApiConfig() {
		return _cfgItem;
	}

	private void RemoveFastLink(String linkId) {
		synchronized (_linkidProcing) {
			if (linkId == null)
				return;
			_linkidProcing.remove(linkId);
		}
	}

	/**
	 * 检查linkid是否正在处理队例中
	 * 
	 * @param linkId
	 * @return
	 */
	private Boolean FastLinkCheck(String linkId) {
		synchronized (_linkidProcing) {
			if (linkId == null)
				return true;
			if (_linkidProcing.contains(linkId))
				return true;
			_linkidProcing.add(linkId);
			return false;
		}
	}

	/** 处理一条数据同步，一次多条同步的，需要多次调用，并传入对应的DataModel */
	public boolean StartProcess(SPDataModel spModel) {
		_lastSpDataModel = spModel;
		if (Funcs.isNullOrEmpty(spModel.getLinkid()))
			return SetError(ErrCode.linkid_not_found);

		if (this.FastLinkCheck(spModel.getLinkid()))
			return SetError(ErrCode.linkid_existed);
		try {
			return ProcessCore();
		} catch (DBRuntimeException ex) {
			return this.SetError(ErrCode.database_busy);
		} finally {
			this.RemoveFastLink(spModel.getLinkid());
		}
		// return SetError(ErrCode.database_busy);
	}

	/** 数据核心处理 */
	protected final boolean ProcessCore() {
		if (!InitItem()) {
			return false;
		}
		ISMS_DataItem sms = null;
		boolean isMo = _lastSpDataModel.getDataType() == DataType.MO;
		if (isMo) {
			FillPubData(_moItem);
			sms = _moItem;
		} else {
			sms = _mrItem;
			FillPubData(_mrItem);
			FillAreaInfo(_mrItem);
		}
		if (Funcs.isNullOrEmpty(sms.get_ori_trone()) && _cfgItem.getFieldMap().containsKey("ori_trone")) {// 虚似端口
			if (C_VIRTUAL_PORT.equalsIgnoreCase(_cfgItem.getFieldMap().get("ori_trone")))
				sms.set_ori_trone(String.format("3%05d", sms.get_sp_api_url_id()));
		}
		if (Funcs.isNullOrEmpty(sms.get_ori_order()) && _cfgItem.getFieldMap().containsKey("ori_order")) {// 虚似指令
			if (C_VIRTUAL_MSG.equalsIgnoreCase(_cfgItem.getFieldMap().get("ori_order")))
				sms.set_ori_order(String.format("ht_3%05d_%d", sms.get_sp_api_url_id(), sms.get_price()));
		}

		if (!_cfgItem.getFieldMap().containsKey("mmc"))
			sms.set_mcc("460");// 没有设置mmc动作时，写入默认值

		if (!isMo && Funcs.isNullOrEmpty(_cfgItem.getOriCfg().get_MrStatus())) // MR安全检查
		{// MR 数据，未配置状态检查时，进行“状态”关键字检查，以防万一
			if (checkStatusKeyword(_lastSpDataModel.Request)) {
				sms.set_trone_id(-3);// SP传入数据中 存在“状态”关键字，但接收数据时没有配置
			} else {
				if (sms.get_trone_id() <= 0) {
					troneMatch(sms);
				}
			}
		} else {
			if (sms.get_trone_id() <= 0) {
				troneMatch(sms);
			}
		}

		if (_moItem != null)
			_dBase.saveData(_moItem);

		if (_mrItem != null) {
			if (_moItem != null) {
				_mrItem.set_mo_id(_moItem.get_id());
				_mrItem.set_mo_table(_moItem.TableName());
				if (_mrItem.get_trone_id() <= 0 && _moItem.get_trone_id() > 0)
					_mrItem.set_trone_id(_moItem.get_trone_id());
			}
			_mrItem.set_IsMatch(_mrItem.get_trone_id() > 0);
			_dBase.saveData(_mrItem);
		}
		try {
			doPush(_mrItem);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.SetError(ErrCode.ok);
	}

	/* 匹配渠道并推送数据 */
	private void doPush(tbl_mrItem _mr) {
		BasePusher bp = null;
		bp = new APITronePusher();
		bp.setDBase(_dBase);
		bp.setPushObject(_mr);
		bp.setTrone(_lastTrone);
		if (bp.LoadCPAPI(0)) {
			if (bp.doPush()) {
				return;// AP匹配成功，并推送 成功
			}
		}
		// 未走API模式数据匹配
		bp = new CPTronePusher();
		bp.setDBase(_dBase);
		bp.setPushObject(_mr);
		bp.setTrone(_lastTrone);
		if (bp.LoadCPAPI(0)) {
			bp.doPush();
		}
	}

	/**
	 * 
	 * @param Request
	 * @return true 表示存在状态关键字，可能存在问题。false 未发现在状态信息
	 */
	boolean checkStatusKeyword(HttpServletRequest request) {
		if (_lastStatusChecked != null)
			return _lastStatusChecked;
		if (request == null)
			return _lastStatusChecked = false; // 赋值并返回
		Pattern rx = Pattern.compile("\\b(" + C_STATUS_KEYWORD + ")\\b", Pattern.CASE_INSENSITIVE);
		String qs = request.getQueryString();
		if (Funcs.isNullOrEmpty(qs))
			return _lastStatusChecked = false;// 赋值并返回
		Matcher m = rx.matcher(qs);
		return _lastStatusChecked = m.find();// 赋值并返回
	}

	/** 通道匹配 填充trone_id / trone_type */
	private void troneMatch(ISMS_DataItem sms) {
		if (Funcs.isNullOrEmpty(sms.get_ori_trone()))
			return; // 接收的通道号为空

		// String sql = String.format(
		// "select id,match_price,orders,is_dynamic,price,sp_trone_id from `%s`
		// where trone_num ='%s' and sp_api_url_id=%d and status=1",
		// tbl_troneItem.tableName, _dBase.sqlEncode(sms.get_ori_trone()),
		// sms.get_sp_api_url_id());
		//
		// List<tbl_troneItem> trones = _dBase.sqlToModels(new tbl_troneItem(),
		// sql);

		List<tbl_troneItem> trones = tbl_troneDao.queryByPort(_dBase, sms.get_sp_api_url_id(), sms.get_ori_trone());

		if (trones.size() == 0) {
			sms.set_trone_id(0);// 没有可用通道
			return;
		}
		String mMsg = sms.get_ori_order();
		tbl_troneItem trone = null;
		for (tbl_troneItem cmd : trones) {
			String cMsg = cmd.get_orders();
			if (Funcs.isNullOrEmpty(cMsg) && Funcs.isNullOrEmpty(mMsg)) {
				trone = cmd;
				break;
			}
			if (cmd.get_match_price()) {// 无规则指令，直接匹配价格
				if ((int) (cmd.get_price().floatValue() * 100) == sms.get_price()) { // 两处单位不一样
					trone = cmd;
					break;
				}
			} else if (cmd.get_is_dynamic()) {// 模糊指令
				Pattern rx = Funcs.genericToRegx(cmd.get_orders());
				Matcher m = rx.matcher(mMsg);
				if (m.find()) {
					trone = cmd;
					break;
				}
			} else {// 精确指令
				if (mMsg.equalsIgnoreCase(cMsg)) {
					trone = cmd;
					break;
				}
			} // end if
		} // end for
		_lastTrone = trone;
		if (trone == null) {
			sms.set_trone_id(-2);
			return;
		}

		sms.set_trone_id(trone.get_id());
		String sql = "select trone_type from tbl_sp_trone where id=" + trone.get_sp_trone_id();
		Object t = _dBase.executeScalar(sql);
		if (t != null) {
			sms.set_trone_type((Integer) t);
		}
	}

	/** spModel转化为 tbl_mo / tbl_mo对像 及 排查重复linkid */
	private boolean InitItem() {

		if (Funcs.isNullOrEmpty(_lastSpDataModel.getLinkid()))
			return SetError(ErrCode.linkid_not_found);

		if (_lastSpDataModel.getDataType() == DataType.MO) {
			this._moItem = GetMOItemByLinkId(_lastSpDataModel.getLinkid());
			if (_moItem != null)
				return SetError(ErrCode.linkid_existed);
			this._moItem = (tbl_moItem) _lastSpDataModel.toDataItem();
			this._mrItem = GetMRItemByLinkId(_lastSpDataModel.getLinkid());
		} else {
			if (!this._cfgItem.isSuccessMr(_lastSpDataModel.getStatus())) { // Mr状态检查
				return SetError(ErrCode.fail_mr);
			}
			this._mrItem = GetMRItemByLinkId(_lastSpDataModel.getLinkid());
			if (_mrItem != null)
				return SetError(ErrCode.linkid_existed);
			this._moItem = GetMOItemByLinkId(_lastSpDataModel.getLinkid());
			this._mrItem = (tbl_mrItem) _lastSpDataModel.toDataItem();
		}

		MoCopyToMr();

		return true;
	}

	private boolean SetError(ErrCode code) {
		this._errcode = code;
		return code == ErrCode.ok;
	}

	/** 从数据加载MO对像 */
	protected tbl_moItem GetMOItemByLinkId(String linkId) {
		if (Funcs.isNullOrEmpty(_cfgItem.getOriCfg().get_MoCheck()))
			return null;// 仅MR模式

		String sql = "select * from " + tbl_moItem.pfxSchame + ".%s where linkid='" + this.GetDBase().sqlEncode(linkId)
				+ "' and sp_api_url_id = " + _cfgItem.getOriCfg().get_id();

		Calendar cal = Calendar.getInstance();

		tbl_moItem mo = new tbl_moItem();
		for (int i = 0; i > -3; i--) {
			if (this.GetDBase().sqlToModel(mo, String.format(sql, mo.TableName())))
				return mo;
			cal.add(Calendar.MONTH, -1);
			mo.SetTabelDate(cal.getTime());
		}
		return null;
	}

	/** 从数据加载MR对像 */
	protected tbl_mrItem GetMRItemByLinkId(String linkId) {
		String sql = "select * from " + tbl_mrItem.pfxSchame + ".%s where linkid='" + this.GetDBase().sqlEncode(linkId)
				+ "' and sp_api_url_id = " + _cfgItem.getOriCfg().get_id();

		Calendar cal = Calendar.getInstance();
		tbl_mrItem mr = new tbl_mrItem();

		for (int i = 0; i > -3; i--) {
			if (this.GetDBase().sqlToModel(mr, String.format(sql, mr.TableName())))
				return mr;
			cal.add(Calendar.MONTH, -1);
			mr.SetTabelDate(cal.getTime());
		}
		return null;
	}

	/** 从mo表复制指定字段到到mr表中 */
	private void MoCopyToMr() {
		if (this._moItem == null || this._mrItem == null)
			return;

		String[] fields = _cfgItem.getMoToMrFields();
		if (fields == null)
			return;
		for (String f : fields) {
			this._mrItem.SetValue(f, this._moItem.GetValue(f));
		}
	}

	/** 填充公用数据 */
	private void FillPubData(ISMS_DataItem sms) {
		sms.set_ip(_clientIp);
		sms.set_sp_api_url_id(_cfgItem.getOriCfg().get_id());
		sms.set_sp_id(_cfgItem.getOriCfg().get_sp_id());
		sms.set_recdate(new Timestamp(System.currentTimeMillis()));
	}

	private void FillAreaInfo(tbl_mrItem sms) {

		sms.set_city_id(416);
		sms.set_province_id(32);

		String m = sms.get_mobile();
		if (m != null && m.length() == 11 && m.startsWith("1")) {
			if (Pattern.matches("^\\d+$", m))
				m = m.substring(0, 7);
			else
				m = null;
		} else // 非常正格式手机号
			m = null;
		if (m == null) {
			m = Funcs.imsiToPhone7(sms.get_imsi());
		}
		if (Funcs.isNullOrEmpty(m))
			return;

		tbl_cityItem city = tbl_phone_locateDao.queryCityInfoByPhone(_dBase, m);
		if (city == null)
			return;
		sms.set_city_id(city.get_id());
		sms.set_province_id(city.get_province_id());
	}

	public ErrCode getErrCode() {
		return _errcode;
	}

	// /** 检查是否已经存在 */
	// private boolean LinkExisted(String linkid, Boolean isMr) {
	// if (FastLinkCheck(linkid))
	// return true;
	//
	// Calendar cal = Calendar.getInstance();
	// SimpleDateFormat dfmt = new SimpleDateFormat("YYYYMM");
	// String slnk = this.GetDBase().SqlEncode(linkid);
	// for (int i = 0; i < 3; i++) {
	// Object obj = this.GetDBase()
	// .ExecuteScalar(String.format("select id from tbl_%s_%s where linkid ='%s'
	// limit 1;",
	// (isMr ? "mr" : "mo"), dfmt.format(cal.getTime()), slnk));
	// if (obj != null) {
	// this.RemoveFastLink(linkid);
	// return true;
	// }
	// cal.add(Calendar.MONTH, -1);
	// }
	//
	// return false;
	// }

	// private Boolean SetExisted() {
	// this._errcode = ErrCode.existed;
	// return false;
	// }

	/** 将当前处理错误代码转换为SP要求的结果（后台配置） */
	public String GetSpError() {
		String msg = _cfgItem.getOriCfg().get_MsgOutput();
		if (Funcs.isNullOrEmpty(msg)) {
			return _errcode.toString();
		}

		String[] msgs = msg.split("/", 3);
		switch (_errcode) {
		case ok:
			if (Funcs.isNullOrEmpty(msgs[0]))
				return msgs[0];
			return _errcode.toString();
		case linkid_existed:
			if (msgs.length > 2 && !Funcs.isNullOrEmpty(msgs[1]))
				return msgs[1];
			return _errcode.toString();
		case fail_mr:
			if (msgs.length > 3)
				return msgs[2];
			return String.format("'%s' can't be '%s'", _cfgItem.getFieldMap().get("status"),
					_lastSpDataModel.getStatus());
		case database_busy:
		case linkid_not_found:
			if (msgs.length > 3)
				return msgs[2];
			return _errcode.toString();
		default:
			return _errcode.toString();
		}
	}

}
