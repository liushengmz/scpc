<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if(session.getAttribute("user")==null)
	{
		response.sendRedirect("login.jsp");
		return;
	}

	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	//取得SP业务对应的通道
	if(type==1)
	{
	}
	//取得SP对应的业务
	else if(type==2)
	{
		
	}
	//取得SP业务对应的CP业务
	else if(type==3)
	{
		
	}
	//取得该通道已经分配给那些CP，一并列出来
	else if(type==4)
	{
		
	}
	
%>	



















