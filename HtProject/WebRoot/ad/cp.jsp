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
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getMonthEndDate();

	int userId = ((UserModel)session.getAttribute("user")).getId();
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	
	//Map<String,Object> map = new AdCpServer().loadCpQd(pageIndex, userId);
	Map<String,Object> map = new AdCpServer().loadCpQd(pageIndex, userId,startDate, endDate);
	//Map<String, Object> map =  new FeeServer().loadQdAppFee(startDate, endDate, userId, pageIndex);
	
	List<AdCpModel> list = (List<AdCpModel>)map.get("list");
		
	//List<XyFeeModel> list = (List<XyFeeModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	
	String pageData = PageUtil.initPageQuery("cp.jsp",params,rowCount,pageIndex);
	
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
			<form action="cp.jsp" method="get">
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
					<td>应用名</td>
					<td>用户数</td>
					<td>收入</td>
					<td>推广费用</td>
					<td>收益</td>
				</tr>
			</thead>
			<tbody>
				<%
					for (AdCpModel model : list)
					{
				%>
				<tr>
					<td><%=model.getFeeDate() %></td>
					<td class="or"><%=model.getAppname()%></td>
					<td><%= model.getNewUserRows() %></td>
					<td class="or"><%= model.getAmount() %></td>
					<td class="or"><%= model.getExtendFee() %></td>
					<%
						double profit = model.getAmount()-model.getExtendFee();
						BigDecimal b = new BigDecimal(profit);
						profit = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					%>
					<td class="or"><%= profit %></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>
						<span>总用户数:<%= map.get("userrows") %></span>
					</td>
					<td>
						<span>总收入:<label id="amount"><%= StringUtil.getDecimalFormat((Double)map.get("amount")) %></label></span>
					</td>
					<td>
						<span>总推广费用:<label id="extendfee"><%= StringUtil.getDecimalFormat((Double)map.get("extendfee")) %></label></span>
					</td>
					<td>
						<span>总收益:<label id="extendfee"><%= StringUtil.getDecimalFormat((Double)map.get("profit")) %></label></span>
					</td>
				</tr>
				<tr>
					<td colspan="5" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>