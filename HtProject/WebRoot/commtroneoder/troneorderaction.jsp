<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int troneId = StringUtil.getInteger(request.getParameter("trone_id"),-1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	String orderTroneName = StringUtil.getString(request.getParameter("order_trone_name"),"");
	int dynamic = StringUtil.getInteger(request.getParameter("dynamic"), 0);
	int pushUrlId = StringUtil.getInteger(request.getParameter("push_url_id"), -1);
	int disable = StringUtil.getInteger(request.getParameter("status"), 0);
	int holdPercent = StringUtil.getInteger(request.getParameter("hold_percent"), 0);
	float holdAmount = StringUtil.getFloat(request.getParameter("hold_amount"), 0);
	int holdIsCustom = StringUtil.getInteger(request.getParameter("hold_custom"), 0);
	String orderNum = StringUtil.getString(request.getParameter("order_num"),"");
	int holdAccount = StringUtil.getInteger(request.getParameter("hold_account"), 0);
	
	String query = StringUtil.getString(request.getParameter("query"), "");
	
	TroneOrderModel model = new TroneOrderModel();
	model.setId(id);
	model.setTroneId(troneId);
	model.setCpId(cpId);
	model.setOrderTroneName(orderTroneName);
	model.setDynamic(dynamic);
	model.setPushUrlId(pushUrlId);
	model.setDisable(disable);
	model.setHoldPercent(holdPercent);
	model.setHoldAmount(holdAmount);
	model.setIsHoldCustom(holdIsCustom);
	model.setOrderNum(orderNum);
	model.setHoldAcount(holdAccount);
	
	if(id<=0)
	{
		new TroneOrderServer().addTroneOrder(model);
	}
	else
	{
		new TroneOrderServer().updateTroneOrder(model);
	}
	
	response.sendRedirect("troneorder.jsp?" + Base64UTF.decode(query));
	
%>
