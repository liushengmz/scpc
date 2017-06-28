
<%@page import="com.database.Logical.MysqlDatabase"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.Map.Entry"%>

<%@page import="com.dianfu.logical.SPCallbackProces"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.Out"%>
<%@page import="com.dianfu.logical.SPApiConfig"%>
<%@page import="java.io.OutputStreamWriter"%>

<%@page import="com.dianfu.logical.model.SPDataModel"%>
<%@page import="javax.websocket.OnOpen"%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
	out.clear();
 

	SPDataModel m = new SPDataModel(request);
	MysqlDatabase dbase = new MysqlDatabase();
	SPCallbackProces sproc = new SPCallbackProces();
	request.getParameter("test");
	
	
	
//	InputStream stm = request.getInputStream();

/*	stm.reset();
	byte[] bin = new byte[1024];
	stm.read(bin);
	String data = new String(bin, "utf-8");
	out.write(data);
	if (data == null)
		return;
	*/
	try {
		sproc.SetDBase(dbase);
		if (!sproc.InitSpCfg(request)) {//未找到配置信息，或配置被停用
			response.setStatus(404);
			out.write("config not found");
			return;
		}
		SPApiConfig cfg = sproc.GetApiConfig();
		cfg.switchToMrMode();

		HashMap<String, String> map = cfg.getFieldMap();
		for (Entry<String, String> kv : map.entrySet()) {
			//key 数据库字段(一般不需要处理)，value为url传入参数名
			m.put(kv.getKey(), request.getParameter(kv.getValue()));
		}
		m.setPrice(cfg.convertPrice(request.getParameter(cfg.getPriceField())));
		sproc.StartProcess(m);

	} finally {
		dbase.close();
	}
	out.write(sproc.GetSpError());
%>