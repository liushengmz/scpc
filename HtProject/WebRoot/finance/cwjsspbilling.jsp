<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.SpBillingModel"%>
<%@page import="com.system.server.SpBillingServer"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.model.SettleAccountModel"%>
<%@page import="com.system.server.SettleAccountServer"%>
<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.model.CpBillingModel"%>
<%@page import="com.system.server.CpBillingServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	int spBillingId = StringUtil.getInteger(request.getParameter("spbillingid"), -1);

	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	SpBillingServer server = new SpBillingServer();
	
	int status = StringUtil.getInteger(request.getParameter("status"), 1);
	
	boolean isRecall = false;
	
	boolean isRecallSuc = false;
	
	//导出EXCEL数据
	if(spBillingId>0 && type ==1)
	{
		List<SettleAccountModel> list = server.exportExcelData(spBillingId);
		
		SpBillingModel spBillingModel = server.getSpBillingModel(spBillingId);
		
		response.setContentType("application/octet-stream;charset=utf-8");
		
		String fileName = spBillingModel.getSpName() + "(" + spBillingModel.getStartDate() + "_" + spBillingModel.getEndDate() + ").xls";
		
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) 
		{
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} 
		else 
		{
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}

		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		SettleAccountServer accountServer = new SettleAccountServer();
		
		accountServer.exportSettleAccount(1, spBillingModel.getJsType(), spBillingModel.getSpName(), spBillingModel.getStartDate() , spBillingModel.getEndDate(), list,
				response.getOutputStream());
		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
	}
	//重新生成指定帐单的数据
	else if(type==2 && spBillingId > 0)
	{
		server.reExportSpBillint(spBillingId);
	}
	//删除帐单数据
	else if(type==3 && spBillingId > 0)
	{
		server.delSpBilling(spBillingId);
	}
	//确定对帐单无误，更新对帐单状态
	else  if(type==4 && spBillingId > 0)
	{
		server.updateSpBillingStatus(spBillingId, 1);
	}
	//撤回待运营已审核的帐单
	else if(type==6 && spBillingId > 0)
	{
		isRecall = true;
		isRecallSuc =  server.recallSpBilling(spBillingId);
	}

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());

	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	
	int jsType = StringUtil.getInteger(request.getParameter("js_type"), -1);
	
	String startDate = StringUtil.getString(request.getParameter("startdate"), "");
	
	String endDate = StringUtil.getString(request.getParameter("enddate"), "");

	Map<String, Object> map =  server.loadSpBilling(startDate, endDate, spId, jsType,status,pageIndex);
		
	List<SpBillingModel> list = (List<SpBillingModel>)map.get("list");
	
	List<SpModel> spList = new SpServer().loadSp();
	
	List<JsTypeModel> jsTypeList = new JsTypeServer().loadJsType();
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String, String> params = new HashMap<String,String>();
	
	params = new HashMap<String,String>();
	params.put("js_type", jsType + "");
	params.put("sp_id", spId + "");
	params.put("startdate", startDate);
	params.put("enddate",endDate);
	params.put("status",""+status);
	
	String pageData = PageUtil.initPageQuery("cwjsspbilling.jsp",params,rowCount,pageIndex);
	
	String[] statusData = {"运营发起","运营审核","对帐完成","上游已发帐单","结算申请开票","财务已开票"};
	
	String[] btnStrings = {"","<a href='#' onclick='showConfirmDialog(helloisthereany,1)''>上游开票</a>","","<a href='#' onclick='showConfirmDialog(helloisthereany,2)''>结算申请开票</a>","",""};
	String[] btnMore = {"","<a href='#' onclick='showConfirmDialog(helloisthereany,0)''>更多</a>","",""};
	String[] year={"年份","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};
	String[] month={"月份","01","02","03","04","05","06","07","08","09","10","11","12"};
	String[] days={"日期","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	String defYear=StringUtil.getDefaultDate();
	String[] defDate=defYear.split("-");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
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
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp_id").val(joData.id);
	}

	//删除帐单
	function delSpBilling(id)
	{
		if(confirm('真的要删除帐单吗？删除后可以在SP数据下重新生成帐单'))
		{
			window.location.href = "spbilling.jsp?type=3&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";	
		}
	}
	
	//重新生成帐单
	function reExportSpBilling(id)
	{
		if(confirm("确定是否重新生成帐单？"))
		{
			window.location.href = "spbilling.jsp?type=2&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//审核帐单
	function sendToFinance(id)
	{
		if(confirm("是否确认数据无误？审核后会进入财务结款!"))
		{
			window.location.href = "spbilling.jsp?type=4&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//召回已发送给财务的帐单
	function reCallSpBillingBack(id)
	{
		if(confirm("确定是否要撤回该SP帐单？"))
		{
			window.location.href = "spbilling.jsp?type=6&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	$(function()
	{
		$("#sel_js_type").val(<%= jsType %>);
		$("#sel_sp_id").val(<%= spId %>);
		$("#sel_status").val(<%= status %>);
	});
	
	var confirmBillingList = new Array();
	
	function showConfirmDialog(id,type)
	{	
		$("#lab_title").text($("#lab_sp_name_" + id).text() + "[" + $("#lab_start_date_" + id).text() + "至" + $("#lab_end_date_" + id).text() + "][" + $("#lab_js_name_" + id).text() + "]");
  		$("#lab_sp").text($("#lab_sp_name_" + id).text());
  		$("#lab_start_date").text($("#lab_start_date_" + id).text());
  		var dateString=ajaxGetgetSpBillingDate(id,0);
  		var dateArray=dateString.split("#");
  		$("#lab_end_date").text($("#lab_end_date_" + id).text());
  		$("#lab_js_name").text($("#lab_js_name_" + id).text());
  		$("#lab_amount").text($("#lab_amount_" + id).text());
  		$("#lab_prebilling").text($("#lab_prebilling_" + id).text());
  		$("#lab_reduceamount").text($("#lab_reduceamount_" + id).text());
  		$("#lab_fact_amount").text($("#lab_fact_amount_" + id).text());
  		$("#lab_acturebilling").text($("#lab_acturebilling_" + id).text());
  		$("#lab_create_date").text($("#lab_create_date_" + id).text());
  		if(type==0){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_billing_year").val('<%=year[0]%>');
  		  		$("#lab_billing_month").val('<%=month[0]%>');
  		  		$("#lab_billing_day").val('<%=days[0]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_billing_year").val(splitDate[0]);
  		  		$("#lab_billing_month").val(splitDate[1]);
  		  		$("#lab_billing_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_apply_kaipiao_year").val('<%=year[0]%>');
  		  		$("#lab_apply_kaipiao_month").val('<%=month[0]%>');
  		  		$("#lab_apply_kaipiao_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_apply_kaipiao_year").val(splitDate[0]);
  		  		$("#lab_apply_kaipiao_month").val(splitDate[1]);
  		  		$("#lab_apply_kaipiao_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_kaipiao_year").val('<%=year[0]%>');
  		  		$("#lab_kaipiao_month").val('<%=month[0]%>');
  		  		$("#lab_kaipiao_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_kaipiao_year").val(splitDate[0]);
  		  		$("#lab_kaipiao_month").val(splitDate[1]);
  		  		$("#lab_kaipiao_day").val(splitDate[2]);
  			}
  			if(dateArray[3]==""||null==dateArray[3]){
  		  		$("#lab_pay_day").val('<%=year[0]%>');
  		  		$("#lab_pay_month").val('<%=month[0]%>');
  		  		$("#lab_pay_month").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[3]);
  		  		$("#lab_pay_year").val(splitDate[0]);
  		  		$("#lab_pay_month").val(splitDate[1]);
  		  		$("#lab_pay_day").val(splitDate[2]);
  			}
  			$("#lab_billing_year").attr("disabled",true);
		  	$("#lab_billing_month").attr("disabled",true);
		  	$("#lab_billing_day").attr("disabled",true);
		  	$("#lab_apply_kaipiao_year").attr("disabled",true);
		  	$("#lab_apply_kaipiao_month").attr("disabled",true);
		  	$("#lab_apply_kaipiao_day").attr("disabled",true);
		  	$("#lab_kaipiao_year").attr("disabled",true);
  		  	$("#lab_kaipiao_month").attr("disabled",true);
  		  	$("#lab_kaipiao_day").attr("disabled",true);
  		 	$("#lab_pay_year").attr("disabled",true);
  		  	$("#lab_pay_month").attr("disabled",true);
  		  	$("#lab_pay_day").attr("disabled",true);
  		}
  		if(type==1){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_billing_year").val('<%=defDate[0]%>');
  		  		$("#lab_billing_month").val('<%=defDate[1]%>');
  		  		$("#lab_billing_day").val('<%=defDate[2]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_billing_year").val(splitDate[0]);
  		  		$("#lab_billing_month").val(splitDate[1]);
  		  		$("#lab_billing_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_apply_kaipiao_year").val('<%=year[0]%>');
  		  		$("#lab_apply_kaipiao_month").val('<%=month[0]%>');
  		  		$("#lab_apply_kaipiao_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_apply_kaipiao_year").val(splitDate[0]);
  		  		$("#lab_apply_kaipiao_month").val(splitDate[1]);
  		  		$("#lab_apply_kaipiao_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_kaipiao_year").val('<%=year[0]%>');
  		  		$("#lab_kaipiao_month").val('<%=month[0]%>');
  		  		$("#lab_kaipiao_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_kaipiao_year").val(splitDate[0]);
  		  		$("#lab_kaipiao_month").val(splitDate[1]);
  		  		$("#lab_kaipiao_day").val(splitDate[2]);
  			}
  			$("#lab_billing_year").attr("disabled",false);
		  	$("#lab_billing_month").attr("disabled",false);
		  	$("#lab_billing_day").attr("disabled",false);
		  	$("#lab_apply_kaipiao_year").attr("disabled",true);
		  	$("#lab_apply_kaipiao_month").attr("disabled",true);
		  	$("#lab_apply_kaipiao_day").attr("disabled",true);
		  	$("#lab_kaipiao_year").attr("disabled",true);
  		  	$("#lab_kaipiao_month").attr("disabled",true);
  		  	$("#lab_kaipiao_day").attr("disabled",true);
  		  	$("#lab_pay_year").attr("disabled",true);
		  	$("#lab_pay_month").attr("disabled",true);
		  	$("#lab_pay_day").attr("disabled",true);
  		}
  		if(type==2){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_billing_year").val('<%=year[0]%>');
  		  		$("#lab_billing_month").val('<%=month[0]%>');
  		  		$("#lab_billing_day").val('<%=days[0]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_billing_year").val(splitDate[0]);
  		  		$("#lab_billing_month").val(splitDate[1]);
  		  		$("#lab_billing_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_apply_kaipiao_year").val('<%=defDate[0]%>');
  		  		$("#lab_apply_kaipiao_month").val('<%=defDate[1]%>');
  		  		$("#lab_apply_kaipiao_day").val('<%=defDate[2]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_apply_kaipiao_year").val(splitDate[0]);
  		  		$("#lab_apply_kaipiao_month").val(splitDate[1]);
  		  		$("#lab_apply_kaipiao_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_kaipiao_year").val('<%=year[0]%>');
  		  		$("#lab_kaipiao_month").val('<%=month[0]%>');
  		  		$("#lab_kaipiao_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_kaipiao_year").val(splitDate[0]);
  		  		$("#lab_kaipiao_month").val(splitDate[1]);
  		  		$("#lab_kaipiao_day").val(splitDate[2]);
  			}
  			$("#lab_billing_year").attr("disabled",true);
		  	$("#lab_billing_month").attr("disabled",true);
		  	$("#lab_billing_day").attr("disabled",true);
		  	$("#lab_apply_kaipiao_year").attr("disabled",false);
		  	$("#lab_apply_kaipiao_month").attr("disabled",false);
		  	$("#lab_apply_kaipiao_day").attr("disabled",false);
		  	$("#lab_kaipiao_year").attr("disabled",true);
  		  	$("#lab_kaipiao_month").attr("disabled",true);
  		  	$("#lab_kaipiao_day").attr("disabled",true);
  		  	$("#lab_pay_year").attr("disabled",true);
		  	$("#lab_pay_month").attr("disabled",true);
		  	$("#lab_pay_day").attr("disabled",true);
  		}
  		$("#btn_confirm").click(function(){
  			if(type==0){
  				$( "#dialog" ).dialog("close");
  			}
  			//上游开票，更改状态和时间
  			if(type==1){  
  				var billyear=$("#lab_billing_year").val();
  				var billmonth=$("#lab_billing_month").val();
  				var billday=$("#lab_billing_day").val();
				var date=billyear+"-"+billmonth+"-"+billday;
  				ajaxGetgetSpBillingDate(id,type,3,date);
  				$( "#dialog" ).dialog("close");

  				
  			}
  			//结算申请开票时间，更改状态和时间
  			if(type==2){
  				var jskaipiaoyear=$("#lab_apply_kaipiao_year").val();
  				var jskaipiaomonth=$("#lab_apply_kaipiao_month").val();
  				var jskaipiaoday=$("#lab_apply_kaipiao_day").val();
				var date=jskaipiaoyear+"-"+jskaipiaomonth+"-"+jskaipiaoday;
				ajaxGetgetSpBillingDate(id,type,4,date);
  				$( "#dialog" ).dialog("close");
  			}
  			
  		});
		
		$( "#dialog" ).dialog();
	}
	function ajaxGetgetSpBillingDate(id,type,status,date) {
		var result = "";
		$.ajax({
			url : "util.jsp",
			data : "spbillingid=" + id + "&type="+type+"&status="+status+"&date="+date,
			cache : false,
			async : false,
			success : function(html) {
				result = $.trim(html);
			}
		});
		return result;
	}
	
	function confirmActureBilling(id)
	{
		var actureBilling = parseFloat($("#lab_acture_billing").val()).toFixed(2);
		
		if(isNaN(actureBilling) || actureBilling < 0)
		{
			alert("难道你能真的能收到这样的钱？");
			return;
		}
		
		getAjaxValue("action.jsp?type=4&id=" + id + "&money=" + actureBilling,onConfirmSpBilling);
		
		$( "#dialog" ).dialog("close");
	}
	
	function onConfirmSpBilling(data)
	{
		if(!(data==null || data==""))
		{
			var strData = data.split(",");
			if("OK"==strData[0])
			{
				confirmBillingList.push(strData[1]);
				alert("已经完成对帐！");
				return;
			}
			else
			{
				alert("完成对帐失败！");	
				return;
			}
		}
	}
	function getYearMonthDayByDate(date){
  		var dateArray=date.split("-");
  		return dateArray;
	}
</script>

<style type="text/css">
.ui-button-icon-only .ui-icon{left:0}
.ui-button-icon-only .ui-icon, 
.ui-button-text-icon-primary .ui-icon, 
.ui-button-text-icon-secondary .ui-icon, 
.ui-button-text-icons .ui-icon, 
.ui-button-icons-only .ui-icon
{top:0}
</style>

<body style="min-height: 800px;padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="cwjsspbilling.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me" style="margin-left: -10px;">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp_id" onclick="namePicker(this,spList,onSpDataSelect)">
						<option value="-1">全部</option>
							<%
							for(SpModel sp : spList)
							{
								%>
							<option value="<%= sp.getId() %>"><%= sp.getShortName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd04_me">
						<select name="js_type" id="sel_js_type" >
						<option value="-1">请选择</option>
						<%
						for(JsTypeModel jsTypeModel : jsTypeList)
						{
							%>
							<option value="<%= jsTypeModel.getJsType() %>"><%= jsTypeModel.getJsName() %></option>
							<%
						}
						%>
						</select>
					</dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd04_me">
						<select name="status" id="sel_status" >
						<option value="-1">请选择</option>
							<option value="0">运营发起</option>
							<option value="1">运营审核</option>
							<option value="3">上游已发帐单</option>
							<option value="4">结算申请开票</option>
							<option value="5">财务已开票</option>
							<option value="2">对帐完成</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查     询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
				<br/>
				<br/>
				<br/>
					<td>序号</td>
					<td>SP</td>
					<td>开始时间</td>
					<td>结束时间</td>
					<td>结算类型</td>
					<td>信息费</td>
					<td>应收款</td>
					<td>核减款</td>
					<td>实际应收款</td>
					<td>实际到款</td>
					<td>备注</td>
					<td>创建时间</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SpBillingModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					
					<td><label id="lab_sp_name_<%= model.getId() %>"><%=model.getSpName() %></label> </td>
					<td><label id="lab_start_date_<%= model.getId() %>"><%=model.getStartDate() %></label></td>
					<td><label id="lab_end_date_<%= model.getId() %>"><%=model.getEndDate()%></label></td>
					<td><label id="lab_js_name_<%= model.getId() %>"><%= model.getJsName() %></label></td>
					<td><label id="lab_amount_<%= model.getId() %>"><%= model.getAmount() %></label></td>
					<td><label id="lab_prebilling_<%= model.getId() %>"><%=model.getPreBilling() %></label></td>
					<td><label id="lab_reduceamount_<%= model.getId() %>"><%= model.getReduceAmount() %></label></td>
					<td><label id="lab_fact_amount_<%= model.getId() %>"><%= model.getPreBilling() - model.getReduceAmount() %></label></td>
					<td><label id="lab_acturebilling_<%= model.getId() %>"><%= model.getActureBilling() %></label></td>
					<td><%=model.getRemark() %></td>
					<td><label id="lab_create_date_<%= model.getId() %>"><%= model.getCreateDate() %></label></td>
					<td><%= statusData[model.getStatus()] %></td>
					<td >
						<%= btnMore[1].replaceAll("helloisthereany", "" + model.getId()) %>
						<a href="spbillingdetail.jsp?query=<%= query %>&spbillingid=<%= model.getId() %>" >详细</a>
						<%= btnStrings[model.getStatus()].replaceAll("helloisthereany", "" + model.getId()) %>
						<a href="spbilling.jsp?type=1&spbillingid=<%= model.getId() %>">导出</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="100" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="dialog" title="账单明细" style="">
  		<label id="lab_title" >账单明细</label>
  		<br />
  		 SP:<label id="lab_sp">123456</label>
  		<br />
  		开始时间：<label id="lab_start_date">123456</label>
  		<br />
  		  结束时间：<label id="lab_end_date">123456</label>
  		<br />
  		 结算类型：<label id="lab_js_name">123456</label>
  		<br />
  		  信息费 ：<label id="lab_amount">123456</label>
  		<br />
  		  应收款 ：<label id="lab_prebilling">123456</label>
  		<br />
  		 核减款 ：<label id="lab_reduceamount">123456</label>
  		<br />
  		 实际应收款 ：<label id="lab_fact_amount">123456</label>
  		<br />
  		实际到款 ：<label id="lab_acturebilling">123456</label>
  		<br />
  		 创建时间 ：<label id="lab_create_date">00:00:00</label>
  		<br />
  		 上游确认账单 ：<label></label><select name="lab_billing_year" id="lab_billing_year" style="width: 55px">
						<%
							for(int i=0;i<year.length;i++)
							{
								
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
								

							}
						
							%>
						</select>-
						<select name="lab_billing_month" id="lab_billing_month" style="width: 55px">
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_billing_day" id="lab_billing_day" style="width: 55px">
						<%
							for(int i=0;i<days.length;i++)
							{
								%>
							<option value="<%= days[i]%>"><%= days[i]%></option>	
								<%
							}
							%>
						</select>
  		<br />
  		  结算申请开票 ：<label></label><select name="lab_apply_kaipiao_year" id="lab_apply_kaipiao_year" >
						<%
							for(int i=0;i<year.length;i++)
							{
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_apply_kaipiao_month" id="lab_apply_kaipiao_month" >
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_apply_kaipiao_day" id="lab_apply_kaipiao_day" >
						<%
							for(int i=0;i<days.length;i++)
							{
								%>
							<option value="<%= days[i]%>"><%= days[i]%></option>	
								<%
							}
							%>
						</select>
  		    		<br />
  		 财务开票日期  ：<label></label><select name="lab_kaipiao_year" id="lab_kaipiao_year" >
						<%
							for(int i=0;i<year.length;i++)
							{
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_kaipiao_month" id="lab_kaipiao_month" >
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_kaipiao_day" id="lab_kaipiao_day" >
						<%
							for(int i=0;i<days.length;i++)
							{
								%>
							<option value="<%= days[i]%>"><%= days[i]%></option>	
								<%
							}
							%>
						</select>
  		 
  		<br />
  		 回款日期  ：<label></label><select name="lab_pay_year" id="lab_pay_year" >
						<%
							for(int i=0;i<year.length;i++)
							{
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_pay_month" id="lab_pay_month" >
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_pay_day" id="lab_pay_day" >
						<%
							for(int i=0;i<days.length;i++)
							{
								%>
							<option value="<%= days[i]%>"><%= days[i]%></option>	
								<%
							}
							%>
						</select>
  		 
  		<br />
  		
  		<input id="btn_confirm" style="float: right;font-size: 14px;font-weight: bold;cursor: pointer;" type="button" value="确认" >
	</div>
	
</body>
</html>
