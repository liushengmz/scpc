<%@page import="com.system.server.WmTroneServer"%>
<%@page import="com.system.model.WmTroneModel"%>
<%@page import="com.system.model.WmDataModel"%>
<%@page import="com.system.server.WmDataServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.server.TroneServer"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int userId = ((UserModel)session.getAttribute("user")).getId();
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getMonthEndDate();
	String startDate = StringUtil.getString(request.getParameter("start_date"), defaultStartDate);
	String endDate = StringUtil.getString(request.getParameter("end_date"), defaultEndDate);
	int troneId = StringUtil.getInteger(request.getParameter("trone_id"), -1);
	List<WmDataModel> list = new WmDataServer().loadWmCpDataDetail(userId, troneId, startDate, endDate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据详细</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

	
</script>

<body>
	<div class="main_content">
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>产品名称</td>
					<td>价格(元)</td>
					<td>日期</td>
					<td>数量(条)</td>
					<td>金额(元)</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (WmDataModel model : list)
					{
				%>
				<tr>
					<td><%=  rowNum++ %></td>
					<td><%= model.getTroneName()%></td>
					<td><%= model.getCpPrice() %></td>
					<td><%= model.getMrDate() %></td>
					<td><%=model.getCpDataRows()%></td>
					<td><%= StringUtil.getDecimalFormat(model.getCpDataAmount())  %></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
	
</body>
</html>