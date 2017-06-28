<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.UserModel" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserModel user = (UserModel)session.getAttribute("user");
	int userId=-1;
	if(user!=null){
		userId=user.getId();
	}
	int flag=new CpServer().checkAdd(userId);
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	Map<String, Object> map =  new CpServer().loadCp(pageIndex,keyWord,userId);
		
	List<CpModel> list = (List<CpModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("cp.jsp",params,rowCount,pageIndex);
	
	String query = Base64UTF.encode(request.getQueryString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript">
	
	function delTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "troneaction.jsp?did=" + id;	
		}
	}
	function checkCommUser(commUserId){
		var userId='<%=userId%>';

		if(commUserId==userId){
			return true;
		}else{
			alert("你没有权限操作！");
			return false;
		}
		
	}
	function checkAdd(){
		var flag='<%=flag%>';
		if(flag==1){
			return true;
		}else{
			alert("你没有权限增加SP!");
			return false;
		}
	}
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="cpadd.jsp" onclick="return checkAdd()">增  加</a></dd>
				<form action="cp.jsp"  method="get" id="formid">
				<dl>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input name="keyword" id="input_keyword" value="<%= keyWord %>" type="text" style="width: 150px">
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
					<td>CPID</td>
					<td>全称</td>
					<td>简称</td>
					<td>联系人</td>
					<td>QQ</td>
					<td>电话</td>
					<td>邮箱</td>
					<td>商务</td>
					<td>登录名</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CpModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%= model.getId() + 2000 %></td>
					<td><%=model.getFullName()%></td>
					<td><%=model.getShortName()%></td>
					<td><%=model.getContactPerson()%></td>
					<td><%=model.getQq()%></td>
					<td><%=model.getPhone() %></td>
					<td><%=model.getMail() %></td>
					<td><%= model.getCommerceUserName() %></td>
					<td><%=model.getUserName() %></td>
					<td>
						<a href="cpedit.jsp?id=<%= model.getId() %>&query=<%= query %>" onclick="return checkCommUser('<%=model.getCommerceUserId()%>')">修改</a>
						<a href="cpaccount.jsp?id=<%= model.getId() %>&query=<%= query %>" onclick="return checkCommUser('<%=model.getCommerceUserId()%>')">用户分配</a>
					</td>
				</tr>
				<%
					}
				%>
			</tbody>	
			<tbody>
				<tr>
					<td colspan="12" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>