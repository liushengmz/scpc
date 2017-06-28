<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.model.CpBillingModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.SpBillingModel"%>
<%@page import="com.system.server.SpBillingServer"%>
<%@page import="com.system.server.CpBillingServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.SpBillExportModel" %>
<%@page import="com.system.model.CpBillExportModel" %>

<%@page import="com.system.model.SettleAccountModel" %>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.server.SettleAccountServer"%>



<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	int id = StringUtil.getInteger(request.getParameter("spbillingid"),-1);
	int type = StringUtil.getInteger(request.getParameter("type"),-1);//0：获取上游确认帐单日期#结算申请开票日期#财务开票日期字符串
	int status=StringUtil.getInteger(request.getParameter("status"),-1);
	String date=StringUtil.getString(request.getParameter("date"),"");
	String startDateExp=StringUtil.getString(request.getParameter("start_date"), "");
	String endDateExp=StringUtil.getString(request.getParameter("end_date"),"");
	String jsTypeExp=StringUtil.getString(request.getParameter("js_types"), "");
	String statusExp=StringUtil.getString(request.getParameter("status_exp"), "");
	int load=StringUtil.getInteger(request.getParameter("load"), -1);
	int spIdExp=StringUtil.getInteger(request.getParameter("sp_id"), -1);
	
	int cpBillingId = StringUtil.getInteger(request.getParameter("cpbillingid"),-1);
	int cpBillingType = StringUtil.getInteger(request.getParameter("cpbilltype"),-1);//CP账单type
	int cpBillingStatus=StringUtil.getInteger(request.getParameter("cpbillstatus"),-1);//CP账单状态
	String cpdate=StringUtil.getString(request.getParameter("cpdate"),"");			  //CP账单时间
	float cpkaipiaoBilling=StringUtil.getFloat(request.getParameter("cpkaipiaoBilling"), 0);//开票金额
	
	//CP账单导出数据参数
	String startDateCpExp=StringUtil.getString(request.getParameter("cp_start_date"), "");
	String endDateCpExp=StringUtil.getString(request.getParameter("cp_end_date"),"");
	String jsTypeCpExp=StringUtil.getString(request.getParameter("cp_js_types"), "");
	String statusCpExp=StringUtil.getString(request.getParameter("cp_status_exp"), "");
	int cpLoad=StringUtil.getInteger(request.getParameter("cp_load"), -1);
	int cpIdExp=StringUtil.getInteger(request.getParameter("cp_id_exp"), -1);
	
	//CP运营账单批量导出
	String cpbillingIds=StringUtil.getString(request.getParameter("cpbilling_ids"), "");
	int exportZip=StringUtil.getInteger(request.getParameter("exprort_zip"), -1);

	//CP账单基础数据导出
	String baseCpIds=StringUtil.getString(request.getParameter("base_cp_ids"), "");
	int dateType=StringUtil.getInteger(request.getParameter("base_js_type"), -1);
	String baseStartDate=StringUtil.getString(request.getParameter("base_startdate"), "");
	String baseEndDate=StringUtil.getString(request.getParameter("base_enddate"), "");
	if(type==0){
	SpBillingModel billingModel=new SpBillingServer().getSpBillingModel(id);
	String data=billingModel.getBillingDate()+"#"+billingModel.getApplyKaipiaoDate()+"#"+billingModel.getKaipiaoDate()+"#"+billingModel.getPayTime();
	out.clear();
	out.print(data); 
	}
	//更新状态和时间
	if(type==1||type==2||type==3){    
		new SpBillingServer().updateSpBillingModel(id, type, status, date);
	}
	if(load>=0)
	{
		SpBillingServer server = new SpBillingServer();
		List<SpBillExportModel> list = server.exportExcelData(startDateExp,endDateExp,spIdExp,jsTypeExp,statusExp);
		if(StringUtil.isNullOrEmpty(endDateExp)){
			endDateExp=StringUtil.getDefaultDate();
		}
		
		response.setContentType("application/octet-stream;charset=utf-8");
		String fileName ="";
		if(!StringUtil.isNullOrEmpty(startDateExp)){
		String fileStartDate=startDateExp.replace("-", "");
		fileName+=fileStartDate;
		}
		fileName+="-";
		if(!StringUtil.isNullOrEmpty(endDateExp)){
		String fileEndDate=endDateExp.replace("-", "");
		fileName+=fileEndDate;
		}
		fileName+="-";
		if(spIdExp>0){
			SpModel spModel=new SpServer().loadSpById(spIdExp);
			fileName+=spModel.getShortName();
		}else{
			fileName+="全部上游";
		}
		fileName+="-账单.xls";
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) 
		{
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} 
		else 
		{
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}

		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		server.exportSettleAccount(startDateExp, endDateExp, list, response.getOutputStream());
		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
	}
	//CP账单导出
	if(cpLoad>=0)
	{
		CpBillingServer server = new CpBillingServer();
		List<CpBillExportModel> list = server.exportExcelData(startDateCpExp,endDateCpExp,cpIdExp,jsTypeCpExp,statusCpExp);
		if(StringUtil.isNullOrEmpty(endDateCpExp)){
			endDateCpExp=StringUtil.getDefaultDate();
		}
		
		response.setContentType("application/octet-stream;charset=utf-8");
		String fileName ="";
		if(!StringUtil.isNullOrEmpty(startDateCpExp)){
		String fileStartDate=startDateCpExp.replace("-", "");
		fileName+=fileStartDate;
		}
		fileName+="-";
		if(!StringUtil.isNullOrEmpty(endDateCpExp)){
		String fileEndDate=endDateCpExp.replace("-", "");
		fileName+=fileEndDate;
		}
		fileName+="-";
		if(cpIdExp>0){
			CpModel cpModel=new CpServer().loadCpById(cpIdExp);
			fileName+=cpModel.getShortName();
		}else{
			fileName+="全部渠道";
		}
		fileName+="-账单.xls";
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) 
		{
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} 
		else 
		{
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}

		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		server.exportSettleAccount(startDateCpExp, endDateCpExp, list, response.getOutputStream());
		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
	}
	//CP账单处理
	if(cpBillingType==0){
		CpBillingModel billingModel=new CpBillingServer().getCpBillingModel(cpBillingId);
		String data=billingModel.getStartBillDate()+"#"+billingModel.getGetBillDate()+"#"+billingModel.getApplyPayBillDate()+"#"+billingModel.getPayTime();
		out.clear();
		out.print(data); 
		}
	//CP账单更新状态和时间
		if(cpBillingType==1||cpBillingType==2||cpBillingType==3){    
			new CpBillingServer().updateCpBillingModel(cpBillingId, cpBillingType, cpBillingStatus, cpdate,cpkaipiaoBilling);
		}
	if(exportZip==1){
		response.setContentType("application/octet-stream;charset=utf-8");
		String defultDate=StringUtil.getDefaultDate();
		int billingStatus=StringUtil.getInteger(request.getParameter("billingStatus"), -1);
		String fileName="";
		if(billingStatus==1){
			fileName = "运营账单导出汇总.zip";
		}else if(billingStatus==2){
			fileName = "CP结算账单导出汇总.zip";
		}else if(billingStatus==3){
			fileName = "财务账单导出汇总.zip";
		}else{
			fileName = "CP账单导出汇总.zip";
		}
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
		
		accountServer.exportSettleAccountBatchZip(2,cpbillingIds,response.getOutputStream());
		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
	}
	//CP账单基础数据批量导出
	if(exportZip==2){
		response.setContentType("application/octet-stream;charset=utf-8");
		String defultDate=StringUtil.getDefaultDate();
		String fileName = "CP账单基础数据.zip";
		
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
		accountServer.exportSettleAccountBaseBatchZip(2, baseCpIds, baseStartDate, baseEndDate, dateType, response.getOutputStream());

		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
		
	}
	
%>
