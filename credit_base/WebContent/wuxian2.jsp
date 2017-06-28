<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.Base64"%>
<%@page import="com.system.model.ApiOrderModel"%>
<%@page import="com.system.server.RequestServerV1"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.BaseRequestModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	long curMils = System.currentTimeMillis();

	ApiOrderModel model = new ApiOrderModel();
	
	model.setTroneOrderId(StringUtil.getInteger(request.getParameter("feecode"), 0)); //paycode
	model.setExtrData(StringUtil.getString(request.getParameter("cpparams"), "")); //params
	model.setImei(StringUtil.getString(request.getParameter("imei"), ""));
	model.setImsi(StringUtil.getString(request.getParameter("imsi"),""));
	model.setMobile(StringUtil.getString(request.getParameter("phone"), ""));//phone
	model.setPackageName(StringUtil.getString(request.getParameter("package"), ""));
	model.setSdkVersion(StringUtil.getString(request.getParameter("androidversion"),""));
	model.setNetType(StringUtil.getString(request.getParameter("nettype"), ""));
	model.setIp(StringUtil.getString(request.getHeader("X-Real-IP"), ""));
	
	if(StringUtil.isNullOrEmpty(model.getIp()))
	{
		model.setIp(request.getRemoteAddr());
	}
	
	model.setClientIp(StringUtil.getString(request.getParameter("clientip"), request.getRemoteAddr()));
	model.setLac(StringUtil.getInteger(request.getParameter("lac"), 0));
	model.setCid(StringUtil.getInteger(request.getParameter("cid"), 0));
	model.setIccid(StringUtil.getString(request.getParameter("iccid"), ""));
	model.setUserAgent(StringUtil.getString(request.getParameter("useragent"), ""));
	model.setExtraParams(StringUtil.getString(request.getParameter("extra_params"), ""));
	
	//还原基础数据
	model.setTroneOrderId(model.getTroneOrderId()-100000);
	
	out.clear();
	
	out.print(Base64UTF.encode(new RequestServerV1().handlCpQuery(model)));
	
	System.out.println("SpendTime:" + (System.currentTimeMillis()-curMils));
%>