<%@page import="com.system.util.JspParamsUtil"%>
<%@page import="com.system.util.MapUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.system.cache.CacheConfigMgr"%>
<%@page import="com.system.util.ServiceUtil"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), 0);
	if(type==1)
	{
		CacheConfigMgr.refreshFrequenceCache();
		out.println("刷新常用数据完成 " + StringUtil.getNowFormat());
	}
	else if(type==2)
	{
		CacheConfigMgr.refreshUnusualCache();
		out.println("刷新非常用数据完成 " + StringUtil.getNowFormat());
	}
	else if(type==3)
	{
		CacheConfigMgr.refreshAllCache();
		out.println("刷新所有数据完成 " + StringUtil.getNowFormat());
	}
	else
	{
		out.println("hello boy ~");
	}

%>