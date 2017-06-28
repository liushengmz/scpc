<%@page import="java.math.BigDecimal"%>
<%@page import="com.system.server.CpAppServer"%>
<%@page import="com.system.model.CpAppModel"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="com.system.model.AdAppModel"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int emp = StringUtil.getInteger(request.getParameter("emp"), 0);
	String appidstr = StringUtil.getString(request.getParameter("appname"), "");
	String appidnamestr = StringUtil.getString(request.getParameter("appnamestr"), "");
	String feeDate = StringUtil.getString(request.getParameter("fee_date"), "");
	int newUserRows = StringUtil.getInteger(request.getParameter("newuser"), 0);
	double amount = StringUtil.getDouble(request.getParameter("amount"), 0);
	int showNewUserRows = StringUtil.getInteger(request.getParameter("shownewuser"), 0);
	double showAmount = StringUtil.getDouble(request.getParameter("showamount"), 0);
	double extendFee = StringUtil.getDouble(request.getParameter("extendfee"), 0);
	double profit = showAmount-extendFee;
	BigDecimal b = new BigDecimal(profit);
	profit = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	int type = StringUtil.getInteger(request.getParameter("type"), 0);
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), "");  /* 获取用户输入开始时间 */
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), "");  /* 获取用户输入的结束时间 */
	
	int appid = StringUtil.getInteger(appidstr, 0);
	
	/* if(type<0)
	{
		type = 0;
	}*/
			
	if(emp>0)
    {
    	if(id>0){
    		new CpAppServer().deletCpApp(id);
    		//response.sendRedirect("cpapp.jsp?pageindex="+pageIndex+"&enddate="+endDate);
    		response.sendRedirect("cpapp.jsp?pageindex="+pageIndex+"&startdate="+startDate+"&enddate="+endDate+"&appname="+appidnamestr);
    		return;
    	}
    	response.sendRedirect("cpapp.jsp?pageindex="+pageIndex+"&startdate="+startDate+"&enddate="+endDate);
    }
	
	CpAppModel model = new CpAppModel();
	model.setId(id);
	model.setAppid(appid);
	model.setFeeDate(feeDate);
	model.setNewUserRows(newUserRows);
	model.setAmount(amount);
	model.setShowNewUserRows(showNewUserRows);
	model.setShowAmount(showAmount);
	model.setExtendFee(extendFee);
	model.setProfit(profit);
	model.setStatus(type);
	
	if(id>0)
	{
		new CpAppServer().updateCpApp(model);
	}
	else
	{
		new CpAppServer().addCpApp(model);
	}
	
	
	
	response.sendRedirect("cpapp.jsp?pageindex="+pageIndex+"&startdate="+startDate+"&enddate="+endDate+"&appname="+appidnamestr);
	
%>
