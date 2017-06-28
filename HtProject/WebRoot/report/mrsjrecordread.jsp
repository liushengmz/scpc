<%@page import="java.util.Date"%>
<%@page import="com.system.model.SjMrSummerRecordModel"%>
<%@page import="com.system.server.SjMrSummerRecordServer"%>
<%@page import="com.system.model.analy.MrSummerRecordModel"%>
<%@page import="com.system.server.analy.MrSummerRecordServer"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.CityServer"%>
<%@page import="com.system.model.CityModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneServer"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.MrServer"%>
<%@page import="com.system.model.MrReportModel"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.xy.UserServer"%>
<%@page import="com.system.model.xy.XyUserModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String query = request.getQueryString();

	String startMonth = StringUtil
			.getString(request.getParameter("start_month"), "2017-01");
	String endMonth = StringUtil
			.getString(request.getParameter("end_month"), StringUtil.getMonthFormat2(new Date()));
	
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int saveLocateFlag = StringUtil.getInteger(request.getParameter("save_locate"), 0);
	
	SjMrSummerRecordModel paramsModel = new SjMrSummerRecordModel();
	
	paramsModel.setSpId(spId);
	paramsModel.setSpTroneId(spTroneId);
	paramsModel.setSaveLocate(saveLocateFlag);

	Map<String, Object> map =  new SjMrSummerRecordServer().loadSjMrSummerRecordData(startMonth, endMonth, paramsModel, pageIndex);
	
	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
		
	List<SjMrSummerRecordModel> list = (List<SjMrSummerRecordModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("sp_id", spId + "");
	params.put("start_month", startMonth);
	params.put("end_date", endMonth);
	params.put("sp_trone_id",spTroneId + "");
	params.put("save_locate",saveLocateFlag + "");
	
	
	String[] saveLocate = {"","大数据平台","商务平台","两者"};
	
	String pageData = PageUtil.initPageQuery("mrsjrecord.jsp",params,rowCount,pageIndex); 
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
	<%
	for(SpTroneModel spTroneModel : spTroneList)
	{
		%>
	spTroneArray.push(new joBaseObject(<%= spTroneModel.getId() %>,<%=spTroneModel.getSpId() %>,'<%= spTroneModel.getSpTroneName() %>'));	
		<%
	}
	%>
	
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
			troneChange();
			return;
		}
		
		$("#input_sp_id").val(joData.text);
		$("#hid_sp_id").val(joData.id);
		
		troneChange();
	}
	
	$(function()
	{
		//SP的二级联动
		for(var i=0; i<spList.length; i++)
		{
			if(spList[i].id == <%= spId %>)
			{
				$("#input_sp_id").val(spList[i].text);
				$("#hid_sp_id").val(spList[i].id);
				break;	
			}
		}
		
		$("#sel_save_locate").val(<%= saveLocateFlag %>);
		
		$("#sel_sp_trone").val(<%= spTroneId %>);
	});
	
	function troneChange()
	{
		var spId = $("#hid_sp_id").val();
		
		$("#sel_sp_trone").empty(); 
		
		$("#sel_sp_trone").append("<option value='-1'>全部</option>");
		
		if(spId=="-1")
			return;
		
		for(i=0; i<spTroneArray.length; i++)
		{
			if(spTroneArray[i].pid==spId)
			{
				$("#sel_sp_trone").append("<option value='" + spTroneArray[i].id + "'>" + spTroneArray[i].name + "</option>");
			}
		}
	}
	
	function delRecordData(delId)
	{
		if(confirm("确定删除这一条数据吗？"))
		{
			window.location.href="recordaction.jsp?type=7&delid="+ delId + "&query=<%= query %>";
		}
	}
	
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<dl>
			<form action="mrsjrecordread.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="start_month"  type="text" value="<%= startMonth %>" style="width: 90px;"
							onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="end_month" type="text" value="<%= endMonth %>" style="width: 90px;"
							onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">SP</dd>
					<dd class="dd03_me">
						<input type="hidden" name="sp_id" id="hid_sp_id" value="-1">
						<input id="input_sp_id" style="width:90px;" onclick="namePicker(this,spList,onDataSpSelect)" />
					</dd>
					<dd class="dd01_me">SP业务</dd>
						<dd class="dd04_me">
						<select name="sp_trone_id" id="sel_sp_trone" style="width: 120px;" ></select>
					</dd>
					<dd class="dd01_me">存储位置</dd>
						<dd class="dd04_me">
						<select name="save_locate" id="sel_save_locate" style="width: 120px;" >
							<option value="0">未选择</option>
							<option value="1">大数据平台</option>
							<option value="2">商务</option>
							<option value="3">两者</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
					</dl>
			</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SP</td>
					<td>业务</td>
					<td>价格</td>
					<td>CP</td>
					<td>TroneOrderId</td>
					<td>月份</td>
					<td>SP数据条数</td>
					<td>SP金额</td>
					<td>CP数据条数</td>
					<td>CP金额</td>
					<td>存储位置</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(SjMrSummerRecordModel model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getSpName() %></td>
					<td><%= model.getSpTroneName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getTroneOrderId() %></td>
					<td><%= model.getYear() + "-" + String.format("%02d",model.getMonth()) %></td>
					<td><%= model.getSpDataRows()  %></td>
					<td><%= model.getSpAmount() %></td>
					<td><%= model.getCpDataRows() %></td>
					<td><%= model.getCpAmount() %></td>
					<td><%= saveLocate[model.getSaveLocate()] %></td>
					<td>
						<a href="#" onclick="delRecordData(<%= model.getId() %>)">删除</a>
					</td>
				</tr>
						<%
					}
				%>
			</tbody>
			<tbody>
				<tr>
					<td colspan="28" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>