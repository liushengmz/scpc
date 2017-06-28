<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.CityServer"%>
<%@page import="com.system.model.CityModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneServer"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.MrServer"%>
<%@page import="com.system.model.MrReportModel"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.xy.UserServer"%>
<%@page import="com.system.model.xy.XyUserModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getDefaultDate();
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);
	
	int userId = ((UserModel)session.getAttribute("user")).getId();
	
	int showType = StringUtil.getInteger(request.getParameter("show_type"), 0);
	
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone"), -1);

	List<SpTroneModel> spTroneList = new SpTroneServer().loadCpTroneList(userId);

	Map<String, Object> map =  new MrServer().getCpMrShowData(startDate, endDate, userId,spTroneId,showType);
		
	List<MrReportModel> list = (List<MrReportModel>)map.get("list");
	
	int showDataRows = (Integer)map.get("show_data_rows");
	float showAmount = (Float)map.get("show_amount");
	
	String[] titles = {"日期","周数","月份","业务","指令"};
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
<script type="text/javascript">
	$(function()
	{
		$("#sel_sp_trone").val("<%= spTroneId %>");
		$("#sel_show_type").val("<%= showType %>");
	});
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="cpmr.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>" style="width: 110px;"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" style="width: 110px;"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">业务名称</dd>
					<dd class="dd04_me">
						<select name="sp_trone" id="sel_sp_trone" style="width: 110px;">
							<option value="-1">全部</option>
							<%
							for(SpTroneModel spTroneModel : spTroneList)
							{
								%>
							<option value="<%= spTroneModel.getId() %>"><%= spTroneModel.getSpTroneName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">展示方式</dd>
					<dd class="dd04_me">
						<select name="show_type" id="sel_show_type" title="展示方式" style="width: 110px;" >
							<option value="0">日期</option>
							<option value="1">周数</option>
							<option value="2">月份</option>
							<option value="3">业务</option>
							<option value="4">指令</option>
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
					<td><%= titles[showType] %></td>
					<td>数据(条)</td>
					<td>金额(元)</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(MrReportModel model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getTitle1() %></td>
					<td><%= model.getShowDataRows() %></td>
					<td><%= model.getShowAmount() %></td>
				</tr>
						<%
					}
				%>
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>总数据(条)：<%= showDataRows %></td>
					<td>总金额(元)：<%= showAmount %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>