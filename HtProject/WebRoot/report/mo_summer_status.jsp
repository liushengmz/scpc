<%@page import="com.system.server.MoStatusSummerServer"%>
<%@page import="com.system.model.MoStatusSummerModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getDefaultDate() + " 00:00:00");
	String endDate = StringUtil.getString(request.getParameter("enddate"),  StringUtil.getDefaultDate() + " 23:59:59");
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	boolean isFirstLoad = StringUtil.getInteger(request.getParameter("first_load"), 0)  == 1 ? false : true;
	List<MoStatusSummerModel> list = isFirstLoad ? new ArrayList<MoStatusSummerModel>() : new MoStatusSummerServer().loadMoStatusSummer(startDate, endDate, keyWord);
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

	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mo_summer_status.jsp" method="get">
				<input type="hidden" name="first_load" value="1" />
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%= startDate %>"
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%= endDate %>"
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})">
					</dd>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input name="keyword" id="input_keyword" value="<%= keyWord %>" type="text" style="width: 100px">
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
			<table cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td>SP</td>
						<td>业务</td>
						<td>通道</td>
						<td>价格</td>
						<td>配置指令</td>
						<td>通道号</td>
						<td>总指令数</td>
						<td>CP</td>
						<td>CP指令</td>
						<td>CP指令数</td>
						<td>CP指令比例</td>
						<td>状态</td>
						<td>状态数</td>
						<td>状态比例</td>
					</tr>
				</thead>
				<tbody>
				<%
					//这个展示是特殊的，不能和普通的来比
					for(MoStatusSummerModel model : list)
					{
						out.print("<tr>");
						
						for(MoStatusSummerModel.TroneOrderDataModel troneOrderModel : model.troneOrderList)
						{
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.spName + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.spTroneName + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.troneName + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.price + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.troneConfigOrders + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.troneNum + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + model.troneIdRows + "</td>");
							
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + troneOrderModel.cpName + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + troneOrderModel.troneOrders + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" + troneOrderModel.troneOrderIdRows + "</td>");
							out.print("<td rowspan=\"" + troneOrderModel.troneOrderRowSpand + "\">" 
								+ StringUtil.getPercent(troneOrderModel.troneOrderIdRows, model.troneIdRows) + "</td>");
							
							for(MoStatusSummerModel.TroneOrderDataModel.TroneOrderStatusDataModel troneOrderStatusModel : troneOrderModel.troneOrderStatusList)
							{
								out.print("<td>" + troneOrderStatusModel.status + "</td>");
								out.print("<td>" + troneOrderStatusModel.statusRows + "</td>");
								out.print("<td>" + StringUtil.getPercent(troneOrderStatusModel.statusRows, troneOrderModel.troneOrderIdRows) + "</td>");
								out.print("</tr>");
								out.println("<tr>");
							}
						}
						out.println("</tr>");
					}
				%>
				</tbody>
			</table>

	</div>
</body>
</html>