<%@page import="com.system.server.SystemUserServer"%>
<%@page import="com.system.dao.SystemUserDao"%>
<%@page import="com.system.model.SystemUserModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);

	int id = StringUtil.getInteger(request.getParameter("id"), -1);

	int userId = StringUtil.getInteger(request.getParameter("userid"), -1);
	
	String name = StringUtil.getString(request.getParameter("login_name"), "");
	
	String pwd = StringUtil.getString(request.getParameter("pwd"), "");
	
	String nickName = StringUtil.getString(request.getParameter("nick_name"), "");
	
	String mail = StringUtil.getString(request.getParameter("mail"), "");
	
	String qq = StringUtil.getString(request.getParameter("qq"), "");
	
	String phone = StringUtil.getString(request.getParameter("phone"), "");
	
	int status = StringUtil.getInteger(request.getParameter("status"), 1);
	
	SystemUserModel model = new SystemUserModel();
	model.setName(name);
	model.setPwd(pwd);
	model.setNick_name(nickName);
	model.setMail(mail);
	model.setQq(qq);
	model.setPhone(phone);
	model.setStatus(status);
	
	if(id>0){
		model.setId(id);
		new SystemUserServer().updataUser(model);
	}else{
		model.setUserid(userId);
		new SystemUserServer().addUser(model);
	}
	
	if(type>0){
    	if(id>0){
    		new SystemUserServer().deleteUser(id);
    		response.sendRedirect("user.jsp");
    		return;
    	}
    	response.sendRedirect("user.jsp");
    }
	
	response.sendRedirect("user.jsp");
%>
