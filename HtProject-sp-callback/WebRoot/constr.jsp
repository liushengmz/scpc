 
<%@page import="com.shotgun.Tools.ConfigManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String url = ConfigManager.getConfigData("dBaseConnectionString");
	out.print(url);
%>