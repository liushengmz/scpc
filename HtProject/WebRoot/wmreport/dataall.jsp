<%@page import="com.system.server.WmCpServer"%>
<%@page import="com.system.model.WmCpModel"%>
<%@page import="com.system.server.WmSpServer"%>
<%@page import="com.system.model.WmSpModel"%>
<%@page import="com.system.vmodel.WmDataShowModel"%>
<%@page import="com.system.server.WmDataServer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.server.FinalcialSpCpDataServer"%>
<%@page import="com.system.vmodel.FinancialSpCpDataShowModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil.getString(request.getParameter("enddate"), StringUtil.getMonthEndDate());
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	List<WmDataShowModel> list = new WmDataServer().loadWmData(startDate, endDate, spId, cpId, keyWord);
	List<WmSpModel> spList = new WmSpServer().loadSp();
	List<WmCpModel> cpList = new WmCpServer().loadCp();
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
		$("#sel_sp").val("<%= spId %>");
		$("#sel_cp").val("<%= cpId %>");
	});
	
	function showDetail(troneOrderId)
	{
		window.open("data_list_detail.jsp?start_date=<%= startDate %>&end_date=<%= endDate %>&trone_order_id=" + troneOrderId);
	}

</script>
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="dataall.jsp" method="get" id="exportform">
				<dl>
					<dd class="dd01_me" style="margin-left:-12px;">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">上游</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" >
								<option value="-1">全部</option>
								<%
								for(WmSpModel model : spList)
								{
									%>
								<option value="<%= model.getId() %>"><%= model.getFullName() %></option>
									<%
								}
								%>
						</select>
					</dd>
					<dd class="dd01_me">渠道</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp"  >
								<option value="-1">全部</option>
								<%
								for(WmCpModel model : cpList)
								{
									%>
								<option value="<%= model.getId() %>"><%= model.getFullName() %></option>
									<%
								}
								%>
						</select>
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
						<td>上游名称</td>
						<!--
						<td>业务名称</td>
						-->
						<td>产品名称</td>
						<td>产品单价(元)</td>
						<td>上游数据量(条)</td>
						<td>上游金额(元)</td>
						<td>渠道名称</td>
						<td>渠道价格(元)</td>
						<td>渠道数据量(条)</td>
						<td>渠道金额(元)</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody>
				<%
					for(WmDataShowModel model : list)
					{
						out.println("<tr><td rowspan='" + model.spRowSpand + "'>" + model.spName + "</td>");
						
						for(WmDataShowModel.SpTroneModel spTroneModel : model.spTroneList)
						{
							//out.println("<td rowspan='" + spTroneModel.spTroneRowSpand + "'>" + spTroneModel.spTroneName + "</td>");
							
							for(WmDataShowModel.SpTroneModel.TroneModel troneModel : spTroneModel.troneList)
							{
								out.println("<td rowspan='" + troneModel.troneRowSpand + "'>" + troneModel.troneName + "</td>");
								out.println("<td rowspan='" + troneModel.troneRowSpand + "'>" + troneModel.spPrice + "</td>");
								
								for(WmDataShowModel.SpTroneModel.TroneModel.TroneOrderModel troneOrderModel : troneModel.troneOrderList)
								{
									out.print("<td>" + troneOrderModel.dataRows + "</td>");
									out.print("<td>" + StringUtil.getDecimalFormat(troneOrderModel.dataAmount) + "</td>");
									out.print("<td>" + troneOrderModel.cpName + "</td>");
									out.print("<td>" + troneOrderModel.cpPrice + "</td>");
									out.print("<td>" + troneOrderModel.showDataRows + "</td>");
									out.print("<td>" + StringUtil.getDecimalFormat(troneOrderModel.showDataAmount) + "</td>");
									out.print("<td><a style='cursor:pointer;' onclick='showDetail(" + troneOrderModel.troneOrderId + ")'>详细</a></td>");
									out.print("</tr>\r\n");
									out.print("<tr>");
								}
							}
						}
						
						out.println("</tr>");
					}
				%>
				</tbody>
			</table>
	</div>
</body>
</html>