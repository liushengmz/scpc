<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String name = StringUtil.getString(request.getParameter("name"), "");
	
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	//Map<String, Object> map =  new GroupServer().loadGroup(pageIndex);
	
	Map<String, Object> map =  new GroupServer().loadGroup(pageIndex,name);
		
	List<GroupModel> list = (List<GroupModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	String pageData = PageUtil.initPageQuery("groupuser.jsp",null,rowCount,pageIndex);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function delSpTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "sptroneaction.jsp?did=" + id;	
		}
	}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<form action="groupuser.jsp"  method="post" id="formid">
				<dl>
					<dd class="dd01_me">名称</dd>
					<dd class="dd03_me">
						<input name="name" id="name" value="<%=name %>" type="text" style="width: 150px">
					</dd>
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
					<td>名称</td>
					<td>备注</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (GroupModel model : list)
					{
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td>
						<%= model.getName() %>
					</td>
					<td><%= model.getRemark() %></td>
					<td>
						
						<a href="groupuseredit.jsp?id=<%= model.getId() %>&pageindex=<%=pageIndex %>&encodeStr=<%=name%>&type=2">角色用户管理</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="4" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>