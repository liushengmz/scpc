<%@page import="com.system.flow.server.BasePriceServer"%>
<%@page import="com.system.flow.model.BasePriceModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	List<BasePriceModel> list = new BasePriceServer().loadBasePrice(); 

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>

<body>
	<div class="main_content">
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>运营商</td>
					<td>名称</td>
					<td>大小</td>
					<td>单价（分）</td>
				</tr>
			</thead>
			<tbody>
				<%
					for (int i=0; i<list.size(); i++)
					{
						 BasePriceModel model = list.get(i);
				%>
				<tr>
					<td><%= i+1 %></td>
					<td><%=model.getOperatorName()%></td>
					<td><%= model.getName() %></td>
					<td><%=model.getNum() %></td>
					<td><%=model.getPrice()%></td>
				</tr>
				<%
					}
				%>
			</tbody>	
		</table>
	</div>
	
</body>
</html>