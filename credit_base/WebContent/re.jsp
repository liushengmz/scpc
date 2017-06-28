<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.system.cache.CacheConfigMgr"%>
<%@page import="com.system.util.ServiceUtil"%>
<%@page import="com.system.model.ApiOrderModel"%>
<%@page import="com.system.server.RequestServerV1"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.BaseRequestModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
	String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	int type = StringUtil.getInteger(request.getParameter("type"), 1);
	
	if(type==1)
	{
		out.print(date + ":刷新通道数据成功！");
		CacheConfigMgr.refreshAllTroneCache();
	}
	else if(type==2)
	{
		out.print(date + ":刷新通道日月限数据成功！");
		CacheConfigMgr.refreshDayMonthLimitCache();
	}
	else if(type==3)
	{
		out.print(date + ":刷新所有缓存数据成功！");
		CacheConfigMgr.refreshDayMonthLimitCache();
	}
%>