package com.dianfu.logical;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.database.LightModel.tbl_sp_api_urlItem;
import com.shotgun.Tools.Funcs;

public class SPApiConfig {
	HashMap<String, String> _map;
	tbl_sp_api_urlItem _cfgItem;
	private ModeType _mode;
	private String _priceField;
	private int _priceType;
	private String _priceExtrInfo;

	public SPApiConfig(tbl_sp_api_urlItem cfg) {
		_cfgItem = cfg;
	}

	private enum ModeType {
		undefine, MO, MR
	}

	public void switchToMrMode() {
		_mode = ModeType.MR;
	}

	public void switchToMoMode() {
		_mode = ModeType.MO;
	}

	/** 收集字段映射关系，key为数据字段，value为SP传入参数名（包括 linkid） */
	public HashMap<String, String> getFieldMap() {
		if (_map != null)
			return _map;
		_map = UrlFieldsMap(_mode == ModeType.MO ? _cfgItem.get_MoFieldMap() : _cfgItem.get_MrFidldMap());
		return _map;
	}

	/** 取得Price传用字段名 */
	public String getPriceField() {
		if (_priceField != null)
			return _priceField;
		_priceField = "";
		String f = _mode == ModeType.MR ? _cfgItem.get_MrPrice() : _cfgItem.get_MoPrice();
		if (Funcs.isNullOrEmpty(f)) {
			return _priceField;
		}

		// System.out.println("price config:"+f);
		Pattern p = Pattern.compile("^([^,]+?)(,{0,1})\\s{0,}(\\d+)");
		Matcher mc = p.matcher(f);
		if (!mc.find())
			return _priceField;
		_priceField = mc.group(1);
		_priceType = Funcs.parseInt(mc.group(3), -1);
		switch (_priceType) {
		case 2:
			_priceExtrInfo = f.substring(mc.group().length());
			if (_priceExtrInfo.startsWith(","))
				_priceExtrInfo = _priceExtrInfo.substring(1);
			break;
		case 1:
		case 0:
		case 3:
			break;
		default:
			_priceField = "";
			break;
		}

		return _priceField;
	}

	/** 输入价格转换 单位：分 */
	public int convertPrice(String value) {
		if (Funcs.isNullOrEmpty(value))
			return -1;
		if (_priceField == null)
			getPriceField();// 初化price信息
		// System.out.println("price value:"+value);
		Float fValue;
		switch (_priceType) {
		case 0:// 分
			return Funcs.parseInt(value, -1);
		case 1:// 元
			fValue = Funcs.parseFloat(value, -0.01F);
			return (int) fValue.floatValue();
		case 3:// 角
			fValue = Funcs.parseFloat(value, -0.1F);
			return (int) fValue.floatValue();
		case 2:// 复杂模式
			break;
		default:
			return -1;
		}
		if (Funcs.isNullOrEmpty(_priceExtrInfo))
			return -2;
		Pattern rx = Pattern.compile("(.+?):(\\d+)(,|$)");
		Matcher m = rx.matcher(_priceExtrInfo);
		while (m.find()) {
			if (value.equals(m.group(1)))
				return Funcs.parseInt(m.group(2), -1);
		}
		return -1;
	}

	private HashMap<String, String> UrlFieldsMap(String ptr) {
		if (Funcs.isNullOrEmpty(ptr))
			return new HashMap<String, String>();

		String[] fields = ptr.split(",");

		HashMap<String, String> U2DMap = new HashMap<String, String>();
		for (String f : fields) {
			String[] ar = f.split(":");
			String url = ar[0];
			String sql;
			if (ar.length == 1)
				sql = url.toLowerCase();
			else
				sql = ar[1].toLowerCase();
			if ("msg".equals(sql))
				sql = "ori_order";
			else if ("port".equals(sql))
				sql = "ori_trone";
			else if ("servicecode".equals(sql))
				sql = "service_code";
			else if ("cpparam".equals(sql))
				sql = "cp_param";

			U2DMap.put(sql, url);
		}

		String s = getLinkIdField();
		if (!Funcs.isNullOrEmpty(s))
			U2DMap.put("linkid", s);

		return U2DMap;

	}

	public String[] getMoCheck() {
		if (Funcs.isNullOrEmpty(_cfgItem.get_MoCheck()))
			return null;
		return _cfgItem.get_MoCheck().split(",", 2);
	}

	/** SP传入的linkId参数名， 该值已经存在于getFieldMap()结果集中 */
	public String getLinkIdField() {
		return _mode == ModeType.MO ? _cfgItem.get_MoLink() : _cfgItem.get_MrLink();
	}

	public tbl_sp_api_urlItem getOriCfg() {
		return _cfgItem;
	}

	public String[] getMoToMrFields() {
		String val = _cfgItem.get_MoToMr();
		if (Funcs.isNullOrEmpty(val))
			return null;

		String[] arrs = _cfgItem.get_MoToMr().split(",");
		if (arrs.length == 0)
			return null;

		for (int i = arrs.length - 1; i >= 0; --i) {
			if ("msg".equalsIgnoreCase(arrs[i]))
				arrs[i] = "ori_order";
			else if ("port".equalsIgnoreCase(arrs[i]))
				arrs[i] = "ori_trone";
			else if ("servicecode".equalsIgnoreCase(arrs[i]))
				arrs[i] = "service_code";
	}
		return arrs;

	}

	/**
	 * 检查传入状态值,是否为成功标记
	 * 
	 * @param get_status
	 *            状态值
	 * @return
	 */
	public boolean isSuccessMr(String status) {
		String cfgStatus = _cfgItem.get_MrStatus();
		if (Funcs.isNullOrEmpty(cfgStatus))
			return true;
		if (Funcs.isNullOrEmpty(status))
			return false;
		Pattern rx = Funcs.genericToRegx(cfgStatus);
		Matcher m = rx.matcher(status);
		return m.find();
	}
}
