<%@page import="com.system.server.BlackServer"%>
<%@page import="com.system.model.BlackModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
int type=StringUtil.getInteger(request.getParameter("type"), -1);
System.out.println(type);
 if(type==1){//单个增加
	 int id = StringUtil.getInteger(request.getParameter("id"), -1);
		String imei = StringUtil.getString(request.getParameter("imei"), "");
		String imsi = StringUtil.getString(request.getParameter("imsi"), "");
		String phone = StringUtil.getString(request.getParameter("phone"), "");
		String remark = StringUtil.getString(request.getParameter("remark"), "");
		String query = StringUtil.getString(request.getParameter("query"), "");
		
		
		BlackModel model = new BlackModel();
		model.setId(id);
		model.setImei(imei);
		model.setImsi(imsi);
		model.setPhone(phone);
		model.setRemark(remark);
		
		
		
		if(id>0)
		{
			new BlackServer().updateBlack(model);
		}
		else
		{
			new BlackServer().addBlack(model);
		}
		
		response.sendRedirect("black.jsp?" + Base64UTF.decode(query));
	
}else if(type==2){//批量增加
	String plData=StringUtil.getString(request.getParameter("pl_data"),"");
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	int chkType = StringUtil.getInteger(request.getParameter("chktype"), 1);

	String query = StringUtil.getString(request.getParameter("query"), "");
	
	
	BlackModel model = new BlackModel();
	model.setCytype(chkType);
	model.setPlData(plData);
	model.setRemark(remark);
	new BlackServer().addBlack(model,chkType);
	
	response.sendRedirect("black.jsp?" + Base64UTF.decode(query));
	
	
}else if(type==3){//删除单个
	int id = StringUtil.getInteger(request.getParameter("id"), -1);

	String query = StringUtil.getString(request.getParameter("query"), "");
	
	
	
	if(id>0)
	{
		new BlackServer().delete(id);
	}
	response.sendRedirect("black.jsp?" + Base64UTF.decode(query));
	
}else{
	String query = StringUtil.getString(request.getParameter("query"), "");

	response.sendRedirect("black.jsp?" + Base64UTF.decode(query));
}
	
	
%>
