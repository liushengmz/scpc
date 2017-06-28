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
	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
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

	var spList = new Array();
	<%
	for(SpModel spModel : spList)
	{
		%>
		spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
		<%
	}
	%>

	function joSpTrone(id,spId,troneName,spTroneApiId)
	{
		var obj = {};
		obj.id = id;
		obj.spId = spId;
		obj.troneName = troneName;
		obj.spTroneApiId = spTroneApiId;
		return obj;
	}
	
	var spTroneList = new Array();
	<%for(SpTroneModel spTrone : spTroneList){%>spTroneList.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%= spTrone.getSpName() + "-" +spTrone.getSpTroneName() %>',<%= spTrone.getTroneApiId() %>));<%}%>

	function spChange()
	{
		var spId = $("#sel_sp").val();
		$("#sel_sp_trone").empty();
		$("#sel_sp_trone").append("<option value='-1'>请选择</option>");
		for(i=0; i<spTroneList.length; i++)
		{
			if(spTroneList[i].spId==spId)
			{
				$("#sel_sp_trone").append("<option value='" + spTroneList[i].id + "'>" + spTroneList[i].troneName + "</option>");
			}
		}
	}
	
	$(function()
	{
		//SP的二级联动
		$("#sel_sp").change(spChange);
	});
	
	function subForm() 
	{
		if ($("#sel_sp").val() == "-1") 
		{
			alert("请选择SP");
			$("#sel_sp").focus();
			return;
		}
		
		if ($("#sel_sp_trone").val() == "-1") 
		{
			alert("请选择SP业务");
			$("#sel_sp_trone").focus();
			return;
		}
		
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
		
		var spTroneId = $("#sel_sp_trone").val();
		
		ajaxData(spTroneId,startDate,endDate);
	}
	
	function ajaxData(spTroneId,startDate,endDate)
	{
		getAjaxValue("sptronerateaction.jsp?type=1&sptroneid=" + spTroneId + "&startdate=" + startDate + "&enddate=" + endDate,onDataChange);
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
				<label style="width:200px;display: block">增加SP业务结算率</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="sptronerateaction.jsp" method="post" id="addform">
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" title="选择SP" style="width: 200px" onclick="namePicker(this,spList,onSpDataSelect)">
							<option value="-1">请选择SP名称</option>
							<%
								for (SpModel sp : spList)
								{
							%>
							<option value="<%=sp.getId()%>"><%=sp.getShortName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sptroneid" id="sel_sp_trone" title="选择SP业务" style="width: 200px" >
							<option value="-1">请选择SP业务</option>
						</select>
					</dd>
					
					<br />
					<br />
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