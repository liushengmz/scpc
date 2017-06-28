<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
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
	int spTroneId = StringUtil.getInteger(request.getParameter("sptroneid"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cpid"), -1);
	Float money = StringUtil.getFloat(request.getParameter("money"), 0.0F);
	int troneId = StringUtil.getInteger(request.getParameter("trone_id"), 0);
	int troneOrderId = StringUtil.getInteger(request.getParameter("trone_order_id"), 0);
	int provinceId = StringUtil.getInteger(request.getParameter("province_id"), 0);
	String curDate = StringUtil.getDefaultDate();
	String curMonth = StringUtil.getMonthFormat();
	
	if(spTroneId<0 || cpId <0)
	{
		out.print("NO");
		return;
	}
	
	out.print("OK");
	
	DayMonthLimitCache.addToLimit(spTroneId, cpId, money, curDate, curMonth);
%>

