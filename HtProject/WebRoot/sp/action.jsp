<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String fullName = StringUtil.getString(request.getParameter("full_name"), "");
	String shortName = StringUtil.getString(request.getParameter("short_name"), "");
	String contractPerson = StringUtil.getString(request.getParameter("contract_person"), "");
	String qq = StringUtil.getString(request.getParameter("qq"), "");
	String email = StringUtil.getString(request.getParameter("email"), "");
	String phone = StringUtil.getString(request.getParameter("phone"), "");
	String address = StringUtil.getString(request.getParameter("address"), "");
	String contractStartDate = StringUtil.getString(request.getParameter("contract_start_date"), StringUtil.getDefaultDate());
	String contractEndDate = StringUtil.getString(request.getParameter("contract_end_date"), StringUtil.getDefaultDate());
	int commerceUserId = StringUtil.getInteger(request.getParameter("commerce_user_id"), -1);
	int status = StringUtil.getInteger(request.getParameter("status"), 1);

	
	String query = StringUtil.getString(request.getParameter("query"), "");
	
	SpModel model = new SpModel();
	model.setId(id);
	model.setFullName(fullName);
	model.setShortName(shortName);
	model.setContactPerson(contractPerson);
	model.setQq(qq);
	model.setMail(email);
	model.setPhone(phone);
	model.setAddress(address);
	model.setContractStartDate(contractStartDate);
	model.setContractEndDate(contractEndDate);
	model.setCommerceUserId(commerceUserId);
	model.setStatus(status);
	
	if(id>0)
	{
		new SpServer().updateSp(model);
	}
	else
	{
		new SpServer().addSp(model);
	}
	response.sendRedirect("sp.jsp?" + Base64UTF.decode(query));
	//response.sendRedirect(request.getContextPath() + "sp.jsp?"+ Base64UTF.decode(query));
	
%>
