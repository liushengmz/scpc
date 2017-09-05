<%@page import="com.system.server.ToBusinessRequestServerV1"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.MapUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.JspParamsUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.system.cache.CacheConfigMgr"%>
<%@page import="com.system.util.ServiceUtil"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String postData = ServiceUtil.requestPostData(request);
	String ip = request.getHeader("X-Real-IP");
	ip = StringUtil.isNullOrEmpty(ip) ? request.getRemoteAddr() : ip; 
	System.out.println("ip: " + ip+ ";postData:" + postData);
	out.clear();
	out.println(StringUtil.getJsonFormObject(ToBusinessRequestServerV1.handleBusinessRequest(postData,ip)));
%>