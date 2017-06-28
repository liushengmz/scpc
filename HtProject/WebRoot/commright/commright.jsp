<%@page import="com.system.model.CommRightModel"%>
<%@page import="com.system.server.CommRightServer"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserModel user = (UserModel)session.getAttribute("user");
	int userId=-1;
	if(user!=null){
		userId=user.getId();
	}
	//int flag=new CommRightServer().checkUser(userId);
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	int type=StringUtil.getInteger(request.getParameter("type"), -1);
	Map<String, Object> map =  new CommRightServer().loadCommRight(pageIndex, keyWord, type);		
	List<CommRightModel> list = (List<CommRightModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("commright.jsp",params,rowCount,pageIndex);
	
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
$(function()
		{	
			$("#type").val('<%=type%>');
		});
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="commrightadd.jsp" >增  加</a></dd>
				<form action="commright.jsp"  method="get" id="formid">
				<dl>
					<dd class="dd01_me">商务类型</dd>
					<dd class="dd04_me">
						<select name="type" id="type" >
						<option value="-1">全部</option>
						<option value="0">SP商务</option>	
						<option value="1">CP商务</option>
						</select>	
							
					</dd>
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
					<td>商务类型</td>
					<td>商务</td>
					<td>授权商务</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CommRightModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getType()==0?"SP商务":"CP商务"%></td>
					<td><%=model.getUserName()%></td>
					<td><%=model.getRightListName()%></td>
					<td>
						<a href="commrightedit.jsp?query=<%= query %>&id=<%= model.getId() %>" onclick="return checkUser('<%=model.getId()%>')">修改</a>
						<a href="action.jsp?act=1&id=<%= model.getId()%>" onclick="if(confirm('确定删除?')==false)return false">删除</a>
					
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="12" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>