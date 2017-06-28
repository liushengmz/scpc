<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.model.GroupRightModel"%>
<%@page import="com.system.server.GroupRightServer"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String group = StringUtil.getString(request.getParameter("group"), "");
    
	Map<String,Object> map = new GroupRightServer().load(pageIndex, group);
	//Map<String,Object> map = new AdAppServer().loadApp(pageIndex, appname, appkey);
	
	String query = Base64UTF.encode(request.getQueryString());
	
	List<GroupRightModel> list = (List<GroupRightModel>)map.get("list");
	List<GroupRightModel> list2 = new ArrayList<GroupRightModel>();
	String[] strArray = null; 
	String strList = "";
	for(GroupRightModel model:list){
		String str = model.getGroupList();
		strArray = str.split(",");
		strList = "";
		for(int i=0;i<strArray.length;i++){
			//DAO将strArray[i]中的值转化为name,并且拼接到字符串strlist上
			//str = strArray[i];
			strList += new GroupRightServer().loadNameById(StringUtil.getInteger(strArray[i], 0))+"&nbsp;| ";
		}
		strList = strList.substring(0, strList.length()-2);
		model.setGroupList(strList);
		list2.add(model);
	}
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("group", group);
	//params.put("appkey", appkey);
	
	String pageData = PageUtil.initPageQuery("group_right.jsp",params,rowCount,pageIndex); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>翔通运营管理平台</title>
    <link href="../wel_data/right.css" rel="stylesheet" type="text/css">
    <link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
    <script type="text/javascript">
	
	  function delTrone(id)
	  {
		  if(confirm('真的要删除吗？'))
		  {
			  window.location.href = "groupRightAction.jsp?id=" + id+"&type=1";	
		  }
	  }
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="groupRightAdd.jsp?pageindex=<%=pageIndex %>">增  加</a></dd>
				<form action="group_right.jsp"  method="post" id="formid">
				<dl>
					<dd class="dd01_me">角色</dd>
					<dd class="dd03_me">
						<input name="group" id="input_appkey" value="<%=group %>" type="text" style="width: 150px">
					</dd>
					<!--  <dd class="dd01_me">应用KEY</dd>
					<dd class="dd03_me">
						<input name="appkey" id="input_appkey" value="" type="text" style="width: 150px">
					</dd>-->
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" >
					</dd>
				</dl>
			</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>角色</td>
					<td>授权角色</td>
					<td>备注</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (GroupRightModel model : list2)
					
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getName() %></td>
					<td><%=model.getGroupList() %></td>
					<td><%=model.getRemark() %></td>
					
					<td>
						<a href="groupRightEdit.jsp?id=<%= model.getId() %>&query=<%= query %>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="6" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>