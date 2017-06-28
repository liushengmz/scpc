<%@page import="com.system.cache.RightConfigCacheMgr"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	RightConfigCacheMgr.refreshAllCache();
	out.print("OK");
%>