<%@page import="com.system.server.FinalcialSpCpDataServerSj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.FinalcialSpCpDataServer"%>
<%@page import="com.system.vmodel.FinancialSpCpDataShowModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil.getString(request.getParameter("enddate"), StringUtil.getMonthEndDate());
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int dataType = StringUtil.getInteger(request.getParameter("data_type"), -1);
	int loadData = StringUtil.getInteger(request.getParameter("load"),-1);
	
	List<SpModel> spList = new SpServer().loadSp();
	List<CpModel> cpList = new CpServer().loadCp();
	
	//List<FinancialSpCpDataShowModel> list = loadData > 0 ?  new FinalcialSpCpDataServerSj().loadData(startDate, endDate, spId, cpId, dataType):new ArrayList<FinancialSpCpDataShowModel>();
	List<FinancialSpCpDataShowModel> list = loadData > 0 ?  new FinalcialSpCpDataServer().loadData(startDate, endDate, spId, cpId, dataType):new ArrayList<FinancialSpCpDataShowModel>();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

	$(function()
	{
		$("#sel_sp").val("<%= spId %>");
		$("#sel_cp").val("<%= cpId %>");
		$("#sel_data_type").val(<%= dataType %>);
	});

	function subForm() 
	{
		document.getElementById("exportform").submit();
	}
	var spList = new Array();
	<%for (SpModel spModel : spList) {%>
		spList.push(new joSelOption(<%=spModel.getId()%>,1,'<%=spModel.getShortName()%>'));
		<%}%>
	
	var cpList = new Array();
	<%for (CpModel cpModel : cpList) {%>
		cpList.push(new joSelOption(<%=cpModel.getId()%>,1,'<%=cpModel.getShortName()%>'));
		<%}%>
		
	function onSpDataSelect(joData)
	{
		$("#sel_sp").val(joData.id);
	}
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp").val(joData.id);
	}
</script>
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="spcpdatasj.jsp" method="get" id="exportform">
				<dl>
					<input type="hidden" value="1" name="load" />
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
					<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" onclick="namePicker(this,spList,onSpDataSelect)" title="选择SP">
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
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp" onclick="namePicker(this,cpList,onCpDataSelect)" title="选择CP">
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
					<!--  
					<dd class="dd01_me">数据类型</dd>
						<dd class="dd04_me">
						<select name="data_type" id="sel_data_type" style="width: 100px;">
							<option value="-1">全部</option>
							<option value="0">普通</option>
							<option value="1">包月</option>
						</select>
					</dd>
					-->
					<dd class="dd00_me"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
					<!--  
					<dd class="dd01_me">
						<a style="color: blue;"
							href="mr_lr_spcp.jsp?<%=request.getQueryString()%>">查看利润</a>
					</dd>
					-->
				</dl>
			</form>
			</div>
			<br /><br />
			<table cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td>SP全称</td>
						<td>SP简称</td>
						<td>SP业务名称</td>
						<!-- <td>SP数据量</td>-->
						<td>SP金额</td>
						<td>SP结算比例</td>
						<td>CP全称</td>
						<td>CP简称</td>
						<!-- <td>CP数据量</td> -->
						<td>CP金额</td>
						<td>CP结算比例</td>
					</tr>
				</thead>
				<tbody>
				<%
					for(FinancialSpCpDataShowModel model : list)
					{
						out.println("<tr><td rowspan=\"" + model.spRowSpan + "\">" + model.spFullName + "</td><td rowspan=\"" + model.spRowSpan + "\">" + model.spShortName + "</td>");
						for(FinancialSpCpDataShowModel.SpTroneModel spTroneModel : model.list)
						{
							out.println("<td rowspan=\"" + spTroneModel.spTroneRowSpan + "\">" + spTroneModel.spTroneName + "</td>");
							for(FinancialSpCpDataShowModel.SpTroneModel.CpModelData cpModelData : spTroneModel.list)
							{
								out.println("<td>" + cpModelData.amount + "</td><td>"+ 
										spTroneModel.spJieSuanLv +"</td><td>" + cpModelData.cpFullName + "</td><td>" + cpModelData.cpShortName + "</td><td>"+ cpModelData.showAmount +"</td><td>" + cpModelData.cpJieSuanLv + "</td></tr>");
							}
						}
					}
				%>
				</tbody>
			</table>

	</div>
</body>
</html>