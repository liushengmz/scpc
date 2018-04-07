<%@page import="java.util.Map"%>
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
	int troneId = StringUtil.getInteger(request.getParameter("trone_id"), -1);
	int showType = StringUtil.getInteger(request.getParameter("showType"), 1);
	String[] showName = {"","日期","上游","下游","产品","所属公司"};
	List<Map<String,Object>> list = new WmDataServer().loadWmShowTypeData(startDate, endDate, spId, troneId,cpId, showType);
	List<WmSpModel> spList = new WmSpServer().loadSp();
	List<WmCpModel> cpList = new WmCpServer().loadCp();
	List<WmSpModel> troneList = new WmSpServer().loadTrone();
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
		$("#selTrone").val("<%= troneId %>");
		$("#selShowType").val("<%= showType %>");
	});

</script>
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="summer.jsp" method="get" id="exportform">
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
					<!--
					<dd class="dd01_me">产品</dd>
					<dd class="dd04_me">
						<select name="trone_id" id="selTrone" >
								<option value="-1">全部</option>
								<%
								for(WmSpModel model : troneList)
								{
									%>
								<option value="<%= model.getId() %>"><%= model.getFullName() %></option>
									<%
								}
								%>
						</select>
					</dd>
					-->
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
					<dd class="dd01_me" style="font-weight: bold;">展示方式</dd>
					<dd class="dd04_me">
						<select name="showType" id="selShowType"  >
								<option value="1">日期</option>
								<option value="2">上游</option>
								<option value="4">产品</option> 
								<option value="3">下游</option>
							<!--<option value="5">所属公司</option> -->
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
						<td><%= showName[showType] %></td>
						<td>上游数据量(条)</td>
						<td>上游金额(元)</td>
						<td>渠道数据量(条)</td>
						<td>渠道金额(元)</td>
						<td>利润(元)</td>
					</tr>
				</thead>
				<tbody>
				<%
					int totalDataRows = 0;
					Float totalDataAmount = 0F;
					int showTotalDataRows = 0;
					Float showTotalDataAmount = 0F;
				
					for(Map<String,Object> map : list)
					{
						totalDataRows +=  (Integer)map.get("dataRows");
						totalDataAmount += (Float)map.get("dataAmount");
						showTotalDataRows += (Integer)map.get("showDataRows");
						showTotalDataAmount += (Float)map.get("showDataAmount");
						%>
					<tr>
						<td><%= map.get("showName") %></td>
						<td><%= map.get("dataRows") %></td>
						<td><%= StringUtil.getDecimalFormat((Float)map.get("dataAmount")) %></td>
						<td><%= map.get("showDataRows") %></td>
						<td><%= StringUtil.getDecimalFormat((Float)map.get("showDataAmount")) %></td>
						<td><%= StringUtil.getDecimalFormat((Float)map.get("dataAmount")-(Float)map.get("showDataAmount")) %></td>
					</tr>		
						<%
					}
				%>
				<tr>
						<td>总数</td>
						<td><%= totalDataRows %></td>
						<td><%= StringUtil.getDecimalFormat(totalDataAmount) %></td>
						<td><%= showTotalDataRows %></td>
						<td><%= StringUtil.getDecimalFormat(showTotalDataAmount) %></td>
						<td><%= StringUtil.getDecimalFormat(totalDataAmount-showTotalDataAmount) %></td>
				</tr>
				</tbody>
			</table>
	</div>
</body>
</html>