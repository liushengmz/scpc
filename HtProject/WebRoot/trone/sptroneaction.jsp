<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String query = Base64UTF.decode(StringUtil.getString(request.getParameter("query"), ""));
	query = PageUtil.queryFilter(query, "id");
	int delId = StringUtil.getInteger(request.getParameter("did"), -1);
	if(delId>0)
	{
		new SpTroneServer().delSpTrone(delId);
		response.sendRedirect("sptrone.jsp");
		return;
	}

	int serviceCodeId = StringUtil.getInteger(request.getParameter("service_code"), -1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int spId = StringUtil.getInteger(request.getParameter("sp_id_1"), -1);
	int operator = StringUtil.getInteger(request.getParameter("operator"), -1);	
	String spTroneName = StringUtil.getString(request.getParameter("sp_trone_name_1"), "");
	float jieSuanLv = StringUtil.getFloat(request.getParameter("jiesuanlv"), 0.0F);
	String provinces = StringUtil.mergerStrings(request.getParameterValues("area[]"), ",");
	int troneType = StringUtil.getInteger(request.getParameter("trone_type"), 0);
	int troneApiId = StringUtil.getInteger(request.getParameter("sp_trone_api"), 0);
	int status = StringUtil.getInteger(request.getParameter("status"), 0);
	int jsType = StringUtil.getInteger(request.getParameter("js_type"), 0);
	
	
	float dayLimit = StringUtil.getFloat(request.getParameter("day_limit"), 0.0F);
	float monthLimit = StringUtil.getFloat(request.getParameter("month_limit"), 0.0F);
	float userDayLimit = StringUtil.getFloat(request.getParameter("user_day_limit"), 0.0F);
	float userMonthLimit = StringUtil.getFloat(request.getParameter("user_month_limit"), 0.0F);
	
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	//增加的4个字段
	int apiStatus=StringUtil.getInteger(request.getParameter("api_status"), 0);
	String shieldStart=StringUtil.getString(request.getParameter("shield_start"), "");
	String shieldEnd=StringUtil.getString(request.getParameter("shield_end"), "");
	String remark=StringUtil.getString(request.getParameter("remark"), "");
	//增加的限量类型和上量类型
	int upDataType=StringUtil.getInteger(request.getParameter("up_data_type"), -1);
	int limiteType=StringUtil.getInteger(request.getParameter("limit_type"), -1);
	
	int isUnHoldData = StringUtil.getInteger(request.getParameter("is_unhold_data"), 0);
	int isForceHold = StringUtil.getInteger(request.getParameter("is_force_hold"), 0);
	int isWatchData = StringUtil.getInteger(request.getParameter("is_watch_data"), 0);
	
	int alarmStartHour = StringUtil.getInteger(request.getParameter("alarm_start_hour"), 0);
	int alarmEndHour = StringUtil.getInteger(request.getParameter("alarm_end_hour"), 0);
	
	//更新SP业务默认结算率
	if(type==1)
	{
		out.println(new SpTroneServer().updateSpTroneRate(id, jieSuanLv) ? "OK" : "NO");
		return;
	}
	
	SpTroneModel model = new SpTroneModel();
	model.setSpId(spId);
	model.setOperator(operator);
	model.setSpTroneName(spTroneName);
	model.setJieSuanLv(jieSuanLv);
	model.setProvinces(provinces);
	model.setId(id);
	model.setTroneType(troneType);
	model.setTroneApiId(troneApiId);
	model.setStatus(status);
	model.setDayLimit(dayLimit);
	model.setMonthLimit(monthLimit);
	model.setUserDayLimit(userDayLimit);
	model.setUserMonthLimit(userMonthLimit);
	model.setServiceCodeId(serviceCodeId);
	model.setJsTypes(jsType);
	//增加的4个字段
	model.setApiStatus(apiStatus);
	model.setShieldStart(shieldStart);
	model.setShieldEnd(shieldEnd);
	model.setRemark(remark);
	//新增的限量类型和上量类型
	model.setUpDataType(upDataType);
	model.setLimiteType(limiteType);
	model.setIsUnHoldData(isUnHoldData);
	model.setIsForceHold(isForceHold);
	
	model.setIsWatchData(isWatchData);
	model.setAlarmStartHour(alarmStartHour);
	model.setAlarmEndHour(alarmEndHour);
	
	if(id==-1)
		new SpTroneServer().addSpTrone(model); 
	else 
		new SpTroneServer().updateSpTrone(model);
	
	response.sendRedirect("sptrone.jsp?" + query);
%>
