<%@page import="jxl.common.BaseUnit"%>
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

	int cpBillingId = StringUtil.getInteger(request.getParameter("cpbillingid"), -1);

	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	CpBillingServer server = new CpBillingServer();
	
	int status = StringUtil.getInteger(request.getParameter("status"), 2);
	
	//导出EXCEL数据
	if(cpBillingId>0 && type ==1)
	{
		List<SettleAccountModel> list = server.exportExcelData(cpBillingId);
		
		CpBillingModel cpBillingModel = server.getCpBillingModel(cpBillingId);
		
		response.setContentType("application/octet-stream;charset=utf-8");
		
		String fileName = cpBillingModel.getCpName() + "(" + cpBillingModel.getStartDate() + "_" + cpBillingModel.getEndDate() + ").xls";
		
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
		
		accountServer.exportSettleAccount(2, cpBillingModel.getJsType(), cpBillingModel.getCpName(), cpBillingModel.getStartDate() , cpBillingModel.getEndDate(), list,
				response.getOutputStream());
		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
	}
	//重新生成指定帐单的数据
	else if(type==2 && cpBillingId > 0)
	{
		server.reExportCpBillint(cpBillingId);
	}
	//删除帐单数据
	else if(type==3 && cpBillingId > 0)
	{
		server.delCpBilling(cpBillingId);
	}
	//确定对帐单无误，更新对帐单状态
	else  if(type==4 && cpBillingId > 0)
	{
		//把帐单更新为待CP审核
		server.updateCpBillingStatus(cpBillingId, 1);
	}
	//帮CP确认帐单无误，更新对帐单状态
	else  if(type==5 && cpBillingId > 0)
	{
		//把帐单更新为CP已审核
		server.updateCpBillingStatus(cpBillingId, 2);
	}
	//撤回待CP审核的帐单
	else if(type==6 && cpBillingId > 0)
	{
		server.updateCpBillingStatus(cpBillingId, 0);
	}

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());

	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	
	int jsType = StringUtil.getInteger(request.getParameter("js_type"), -1);
	
	String startDate = StringUtil.getString(request.getParameter("startdate"), "");
	
	String endDate = StringUtil.getString(request.getParameter("enddate"), "");

	Map<String, Object> map =  server.loadCpBilling(startDate, endDate, cpId, jsType,status,pageIndex);
		
	List<CpBillingModel> list = (List<CpBillingModel>)map.get("list");
	
	List<CpModel> cpList = new CpServer().loadCp();
	
	List<JsTypeModel> jsTypeList = new JsTypeServer().loadJsType();
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String, String> params = new HashMap<String,String>();
	
	params = new HashMap<String,String>();
	params.put("js_type", jsType + "");
	params.put("cp_id", cpId + "");
	params.put("startdate", startDate);
	params.put("enddate",endDate);
	params.put("status",""+status);
	
	String pageData = PageUtil.initPageQuery("cpbilling.jsp",params,rowCount,pageIndex);
	
	String[] statusData = {"发起","运营审核","CP审核","发起帐单","收到票据","申请付款","已付款"};

	String[] btnStrings = {"","","<a href='#' onclick='showConfirmDialog(helloisthereany,1)''>发起帐单</a>","<a href='#' onclick='showConfirmDialog(helloisthereany,2)''>收到票据</a>","<a href='#' onclick='showConfirmDialog(helloisthereany,3)''>申请付款</a>","",""};
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
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">

