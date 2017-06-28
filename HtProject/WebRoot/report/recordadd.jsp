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
	String lastDate = StringUtil.getPreDayOfMonth();
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
	
	var feeDate = "";
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
	
	function spChange()
	{
		var spId =  $("#sel_sp").val();
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
		$("#sel_sp").val(joData.id);
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
		//sss
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
		//SP的二级联动
		$("#sel_sp").change(spChange);
		//SP业务的二级联动
		$("#sel_sp_trone").change(spTroneChange);
		//CP业务的变化
		$("#sel_trone_order").change(troneOrderChange);
		//输入的数据条数发生变化
		$("#input_data_rows").change(dataRowChange);
		//CP数据条数发生变化
		$("#input_show_data_rows").change(showDataRowChange);
	});
	
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
		getAjaxValue("recordaction.jsp?type=1&fee_date=" + feeDate + "&trone_order_id=" + troneOrderId,onCheckExistDataResult);
	}
	
	function onCheckExistDataResult(data)
	{
		data = $.trim(data);
		if(data=="true")
		{
			alert("同一天内相同的渠道已经存在数据");	
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
		var params = "sp_id=" + spId + "&cp_id=" + cpId + "&trone_id=" + troneId + "&trone_order_id=" + troneOrderId 
			+ "&fee_date=" + feeDate + "&data_rows=" + dataRows + "&show_data_rows=" + showDataRows 
			+ "&amount=" + amount + "&show_amount=" + showAmount;
		getAjaxValue("recordaction.jsp?type=3&" + params,onFinishRecordData);
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
			return;
		
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
		feeDate = $("#input_fee_date").val();
		
		
		var intDataRows = parseInt(dataRows);
		
		var intShowDataRows = parseInt(showDataRows);
		
		var floatAmount = parseFloat(amount);
		
		var floatShowAmount = parseFloat(showAmount);

		if(isNaN(intShowDataRows))
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
		
		isOnSummit = true;
		
		startSubmitData();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>增加隔天数据</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="recordaction.jsp" method="post" id="addform">
					
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">日期</dd>
					<dd class="dd03_me">
						<input type="text" name="fee_date" value="<%= lastDate %>"   id="input_fee_date"  onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 200px">
					</dd>	
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" title="选择SP" style="width: 200px" onclick="namePicker(this,spList,onDataSpSelect)">
							<option value="-1">请选择</option>
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
					<dd class="dd01_me">数据量</dd>
					<dd class="dd03_me">
						<input type="text" name="data_rows"  id="input_data_rows" value="0" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">金额</dd>
					<dd class="dd03_me">
						<input type="text" name="amount"  id="input_amount" value="0" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP数据量</dd>
					<dd class="dd03_me">
						<input type="text" name="show_data_rows"  id="input_show_data_rows" value="0" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP金额</dd>
					<dd class="dd03_me">
						<input type="text" name="show_amount"  id="input_show_amount" value="0" style="width: 200px">
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