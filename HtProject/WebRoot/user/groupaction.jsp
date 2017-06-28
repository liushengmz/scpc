<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);

	if(type==1)
	{
		String[] rights = request.getParameterValues("menu2id");
		List<Integer> list = new ArrayList<Integer>();
		
		if(rights!=null)
		for(String s : rights)
		{
			list.add(StringUtil.getInteger(s, -1));
		}
		
		new GroupServer().updateGroupRight(id, list);
		
		response.sendRedirect("groupright.jsp?msg=1&id=" + id);
		
		return;
	}
	if(type==2){  //更新group_user表
		String[] users = request.getParameterValues("user_chex");
		int pageIndex = StringUtil.getInteger(request.getParameter("pageIndex"), 1);
		List<Integer> list = new ArrayList<Integer>();
		for(String userId : users){
			list.add(StringUtil.getInteger(userId, -1));
		}	
		new GroupServer().updateGroupUser(id, list);
		
		response.sendRedirect("groupuseredit.jsp?pageIndex="+pageIndex+"&msg=1&id=" + id);
		
		return;
		
	}


	
	String groupName = StringUtil.getString(request.getParameter("group_name"), "");
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	String name = StringUtil.getString(request.getParameter("name"), "");
	String query = request.getQueryString();
	//String encodeStr = URLEncoder.encode(name,"GBK"); 
	
	GroupModel model = new GroupModel();
	model.setId(id);
	model.setName(groupName);
	model.setRemark(remark);
	
	if(id>0)
		new GroupServer().updateGroup(model);
	else
		new GroupServer().addGroup(model);
	
	response.sendRedirect("group.jsp?"+query);
	
%>
