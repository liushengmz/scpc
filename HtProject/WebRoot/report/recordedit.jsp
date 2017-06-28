<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.server.analy.MrSummerRecordServer"%>
<%@page import="com.system.model.analy.MrSummerRecordModel"%>
<%@page import="com.system.server.CpPushUrlServer"%>
<%@page import="com.system.model.CpPushUrlModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.SpApiUrlServer"%>
<%@page import="com.system.model.SpApiUrlModel"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String query = request.getQueryString();
	
	query = PageUtil.queryFilter(query, "mrsummerid","cpmrsummerid");
	
	int mrSummerId = StringUtil.getInteger(request.getParameter("mrsummerid"), -1);
	int cpMrSummerId = StringUtil.getInteger(request.getParameter("cpmrsummerid"),-1);
	MrSummerRecordModel model = new MrSummerRecordServer().loadMrSummerRecordById(mrSummerId, cpMrSummerId);
	if(mrSummerId<0 || cpMrSummerId<0)
	{
		response.sendRedirect("recordadd.jsp?" + query);
		return;
	}
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
	
	var feeDate = "<%= model.getFeeDate() %>";
	var cpId = <%= model.getCpId() %>;
	var spId = <%= model.getSpId() %>;
	var holdPercent = <%= model.getHoldPercent() %>;
	var troneId = <%= model.getTroneId() %>;
	var troneOrderId = <%= model.getTroneOrderId() %>;
	var price = <%= model.getPrice() %>;
	var dataRows = <%= model.getDataRows() %>;
	var showDataRows = <%= model.getShowDataRows() %>;
	var amount = <%= model.getAmount() %>;
	var showAmount = <%= model.getShowAmount() %>;
	
	function dataRowChange()
	{
		var dataRows = $("#input_data_rows").val();
		$("#input_show_data_rows").val(Math.floor(dataRows*(100-holdPercent)/100));
		$("#input_amount").val(dataRows*price);
		$("#input_show_amount").val($("#input_show_data_rows").val()*price);
	}
	
	function showDataRowChange()
	{
		$("#input_show_amount").val($("#input_show_data_rows").val()*price);
	}
	
	function startSubmitData()
	{
		var params = "data_rows=" + dataRows + "&show_data_rows=" + showDataRows  + "&amount=" + amount + "&show_amount=" + showAmount + "&mrsummerid=<%= mrSummerId %>&cpmrsummerid=<%= cpMrSummerId %>";
				
		getAjaxValue("recordaction.jsp?type=4&" + params,onFinishRecordData);
	}
	
	function onFinishRecordData(data)
	{
		if("true"==$.trim(data))
		{
			window.location.href = "mrrecord.jsp?<%= query %>";
		}
		else
		{
			alert("修改失败");	
		}
	}
	
	function subForm() 
	{
		if (!isPositiveInteger($("#input_data_rows").val()))
		{
			$("#input_data_rows").focus();
			alert("请输入数据量");
			return;
		}
		
		dataRows = $("#input_data_rows").val();
		showDataRows = $("#input_show_data_rows").val();
		amount = $("#input_amount").val();
		showAmount = $("#input_show_amount").val();
		
		var intShowDataRows = parseInt(showDataRows);
		
		var floatAmount = parseFloat(amount);
		
		var floatShowAmount = parseFloat(showAmount);

		if(isNaN(intShowDataRows) || intShowDataRows < 0)
		{
			alert("请输入正确的CP数据量");
			$("#input_show_data_rows").focus();
			return;
		}
		
		$("#input_show_data_rows").val(intShowDataRows);
		
		if(isNaN(floatAmount))
		{
			alert("请输入正确的SP金额");
			$("#input_amount").focus();
			return;
		}
		
		if(isNaN(floatShowAmount))
		{
			alert("请输入正确的CP金额");
			$("#input_show_amount").focus();
			return;
		}
		
		$("#input_amount").val(floatAmount);
		$("#input_show_amount").val(floatShowAmount);
		
		startSubmitData();
	}
	
	$(function()
	{
		//输入的数据条数发生变化
		$("#input_data_rows").change(dataRowChange);
		//CP数据条数发生变化
		$("#input_show_data_rows").change(showDataRowChange);
	});
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>修改隔天数据</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="recordaction.jsp" method="post" id="addform">
					
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">日期</dd>
					<dd class="dd04_me" style="margin-left: 20px;"><%= model.getFeeDate() %></dd>	
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd04_me" style="margin-left: 20px;"><%= model.getSpShortName() %></dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me" style="margin-left: 20px;"><%= model.getSpTroneName() %>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">CP业务</dd>
					<dd class="dd04_me" style="margin-left: 20px;"><%= model.getCpShortName() + "-" + model.getTroneName() + "-" + model.getPrice() + "-扣("+ model.getHoldPercent() +"%)" %></dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">数据量</dd>
					<dd class="dd03_me">
						<input type="text" name="data_rows"  id="input_data_rows" value="<%= model.getDataRows() %>" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">金额</dd>
					<dd class="dd03_me">
						<input type="text"  name="amount"  id="input_amount" value="<%= model.getAmount() %>" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP数据量</dd>
					<dd class="dd03_me">
						<input type="text" name="show_data_rows"  id="input_show_data_rows" value="<%= model.getShowDataRows() %>" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP金额</dd>
					<dd class="dd03_me">
						<input type="text" name="show_amount"  id="input_show_amount" value="<%= model.getShowAmount() %>" style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>
				</form>
			</dl>
		</div>

	</div>
</body>
</html>