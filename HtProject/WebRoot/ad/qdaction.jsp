<%@page import="java.math.BigDecimal"%>
<%@page import="com.system.server.AdChannelServer"%>
<%@page import="com.system.model.AdChannelModel"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="com.system.model.AdAppModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.ChannelServer"%>
<%@page import="com.system.model.ChannelModel"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int userId = StringUtil.getInteger(request.getParameter("userid"), -1);
	int type = StringUtil.getInteger(request.getParameter("type"), 0);
	//String appname = StringUtil.getString(request.getParameter("appname"), "");
	int appid = StringUtil.getInteger(request.getParameter("appname"), -1);
	String appkey = StringUtil.getString(request.getParameter("app_key"), "");
	String channel = StringUtil.getString(request.getParameter("channel"), "");
	int num = StringUtil.getInteger(request.getParameter("hold_persent"), 0);
	String channelkey = StringUtil.getString(request.getParameter("channelkey"), "");
	double scale = StringUtil.getDouble(request.getParameter("scale"), 0);
	BigDecimal b = new BigDecimal(scale);
	scale = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
	
	
	if(type>0)
    {
    	if(id>0){
    		new AdChannelServer().deletChannel(id);
    		response.sendRedirect("channel.jsp");
    		return;
    	}
    	response.sendRedirect("channel.jsp");
    }
	
	
	AdAppModel appModel = new AdAppServer().loadAppById(appid);
	
	
	String hold_percent = num+"";
	
	scale = scale/100;
	
	AdChannelModel model = new AdChannelModel();
	model.setId(id);
	model.setAppname(appModel.getAppname());
	model.setAppkey(appModel.getAppkey());
	model.setName(channel);
	model.setChannel(channelkey);
	model.setScale(scale);
	model.setHold_percent(hold_percent);
	
	if(id>0)
	{
		new AdChannelServer().updataChannel(model);
	}
	else
	{
		model.setUserid(userId);
		new AdChannelServer().addChannel(model);
	}
	
	
	
	response.sendRedirect("channel.jsp?pageindex="+pageIndex);
	
%>
