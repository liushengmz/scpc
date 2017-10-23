<%@page import="com.system.model.ReportMoSummer"%>
<%@page import="com.system.server.MoServer"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.vo.DetailDataVo"%>
<%@page import="com.system.server.MrDetailServer"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="java.util.List"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getDefaultDate() + " 00:00:00";

	String defaultEndDate = StringUtil.getDefaultDate() + " 23:59:59";
	
	boolean isFirstLoad = StringUtil.getInteger(request.getParameter("first_load"), 0)  == 1 ? true : false;
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);
	
	String keyWord  = StringUtil.getString(request.getParameter("keyword"), "");
	
	List<ReportMoSummer> list = isFirstLoad ? new MoServer().loadMoSummer(startDate, endDate,keyWord) : new ArrayList<ReportMoSummer>();
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
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mo_summer.jsp"  method="post" style="margin-top: 10px" >
				<input type="hidden" name="first_load" value="1" />
				<dl>
					<dd class="dd01_me" style="margin-left:-15px">开始时间</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 120px;">
					</dd>
					<dd class="dd01_me">结束时间</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 120px;">
					</dd>
					<dd class="dd01_me">关键字</dd>
						<dd class="dd03_me">
						<input type="text" value="<%= keyWord %>" name="keyword" />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit"  />
					</dd>
					</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>CP</td>
					<td>SP</td>
					<td>SP业务</td>
					<td>通道</td>
					<td>价格</td>
					<td>配置指令</td>
					<td>配置通道</td>
					<td>上量指令</td>
					<td>MO数据</td>
					<td>MR数据</td>
					<td>MR同步数据</td>
					<td>MO转化率</td>
					<td>下游MO转化率</td>
				</tr>
			</thead>
			<tbody>		
			<tbody>
				<%
					int index = 1;
					if(list!=null)
					for(ReportMoSummer model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getSpName() %></td>
					<td><%= model.getSpTroneName() %></td>
					<td><%= model.getTroneName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getTroneOrders() %></td>
					<td><%= model.getTroneNum() %></td>
					<td><%= model.getCpTroneOrders() %></td>
					<td><%= model.getMoDataRows() %></td>
					<td><%= model.getMrDataRows() %></td>
					<td><%= model.getSynMrDataRows() %></td>
					<td><%= StringUtil.getPercent(model.getMrDataRows(),model.getMoDataRows()) %></td>
					<td><%= StringUtil.getPercent(model.getSynMrDataRows(),model.getMoDataRows()) %></td>
				</tr>
						<%
					}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>