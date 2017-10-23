<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SingleUserOrderServerV1"%>
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
	long curMils = System.currentTimeMillis();
	String postData = ServiceUtil.requestPostData(request);
	System.out.println("postData:" + postData);
	String ip = request.getHeader("X-Real-IP");
	ip = StringUtil.isNullOrEmpty(ip) ? request.getRemoteAddr() : ip; 
	out.clear();
	Map<String,Object> map = SingleUserOrderServerV1.handleUserOrder(postData, ip);
	System.out.println("SpendMils:" + (System.currentTimeMillis() - curMils) + ";Result:" + map);
	out.println(Base64UTF.encode(StringUtil.getJsonFormObject(map)));
%>