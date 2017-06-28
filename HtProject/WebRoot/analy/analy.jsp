<%@page import="com.system.server.DailyDataServer"%>
<%@page import="com.system.server.DailyAnalyServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String date1 = StringUtil.getString(request.getParameter("date1"), StringUtil.getDefaultDate());
	String date2 = StringUtil.getString(request.getParameter("date2"), StringUtil.getDefaultDate());
	int analyType = StringUtil.getInteger(request.getParameter("analy"), -1);
	String msg = "重新分析数据成功";
	
	if (analyType==1) 
	{
		boolean isSuc = new DailyAnalyServer().analyDailyMr(date1);
		
		if(!isSuc)
			msg = "分析失败，有分析任务进行中";
	}
	else if(analyType==2)
	{
		new DailyDataServer().analyDataToSummer(date2, date2);
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
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	function onSubmit(id)
	{
		var date = document.getElementById("input_date" + id).value;
		if ( confirm("确定要重新分析" + (id==1 ? "本地数据" : "大无线数据平台") + "【" + date + "】这一天的数据吗？") )
		{
			document.getElementById("formid" + id).submit();
		}
	}
	
	$(
		function()
		{
			$("#span_msg_<%= analyType %>").html("<%= msg %>");
		}
	);
	
</script>
<body>
	<div class="main_content">
		<div class="content">

			<form action="analy.jsp" method="post" id="formid1"
				style="border: 1px solid #ccc; padding-bottom: 32px; width: 60%; margin-left: 10px;margin-top: 10px;">
				<input type="hidden" value="1" name="analy" />
				<h1
					style="background: #ccc; margin: 0 0 18px 0; padding: 0 0 0 4px;font-size: 14.5px; line-height: 2em;">本地数据分析</h1>
				<div style='clear: both'></div>

				<dl>

					<dd class="dd01_me">选择日期</dd>
					<dd class="dd03_me">
						<input name="date1" id="input_date1" value="<%=date1%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})"
							type="text" style="width: 80px">
					</dd>
					<dd class="ddbtn"
						style="margin-left: 10px; margin-top: 0px; width: 200px">
						<input class="btn_match" name="search" value="重新分析" type="button"
							onclick="onSubmit(1)" />
						<span id="span_msg_1"></span>
					</dd>
				</dl>
				<div style="clear: both;"></div>
			</form>
			
			<form style="display:none" action="analy.jsp" method="post" id="formid2"
				style="border: 1px solid #ccc; padding-bottom: 32px; width: 60%; margin-left: 10px;margin-top: 10px;">
				<input type="hidden" value="2" name="analy" />
				<h1
					style="background: #ccc; margin: 0 0 18px 0; padding: 0 0 0 4px;font-size: 14.5px; line-height: 2em;">大无线数据平台数据分析</h1>
				<div style='clear: both'></div>

				<dl>
					<dd class="dd01_me">选择日期</dd>
					<dd class="dd03_me">
						<input name="date2" id="input_date2" value="<%=date2%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})"
							type="text" style="width: 80px">
					</dd>
					<dd class="ddbtn"
						style="margin-left: 10px; margin-top: 0px; width: 200px">
						<input class="btn_match" name="search" value="重新分析" type="button"
							onclick="onSubmit(2)">
						<span id="span_msg_2"></span>
					</dd>
				</dl>
				<div style="clear: both;"></div>
			</form>
		</div>
	</div>

</body>
</html>