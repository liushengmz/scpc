<%@page import="com.system.sdk.server.SdkSpTroneServer"%>
<%@page import="com.system.sdk.model.SdkSpTroneModel"%>
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
	Integer spId = StringUtil.getInteger(request.getParameter("sp_id"),0);
	String troneName = StringUtil.getString(request.getParameter("trone_name"), "");
	Integer spTroneId = StringUtil.getInteger(request.getParameter("spTroneId"),0);
	Integer operatorId = StringUtil.getInteger(request.getParameter("operatorId"),0);

	SdkSpTroneModel model = new SdkSpTroneModel();
	model.setId(id);
	model.setSpId(spId);
	model.setSpTroneId(spTroneId);
	model.setOperatorId(operatorId);
	model.setName(troneName);

	if(id>0)
	{
		new SdkSpTroneServer().updateSdkSpTrone(model);
	}
	else
	{
		new SdkSpTroneServer().addSdkSpTrone(model);
	}
	response.sendRedirect("sdksptrone.jsp?"+ Base64UTF.decode(query));

}
if(type==3){
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	if(id>0){
		new SdkSpTroneServer().deleteSdkSpTrone(id);
	}
	response.sendRedirect("sdksptrone.jsp?"+ Base64UTF.decode(query));
}
	
%>
