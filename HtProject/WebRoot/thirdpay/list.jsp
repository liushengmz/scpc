<%@page import="com.system.model.ThirdPayModel"%>
<%@page import="com.system.server.ThridPayServer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getMonthEndDate();
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);
	
	int dataType = StringUtil.getInteger(request.getParameter("data_type"), -1);

	Map<String, Object> map =  new ThridPayServer().getThridPayData(startDate, endDate, dataType);
	
	List<ThirdPayModel> list = (List<ThirdPayModel>)map.get("list");
	
	int dataRows = (Integer)map.get("datarows");
	int showDataRows = (Integer)map.get("showdatarows");
	float amount = (Float)map.get("amount");
	float showAmount = (Float)map.get("showamount");
	
	String[] titles = {"日期","周数","月份","SP","CP","通道","CP业务","省份","城市","SP业务"};
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	
	
	
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="list.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 100px;">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 100px;">
					</dd>
					<dd class="dd01_me">支付类型</dd>
						<dd class="dd04_me">
						<select name="data_type" id="sel_data_type" style="width: 100px;">
							<option value="-1">全部</option>
							<option value="0">支付宝</option>
							<option value="1">银联</option>
							<option value="2">微信</option>
						</select>
					</dd>
				
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
					
					</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>时间</td>
					<td>价格(分)</td>
					<td>订单号</td>
					<td>支付类型</td>
					<td>Ip地址</td>
					<td>发行通道ID</td>
					<td>AppKey</td>
				</tr>
			</thead>
			
			<tbody>		
				<%
					int index = 1;
					for(ThirdPayModel model : list)
					{
						%>
				<tr>
				<td><%= index++ %></td>
					<td><%= model.getTimeId() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getPayChannelOrderId() %></td>
					<td><%= model.getPayChannel() %></td>
					<td><%= model.getIp() %></td>
					<td><%= model.getReleaseChannel() %></td>
					<td><%= model.getAppKey() %></td>
				</tr>
						<%
					}
				%>
		</table>
	</div>
	
</body>
</html>