<%@page import="com.system.model.CpSpTroneRateModel"%>
<%@page import="com.system.model.SingleCpSpTroneRateModel"%>
<%@page import="com.system.server.SingleCpSpTroneRateServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.CpSpTroneRateServer"%>
<%@page import="com.system.server.CpJieSuanLvServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	String query2 = StringUtil.getString(request.getParameter("query2"), "");
	String query = StringUtil.getString(request.getParameter("query"), "");
	
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int cpTroneRateId = StringUtil.getInteger(request.getParameter("cpsptroneid"), -1);
	String startDate = StringUtil.getString(request.getParameter("startdate"), "");
	String endDate = StringUtil.getString(request.getParameter("enddate"), "");
	float rate = StringUtil.getFloat(request.getParameter("rate"),0.0F);
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	float dayLimit = StringUtil.getFloat(request.getParameter("day_limit"), 0F);
	float monthLimit = StringUtil.getFloat(request.getParameter("month_limit"), 0F);
	String provinceHoldRate = StringUtil.getString(request.getParameter("pros_data"), "");
	int jsType = StringUtil.getInteger(request.getParameter("js_type"), 0);
	
	SingleCpSpTroneRateModel model = new SingleCpSpTroneRateModel();
	
	model.setId(id);
	model.setCpSpTroneId(cpTroneRateId);
	model.setStartDate(startDate);
	model.setEndDate(endDate);
	model.setRemark(remark);
	model.setRate(rate);
	
	//更新CP-SP-TRONE的基本结算率
	if(type==1)
	{
		new CpSpTroneRateServer().updateCpSpTroneRate(id, rate);
		out.println("OK");
	}
	else if(type==2) //增加指定的CP-SP-TRONE-RATE
	{
		new SingleCpSpTroneRateServer().addSingleRate(model);
		
		response.sendRedirect("ratelist.jsp?id=" + cpTroneRateId + "&" + Base64UTF.decode(query2));
	}
	else if(type==3) //检查同一时间段内有没有重复的
	{
		out.print(new SingleCpSpTroneRateServer().isRateDateCross(cpTroneRateId, startDate, endDate) ? "NO" : "OK");
	}
	else if(type==4) //更新单个结算率的数据
	{
		new SingleCpSpTroneRateServer().updateSingleRate(model);
		
		response.sendRedirect("ratelist.jsp" + "?query=" + query + "&" + Base64UTF.decode(query2));
	}
	else if(type==5) //删除单个结算率列表里的数据
	{
		new SingleCpSpTroneRateServer().delSingleRate(id);
		
		response.sendRedirect("ratelist.jsp?" + Base64UTF.decode(query2));
	}
	else if(type==6) //更新CP业务的日月限
	{
		CpSpTroneRateModel tModel = new CpSpTroneRateModel();
		
		tModel.setId(id);
		tModel.setDayLimit(dayLimit);
		tModel.setMonthLimit(monthLimit);
		tModel.setRate(rate);
		tModel.setProsData(provinceHoldRate);
		tModel.setJsType(jsType);
		
		new CpSpTroneRateServer().updateCpSpTroneLimit(tModel);
		
		response.sendRedirect("rate.jsp" + "?" + Base64UTF.decode(query));
	}
%>