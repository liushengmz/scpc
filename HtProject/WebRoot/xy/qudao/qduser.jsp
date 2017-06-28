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
	
<!-- 游戏 渠道 CPA 分成 显示数据 -->
	
<%
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getDefaultDate();

	int userId = ((UserModel)session.getAttribute("user")).getId();

	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	Map<String, Object> map =  new UserServer().loadQdUserData(startDate,endDate,userId,pageIndex);
		
	List<XyQdUserModel> list = (List<XyQdUserModel>)map.get("list");
	
	int rowCount = (Integer)map.get("row");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	
	String pageData = PageUtil.initPageQuery("qduser.jsp",params,rowCount,pageIndex);
	
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
			<form action="qduser.jsp" method="get">
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
					<td>激活日期</td>
					<td class="or">渠道号</td>
					<td class="or">游戏名</td>
					<td class="or">用户激活数</td>
				</tr>
			</thead>
			<tbody>
				<%
					for (XyQdUserModel model : list)
					{
				%>
				<tr>
					<td><%=model.getActiveDate()%></td>
					<td class="or"><%=model.getChannelKey()%></td>
					<td class="or"><%=model.getAppName()%></td>
					<td class="or"><%=model.getShowDataRows()%></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>
						<span>用户总数:<label id="show_data_rows_label"><%= map.get("show_data_rows") %></label></span>
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