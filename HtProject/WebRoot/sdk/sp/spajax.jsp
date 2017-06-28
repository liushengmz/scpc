<%@page import="com.system.sdk.server.SdkSpServer"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Map;" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
		String fullName = StringUtil.getString(request.getParameter("fullName"), "");
		String shortName = StringUtil.getString(request.getParameter("shortName"), "");
		int id = StringUtil.getInteger(request.getParameter("id"), -1);

		Map<String,Integer> map=SdkSpServer.checkData(fullName, shortName, id);
		int status=(Integer)map.get("flag");
		out.clear();
		out.print(status);
	
%>