<%@page import="com.system.model.xy.XyFeeModel"%>
<%@page import="com.system.server.xy.FeeServer"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.model.xy.XyQdUserModel"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.server.xy.UserServer"%>
<%@page import="com.system.model.xy.XyUserModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!-- 游戏 CP  CPS 分成 显示界面 -->
	
<%
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getDefaultDate();

	int userId = ((UserModel)session.getAttribute("user")).getId();
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	Map<String, Object> map =  new FeeServer().loadQdAppFee(startDate, endDate, userId, pageIndex);
		
	List<XyFeeModel> list = (List<XyFeeModel>)map.get("list");
	
	int rowCount = (Integer)map.get("row");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	
	String pageData = PageUtil.initPageQuery("qdfee.jsp",params,rowCount,pageIndex);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../My97DatePicker/WdatePicker.js"></script>
<body>
	<div class="main_content">
		<div class="content">
			<form action="qdfee.jsp" method="get">
				<dl>
					<dd class="dd01_me" style="width:80px;">开始日期</dd>
					<dd class="dd03_me" style="width:100px;">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me" style="width:80px;">结束日期</dd>
					<dd class="dd03_me" style="width:100px;">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="ddbtn" style="margin-left: 20px;">
						<input class="btn_match" name="search" value="查 询" type="submit">
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>计费日期</td>
					<td>游戏名</td>
					<td>激活数</td>
					<td>信息费</td>
				</tr>
			</thead>
			<tbody>
				<%
					for (XyFeeModel model : list)
					{
				%>
				<tr>
					<td><%=model.getFeeDate()%></td>
					<td class="or"><%=model.getAppName()%></td>
					<td><%= model.getDataRows() %></td>
					<td class="or"><%= StringUtil.getDecimalFormat((Float)model.getShowAmount()) %></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>
						<span>总激活数:<%= map.get("show_rows") %></span>
					</td>
					<td>
						<span>总信息费:<label id="show_data_rows_label"><%= StringUtil.getDecimalFormat((Float)map.get("show_amounts")) %></label></span>
					</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="3" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>