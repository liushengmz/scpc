<%@page import="com.system.server.TestApServer"%>
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
    	String query = request.getQueryString();
    	if(id>0){
    		new AppServer().deletApp(id);
    		response.sendRedirect("app.jsp");
    		return;
    	}
    	response.sendRedirect("app.jsp?"+query);
    }
    
    if(type==2)
    {
    	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
    	int appnameid = StringUtil.getInteger(request.getParameter("appname"), 0);
    	String appkey = StringUtil.getString(request.getParameter("appkey"), "");
    	String channel = StringUtil.getString(request.getParameter("channel"), "");
    	int typeid = StringUtil.getInteger(request.getParameter("type_id"),0);
    	if(id>0){
    		new ChannelServer().deletChannel(id);
    		response.sendRedirect("channel.jsp?pageindex="+pageIndex+"&appname="+appnameid+"&appkey="+appkey+"&channel="+channel+"&type_id="+typeid);
    		return;
    	}
    	response.sendRedirect("channel.jsp?pageindex="+pageIndex+"&appname="+appnameid+"&appkey="+appkey+"&channel="+channel+"&type_id="+typeid);
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
    
	if(id>0){
		new TestApServer().deletById(id);
		response.sendRedirect("ap.jsp");
	}else{
		response.sendRedirect("ap.jsp");
	}
%>