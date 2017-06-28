<%@page import="com.system.model.CpChannelModel"%>
<%@page import="com.system.server.AdChannelServer"%>
<%@page import="com.system.model.AdChannelModel"%>
<%@page import="com.system.server.CpChannelServer"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="com.system.model.AdAppModel"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.system.model.AdCpModel"%>
<%@page import="com.system.server.AdCpServer"%>
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
<%
	int userId = ((UserModel)session.getAttribute("user")).getId();
	Map<String,Object> name = new AdAppServer().loadApp(1);
	String defaultStartDate = StringUtil.getPreDayOfMonth();/* 获取之前一个随机的时间 */
	String defaultEndDate = StringUtil.getMonthEndDate();    /* 获得当前系统时间 */
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int appid = StringUtil.getInteger(request.getParameter("appname"), -1);
	int channelid = StringUtil.getInteger(request.getParameter("channel"), -1);
	String appkey = StringUtil.getString(request.getParameter("appkey"), "");
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);  /* 获取用户输入开始时间 */
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);  /* 获取用户输入的结束时间 */
	String appname = "";
		
		
	if(appid>0)
	{
		AdAppModel appmodel = new AdAppServer().loadAppById(appid);
		appname = appmodel.getAppname();
	}
	
	Map<String,Object> map = new CpChannelServer().loadQdShow(pageIndex, userId, startDate, endDate,"","");
//Map<String,Object> map = new CpChannelServer().loadCpChannel(pageIndex);

//Map<String,Object> map = new CpAppServer().loadCpApp(pageIndex, appname, startDate, endDate);
//Map<String,Object> map = new AdAppServer().loadApp(pageIndex, appname, appkey);
	Map<String,Object> channel = new AdChannelServer().loadAdChannelName();
	List<AdChannelModel> qdlist = (List<AdChannelModel>)channel.get("list");

	List<CpChannelModel> list = (List<CpChannelModel>)map.get("list");

	List<AdAppModel> applist = (List<AdAppModel>)name.get("list");

	int rowCount = (Integer)map.get("rows");

	Map<String,String> params = new HashMap<String,String>();

	appname = appid+"";

	params.put("appname", appname);
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	params.put("channel", channelid+"");

	String pageData = PageUtil.initPageQuery("cpqdshow.jsp",params,rowCount,pageIndex);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<body>
	<div class="main_content">
		<div class="content">
			<form action="cpqdshow_game.jsp" method="get">
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
					<!-- <td>激活数</td> -->
					<td>收入</td>
				</tr>
			</thead>
			<tbody>
				<%
				    double sum = 0 ; 
					for (CpChannelModel model : list)
					{
				%>
				<tr>
					<td><%=model.getFeeDate() %></td>
					<td class="or"><%=model.getShowNewUserRows()%></td>
					<!-- <td></td> -->
					<td class="or"><%= model.getShowAmount() %></td>
					<%
						double num = model.getScale()*100;
						BigDecimal b = new BigDecimal(num);
						num = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
						double amount = model.getShowAmount();
						amount = amount*(model.getScale());
						b = new BigDecimal(amount);
						amount = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
						sum = amount+sum;
						
					%>
					<td><%=num%>%</td>
					<td><%=amount %></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td></td>
					<td>
						<span>总用户数:<%= map.get("sumUserRows") %></span>
					</td>
					<td>
						<span>总收入:<label id="extendfee"><%= StringUtil.getDecimalFormat((Double)map.get("sumAmount")) %></label></span>
					</td>
					<td></td>
					<td>
						<%
							BigDecimal b = new BigDecimal(sum);
							sum = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						%>
						<span>实际总收入:<label id="extendfee"><%=sum  %></label></span>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>