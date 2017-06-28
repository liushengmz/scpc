<%@page import="java.util.Date"%>
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
	String lastDate = StringUtil.getMonthFormat2(new Date());
	List<SpModel> spList = new SpServer().loadSp();
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

	var spTroneArray = new Array();
	
	var troneOrderArray = new Array();
	
	var isOnSummit = false;
	
	var year = 2016;
	var month = 12;
	var cpId = 0;
	var spId = 0;
	var holdPercent = 0;
	var troneId = 0;
	var troneOrderId = 0;
	var price = 0;
	var dataRows = 0;
	var showDataRows = 0;
	var amount = 0;
	var showAmount = 0;
	var saveLocate = 1;
	
	function spChange()
	{
		var spId =  $("#hid_sp_id").val();
		getAjaxValue("../ajaction.jsp?type=2&spid=" + spId + "&arrname=spTroneArray",onSpChange);
	}
	
	var spList = new Array();
	<%
	for(SpModel spModel : spList)
	{
		%>
		spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
		<%
	}
	%>
	
	function onDataSpSelect(joData) 
	{
		if(joData.id == -1)
		{
			$("#input_sp_id").val("");
			$("#hid_sp_id").val(-1);
			spChange();
			return;
		}
		
		$("#input_sp_id").val(joData.text);
		$("#hid_sp_id").val(joData.id);
		spChange();
	}
	
	function onSpChange(data)
	{
		spTroneArray.length = 0;
		eval(data);
		$("#sel_sp_trone").empty();
		$("#sel_sp_trone").append("<option value='-1'>请选择</option>");
		$("#sel_trone_order").empty();
		$("#sel_trone_order").append("<option value='-1'>请选择</option>");
		for(i=0; i<spTroneArray.length; i++)
		{
			$("#sel_sp_trone").append("<option value='" + spTroneArray[i].id + "'>" + spTroneArray[i].name + "</option>");
		}
		cpId = 0;
		spId = 0;
		holdPercent = 0;
		troneId = 0;
		price = 0;
		troneOrderId = 0;
		troneOrderArray.length = 0;
	}
	
	function spTroneChange()
	{
		var spTroneId =  $("#sel_sp_trone").val();
		getAjaxValue("../ajaction.jsp?type=3&sp_trone_id=" + spTroneId,onSpTroneChange);
	}
	
	function onSpTroneChange(data)
	{
		//EVAL DATA 之前先把 troneOrderArray 清空
		troneOrderArray.length = 0;
		eval(data);
		$("#sel_trone_order").empty();
		$("#sel_trone_order").append("<option value='-1'>请选择</option>");
		for(i=0; i<troneOrderArray.length; i++)
		{
			$("#sel_trone_order").append("<option value='" + troneOrderArray[i].id + "'>" + troneOrderArray[i].cpShortName +
					
				"-"	+ troneOrderArray[i].troneName + "-" + troneOrderArray[i].price +  "-扣(" + troneOrderArray[i].holdPercent + "%)" +"</option>");
		}
	}
	
	function troneOrderChange()
	{
		cpId = 0;
		spId = 0;
		holdPercent = 0;
		troneId = 0;
		price = 0;
		troneOrderId =  $("#sel_trone_order").val();
		
		for(i=0; i<troneOrderArray.length; i++)
		{
			if(troneOrderArray[i].id == troneOrderId)
			{
				cpId = troneOrderArray[i].cpId;
				spId = troneOrderArray[i].spId;
				holdPercent = troneOrderArray[i].holdPercent;
				troneId = troneOrderArray[i].troneId;
				price = troneOrderArray[i].price;
				break;
			}
		}
	}
	
	$(function()
	{
		//SP业务的二级联动
		$("#sel_sp_trone").change(spTroneChange);
		//CP业务的变化
		$("#sel_trone_order").change(troneOrderChange);
		//输入的数据条数发生变化
		$("#input_amount").change(dataRowChange);
		//CP数据条数发生变化
		$("#input_show_amount").change(showDataRowChange);
	});
	
	function dataRowChange()
	{
		$("#lab_sp_amount_msg").css("display","none");
		
		var spAmount = $("#input_amount").val();
		
		if(spAmount%price!=0)
		{
			$("#input_data_rows").val(0);
			$("#input_show_data_rows").val(0);
			$("#input_show_amount").val(0);
			$("#lab_sp_amount_msg").css("display","block");
			return;
		}
		
		var spDataRows = spAmount/price;
		var cpAmount = Math.floor(spAmount*(100-holdPercent)/100);
		var cpDataRows = cpAmount/price;
		
		$("#input_data_rows").val(spDataRows);
		$("#input_show_data_rows").val(cpDataRows);
		$("#input_amount").val(spAmount);
		$("#input_show_amount").val(cpAmount);
	}
	
	function showDataRowChange()
	{
		$("#lab_cp_amount_msg").css("display","none");
		
		var cpAmount = $("#input_show_amount").val();
		
		if(cpAmount%price!=0)
		{
			$("#lab_cp_amount_msg").css("display","block");
			$("#input_show_data_rows").val(0);
			return;
		}
		
		var cpDataRows = cpAmount/price;
		
		$("#input_show_data_rows").val(cpDataRows);
		$("#input_show_amount").val(cpAmount);
		
	}
	
	function startSubmitData()
	{
		//提交数据之前先检查是否有数据存在，如果有，不好意思，不干了
		getAjaxValue("recordaction.jsp?type=5&year=" + year + "&month=" + month + "&trone_order_id=" + troneOrderId,onCheckExistDataResult);
	}
	
	function onCheckExistDataResult(data)
	{
		data = $.trim(data);
		if(data=="true")
		{
			alert("同一个月内相同的渠道已经存在数据，如要增加请先删除");	
			isOnSummit = false;
			return;
		}
		else(data=="false")
		{
			submitRecordData();
		}
	}
	
	function submitRecordData()
	{
		var params = "sp_id=" + spId + "&cp_id=" + cpId + "&trone_id=" + troneId + "&trone_order_id=" + troneOrderId + "&price=" + price + "&year=" + year + "&month=" + month
			+ "&data_rows=" + dataRows + "&show_data_rows=" + showDataRows + "&amount=" + amount + "&show_amount=" + showAmount + "&save_locate=" + saveLocate;
		getAjaxValue("recordaction.jsp?type=6&" + params,onFinishRecordData);
	}
	
	function onFinishRecordData(data)
	{
		isOnSummit = false;
		if("true"==$.trim(data))
		{
			alert("增加成功");
		}
		else
		{
			alert("增加失败");	
		}
	}
	
	function subForm() 
	{
		if(isOnSummit)
		{
			alert("提交中，请稍候...");
			return;
		}
			
		
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
		
		if ($("#sel_trone_order").val() == "-1") 
		{
			alert("请选择CP业务");
			$("#sel_trone_order").focus();
			return;
		}
		
		if (!isInteger($("#input_data_rows").val()))
		{
			$("#input_data_rows").focus();
			alert("请输入数据量");
			return;
		}
		
		dataRows = $("#input_data_rows").val();
		showDataRows = $("#input_show_data_rows").val();
		amount = $("#input_amount").val();
		showAmount = $("#input_show_amount").val();
		year = $("#sel_year").val();
		month = $("#sel_month").val();
		saveLocate = $('input:radio[name="save_locate"]:checked').val();
		
		var intDataRows = parseInt(dataRows);
		
		var intShowDataRows = parseInt(showDataRows);
		
		var floatAmount = parseFloat(amount);
		
		var floatShowAmount = parseFloat(showAmount);
		
		if(isNaN(intDataRows) || intDataRows<=0)
		{
			alert("请输入正确的SP数据量");
			$("#input_data_rows").focus();
			return;
		}

		if(isNaN(intShowDataRows) || intShowDataRows<0)
		{
			alert("请输入正确的CP数据量");
			$("#input_show_data_rows").focus();
			return;
		}
		
		if(isNaN(floatAmount) || floatAmount<=0)
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
		
		$("#input_data_rows").val(intDataRows);
		$("#input_show_data_rows").val(intShowDataRows);
		$("#input_amount").val(floatAmount);
		$("#input_show_amount").val(floatShowAmount);
		
		isOnSummit = true;
		
		startSubmitData();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>增加月数据</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
					
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">日期</dd>
					<dd class="dd04_me">
						<select name="year" id="sel_year"   style="width: 95px">
							<option value="2012">2012</option>
							<option value="2013">2013</option>
							<option value="2014">2014</option>
							<option value="2015">2015</option>
							<option value="2016">2016</option>
							<option value="2017" selected="selected">2017</option>
							<option value="2018">2018</option>
							<option value="2019">2019</option>
							<option value="2020">2020</option>
						</select>
						<select name="month" id="sel_month"   style="width: 95px">
							<option value="1" selected="selected">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
						</select>
					</dd>	
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd03_me">
						<input type="hidden" name="sp_id" id="hid_sp_id" value="-1">
						<input id="input_sp_id" style="width:200px;" readonly="readonly" onclick="namePicker(this,spList,onDataSpSelect)" />
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone_id" id="sel_sp_trone" title="选择SP业务" style="width: 200px">
							<option value="-1">请选择</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">CP业务</dd>
					<dd class="dd04_me">
						<select name="trone_order_id" id="sel_trone_order" style="width: 200px">
							<option value="-1">请选择</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">SP金额</dd>
					<dd class="dd03_me">
						<input type="text" name="amount"  id="input_amount"  value="0" style="width: 200px">
						<label id="lab_sp_amount_msg" style="color: red;display: none">金额必须是价格的倍数</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">SP数据量</dd>
					<dd class="dd03_me">
						<input type="text" name="data_rows"  id="input_data_rows" readonly="readonly" value="0" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP金额</dd>
					<dd class="dd03_me">
						<input type="text" name="show_amount"  id="input_show_amount"  value="0" style="width: 200px">
						<label id="lab_cp_amount_msg" style="color: red;display: none">金额必须是价格的倍数</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP数据量</dd>
					<dd class="dd03_me">
						<input type="text" name="show_data_rows"  id="input_show_data_rows" readonly="readonly" value="0" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">存储位置</dd>
					<dd class="dd03_me" style="background: none">
						<input type="radio" name="save_locate" style="width: 35px;float:left" value="1" checked="checked">
						<label style="font-size: 14px;float:left">大数据</label>
						<input type="radio" name="save_locate" style="width: 35px;float:left" value="2"  >
						<label style="font-size: 14px;float:left">商务</label>
						<input type="radio" name="save_locate" style="width: 35px;float:left" value="3" >
						<label style="font-size: 14px;float:left">两者</label>
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
						<input type="button" value="返 回" onclick="window.location.href='mrsjrecord.jsp'">
					</dd>
			</dl>
		</div>

	</div>
</body>
</html>