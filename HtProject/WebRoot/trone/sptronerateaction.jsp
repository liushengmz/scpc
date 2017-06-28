<%@page import="com.system.model.SpTroneRateModel"%>
<%@page import="com.system.server.SpTroneRateServer"%>
<%@page import="com.system.server.MrServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	String query = StringUtil.getString(request.getParameter("query"), "");

	int type = StringUtil.getInteger(request.getParameter("type"), -1);

	int spTroneId =  StringUtil.getInteger(request.getParameter("sptroneid"), -1);
	
	String startDate  = StringUtil.getString(request.getParameter("startdate"),"");
	String endDate = StringUtil.getString(request.getParameter("enddate"),"");
	float rate = StringUtil.getFloat(request.getParameter("rate"), 0.0F);
	
	String remark = StringUtil.getString(request.getParameter("remark"),"");
	
	int id = StringUtil.getInteger(request.getParameter("id"), -1);

	if(type==1)
	{
		out.print(new SpTroneRateServer().isRateDateCross(spTroneId, startDate, endDate) ? "NO" : "OK");
		return;
	}
	else if(type==2)
	{
		if(id>0)
		{
			SpTroneRateServer server = new SpTroneRateServer();
			SpTroneRateModel model = server.loadSpTroneRateById(id);
			server.delSpTroneRate(model);
			response.sendRedirect("sptronerate.jsp");
		}
	}
	else
	{
		SpTroneRateModel model = new SpTroneRateModel();
		model.setId(id);
		model.setSpTroneId(spTroneId);
		model.setStartDate(startDate);
		model.setEndDate(endDate);
		model.setRemark(remark);
		model.setRate(rate);
		
		if(id<0)
		{
			new SpTroneRateServer().addSpTroneRate(model);
		}
		else
		{
			new SpTroneRateServer().updateSpTroneRate(model);
		}
		
		response.sendRedirect("sptronerate.jsp?" + Base64UTF.decode(query));
	}
%>
