package com.dianfu.web.HttpHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.Interface.ISMS_DataItem;
import com.database.LightModel.tbl_moItem;
import com.database.LightModel.tbl_mrItem;
import com.database.LightModel.tbl_sp_api_urlItem;
import com.database.Logical.LightDataModel;
import com.database.Logical.MysqlDatabase;

/**这个是没有的代码*/
public class BaseSpCallback implements Filter {

	HttpServletRequest Request;
	HttpServletResponse Response;
	private MysqlDatabase _db;
	private tbl_sp_api_urlItem _apiCfg;
	/** 0:未知,1:MO,other:Mr */
	private int _IsMo;
	private String _linkId;
	private HashMap<String, String> U2DMap;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		this.Request = (HttpServletRequest) request;
		this.Response = (HttpServletResponse) response;

		String ua = Request.getHeader("UserAgent");

		if (ua != null && ua.length() != 0) {
			if (ua.contains("Alibaba.Security.Heimdall")) {
				Response.sendError(410, "Not welcome");
				return;
			}
		}

		_db = new MysqlDatabase();
		try {
			BeginProcess();
		} finally {
			if (_db == null)
				_db.close();
		}
	}

	void BeginProcess() {
		// Request.getParameter("id");

		if (!LoadConfig()) {
			this.WriteError("Not Found");
			return;
		}
		com.database.Interface.ISMS_DataItem m = this.LoadItem();
		if (m == null)
			return;

		this.WriteError("done");

	}

	private Boolean LoadConfig() {
		String path = this.Request.getRequestURI();

		Pattern rx = java.util.regex.Pattern.compile("/(\\d+)/\\w+\\.\\w+$");
		Matcher mcs = rx.matcher(path);
		if (!mcs.find())
			return false;
		int urlId = Integer.parseInt(mcs.group(1));
		if (urlId == 0)
			return false;

		String sql = "select * from tbl_sp_api_url where id=" + urlId;

		tbl_sp_api_urlItem m = new tbl_sp_api_urlItem();
		if (!_db.sqlToModel(m, sql))
			return false;
		this._apiCfg = m;
		return true;
	}

	public String GetParamValue(String Field) {
		String t = Request.getParameter(Field);
		if (t == null) {
			if (Field.equalsIgnoreCase("virtualport"))
				return Integer.toString(this._apiCfg.get_id() + 300000);
		}
		return t;
	}

	public boolean IsMo() {

		if (_IsMo != 0)
			return _IsMo == 1;
		if (this._apiCfg.Is_MoCheckNull() || this._apiCfg.get_MoCheck().length() == 0) {
			_IsMo = -1;
			return false;
		}
		String[] ar = this._apiCfg.get_MoCheck().split(":");
		String val = GetParamValue(ar[0]);
		if (ar.length == 1) // 只写字段名,表示,只要URL上出生了该参数,即视为MO
		{
			_IsMo = val != null ? 1 : -1;
		} else {
			if (val == null)
				val = "";
			Pattern rx = Pattern.compile(ar[1]);
			_IsMo = rx.matcher(val).find() ? 1 : -1;
		}
		return _IsMo == 1;

	}

	protected String GetLinkId() throws Exception {
		String f = IsMo() ? _apiCfg.get_MoLink() : _apiCfg.get_MrLink();

		if (f == null || f.length() == 0)
			throw new Exception(String.format("请配置Link字段。IsMo:%i", this.IsMo()));
		if (_linkId != null)
			return _linkId;

		_linkId = GetParamValue(f);
		if (_linkId == null)
			_linkId = "";

		return _linkId;
	}

	private ISMS_DataItem LoadItem() {
		String linkId;
		try {
			linkId = GetLinkId();
		} catch (Exception e) {
			this.SetError(500, e.getMessage());
			return null;
		}
		if (linkId == null || linkId.length() == 0) {
			this.WriteLinkidNotExisted();
			return null;
		}
		ISMS_DataItem item;
		if (IsMo()) {
			tbl_moItem mo = new com.database.LightModel.tbl_moItem();
			item = mo;
			mo.set_create_date(new Timestamp(System.currentTimeMillis()));
		} else {

			tbl_mrItem mr = new tbl_mrItem();
			item = mr;
			mr.set_create_date(new Timestamp(System.currentTimeMillis()));
		}

		item.set_linkid(linkId);
		item.set_recdate(new Timestamp(System.currentTimeMillis()));

		Iterator<Entry<String, String>> iter = U2DMap.entrySet().iterator();
		LightDataModel model = (LightDataModel) item;
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if (IsMo() && key.equalsIgnoreCase(com.database.LightModel.tbl_mrItem_ori.Fields.ivr_time)) {
				int time = Integer.parseInt(GetParamValue(val));
				((com.database.LightModel.tbl_mrItem) model).set_ivr_time(time);
				continue;
			}
			model.SetValue(key, GetParamValue(val));
		}

		return item;
	}

	private void WriteLinkidNotExisted() {
		this.WriteError("Linkid not existed or empty");
	}

	void UrlFieldsMap(String ptr) {
		String[] fields = ptr.split(",");

		U2DMap = new HashMap<String, String>();
		for (String f : fields) {
			String[] ar = f.split(",");
			String url = ar[0];
			String sql;
			if (ar.length == 1)
				sql = url.toLowerCase();
			else
				sql = ar[1].toLowerCase();
			if ("msg".equalsIgnoreCase(sql))
				sql = "ori_order";
			else if ("port".equalsIgnoreCase(sql))
				sql = "ori_trone";
			else if ("servicecode".equalsIgnoreCase(sql))
				sql = "service_code";
			else if ("cpparam".equalsIgnoreCase(sql))
				sql = "cp_param";

			U2DMap.put(sql, url);
		}
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		// TODO Auto-generated method stub

	}

	protected void WriteError(String msg) {

		try {
			this.Response.getWriter().write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void SetError(int httpCode, String msg) {

		try {
			Response.sendError(httpCode, msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
