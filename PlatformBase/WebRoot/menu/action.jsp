<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.dao.Menu1Dao"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int type = StringUtil.getInteger(request.getParameter("type"), -1);

	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int headId = StringUtil.getInteger(request.getParameter("head_id"), -1);
	String name = StringUtil.getString(request.getParameter("name"), "");
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	
	if(type==1)
	{
		int sid = StringUtil.getInteger(request.getParameter("mid"), -1);
		int value = StringUtil.getInteger(request.getParameter("value"), 0);
		new Menu1Server().updateMenu1Model(sid, value);
		out.println("OK");
		return;
	}
	
	
	Menu1Model model = new Menu1Model();
	model.setId(id);
	model.setMenuHeadId(headId);
	model.setName(name);
	model.setRemark(remark);
	
	
	
	if(id>0)
		new Menu1Server().updateMenu1Model(model);
	else
		new Menu1Server().addMenu1Model(model);
	
	response.sendRedirect("menu1.jsp?pageindex="+pageIndex);
%>
