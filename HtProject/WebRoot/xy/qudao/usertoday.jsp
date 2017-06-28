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

<!-- 游戏当日激活数据展示 -->	
	
<%
	String appKey = StringUtil.getString(request.getParameter("appkey"), "");

	int appType = StringUtil.getInteger(request.getParameter("app_type"), 0);

	String channelKey = StringUtil
			.getString(request.getParameter("channelkey"), "");
	
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	List<XyUserModel> list =  new UserServer().loadUserTodayData(appKey, channelKey, appType);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function()
		{
			$("#sel_app_type").val("<%= appType %>");
		});
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="usertoday.jsp"  method="get">
				<dl>
					<dd class="dd01_me">应用包KEY</dd>
					<dd class="dd03_me">
						<input name="appkey" value="<%=appKey%>" type="text">
					</dd>
					<dd class="dd01_me">渠道ID</dd>
					<dd class="dd03_me">
						<input name="channelkey" value="<%=channelKey%>" type="text">
					</dd>
					<dd class="dd01_me">应用类型</dd>
					<dd class="dd04_me">
						<select name="app_type" id="sel_app_type" style="width: 150px;" title="应用类型">
							<option value="-1">应用类型</option>
							<option value="1">自营应用</option>
							<option value="2">第三方应用</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit">
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td class="or">游戏名</td>
					<td>应用Key</td>
					<td class="or">渠道ID</td>
					<td>应用类型</td>
					<td>激活数</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					int dataRows = 0;
					for (XyUserModel model : list)
					{
						dataRows += model.getDataRows();
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppName()%></td>
					<td><%=model.getAppKey()%></td>
					<td><%=model.getChannelKey()%></td>
					<td><%= model.getAppType()==1 ? "自营" : "第三方" %></td>
					<td><%=model.getDataRows()%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>总激活数:<%= dataRows %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>