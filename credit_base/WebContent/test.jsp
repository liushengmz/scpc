<%@page import="com.system.cache.DayMonthLimitCache"%>
<%@page import="com.system.cache.CpDataCache"%>
<%@page import="com.system.server.VerifyCodeServerV1"%>
<%@page import="com.system.model.ApiOrderModel"%>
<%@page import="com.system.server.RequestServerV1"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.BaseRequestModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	out.clear();

	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	if(type==1)
	{
		out.println(CpDataCache.loadTroneOrderList());
	}
	else if(type==2)
	{
		out.println(DayMonthLimitCache.loadDayMonthLimit());
	}
	else if(type==3)
	{
		out.println(CpDataCache.loadCpSpTroneList());
	}
	else
	{
		out.print("Hi~");
	}
%>

