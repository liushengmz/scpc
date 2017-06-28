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
	String startDate = StringUtil.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil.getString(request.getParameter("enddate"), defaultEndDate);
	int troneId = StringUtil.getInteger(request.getParameter("trone_id"), -1);
	List<WmDataModel> list = new WmDataServer().loadWmCpData(userId, troneId, startDate, endDate);
	List<WmTroneModel> troneList = new WmTroneServer().loadTroneByCpUserId(userId);
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

	$(function()
	{
		$("#sel_trone").val(<%= troneId %>);
	});
	
	function showDetail(troneId)
	{
		window.open("cp_data_list_detail.jsp?start_date=<%= startDate %>&end_date=<%= endDate %>&trone_id=" + troneId);  
	}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<form action="cp_data_list.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me" style="margin-left: -14px;">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>" readonly="readonly"
							onclick="WdatePicker({isShowClear:false,readOnly:true})"
							style="width: 100px;">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"  readonly="readonly"
							onclick="WdatePicker({isShowClear:false,readOnly:true})"
							style="width: 100px;">
					</dd>
					<dd class="dd01_me">产品名称</dd>
					<dd class="dd04_me">
						<select name="trone_id" id="sel_trone" >
							<option value="-1">全部</option>
							<%
							for(WmTroneModel troneModel : troneList)
							{
								%>
							<option value="<%= troneModel.getId() %>"><%= troneModel.getTroneName() %></option>	
								<%
							}
							%>
						</select>
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
					<td>产品名称</td>
					<td>价格(元)</td>
					<td>数量(条)</td>
					<td>金额(元)</td>
					<td>操作</td>
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
					<td><%=model.getTroneName()%></td>
					<td><%=model.getCpPrice() %></td>
					<td><%=model.getCpDataRows()%></td>
					<td><%= StringUtil.getDecimalFormat(model.getCpDataAmount())  %></td>
					<td>
						<a style="cursor: pointer;" onclick="showDetail(<%= model.getTroneId() %>)" >详细</a>
					</td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
	
</body>
</html>