<link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
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
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp_id").val(joData.id);
	}

	//删除帐单
	function delCpBilling(id)
	{
		if(confirm('真的要删除帐单吗？删除后可以在CP数据下重新生成帐单'))
		{
			window.location.href = "cpbilling.jsp?type=3&cpbillingid=" + id + "&<%= Base64UTF.decode(query) %>";	
		}
	}
	
	//重新生成帐单
	function reExportCpBilling(id)
	{
		if(confirm("确定是否重新生成帐单？"))
		{
			window.location.href = "cpbilling.jsp?type=2&cpbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//审核帐单
	function sendCpBillingToCp(id)
	{
		if(confirm("是否确认数据无误？审核后会进入CP审核!"))
		{
			window.location.href = "cpbilling.jsp?type=4&cpbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//替CP审核帐单
	function confirmCpBillingForCp(id)
	{
		if(confirm("CP是否已确认数据正确？审核后将会进入财务对帐流程！"))
		{
			window.location.href = "cpbilling.jsp?type=5&cpbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//召回已发送给CP的帐单
	function reCallCpBillingFromCpStatus(id)
	{
		if(confirm("确定是否要撤回该CP帐单？"))
		{
			window.location.href = "cpbilling.jsp?type=6&cpbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	$(function()
	{
		$("#sel_js_type").val(<%= jsType %>);
		$("#sel_cp_id").val(<%= cpId %>);
		$("#sel_status").val(<%= status %>);
	});
	
	function showConfirmDialog(id,type)
	{	
		$("#lab_title").text($("#lab_cp_name_" + id).text() + "[" + $("#lab_start_date_" + id).text() + "至" + $("#lab_end_date_" + id).text() + "][" + $("#lab_js_name_" + id).text() + "]");
  		$("#lab_cp_name").text($("#lab_cp_name_" + id).text());
  		$("#lab_start_date").text($("#lab_start_date_" + id).text());
  		var dateString=ajaxGetgetCpBillingDate(id,0);
  		var dateArray=dateString.split("#");
  		$("#lab_end_date").text($("#lab_end_date_" + id).text());
  		$("#lab_js_name").text($("#lab_js_name_" + id).text());
  		$("#lab_amount").text($("#lab_amount_" + id).text());
  		$("#lab_prebilling").text($("#lab_prebilling_" + id).text());
  		$("#lab_reduce_amount").text($("#lab_reduce_amount_" + id).text());
  		$("#lab_fuct_amount").text($("#lab_fuct_amount_" + id).text());
  		$("#lab_acturebilling").text($("#lab_acturebilling_" + id).text());
  		$("#lab_create_date").text($("#lab_create_date_" + id).text());
  		
  		$("#lab_kaipiao_billing").val($("#lab_kaipiao_billing_" + id).text());

  		
  		if(type==0){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_start_bill_year").val('<%=year[0]%>');
  		  		$("#lab_start_bill_month").val('<%=month[0]%>');
  		  		$("#lab_start_bill_day").val('<%=days[0]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_start_bill_year").val(splitDate[0]);
  		  		$("#lab_start_bill_month").val(splitDate[1]);
  		  		$("#lab_start_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_get_bill_year").val('<%=year[0]%>');
  		  		$("#lab_get_bill_month").val('<%=month[0]%>');
  		  		$("#lab_get_bill_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_get_bill_year").val(splitDate[0]);
  		  		$("#lab_get_bill_month").val(splitDate[1]);
  		  		$("#lab_get_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_apply_pay_bill_year").val('<%=year[0]%>');
  		  		$("#lab_apply_pay_bill_month").val('<%=month[0]%>');
  		  		$("#lab_apply_pay_bill_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_apply_pay_bill_year").val(splitDate[0]);
  		  		$("#lab_apply_pay_bill_month").val(splitDate[1]);
  		  		$("#lab_apply_pay_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[3]==""||null==dateArray[3]){
  		  		$("#lab_pay_year").val('<%=year[0]%>');
  		  		$("#lab_pay_month").val('<%=month[0]%>');
  		  		$("#lab_pay_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[3]);
  		  		$("#lab_pay_year").val(splitDate[0]);
  		  		$("#lab_pay_month").val(splitDate[1]);
  		  		$("#lab_pay_day").val(splitDate[2]);
  			}
  			$("#lab_start_bill_year").attr("disabled",true);
		  	$("#lab_start_bill_month").attr("disabled",true);
		  	$("#lab_start_bill_day").attr("disabled",true);
		  	$("#lab_get_bill_year").attr("disabled",true);
		  	$("#lab_get_bill_month").attr("disabled",true);
		  	$("#lab_get_bill_day").attr("disabled",true);
		  	$("#lab_apply_pay_bill_year").attr("disabled",true);
  		  	$("#lab_apply_pay_bill_month").attr("disabled",true);
  		  	$("#lab_apply_pay_bill_day").attr("disabled",true);
  		 	$("#lab_pay_year").attr("disabled",true);
  		  	$("#lab_pay_month").attr("disabled",true);
  		  	$("#lab_pay_day").attr("disabled",true);
		  	$("#lab_kaipiao_billing").attr("disabled",true);

  		}
  		if(type==1){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_start_bill_year").val('<%=defDate[0]%>');
  		  		$("#lab_start_bill_month").val('<%=defDate[1]%>');
  		  		$("#lab_start_bill_day").val('<%=defDate[2]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_start_bill_year").val(splitDate[0]);
  		  		$("#lab_start_bill_month").val(splitDate[1]);
  		  		$("#lab_start_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_get_bill_year").val('<%=year[0]%>');
  		  		$("#lab_get_bill_month").val('<%=month[0]%>');
  		  		$("#lab_get_bill_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_get_bill_year").val(splitDate[0]);
  		  		$("#lab_get_bill_month").val(splitDate[1]);
  		  		$("#lab_get_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_apply_pay_bill_year").val('<%=year[0]%>');
  		  		$("#lab_apply_pay_bill_month").val('<%=month[0]%>');
  		  		$("#lab_apply_pay_bill_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_apply_pay_bill_year").val(splitDate[0]);
  		  		$("#lab_apply_pay_bill_month").val(splitDate[1]);
  		  		$("#lab_apply_pay_bill_day").val(splitDate[2]);
  			}
  			
  			$("#lab_start_bill_year").attr("disabled",false);
		  	$("#lab_start_bill_month").attr("disabled",false);
		  	$("#lab_start_bill_day").attr("disabled",false);
		  	$("#lab_get_bill_year").attr("disabled",true);
		  	$("#lab_get_bill_month").attr("disabled",true);
		  	$("#lab_get_bill_day").attr("disabled",true);
		  	$("#lab_apply_pay_bill_year").attr("disabled",true);
  		  	$("#lab_apply_pay_bill_month").attr("disabled",true);
  		  	$("#lab_apply_pay_bill_day").attr("disabled",true);
  		  	$("#lab_pay_year").attr("disabled",true);
		  	$("#lab_pay_month").attr("disabled",true);
		  	$("#lab_pay_day").attr("disabled",true);
		  	$("#lab_kaipiao_billing").attr("disabled",true);
  		}
  		if(type==2){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_start_bill_year").val('<%=year[0]%>');
  		  		$("#lab_start_bill_month").val('<%=month[0]%>');
  		  		$("#lab_start_bill_day").val('<%=days[0]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_start_bill_year").val(splitDate[0]);
  		  		$("#lab_start_bill_month").val(splitDate[1]);
  		  		$("#lab_start_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_get_bill_year").val('<%=defDate[0]%>');
  		  		$("#lab_get_bill_month").val('<%=defDate[1]%>');
  		  		$("#lab_get_bill_day").val('<%=defDate[2]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_get_bill_year").val(splitDate[0]);
  		  		$("#lab_get_bill_month").val(splitDate[1]);
  		  		$("#lab_get_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_apply_pay_bill_year").val('<%=year[0]%>');
  		  		$("#lab_apply_pay_bill_month").val('<%=month[0]%>');
  		  		$("#lab_apply_pay_bill_day").val('<%=days[0]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_apply_pay_bill_year").val(splitDate[0]);
  		  		$("#lab_apply_pay_bill_month").val(splitDate[1]);
  		  		$("#lab_apply_pay_bill_day").val(splitDate[2]);
  			}
  			
  			$("#lab_get_bill_year").attr("disabled",false);
		  	$("#lab_get_bill_month").attr("disabled",false);
		  	$("#lab_get_bill_day").attr("disabled",false);
		  	$("#lab_start_bill_year").attr("disabled",true);
		  	$("#lab_start_bill_month").attr("disabled",true);
		  	$("#lab_start_bill_day").attr("disabled",true);
		  	$("#lab_apply_pay_bill_year").attr("disabled",true);
  		  	$("#lab_apply_pay_bill_month").attr("disabled",true);
  		  	$("#lab_apply_pay_bill_day").attr("disabled",true);
  		  	$("#lab_pay_year").attr("disabled",true);
		  	$("#lab_pay_month").attr("disabled",true);
		  	$("#lab_pay_day").attr("disabled",true);
		  	$("#lab_kaipiao_billing").attr("disabled",false);

  		}
  		if(type==3){
  			if(dateArray[0]==""||null==dateArray[0]){
  		  		$("#lab_start_bill_year").val('<%=year[0]%>');
  		  		$("#lab_start_bill_month").val('<%=month[0]%>');
  		  		$("#lab_start_bill_day").val('<%=days[0]%>');
  			}else{
				var splitDate=getYearMonthDayByDate(dateArray[0]);
  				$("#lab_start_bill_year").val(splitDate[0]);
  		  		$("#lab_start_bill_month").val(splitDate[1]);
  		  		$("#lab_start_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[1]==""||null==dateArray[1]){
  		  		$("#lab_get_bill_year").val('<%=year[0]%>');
  		  		$("#lab_get_bill_month").val('<%=month[1]%>');
  		  		$("#lab_get_bill_day").val('<%=days[2]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[1]);
  		  		$("#lab_get_bill_year").val(splitDate[0]);
  		  		$("#lab_get_bill_month").val(splitDate[1]);
  		  		$("#lab_get_bill_day").val(splitDate[2]);
  			}
  			if(dateArray[2]==""||null==dateArray[2]){
  		  		$("#lab_apply_pay_bill_year").val('<%=defDate[0]%>');
  		  		$("#lab_apply_pay_bill_month").val('<%=defDate[1]%>');
  		  		$("#lab_apply_pay_bill_day").val('<%=defDate[2]%>');
  			}else{
  				var splitDate=getYearMonthDayByDate(dateArray[2]);
  		  		$("#lab_apply_pay_bill_year").val(splitDate[0]);
  		  		$("#lab_apply_pay_bill_month").val(splitDate[1]);
  		  		$("#lab_apply_pay_bill_day").val(splitDate[2]);
  			}
  			
  			$("#lab_apply_pay_bill_year").attr("disabled",false);
		  	$("#lab_apply_pay_bill_month").attr("disabled",false);
		  	$("#lab_apply_pay_bill_day").attr("disabled",false);
		  	$("#lab_start_bill_year").attr("disabled",true);
		  	$("#lab_start_bill_month").attr("disabled",true);
		  	$("#lab_start_bill_day").attr("disabled",true);
		  	$("#lab_get_bill_year").attr("disabled",true);
  		  	$("#lab_get_bill_month").attr("disabled",true);
  		  	$("#lab_get_bill_day").attr("disabled",true);
  		  	$("#lab_pay_year").attr("disabled",true);
		  	$("#lab_pay_month").attr("disabled",true);
		  	$("#lab_pay_day").attr("disabled",true);
		  	$("#lab_kaipiao_billing").attr("disabled",true);

  		}
  		$("#btn_confirm").click(function(){
  			if(type==0){
  				$( "#dialog" ).dialog("close");
  			}
  			//发起帐单，更改状态和时间
  			if(type==1){  
  				var billyear=$("#lab_start_bill_year").val();
  				var billmonth=$("#lab_start_bill_month").val();
  				var billday=$("#lab_start_bill_day").val();
				var date=billyear+"-"+billmonth+"-"+billday;
				ajaxGetgetCpBillingDate(id,type,3,date);
  				$( "#dialog" ).dialog("close");

  				
  			}
  			//收票时间，更改状态和时间
  			if(type==2){
  				var billyear=$("#lab_get_bill_year").val();
  				var billmonth=$("#lab_get_bill_month").val();
  				var billday=$("#lab_get_bill_day").val();
				var date=billyear+"-"+billmonth+"-"+billday;
				//开票金额
				var kaipiaoBilling=$("#lab_kaipiao_billing").val();
				ajaxGetgetCpBillingDate(id,type,4,date,kaipiaoBilling);
  				$( "#dialog" ).dialog("close");
  			}
  		//申请付款时间，更改状态和时间
  			if(type==3){
  				var billyear=$("#lab_apply_pay_bill_year").val();
  				var billmonth=$("#lab_apply_pay_bill_month").val();
  				var billday=$("#lab_apply_pay_bill_day").val();
				var date=billyear+"-"+billmonth+"-"+billday;
				ajaxGetgetCpBillingDate(id,type,5,date);
  				$( "#dialog" ).dialog("close");
  			}
  			
  		});
		
		$( "#dialog" ).dialog();
	}
	function ajaxGetgetCpBillingDate(id,type,status,date,money) {
		var result = "";
		$.ajax({
			url : "util.jsp",
			data : "cpbillingid=" + id + "&cpbilltype="+type+"&cpbillstatus="+status+"&cpdate="+date+"&cpkaipiaoBilling="+money,
			cache : false,
			async : false,
			success : function(html) {
				result = $.trim(html);
			}
		});
		return result;
	}

	
	
	function getYearMonthDayByDate(date){
  		var dateArray=date.split("-");
  		return dateArray;
	}
	<!--批量导出-->
	function exprortBatch()
	{
		var objs = document.getElementsByName("chk_data");
		var ids = "";
		for(i=0; i<objs.length; i++)
		{
			if(objs[i].checked==true)
			{
				ids += objs[i].id + ",";
			}
		}
		var ids=ids.substring(0, ids.length-1);
		if(ids==null||ids==""){
			alert("请选择需要导出的账单！");
			return;
		}
		window.location.href="util.jsp?cpbilling_ids=" + ids + "&exprort_zip=1&billingStatus=2";
	
	}
	function resetCheckBox(data)
	{
		var objs = document.getElementsByName("chk_data");
		for(i=0; i<objs.length; i++)
		{
			objs[i].checked = data;	
		}
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
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="cwjscpbilling.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp_id" onclick="namePicker(this,cpList,onCpDataSelect)">
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
						<option value="0">发起</option>
						<option value="1">运营审核</option>
						<option value="2">CP审核</option>
						<option value="3">发起帐单</option>
						<option value="4">收到票据</option>
						<option value="5">申请付款</option>
						<option value="6">已付款</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查     询" type="submit" />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" value="批量导出" type="button" onclick="exprortBatch()"  />
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
					<td>CP</td>
					<td>开始时间</td>
					<td>结束时间</td>
					<td>结算类型</td>
					<td>信息费</td>
					<td title="信息费*结算率">预支付</td>
					<td title="从结算款里核减掉的钱">核减款</td>
					<td>开票金额</td>
					<td title="预支付-核减款">实际预支付</td>
					<td title="财务最终支付给渠道的钱">财务支付</td>
					<td>备注</td>
					<td>创建时间</td>
					<td>状态</td>
					<td>操作</td>
					<td>全选<input type="checkbox" onclick="resetCheckBox(this.checked)" /></td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CpBillingModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><label id="lab_cp_name_<%= model.getId() %>"><%=model.getCpName() %></label></td>
					<td><label id="lab_start_date_<%= model.getId() %>"><%=model.getStartDate() %></label></td>
					<td><label id="lab_end_date_<%= model.getId() %>"><%=model.getEndDate()%></label></td>
					<td><label id="lab_js_name_<%= model.getId() %>"><%= model.getJsName()%></label></td>
					<td><label id="lab_amount_<%= model.getId() %>"><%= model.getAmount()%></label></td>
					<td><label id="lab_prebilling_<%= model.getId()%>"><%= model.getPreBilling() %></label></td>
					<td><label id="lab_reduce_amount_<%= model.getId()%>"><%= model.getReduceAmount()%></label></td>
					<td><label id="lab_kaipiao_billing_<%= model.getId()%>"><%= StringUtil.getDecimalFormat(model.getKaipiaoBilling()) %></label></td>
					<td><label id="lab_fuct_amount_<%= model.getId() %>"><%= model.getPreBilling() - model.getReduceAmount() %></label></td>
					<td><label id="lab_acturebilling_<%= model.getId() %>"><%= model.getActureBilling() %></label></td>
					<td><%=model.getRemark() %></td>
					<td><label id="lab_create_date_<%= model.getId() %>"><%= model.getCreateDate() %></label></td>
					<td><%= statusData[model.getStatus()] %></td>
					<td style="text-align: left">
						<%= btnMore[1].replaceAll("helloisthereany", "" + model.getId()) %>
						<a href="cpbillingdetail.jsp?query=<%= query %>&cpbillingid=<%= model.getId() %>" >详细</a>
						<%= btnStrings[model.getStatus()].replaceAll("helloisthereany", "" + model.getId()) %>
						<a href="cpbilling.jsp?type=1&cpbillingid=<%= model.getId() %>">导出</a>
					</td>
					<td><input type="checkbox" id="<%= model.getId() %>" name="chk_data"></td>
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
  		 CP:<label id="lab_cp_name">123456</label>
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
  		 核减款 ：<label id="lab_reduce_amount">123456</label>
  		<br />
  		 实际应收款 ：<label id="lab_fuct_amount">123456</label>
  		<br />
  		实际到款 ：<label id="lab_acturebilling">123456</label>
  		<br />
  		 创建时间 ：<label id="lab_create_date">00:00:00</label>
  		<br />
  		 账单时间 ：<label></label><select name="lab_start_bill_year" id="lab_start_bill_year" style="width: 55px">
						<%
							for(int i=0;i<year.length;i++)
							{
								
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
								

							}
						
							%>
						</select>-
						<select name="lab_start_bill_month" id="lab_start_bill_month" style="width: 55px">
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_start_bill_day" id="lab_start_bill_day" style="width: 55px">
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
  		  收票时间 ：<label></label><select name="lab_get_bill_year" id="lab_get_bill_year" >
						<%
							for(int i=0;i<year.length;i++)
							{
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_get_bill_month" id="lab_get_bill_month" >
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_get_bill_day" id="lab_get_bill_day" >
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
  		<label style="font-weight: bold;">开票金额：<input id="lab_kaipiao_billing" type="text"  value="0" style="background-color: #ccc" /></label>
  		<br />
  		申请付款时间  ：<label></label><select name="lab_apply_pay_bill_year" id="lab_apply_pay_bill_year" >
						<%
							for(int i=0;i<year.length;i++)
							{
								%>
							<option value="<%= year[i]%>"><%= year[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_apply_pay_bill_month" id="lab_apply_pay_bill_month" >
						<%
							for(int i=0;i<month.length;i++)
							{
								%>
							<option value="<%= month[i]%>"><%= month[i]%></option>	
								<%
							}
							%>
						</select>-
						<select name="lab_apply_pay_bill_day" id="lab_apply_pay_bill_day" >
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
  		 付款日期  ：<label></label><select name="lab_pay_year" id="lab_pay_year" >
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