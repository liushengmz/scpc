<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<CpModel> cpList = new CpServer().loadCp();
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
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	
	var cpList = new Array();
	<%
	for(CpModel cpModel : cpList)
	{
		%>
		cpList.push(new joSelOption(<%= cpModel.getId() %>,1,'<%= cpModel.getShortName() %>'));
		<%
	}
	%>
	
	function onCpDataSelect(jo)
	{
		$("#sel_cp").val(jo.id);
	}
	
	function subForm() 
	{
		if ($("#sel_cp").val()==-1) 
		{
			alert("请选择CP");
			$("#sel_cp").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_name").val())) 
		{
			alert("请输入名称");
			$("#input_name").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_url").val())) 
		{
			alert("请输入URL");
			$("#input_url").focus();
			return;
		}
		
		if(!($("#input_hold_percent").val()>=0 && $("#input_hold_percent").val()<=100))
		{
			alert("请输入正确的扣量比");
			$("#input_hold_percent").focus();
			return;
		}
		
		if($("#input_hold_amount").val()<0)
		{
			alert("请输入正确的当日最大同步金额");
			$("#input_hold_amount").focus();
			return;
		}
		
		document.getElementById("addform").submit();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px;">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>CP同步 URL增加</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="cpurlaction.jsp" method="post" id="addform">
										
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="width:120px">选择CP</dd>
					<dd class="dd04_me" >
						<select name="cp_id" id="sel_cp" title="选择CP" style="width:200px" onclick="namePicker(this,cpList,onCpDataSelect)">
							<option value="-1">全部</option>
							<%
							for(CpModel cp : cpList)
							{
								%>
							<option value="<%= cp.getId() %>"><%= cp.getShortName() %></option>	
								<%
							}
							%>
						</select>
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="width:120px">名称</dd>
					<dd class="dd03_me">
						<input type="text" name="name" id="input_name" style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="width:120px">URL</dd>
					<dd class="dd03_me">
						<input type="text" name="url" id="input_url" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="width:120px">扣量比(0-100)</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_percent" id="input_hold_percent" value="0"  style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="width:120px">起扣条数</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_start_count" id="input_hold_start_count" value="0" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="width:120px">当日最大同步金额</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_amount" id="input_hold_amount" value="0" style="width: 200px">
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