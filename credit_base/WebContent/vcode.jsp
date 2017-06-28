<%@page import="java.util.regex.Pattern"%>
<%@page import="com.system.server.VerifyCodeServerV1"%>
<%@page import="com.system.model.ApiOrderModel"%>
<%@page import="com.system.server.RequestServerV1"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.BaseRequestModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String verifyCode = StringUtil.getString(request.getParameter("vcode"), "");
	String transParams = StringUtil.getString(request.getParameter("ordernum"), "");
	
	String regex = "^[0-9]*[1-9][0-9]*$";
	
	if(!Pattern.compile(regex).matcher(transParams).find())
	{
		out.print("ordernum error");
		return;
	}
	
	out.clear();
	
	out.print(new VerifyCodeServerV1().handleVerifyCode(verifyCode, transParams));
%>