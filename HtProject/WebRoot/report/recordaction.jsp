<%@page import="com.system.model.SjMrSummerRecordModel"%>
<%@page import="com.system.server.SjMrSummerRecordServer"%>
<%@page import="com.system.server.analy.MrSummerRecordServer"%>
<%@page import="com.system.model.analy.MrSummerRecordModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	//是否存在相同数据
	if(type==1)
	{
		String feeDate = StringUtil.getString(request.getParameter("fee_date"), "");
		
		int troneOrderId = StringUtil.getInteger(request.getParameter("trone_order_id"), -1);
		
		if(StringUtil.isNullOrEmpty(feeDate)||troneOrderId<0)
			return;
		
		MrSummerRecordModel model = new MrSummerRecordModel();
		
		model.setFeeDate(feeDate);
		
		model.setTroneOrderId(troneOrderId);
		
		boolean result = new MrSummerRecordServer().existMrSummerRecord(model);
		
		out.print(result);
	}
	//删除
	else if(type==2)
	{
		String query =  request.getQueryString();
		
		int mrSummerId = StringUtil.getInteger(request.getParameter("mrsummerid"), -1);
		int cpMrSummerId = StringUtil.getInteger(request.getParameter("cpmrsummerid"),-1);
		
		if(mrSummerId>0 && cpMrSummerId>0)
		{
			new MrSummerRecordServer().deleteMrSummerRecordModel(mrSummerId, cpMrSummerId);
		}
		
		response.sendRedirect("mrrecord.jsp?" + query);
		return;
	}
	//增加
	else if(type==3)
	{
		int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
		int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
		int troneId = StringUtil.getInteger(request.getParameter("trone_id"), -1);
		int troneOrderId = StringUtil.getInteger(request.getParameter("trone_order_id"), -1);
		String feeDate = StringUtil.getString(request.getParameter("fee_date"), "");
		int dataRows = StringUtil.getInteger(request.getParameter("data_rows"), 0);
		int showDataRows = StringUtil.getInteger(request.getParameter("show_data_rows"), -1);
		float amount = StringUtil.getFloat(request.getParameter("amount"), 0);
		float showAmount =  StringUtil.getFloat(request.getParameter("show_amount"), 0);

		MrSummerRecordModel model = new MrSummerRecordModel();
		
		model.setSpId(spId);
		model.setCpId(cpId);
		model.setTroneId(troneId);
		model.setTroneOrderId(troneOrderId);
		model.setFeeDate(feeDate);
		model.setDataRows(dataRows);
		model.setShowDataRows(showDataRows);
		model.setAmount(amount);
		model.setShowAmount(showAmount);
		
		new MrSummerRecordServer().addMrSummerRecordModel(model);
		
		out.print(true);
	}
	//修改
	else if(type==4)
	{
		int dataRows = StringUtil.getInteger(request.getParameter("data_rows"), 0);
		int showDataRows = StringUtil.getInteger(request.getParameter("show_data_rows"), -1);
		float amount = StringUtil.getFloat(request.getParameter("amount"), 0);
		float showAmount =  StringUtil.getFloat(request.getParameter("show_amount"), 0);
		int mrSummerId = StringUtil.getInteger(request.getParameter("mrsummerid"), -1);
		int cpMrSummerId = StringUtil.getInteger(request.getParameter("cpmrsummerid"), -1);
		
		MrSummerRecordModel model = new MrSummerRecordModel();
		
		model.setDataRows(dataRows);
		model.setShowDataRows(showDataRows);
		model.setAmount(amount);
		model.setShowAmount(showAmount);
		model.setMrSummerId(mrSummerId);
		model.setCpMrSummerId(cpMrSummerId);
		
		if(mrSummerId>0 && cpMrSummerId>0)
		{
			new MrSummerRecordServer().updateMrSummerRecordModel(model);
			
			out.print(true);
			
			return;
		}
		
		out.print(false);
	}
	//检查是否有
	//SJ数据增加前检查一下是否存在数据，分两步检查，一是检查 daily_log.tbl_sj_ori_data 同一个 trone_order_id 在同一个月份有没有数据，
	//第二步是检查SUMMER表里面有没有指定的数据
	else if(type==5)
	{
		int year = StringUtil.getInteger(request.getParameter("year"), 0);
		
		int month = StringUtil.getInteger(request.getParameter("month"), 0);
		
		int troneOrderId = StringUtil.getInteger(request.getParameter("trone_order_id"), -1);
		
		if(year<=0 || month <= 0 || troneOrderId<0)
			return;
		
		SjMrSummerRecordServer sjServer = new  SjMrSummerRecordServer();
		
		if(sjServer.isExistDataInRecord(year, month, troneOrderId))
		{
			out.print(true);
			return;
		}
		
		if(sjServer.isExistDataInSummer(year, month, troneOrderId))
		{
			out.print(true);
			return;
		}
		
		out.print(false);
	}
	//ADD SJ SP DATA RECORD
	else if(type==6)
	{
		int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
		int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
		int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
		int troneId = StringUtil.getInteger(request.getParameter("trone_id"), -1);
		int troneOrderId = StringUtil.getInteger(request.getParameter("trone_order_id"), -1);
		int year = StringUtil.getInteger(request.getParameter("year"), 0);
		int month = StringUtil.getInteger(request.getParameter("month"), 0);
		int spDataRows = StringUtil.getInteger(request.getParameter("data_rows"), 0);
		int cpDataRows = StringUtil.getInteger(request.getParameter("show_data_rows"), -1);
		float spAmount = StringUtil.getFloat(request.getParameter("amount"), 0);
		float cpAmount =  StringUtil.getFloat(request.getParameter("show_amount"), 0);
		float price = StringUtil.getFloat(request.getParameter("price"), 0);
		int saveLocate = StringUtil.getInteger(request.getParameter("save_locate"), 1);
		
		
		if(year<=0 || month<= 0 || spDataRows<=0 || cpDataRows<=0 || spAmount<=0 || cpAmount<0 || price<=0 || troneId<=0 || troneOrderId<=0)
		{
			out.print(false);
			return;
		}
		
		SjMrSummerRecordModel recordModel = new SjMrSummerRecordModel();
		recordModel.setSpTroneId(spTroneId);
		recordModel.setSpId(spId);
		recordModel.setCpId(cpId);
		recordModel.setTroneOrderId(troneOrderId);
		recordModel.setYear(year);
		recordModel.setMonth(month);
		recordModel.setSpDataRows(spDataRows);
		recordModel.setCpDataRows(cpDataRows);
		recordModel.setSpAmount(spAmount);
		recordModel.setCpAmount(cpAmount);
		recordModel.setSaveLocate(saveLocate);
		recordModel.setPrice(price);
		recordModel.setTroneId(troneId);
		
		SjMrSummerRecordServer server = new SjMrSummerRecordServer(); 
		server.addSjMrSummerRecord(recordModel);
		
		//这里还要执行数据分析
		server.addSjMrSummer(recordModel);
		out.print(true);
	}
	//删除指定的大数据
	else if(type==7)
	{
		int delId = StringUtil.getInteger(request.getParameter("delid"), -1);
		
		
		if(delId<=0)
		{
			response.sendRedirect("mrsjrecord.jsp?query=" + StringUtil.getString(request.getParameter("query"), ""));
			return;
		}
		
		SjMrSummerRecordServer server = new SjMrSummerRecordServer();
		
		SjMrSummerRecordModel recordModel = server.getSjMrSummerRecord(delId);
		
		if(recordModel!=null)
		{
			server.delSjMrSummerRecord(recordModel.getId());
			server.delSjMrSummer(recordModel);
		}
		
		response.sendRedirect("mrsjrecord.jsp?query=" + StringUtil.getString(request.getParameter("query"), ""));
	}
	
%>