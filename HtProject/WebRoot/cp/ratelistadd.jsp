<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String title = URLDecoder.decode(request.getParameter("title"),"UTF-8");
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String query = StringUtil.getString(request.getParameter("query"), "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript">

	function subForm() 
	{
		if ($("#input_start_date").val() == "") 
		{
			alert("请输入开始时间");
			$("#input_start_date").focus();
			return;
		}
		
		if ($("#input_end_date").val() == "") 
		{
			alert("请输入结束时间");
			$("#input_end_date").focus();
			return;
		}
		
		if ($("#input_rate").val() == "") 
		{
			alert("请输入结算率");
			$("#input_rate").focus();
			return;
		}
		
		var startDate = $("#input_start_date").val();
		var endDate = $("#input_end_date").val();
		
		if(startDate > endDate)
		{
			alert("开始时间不能小于结束时间");
			return;
		}
		
		var rate = parseFloat($("#input_rate").val());
		if(isNaN(rate) || rate>=1 || rate<=0)
		{
			alert("结算率只能介于0和1之间");
			$("#input_rate").focus();
			return;
		}
		
		ajaxData(<%= id %>,startDate,endDate);
	}
	
	function ajaxData(cpSpTroneId,startDate,endDate)
	{
		getAjaxValue("rateaction.jsp?type=3&cpsptroneid=" + cpSpTroneId + "&startdate=" + startDate + "&enddate=" + endDate,onDataChange);
	}
	
	function onDataChange(result)
	{
		if("OK"==result)
		{
			document.getElementById("addform").submit();
		}
		else
		{
			alert("在同一时间段内已存在其它结算率，请检查");	
		}
	}
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp").val(joData.id);
		spChange();
	}

</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label style="width:300px;display: block">增加[<%= title %>]结算率</label>
				</dd>
			</dl>
			<br />	
			<br />		
			<dl>
				<form action="rateaction.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="2" name="type"/>
					<input type="hidden" value="<%= id %>" name="cpsptroneid"/>
					<br />
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" id="input_start_date"  type="text" value="" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 200px;">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" id="input_end_date" type="text" value="" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 200px;">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算率</dd>
					<dd class="dd03_me">
						<input type="text" name="rate"  id="input_rate" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark"  id="input_remark" style="width: 200px">
					</dd>

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