<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.dao.Menu1Dao"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int menu1Id = StringUtil.getInteger(request.getParameter("menu_1_id"), -1);
	int mid = StringUtil.getInteger(request.getParameter("mid"), -1);
	String name = StringUtil.getString(request.getParameter("name"), "");
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	String url = StringUtil.getString(request.getParameter("url"), "");
	String actionUrl = StringUtil.getString(request.getParameter("action_url"), "");
	int newShowRows = StringUtil.getInteger(request.getParameter("value"), -1);
	String query = StringUtil.getString(request.getParameter("query"), "");
	
	Menu2Model model = new Menu2Model();
	model.setId(id);
	model.setMenu1Id(menu1Id);
	model.setName(name);
	model.setRemark(remark);
	model.setUrl(url);
	model.setActionUrl(actionUrl);
	
	if(type==3)
	{
		new Menu2Server().updateMenu2(mid, newShowRows);
		out.println("OK");
		return;
	}
	if(id>0)
		new Menu2Server().updateMenu2(model);
	else
		new Menu2Server().addMenu2(model);
	
	response.sendRedirect("menu2.jsp?" + Base64UTF.decode(query));
%>
