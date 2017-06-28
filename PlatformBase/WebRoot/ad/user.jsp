<%@page import="com.system.server.SystemUserServer"%>
<%@page import="com.system.model.SystemUserModel"%>
<%@page import="com.system.server.GroupRightServer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	int groupId = StringUtil.getInteger(request.getParameter("group_id"), -1);
	
	String userName = StringUtil.getString(request.getParameter("username"), "");
	
	String nickName = StringUtil.getString(request.getParameter("nickname"), "");
	
	int userId =  ((UserModel)session.getAttribute("user")).getId();
	
	userId =77;
	
	//Map<String,Object> map = new SystemUserServer().load(userId);
	
	//String nickName = new String(StringUtil.getString(request.getParameter("nickname"), "").getBytes("utf-8"),"utf-8");
	
	String str = new SystemUserServer().loadUserGroup(userId);
	String[] strArray = str.split(",");
	List<GroupModel> groupList = new ArrayList<GroupModel>();
	GroupModel model2 = null;
	//strList = "";
	for(int i=0;i<strArray.length;i++){
		model2 = new GroupModel();
		model2.setName(new GroupRightServer().loadNameById(StringUtil.getInteger(strArray[i], 0)));
		model2.setId(StringUtil.getInteger(strArray[i], 0));
		groupList.add(model2);
	}
	
	//List<GroupModel> groupList2 = new GroupServer().loadAllGroup();

	//Map<String, Object> map =  new UserServer().loadUser(pageIndex, groupId);
	
	Map<String, Object> map =  new SystemUserServer().load(pageIndex, groupId,userName,nickName,userId);
	
	List<SystemUserModel> list = (List<SystemUserModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String, String> params = new HashMap<String,String>();
	
	params.put("group_id", groupId + "");
	
	params.put("username", userName);
	
	params.put("nickname", nickName);
	
	String pageData = PageUtil.initPageQuery("user.jsp",params,rowCount,pageIndex);
	
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
	function delUser(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "useraction.jsp?type=6&id=" + id;	
		}
	}
	
	$(function()
	{
		$("#input_username").val("<%= userName %>");
		$("#sel_group_id").val("<%= groupId %>");
		$("#input_nickname").val("<%= nickName %>");
	});
	
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="useradd.jsp?userId=<%=userId %>">增  加</a></dd>
			</dl>
			<form action="user.jsp"  method="post" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">角色</dd>
					<dd class="dd04_me">
						<select name="group_id" id="sel_group_id" >
							<option value="-1">全部</option>
							<%
							for(GroupModel group : groupList)
							{
								%>
							<option value="<%= group.getId() %>"><%= group.getName() %></option>	
								<%
							}
							%>
						</select>o
					</dd>
					<dd class="dd01_me">登录名</dd>
					<dd class="dd03_me">
						<input name="username" id="input_username" value="" type="text" style="width: 150px">
					</dd>
					<dd class="dd01_me">昵称</dd>
					<dd class="dd03_me">
						<input name="nickname" id="input_nickname" value="" type="text" style="width: 150px">
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查     询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>登录名</td>
					<td>昵称</td>
					<td>Mail</td>
					<td>QQ</td>
					<td>电话</td>
					<td>状态</td>
					<td>创建者</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SystemUserModel model : list)
					{
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getName()%></td>
					<td><%=model.getNick_name()%></td>
					<td><%=model.getMail()%></td>
					<td><%= model.getQq() %></td>
					<td><%= model.getPhone() %></td>
					<td><%= model.getStatus()==1 ? "正常" : "停用" %></td>
					<td><%= model.getCreate_user() %></td>
					<td>
						<a href="useredit.jsp?id=<%= model.getId() %>">修改</a>
						<a href="#" onclick="delUser(<%= model.getId() %>)">删除</a>
						<a href="usergroup.jsp?id=<%= model.getId() %>">角色分配</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="10" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>