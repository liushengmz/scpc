<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SpTroneRateServer"%>
<%@page import="com.system.model.SpTroneRateModel"%>
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
	String query = StringUtil.getString(request.getParameter("query"), "");
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	SpTroneRateModel model = new SpTroneRateServer().loadSpTroneRateById(id);
	if(model==null)
	{
		response.sendRedirect("sptroneratelist.jsp?" + Base64UTF.decode(query));
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
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript">
	
	function subForm() 
	{
		var rate = parseFloat($("#input_rate").val());
		if(isNaN(rate) || rate>=1 || rate<=0)
		{
			alert("结算率只能介于0和1之间");
			$("#input_rate").focus();
			return;
		}
		document.getElementById("addform").submit();
	}
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label style="width:200px;display: block">增加SP业务结算率</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="sptronerateaction.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" name="id" value="<%= model.getId() %>">
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd03_me">
						<input type="text" name="sp_name"  id="input_sp_name" value="<%= model.getSpName() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd03_me">
						<input type="text" name="sp_trone_name"  id="input_sp_trone_name" value="<%= model.getSpTroneName() %>" readonly="readonly" style="width: 200px;color: #ccc">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" id="input_start_date"  type="text" value="<%= model.getStartDate() %>" readonly="readonly"  style="width: 200px;color: #ccc">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" id="input_end_date" type="text" value="<%= model.getEndDate() %>" readonly="readonly" style="width: 200px;color: #ccc">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算率</dd>
					<dd class="dd03_me">
						<input type="text" name="rate"  id="input_rate" value="<%= model.getRate() %>" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" value="<%= model.getRemark() %>" id="input_remark" style="width: 200px">
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