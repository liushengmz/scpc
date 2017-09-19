<%@page import="com.system.server.SyncFromSpServerV1"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String key = request.getParameter("key");
	out.clear();
	out.print(SyncFromSpServerV1.handleSyncFromSp(key));
%>