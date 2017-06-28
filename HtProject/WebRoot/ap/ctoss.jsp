<%@page import="com.system.model.xy.XyUserCpsModel"%>
<%@page import="com.system.server.xy.XyCpsServer"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Map"%>

<%@page import="java.util.List"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getPreDayOfMonth();/* 获取之前一个随机的时间 */
	String defaultEndDate = StringUtil.getDefaultDate();    /* 获得当前系统时间 */
	String appKey = StringUtil.getString(request.getParameter("appkey"),
			""); /* 获取需查询的应用KEY */
	String channelKey = StringUtil
			.getString(request.getParameter("channelkey"), ""); /* 获取渠道ID */
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);  /* 获取用户输入开始时间 */
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);  /* 获取用户输入的结束时间 */
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
			
    Map<String, Object> map = 
    		new XyCpsServer().loadUserData(startDate,endDate, appKey, channelKey,pageIndex);
    
    List<XyUserCpsModel> list = (List<XyUserCpsModel>)map.get("list");
    
    int rowCount = (Integer)map.get("row");
    
    Map<String,String> params = new HashMap<String,String>();
	
	params.put("appkey", appKey);
	params.put("channelkey", channelKey);
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	
	String pageData = PageUtil.initPageQuery("ctoss.jsp",params,rowCount,pageIndex);
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
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<form action="user.jsp"  method="get">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">应用包KEY</dd>
					<dd class="dd03_me">
						<input name="appkey" value="<%=appKey%>" type="text">
					</dd>
					<dd class="dd01_me">渠道ID</dd>
					<dd class="dd03_me">
						<input name="channelkey" value="<%=channelKey%>" type="text">
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
					<td>计费日期</td>
					<td>应用Key</td>
					<td class="or">渠道ID</td>
					<td>游戏名</td>
					<td class="or">激活数</td>
					<td class="or">费用</td>
					<td class="or">显示费用</td>
					<td class="dd00">状态</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (XyUserCpsModel model : list)
					{
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td><!-- 序号 -->
					<td>
						<%=model.getFee_date()%>
						<input type="hidden" id="hid_<%= model.getId() %>" " /><!-- 计费日期 -->
					</td>
					<td><%=model.getAppKey()%></td><!-- APPKEY -->
					<td class="or"><%=model.getChannelKey()%></td><!-- 渠道ID -->
					<td class="or"><%=model.getAppName()%></td><!-- APP名字 -->
					<td>
						<%=model.getDataRows()%><!-- 序号 -->
						<input type="hidden" id="ori_hid_<%= model.getId() %>" value="<%=model.getDataRows()%>" />
					</td>
					<td><%=model.getAmount() %></td>
					<td><%=model.getShowAmount() %></td>
					
					<td><%= model.getStatus()==1 ? "已同步":"未同步" %></td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>实际总数:<%= map.get("data_rows") %></td>
					<td>
						<span>同步总数:<label id="show_data_rows_label"><%= map.get("show_data_rows") %></label></span>
					</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="8" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
  </body>
</html>