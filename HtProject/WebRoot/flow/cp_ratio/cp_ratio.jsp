<%@page import="com.system.flow.server.CpRatioServer"%>
<%@page import="com.system.flow.model.CpRatioModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.flow.server.CpServer"%>
<%@page import="com.system.flow.model.CpModel"%>
<%@page import="com.system.flow.server.BasePriceServer"%>
<%@page import="com.system.flow.model.BasePriceModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), 0);

	String cpName = "";
	CpServer cpServer = new CpServer();
	List<CpModel> list = cpServer.loadCp(1);
	CpModel cpModel = cpServer.getCpById(cpId);
	
	if(cpModel!=null)
		cpName = cpModel.getShortName();
	
	List<CpRatioModel> cpRatioList = new CpRatioServer().loadCpRatio(cpId);
	
	String[] status = {"非合作","合作"};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../../sysjs/base.js"></script>
<script type="text/javascript" src="../../sysjs/AndyNamePickerV20.js"></script>
<link href="../../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	
	var cpList = new Array();
	
	<%
	for(CpModel model : list)
	{
		%>
		cpList.push(new joSelOption(<%= model.getId() %>,1,'<%= model.getShortName() %>'));
		<%
	}
	%>
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp_name").val(joData.text);
		$("#sel_cp_id").val(joData.id);
	}
	
	function editCpRatio()
	{
		var cpId = $("#sel_cp_id").val();
		if(cpId<=0)
		{
			alert("请选择CP");
			return;
		}
		window.location.href = "cp_ratio_edit.jsp?cp_id=" + cpId;
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<form action="cp_ratio.jsp" method="get" id="formid">
					<dl>					
						<dd class="dd01_me" style="margin-left: -38px;">CP名称</dd>
						<dd class="dd03_me">
							<input  type="text" id="sel_cp_name" value="<%= cpName %>" onclick="namePicker(this,cpList,onCpDataSelect)" style="width: 100px;" readonly="readonly" />
							<input  type="hidden" id="sel_cp_id" name="cp_id" value="<%= cpId %>" />
						</dd>
						<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
							<input class="btn_match" name="search" value="查 询" type="submit">
						</dd>
						<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
							<input class="btn_match" name="search" value="修  改" type="button" onclick="editCpRatio()" >
						</dd>
					</dl>
				</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>运营商</td>
					<td>省份</td>
					<td>折扣</td>
					<td>状态</td>
				</tr>
			</thead>
			<tbody>
				<%
					for (int i=0; i<cpRatioList.size(); i++)
					{
						CpRatioModel model = cpRatioList.get(i);
				%>
				<tr>
					<td><%= i+1 %></td>
					<td><%= model.getOperatorName() %></td>
					<td><%= model.getProName() %></td>
					<td><%= model.getRatio() %></td>
					<td><%= status[model.getStatus()] %></td>
				</tr>
				<%
					}
				%>
			</tbody>	
		</table>
	</div>
	
</body>
</html>