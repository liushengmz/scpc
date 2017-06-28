<%@page import="com.system.sdk.server.SdkSpTroneServer"%>
<%@page import="com.system.sdk.model.SdkSpTroneModel"%>
<%@page import="com.system.sdk.server.SdkSpServer"%>
<%@page import="com.system.sdk.model.SdkSpModel"%>
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
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	Map<String, Object> map =new SdkSpTroneServer().loadSdkSpTrone(pageIndex, keyWord);
		
	List<SdkSpTroneModel> list = (List<SdkSpTroneModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("sdksptrone.jsp",params,rowCount,pageIndex);
	
	String query = Base64UTF.encode(request.getQueryString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript">
	
	function delTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "troneaction.jsp?did=" + id;	
		}
	}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<form action="sdksptrone.jsp"  method="get" id="formid">
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
					<td>SP全称</td>
					<td>SP简称</td>
					<td>SP业务</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SdkSpTroneModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getFullName()%></td>
					<td><%=model.getShortName()%></td>
					<td><%=model.getName()%></td>
					<td>
						<a href="sdksptroneedit.jsp?query=<%= query %>&id=<%= model.getId() %>">修改</a>
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