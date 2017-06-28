<%@page import="com.system.server.CpAppServer"%>
<%@page import="com.system.server.CpChannelServer"%>
<%@page import="com.system.model.CpChannelModel"%>
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
	int emp = StringUtil.getInteger(request.getParameter("emp"), 0);
	int type = StringUtil.getInteger(request.getParameter("type"), 0);
	//String appname = StringUtil.getString(request.getParameter("appname"), "");
	int appid = StringUtil.getInteger(request.getParameter("appname"), -1);
	int channelid = StringUtil.getInteger(request.getParameter("channel"), -1);
	String feeDate = StringUtil.getString(request.getParameter("fee_date"), "");
	int newUserRows = StringUtil.getInteger(request.getParameter("newuser"), 0);
	int active = StringUtil.getInteger(request.getParameter("active"), 0);
	double amount = StringUtil.getDouble(request.getParameter("amount"), 0);
	int showNewUserRows = StringUtil.getInteger(request.getParameter("shownewuser"), 0);
	int showActiveRows = StringUtil.getInteger(request.getParameter("showactive"), 0);
	double showAmount = StringUtil.getDouble(request.getParameter("showamount"), 0);
	double scale = StringUtil.getDouble(request.getParameter("scale"), 0);
	BigDecimal b = new BigDecimal(scale);
	scale = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
	int status = StringUtil.getInteger(request.getParameter("type2"), 0);
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), "");  /* 获取用户输入开始时间 */
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), "");  /* 获取用户输入的结束时间 */
	String appidstr = StringUtil.getString(request.getParameter("appnamestr"), "");
	String channelidstr = StringUtil.getString(request.getParameter("channelidstr"), "");
	
	if(emp>0)
    {
		if(id>0){
			new CpChannelServer().deletCpChannel(id);
			response.sendRedirect("cpchannel.jsp?startdate="+startDate+"&enddate="+endDate+"&appname="+appidstr+"&channel="+channelidstr+"&pageindex="+pageIndex);
			return;
		}else{
			response.sendRedirect("cpchannel.jsp?startdate="+startDate+"&enddate="+endDate+"&appname="+appidstr+"&channel="+channelidstr+"&pageindex="+pageIndex);
			return;
		}
    }
	
	//String channel = channelid+"";
	
	scale = scale/100;
	
	CpChannelModel model = new CpChannelModel();
	//AdChannelModel model = new AdChannelModel();
	model.setId(id);
	model.setChannelid(channelid);
	model.setFeeDate(feeDate);
	model.setNewUserRows(newUserRows);
	model.setActiveRows(active);
	model.setAmount(amount);
	model.setShowNewUserRows(showNewUserRows);
	model.setShowActiveRows(showActiveRows);
	model.setShowAmount(showAmount);
	model.setScale(scale);
	model.setStatus(status);
	
	
	if(id>0)
	{
		new CpChannelServer().updateCpChannel(model);
		//new AdChannelServer().updataChannel(model);
	}
	else
	{
		new CpChannelServer().addcpchannel(model);
		//new AdChannelServer().addChannel(model);
	}
	
	
	
	response.sendRedirect("cpchannel.jsp?startdate="+startDate+"&enddate="+endDate+"&appname="+appidstr+"&channel="+channelidstr+"&pageindex="+pageIndex);
	
%>
