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
	int userId = ((UserModel)session.getAttribute("user")).getId();

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	int spStatus=StringUtil.getInteger(request.getParameter("status"), -1);

	Map<String, Object> map =  new SpServer().loadSp(userId,pageIndex,spStatus,keyWord);
		
	List<SpModel> list = (List<SpModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("sp.jsp",params,rowCount,pageIndex);
	
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
	$(function()
			{
				$("#status").val(<%= spStatus %>);
			
			});
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<form action="sp.jsp"  method="get" id="formid">
				<dl>
					<dd class="dd01_me" style="margin-left:-35px;">关键字</dd>
					<dd class="dd03_me">
						<input name="keyword" id="input_keyword" value="<%= keyWord %>" type="text" style="width: 150px">
					</dd>
					
					<dd class="dd01_me">状态</dd>
						<dd class="dd04_me">
						<select name="status" id="status" style="width: 100px;">
							<option value="-1">全部</option>
							<option value="1">正常</option>
							<option value="0">关闭</option>
						</select>
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
					<td>SPID</td>
					<td>全称</td>
					<td>简称</td>
					<td>联系人</td>
					<td>商务</td>
					<td>QQ</td>
					<td>电话</td>
					<td>邮箱</td>
					<td>合同起始日</td>
					<td>合同结束日</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (SpModel model : list)
					{
				%>
				<tr <%= model.getStatus() == 0 ? stopStyle : "" %>>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%= model.getId() + 1000 %></td>
					<td><%=model.getFullName()%></td>
					<td><%=model.getShortName()%></td>
					<td><%=model.getContactPerson()%></td>
					<td><%=model.getCommerceUserName() %></td>
					<td><%=model.getQq()%></td>
					<td><%=model.getPhone() %></td>
					<td><%=model.getMail() %></td>
					<td><%=model.getContractStartDate() %></td>
					<td><%=model.getContractEndDate() %></td>
					
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