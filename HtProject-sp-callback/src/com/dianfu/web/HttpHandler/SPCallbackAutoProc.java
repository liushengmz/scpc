package com.dianfu.web.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianfu.logical.SPApiConfig;
import com.dianfu.logical.SPCallbackProces;
import com.dianfu.logical.SPCallbackProces.ErrCode;
import com.dianfu.logical.model.SPDataModel;
import com.shotgun.Tools.Funcs;

public class SPCallbackAutoProc {
	com.database.Interface.IDatabase _dbase;
	HttpServletRequest _request;
	HttpServletResponse _response;
	private SPApiConfig _cfg;
	private Boolean _isMo;

	public com.database.Interface.IDatabase getDBase() {
		return _dbase;
	}

	public void setDBase(com.database.Interface.IDatabase value) {
		this._dbase = value;
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public void set_Request(HttpServletRequest value) {
		this._request = value;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public void set_Response(HttpServletResponse value) {
		this._response = value;
	}

	protected boolean onInit() {
		return true;
	}

	public void doProc() {

		SPDataModel m = new SPDataModel(_request);
		SPCallbackProces sproc = new SPCallbackProces();
		sproc.SetDBase(_dbase);

		if (!sproc.InitSpCfg(_request)) {// 未找到配置信息，或配置被停用
			write("config not found", 404);
			return;
		}

		if (!onInit()) {
			write(ErrCode.init_fail, "init fail/invalid data!");
			return;
		}

		_cfg = sproc.GetApiConfig();
		if (isMo()) {
			_cfg.switchToMoMode();
		} else {
			_cfg.switchToMrMode();
		}

		HashMap<String, String> map = _cfg.getFieldMap();
		for (Entry<String, String> kv : map.entrySet()) {
			// key 数据库字段(一般不需要处理)，value为url传入参数名
			m.put(kv.getKey(), getParamValue(kv.getValue()));
		}
		String pf = _cfg.getPriceField();
		if (!Funcs.isNullOrEmpty(pf)) {
			m.setPrice(_cfg.convertPrice(getParamValue(pf)));
		}

		sproc.StartProcess(m);

		write(sproc.getErrCode(), sproc.GetSpError());

	}

	protected void write(ErrCode errCode, String msg) {
		write(msg);
	}

	private boolean isMo() {
		if (_isMo != null)
			return _isMo;

		String[] ar = _cfg.getMoCheck();
		if (ar == null) // 仅MR
		{
			_isMo = false;
			return _isMo;
		}
		String v = getParamValue(ar[0]);
		if (ar.length == 1) // 只写字段名,表示,只要URL上出生了该参数,即视为MO
		{
			_isMo = !Funcs.isNullOrEmpty(v);
			return _isMo;
		}

		Pattern rx = Pattern.compile(ar[1]);
		Matcher m = rx.matcher(v);
		_isMo = m.find();
		return _isMo;
	}

	private void write(String msg, int code) {
		_response.setStatus(code);
		write(msg);
	}

	protected void write(String msg) {
		try {
			_response.getWriter().write(msg);
		} catch (IOException e) {
		}
	}

	protected String getParamValue(String field) {
		return _request.getParameter(field);
	}

}
