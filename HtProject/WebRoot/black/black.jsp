<%@page import="com.system.dao.BlackDao"%>
<%@page import="com.system.server.BlackServer"%>
<%@page import="com.system.model.BlackModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	Map<String, Object> map =  new BlackServer().loadBlack(pageIndex, keyWord);
		
	List<BlackModel> list = (List<BlackModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("black.jsp",params,rowCount,pageIndex);
	
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
	

	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="blackadd.jsp">增  加</a></dd>
				<dd class="ddbtn" ><a href="blackaddbatch.jsp">批量增加</a></dd>
				<form action="black.jsp"  method="get" id="formid">
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
					<td>IMEI</td>
					<td>IMSI</td>
					<td>PHONE</td>
					<td>备注</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (BlackModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getImei()%></td>
					<td><%=model.getImsi()%></td>
					<td><%=model.getPhone()%></td>
					<td><%=model.getRemark()%></td>
					<td>
						<a href="blackedit.jsp?id=<%= model.getId() %>&query=<%= query %>">修改</a>
						<a href="action.jsp?type=3&id=<%= model.getId() %>" onclick="if(confirm('确定删除?')==false)return false">删除</a>
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