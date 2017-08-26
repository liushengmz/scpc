<%@page import="com.system.server.MoServer"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.vo.DetailDataVo"%>
<%@page import="com.system.server.MrDetailServer"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="java.util.List"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getDefaultDate() + " 00:00:00";

	String defaultEndDate = StringUtil.getDefaultDate() + " 23:59:59";
	
	boolean isFirstLoad = StringUtil.getInteger(request.getParameter("first_load"), 0)  == 1 ? true : false;
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);
	
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone"), -1);
	String keyWord  = StringUtil.getString(request.getParameter("keyword"), "");
	
	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
	List<DetailDataVo> list = isFirstLoad ? new MoServer().loadMoDetail(startDate, endDate, spId, spTroneId, keyWord) : new ArrayList<DetailDataVo>();
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
		$("#sel_sp").val(joData.id);
		troneChange();
	}
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp").val(joData.id);
		troneOrderChange();
	}

	var spTroneArray = new Array();
	<%
	for(SpTroneModel spTroneModel : spTroneList)
	{
		%>
	spTroneArray.push(new joBaseObject(<%= spTroneModel.getId() %>,<%=spTroneModel.getSpId() %>,'<%= spTroneModel.getSpTroneName() %>'));	
		<%
	}
	%>
	
	$(function()
	{
		//SP的二级联动
		$("#sel_sp").val(<%= spId %>);
		$("#sel_sp").change(troneChange);
		troneChange();
		$("#sel_sp_trone").val(<%= spTroneId %>);
	});
	
	
	var npSpTroneArray = new Array();
	
	<%
	for(SpTroneModel spTroneModel : spTroneList)
	{
		%>
		npSpTroneArray.push(new joSelOption(<%= spTroneModel.getId() %>,<%=spTroneModel.getSpId() %>,'<%= spTroneModel.getSpTroneName() %>'));	
		<%
	}
	%>
	
	function npSpTroneChange(jodata)
	{
		$("#sel_sp_trone").val(jodata.id);
	}
	
	function troneChange()
	{
		var spId = $("#sel_sp").val();
		
		$("#sel_sp_trone").empty(); 
		$("#sel_sp_trone").append("<option value='-1'>全部</option>");
		for(i=0; i<spTroneArray.length; i++)
		{
			if(spTroneArray[i].pid==spId || spId=="-1")
			{
				$("#sel_sp_trone").append("<option value='" + spTroneArray[i].id + "'>" + spTroneArray[i].name + "</option>");
			}
		}
	}	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mo_detail.jsp"  method="post" style="margin-top: 10px" >
				<input type="hidden" name="first_load" value="1" />
				<dl>
					<dd class="dd01_me" style="margin-left:-15px">开始时间</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 120px;">
					</dd>
					<dd class="dd01_me">结束时间</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 120px;">
					</dd>
					<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" style="width: 110px;" title="选择SP" onclick="namePicker(this,spList,onSpDataSelect)">
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
					<dd class="dd01_me">SP业务</dd>
						<dd class="dd04_me">
						<select name="sp_trone" id="sel_sp_trone" style="width: 110px;" ></select>
					</dd>
					<dd class="dd01_me">关键字</dd>
						<dd class="dd03_me">
						<input type="text" value="<%= keyWord %>" name="keyword" />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit"  />
					</dd>
					<dd><label style="margin-left: 20px;">总条数：<%= list.size() %></label></dd>
					</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>CP</td>
					<td>SP</td>
					<td>SP业务</td>
					<td>IMEI</td>
					<td>IMSI</td>
					<td>手机号</td>
					<td>LinkID</td>
					<td>时间</td>
					<td>省份</td>
					<td>城市</td>
					<td>价格</td>
					<td>指令</td>
					<td>配置指令</td>
					<td>配置通道</td>
				</tr>
			</thead>
			<tbody>		
			<tbody>
				<%
					int index = 1;
					if(list!=null)
					for(DetailDataVo model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getSpName() %></td>
					<td><%= model.getSpTroneName() %></td>
					<td><%= model.getImei() %></td>
					<td><%= model.getImsi() %></td>
					<td><%= model.getMobile()  %></td>
					<td><%= model.getLinkId() %></td>
					<td><%= model.getCreateDate() %></td>
					<td><%= model.getProvinceName() %></td>
					<td><%= model.getCityName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getOrder() %></td>
					<td><%= model.getConfigOrder() %></td>
					<td><%= model.getConfigTrone() %></td>
				</tr>
						<%
					}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>