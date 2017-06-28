<%@page import="com.system.server.CpAppServer"%>
<%@page import="com.system.server.AdChannelServer"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.server.ChannelServer"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
    int type = StringUtil.getInteger(request.getParameter("type"), 0);
    
    if(type==1){
    	if(id>0){
    		new AdAppServer().deletApp(id);
    		response.sendRedirect("app.jsp");
    		return;
    	}
    	response.sendRedirect("app.jsp");
    }
    
    if(type==2)
    {
    	if(id>0){
    		new AdChannelServer().deletChannel(id);
    		response.sendRedirect("channel.jsp");
    		return;
    	}
    	response.sendRedirect("channel.jsp");
    }
    
    if(type==3)
    {
    	if(id>0){
    		new MenuHeadServer().deletMenu(id);
    		response.sendRedirect("menu3.jsp");
    		return;
    	}
    	response.sendRedirect("menu3.jsp");
    }
    
    if(type==5)
    {
    	if(id>0){
    		new CpAppServer().deletCpApp(id);
    		response.sendRedirect("cpapp.jsp");
    		return;
    	}
    	response.sendRedirect("cpapp.jsp");
    }
    
	
%>