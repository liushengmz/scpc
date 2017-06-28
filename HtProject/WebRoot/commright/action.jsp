<%@page import="com.system.server.CommRightServer"%>
<%@page import="com.system.model.CommRightModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.UserModel" %>
<%@page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String query = StringUtil.getString(request.getParameter("query"), "");
	int act = StringUtil.getInteger(request.getParameter("act"), -1);
	if(act==1){
		if(id>0){
			new CommRightServer().deleteCommRight(id);
		}
		response.sendRedirect("commright.jsp?"+ Base64UTF.decode(query));
	}else if(act==2){
			int type = StringUtil.getInteger(request.getParameter("type"),0);
			int userId = StringUtil.getInteger(request.getParameter("user_id"),-1);
			int types = StringUtil.getInteger(request.getParameter("type"), -1);		
			Map<String,Integer> map= CommRightServer.checkData(userId,type,id);
			int flag=(Integer)map.get("flag");
			out.clear();
			out.print(flag); 
	}else{
	int type = StringUtil.getInteger(request.getParameter("type"),0);
	int commSpUser = StringUtil.getInteger(request.getParameter("commerce_sp_user_id"), -1);
	int commCpUser = StringUtil.getInteger(request.getParameter("commerce_cp_user_id"), -1);
	String spRightList=StringUtil.mergerStrings(request.getParameterValues("area"), ",");
	String cpRightList=StringUtil.mergerStrings(request.getParameterValues("cparea"), ",");
	String remark=StringUtil.getString(request.getParameter("remark"), "");
	
	CommRightModel model = new CommRightModel();
	model.setId(id);
	model.setType(type);
	if(type==0){
		model.setUserId(commSpUser);
		model.setRightList(spRightList);

	}else{
		model.setUserId(commCpUser);
		model.setRightList(cpRightList);
	}
	model.setRemark(remark);
	
	if(id>0)
	{
		new CommRightServer().updateCommRight(model);
	}
	else
	{
		new CommRightServer().addCommRight(model);
	}
	
	response.sendRedirect("commright.jsp?"+ Base64UTF.decode(query));
	}
%>
