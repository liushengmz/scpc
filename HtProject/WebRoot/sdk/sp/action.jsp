<%@page import="com.system.sdk.server.SdkSpServer"%>
<%@page import="com.system.sdk.model.SdkSpModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Map;" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int type=StringUtil.getInteger(request.getParameter("type"), -1);
    String query = StringUtil.getString(request.getParameter("query"), "");

if(type==-1){
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String fullName = StringUtil.getString(request.getParameter("full_name"), "");
	String shortName = StringUtil.getString(request.getParameter("short_name"), "");
	String remark = StringUtil.getString(request.getParameter("remark"), "");	
	SdkSpModel model = new SdkSpModel();
	model.setId(id);
	model.setFullName(fullName);
	model.setShortName(shortName);
	model.setRemark(remark);

	if(id>0)
	{
		new SdkSpServer().updateSdkSp(model);
		response.sendRedirect("sdksp.jsp?"+ Base64UTF.decode(query));

	}
	else
	{
		new SdkSpServer().addSdkSp(model);
		response.sendRedirect("sdksp.jsp?"+ Base64UTF.decode(query));

	}
	
}
if(type==3){
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	if(id>0){
		new SdkSpServer().deleteSdkSp(id);
	}
	response.sendRedirect("sdksp.jsp?"+ Base64UTF.decode(query));
}
	
%>
