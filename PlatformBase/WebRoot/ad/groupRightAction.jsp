
<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.Date"%>
<%@page import="javax.xml.crypto.Data"%>
<%@page import="com.system.server.GroupRightServer"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.system.model.GroupRightModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int groupId = StringUtil.getInteger(request.getParameter("group"), -1);
	String groupList = StringUtil.getString(request.getParameter("group_list"), "");
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	String query = StringUtil.getString(request.getParameter("query"), "");
	
	if(type>0){
		new GroupRightServer().deleteGroup(id);
		response.sendRedirect("group_right.jsp");
		return;
	}
	
	GroupRightServer server = new GroupRightServer();
	GroupRightModel model = new GroupRightModel();
	model.setGroupId(groupId);
	model.setRemark(remark);
	
	if(id>0)
	{
		//new AdAppServer().updataApp(model);
		model.setId(id);
		String[] stringList = groupList.split(",");
		List<String> list = new ArrayList<String>();
		String[] groups = request.getParameterValues("groupid");
		for(String grString : groups){
			list.add(grString);
		}
		model.setGroupList(StringUtil.stringListToString(list));
		new GroupRightServer().updateGroup(model);
	}
	else
	{
		List<String> list = new ArrayList<String>();
		String[] groups = request.getParameterValues("groupid");
		for(String grString : groups){
			list.add(grString);
		}
		
		/* String[] stringList ;
		if(groupList.indexOf("，")>0){
			stringList = groupList.split("，");
		}else{
			stringList = groupList.split(",");
		}
		
		for(int i=0;i<stringList.length;i++){
			list.add(server.loadIdByName(stringList[i])+""); 
		}
		for(String str :list){
		}*/
		
		model.setGroupList(StringUtil.stringListToString(list));
		new GroupRightServer().addGroupRight(model);
	}
	
	
	
	response.sendRedirect("group_right.jsp?" + Base64UTF.decode(query));
	
%>
