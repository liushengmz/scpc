<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String menuname = StringUtil.getString(request.getParameter("menu_name"), "");
	int sort = StringUtil.getInteger(request.getParameter("sort"), 0);
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	if(type==1)
	{
		int sid = StringUtil.getInteger(request.getParameter("mid"), -1);
		int value = StringUtil.getInteger(request.getParameter("value"), 0);
		new MenuHeadServer().updateMenu(sid, value);
		out.println("OK");
		return;
	}
	
	if(remark.equals(""))
	{
		remark = "";
	}
	
	
	
	MenuHeadModel model = new MenuHeadModel();
	model.setId(id);
	model.setName(menuname);
	model.setSort(sort);
	model.setRemark(remark);
	
	if(id>0)
	{
		new MenuHeadServer(). updataMenu(model);
	}
	else
	{
		new MenuHeadServer().addMenuHead(model);
	}
	
	response.sendRedirect("menu3.jsp?pageindex="+pageIndex);
	
%>
