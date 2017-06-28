<%@page import="com.system.model.params.ReportParamsModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.server.MrDetailServer"%>
<%@page import="com.system.vo.DetailDataVo"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String queryDate = StringUtil.getString(request.getParameter("date"), StringUtil.getDefaultDate());
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
	int troneId = StringUtil.getInteger(request.getParameter("trone_id"), -1);
	int showType = StringUtil.getInteger(request.getParameter("show_type"), -1);
	String joinId = StringUtil.getString(request.getParameter("joinid"), "");
	String title =  StringUtil.getString(request.getParameter("title"), "没有什么标题啊");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	String defaultMonth = sdf.format(new Date());
					
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	
	int chkType = StringUtil.getInteger(request.getParameter("chktype"), 1);
	
	String month = StringUtil.getString(request.getParameter("month"),defaultMonth);
	
	ReportParamsModel params = new ReportParamsModel();
	params.setStartDate(queryDate);
	params.setEndDate(queryDate);
	params.setSpId(spId);
	params.setCpId(cpId);
	params.setSpTroneId(spTroneId);
	params.setTroneId(troneId);
	params.setShowType(showType);
	params.setOnlyShowSync(true);
	
	
	List<DetailDataVo> list = new MrDetailServer().loadDetailDataBySummer(params, joinId);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 20px;" >
			<span style="font-size: 15px;font-weight: bold;padding-left: 20px;padding-top: 20px;margin-top: 50px;"><%=  "[" + queryDate + "][" + title + "]"  %></span>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>IMEI</td>
					<td>IMSI</td>
					<td>PHONE</td>
					<td>时间</td>
					<td>省份</td>
					<td>城市</td>
					<td>SP</td>
					<td>业务</td>
					<td>价格</td>
					<td>CP</td>
					<td>同步</td>
					
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					if(list!=null)
					for(DetailDataVo model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getImei() %></td>
					<td><%= model.getImsi() %></td>
					<td><%= model.getMobile()  %></td>
					<td><%= model.getCreateDate() %></td>
					<td><%= model.getProvinceName() %></td>
					<td><%= model.getCityName() %></td>
					<td><%= model.getSpName() %></td>
					<td><%= model.getSpTroneName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getSynFlag()==1 ? "已同步" : "未同步" %></td>
				</tr>
						<%
					}
				%>
			<tbody>
		</table>
	</div>
	
</body>
</html>