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
	//String appname = StringUtil.getString(request.getParameter("appname"), "");
	int appid = StringUtil.getInteger(request.getParameter("appname"), -1);
	String appkey = StringUtil.getString(request.getParameter("app_key"), "");
	String channel = StringUtil.getString(request.getParameter("channel"), "");
	int num = StringUtil.getInteger(request.getParameter("hold_persent"), 0);
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	int settelType = StringUtil.getInteger(request.getParameter("settle_type"), 1);
	int type = StringUtil.getInteger(request.getParameter("type"), 1);
	/*         用于返回当前页的数据参数        */
	int appnameid = StringUtil.getInteger(request.getParameter("appName"), -1);
	String appKey = StringUtil.getString(request.getParameter("appKey"), "");
	String channelStr = StringUtil.getString(request.getParameter("Channel"), "");
	int typeId = StringUtil.getInteger(request.getParameter("Type_Id"), -1);
	/*         用于返回当前页的数据参数        */
	
	int accountType = StringUtil.getInteger(request.getParameter("atype"), -1);
	
	if(accountType==1)
	{
		int userId =  StringUtil.getInteger(request.getParameter("userid"), 0);
		new ChannelServer().updateChannelAccount(id, userId);
		String query = StringUtil.getString(request.getParameter("query"), "");
		response.sendRedirect("channelacount.jsp?msg=1&id="+ id +"&query=" + query);
		return;
	}
	
	if(appid<0)
	{
		response.sendRedirect("channel.jsp?pageindex="+pageIndex+"&appname="+appnameid+"&appkey="+appKey+"&channel="+channelStr+"&type_id="+typeId);
	}
	
	AppModel appModel = new AppServer().loadAppById(appid);
	
	
	String hold_percent = num+"";
	
	
	ChannelModel model = new ChannelModel();
	model.setId(id);
	model.setAppname(appModel.getAppname());
	model.setAppkey(appModel.getAppkey());
	model.setChannel(channel);
	model.setSettleType(settelType);
	model.setHold_percent(hold_percent);
	model.setRemark(remark);
	model.setSyn_type(type);
	
	if(id>0)
	{
		new ChannelServer().updataChannel(model);
	}
	else
	{
		new ChannelServer().addChannel(model);
	}
	
	response.sendRedirect("channel.jsp?pageindex="+pageIndex+"&appname="+appnameid+"&appkey="+appKey+"&channel="+channelStr+"&type_id="+typeId);
	
%>
