<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.CpPushUrlServer"%>
<%@page import="com.system.model.CpPushUrlModel"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String name = StringUtil.getString(request.getParameter("name"), "");
	int holdPercent = StringUtil.getInteger(request.getParameter("hold_percent"), 0);
	float holdAmount = StringUtil.getFloat(request.getParameter("hold_amount"), 0);
	String url = StringUtil.getString(request.getParameter("url"), "");
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), 0);
	int holdStartCount = StringUtil.getInteger(request.getParameter("hold_start_count"), 0);
	
	String query = StringUtil.getString(request.getParameter("query"),"");
	
	CpPushUrlModel model = new CpPushUrlModel();
	
	model.setId(id);
	model.setName(name);
	model.setUrl(url);
	model.setHoldAmount(holdAmount);
	model.setHoldPercent(holdPercent);
	model.setHoldStartCount(holdStartCount);
	model.setCpId(cpId);
	
	if(id>0)
	{
		new CpPushUrlServer().updateCpPushUrl(model);
	}
	else
	{
		new CpPushUrlServer().addCpPushUrl(model);
	}
	
	response.sendRedirect("cppushurl.jsp?" + Base64UTF.decode(query));
	
%>
