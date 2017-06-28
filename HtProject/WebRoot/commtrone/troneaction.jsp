<%@page import="com.system.server.TronePayCodeServer"%>
<%@page import="com.system.model.TronePayCodeModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.server.TroneServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int delId = StringUtil.getInteger(request.getParameter("did"), -1);

	String query = StringUtil.getString(request.getParameter("query"), "");

	if(delId>0)
	{
		new TroneServer().deleteTrone(delId);
		response.sendRedirect("trone.jsp");
		return;
	}

	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);	
	int spApiUrlId = StringUtil.getInteger(request.getParameter("api_url_id"), -1);	
	String troneOrder = StringUtil.getString(request.getParameter("trone_order"), "").trim();
	String troneNum = StringUtil.getString(request.getParameter("trone_num"), "").trim();
	String troneName = StringUtil.getString(request.getParameter("trone_name"), "").trim();
	float price = StringUtil.getFloat(request.getParameter("price"), 0.0F);
	int dynamic = StringUtil.getInteger(request.getParameter("dynamic"), -1);
	int matchPrice = StringUtil.getInteger(request.getParameter("match_price"), -1);
	
	int status = StringUtil.getInteger(StringUtil.mergerStrings(request.getParameterValues("status"), ","),0);
	
	TroneModel model = new TroneModel();
	model.setSpId(spId);
	model.setSpTroneId(spTroneId);
	model.setSpApiUrlId(spApiUrlId);
	model.setTroneNum(troneNum);
	model.setOrders(troneOrder);
	model.setPrice(price);
	model.setStatus(status);
	model.setDynamic(dynamic);
	model.setMatchPrice(matchPrice);
	model.setTroneName(troneName);
	
	model.setId(id);
	
	
	if(id==-1)
	{
		int troneId = new TroneServer().insertTrone(model);
		
		//
		int existPayCode = StringUtil.getInteger(request.getParameter("exist_pay_code"), 0);
		if(existPayCode==1)
		{
			String payCode = StringUtil.getString(request.getParameter("paycode"), "");
			String appId = StringUtil.getString(request.getParameter("appid"), "");
			String channelId = StringUtil.getString(request.getParameter("channelid"), "");
			
			TronePayCodeModel tronePayCodeModel = new TronePayCodeModel();
			
			tronePayCodeModel.setTroneId(troneId);
			tronePayCodeModel.setAppId(appId);
			tronePayCodeModel.setChannelId(channelId);
			tronePayCodeModel.setPayCode(payCode);
			
			new TronePayCodeServer().addTronePayCode(tronePayCodeModel);
		}
	}
	else
	{
		new TroneServer().updateTrone(model);
		
		int existPayCode = StringUtil.getInteger(request.getParameter("exist_pay_code"), 0);
		
		int tronePayCodeId = StringUtil.getInteger(request.getParameter("trone_pay_code_id"), -1);
		
		if(existPayCode==1)
		{
			String payCode = StringUtil.getString(request.getParameter("paycode"), "");
			String appId = StringUtil.getString(request.getParameter("appid"), "");
			String channelId = StringUtil.getString(request.getParameter("channelid"), "");
			
			TronePayCodeModel tronePayCodeModel = new TronePayCodeModel();
			
			tronePayCodeModel.setId(tronePayCodeId);
			tronePayCodeModel.setTroneId(model.getId());
			tronePayCodeModel.setAppId(appId);
			tronePayCodeModel.setChannelId(channelId);
			tronePayCodeModel.setPayCode(payCode);
			
			if(tronePayCodeId>0)
				new TronePayCodeServer().updateTronePayCode(tronePayCodeModel);
			else
				new TronePayCodeServer().addTronePayCode(tronePayCodeModel);
		}
	}
	
	response.sendRedirect("trone.jsp?" + Base64UTF.decode(query));
%>
