<%@page import="com.system.model.BaseDataShowModel"%>
<%@page import="com.system.server.BaseDataShowServer"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.constant.Constant"%>
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
	
	int showType = StringUtil.getInteger(request.getParameter("showtype"), 13);
	
	int coId = StringUtil.getInteger(request.getParameter("co_id"), -1);
	
	float amount = 0;

	List<BaseDataShowModel> list =  new BaseDataShowServer().loadAllData(startDate, endDate, coId, showType);
	//1 按日期，2按周，3按月，4按SP，5按CP，6按SP业务线，7按CP业务线，8按SP业务名称，
	//9按CP业务名称，10按SP价格通道，11按CP价格通道，12按省份,13按公司,14按运营商
	String[] titles = {"日期","周数","月份","SP","CP","SP业务线","CP业务线","SP业务名称","CP业务名称","SP价格通道","CP价格通道","省份","公司","运营商"};
	
	out.clear();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>翔通运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePicker.js"></script>
<script type="text/javascript">
	
	$(function(){
		resetForm();
	});
	
	function resetForm()
	{
		$("#sel_showtype").val("<%= showType %>");
		$("#sel_co_id").val("<%= coId %>");
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mr.jsp"  method="get" style="margin-top: 10px">
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
					<dd class="dd01_me">公司</dd>
					<dd class="dd04_me">
						<select name="co_id" id="sel_co_id" style="width: 110px;" title="选择公司" >
							<option value="-1">全部</option>
							<option value="1">浩天</option>
							<option value="2">通联</option>
							<option value="3">游动</option>
							<option value="4">讯宇</option>
						</select>
					</dd>
					<dd class="dd01_me" style="font-weight: bold;font-size: 14px">展示方式</dd>
					<dd class="dd04_me">
						<select name="showtype" id="sel_showtype" title="展示方式" style="width: 110px;" >
							<option value="1">日期</option>
							<option value="2">周数</option>
							<option value="3">月份</option>
							<option value="12">省份</option>
							<option value="13">公司</option>
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
					<td><%= titles[showType-1] %></td>
					<td>金额(元)</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(BaseDataShowModel model : list)
					{
						amount += model.getAmount();
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getTitle() %></td>
					<td><%= StringUtil.getDecimalFormat(model.getAmount()) %></td>
				</tr>
						<%
					}
				%>
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>总金额(元)：<%= StringUtil.getDecimalFormat(amount) %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>