<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpCpDataServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.FinalcialSpCpDataServer"%>
<%@page import="com.system.vmodel.FinancialSpCpDataShowModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Calendar ca = Calendar.getInstance();
	ca.add(Calendar.DAY_OF_YEAR, -1);
	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getDateFormat(ca.getTime()));
	String endDate = StringUtil.getString(request.getParameter("enddate"), StringUtil.getDateFormat(ca.getTime()));
	int userId = ((UserModel)session.getAttribute("user")).getId();
	userId = -1;
	List<Map<String, Object>> list = new SpCpDataServer().loadSpRateData(startDate, endDate, userId);
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

	function subForm() 
	{
		document.getElementById("exportform").submit();
	}
</script>
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="spdata_all.jsp" method="get" id="exportform">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd00_me"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
				</dl>
			</form>
			</div>
			<br /><br />
			<table cellpadding="0" cellspacing="0" style="margin-top: 15px;">
				<thead>
					<tr>
						<td>全称</td>
						<td>简称</td>
						<td>业务线</td>
						<td>业务名称</td>
						<td>结算比例</td>
						<td>金额</td>
						<td>结算金额</td>
					</tr>
				</thead>
				<tbody>
				<%
					for(Map<String, Object> model : list)
					{
						%>
					<tr>
						<td><%= model.get("spFullName") %></td>
						<td><%= model.get("spName") %></td>
						<td><%= model.get("productLineName") %></td>
						<td><%= model.get("spTroneName") %></td>
						<td><%= StringUtil.getDecimalFormat((Double)model.get("rate")) %></td>
						<td><%= StringUtil.getDecimalFormat((Double)model.get("dataAmount")) %></td>
						<td><%= StringUtil.getDecimalFormat((Double)model.get("rateDataAmount")) %></td>
					</tr>	
						<%
					}
				%>
				</tbody>
			</table>

	</div>
</body>
</html>