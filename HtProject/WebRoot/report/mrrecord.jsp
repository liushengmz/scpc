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

	String startDate = StringUtil
			.getString(request.getParameter("startdate"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), StringUtil.getMonthEndDate());
	
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone"), -1);
	String spTroneName = StringUtil.getString(request.getParameter("sp_trone_name"), "");
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	Map<String, Object> map =  new MrSummerRecordServer().loadMrSummerRecordData(startDate, endDate, spId, spTroneName, spTroneId, pageIndex);
	
	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
		
	List<MrSummerRecordModel> list = (List<MrSummerRecordModel>)map.get("data");
	
	int rowCount = (Integer)map.get("count");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("sp_id", spId + "");
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	params.put("sp_trone_name",spTroneName);
	params.put("sp_trone",spTroneId + "");
	
	String pageData = PageUtil.initPageQuery("mrrecord.jsp",params,rowCount,pageIndex); 
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
		$("#sel_sp").val(joData.id);
		 troneChange();
	}
	
	$(function()
	{
		//SP的二级联动
		$("#sel_sp").val(<%= spId %>);
		$("#sel_sp").change(troneChange);
		troneChange();
		$("#sel_sp_trone").val(<%= spTroneId %>);
	});
	
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
	
	function delConfirm(mrSummerId,cpMrSummerId)
	{
		if(confirm("确定删除吗？"))
		{
			window.location.href="recordaction.jsp?type=2&mrsummerid="+ mrSummerId +"&cpmrsummerid=" + cpMrSummerId + "&<%= query %>";
		}
	}
	
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<dl>
			<dd class="ddbtn" ><a href="recordadd.jsp">增  加</a></dd>
			<form action="mrrecord.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>" style="width: 140px;"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" style="width: 138px;"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" style="width: 150px;" title="选择SP" onclick="namePicker(this,spList,onDataSpSelect)">
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
						<select name="sp_trone" id="sel_sp_trone" style="width: 150px;" ></select>
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
					<td>日期</td>
					<td>业务(SP-业务-通道-价格-CP)</td>
					<td>数据量(条)</td>
					<td>失败量(条)</td>
					<td>推送量(条)</td>
					<td>金额(元)</td>
					<td>失败金额(元 )</td>
					<td>推送金额(元)</td>
					<td>失败率</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(MrSummerRecordModel model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getFeeDate() %></td>
					<td><%= model.getSpShortName() + "-" + model.getSpTroneName() + "-" + model.getTroneName() + "-" + model.getPrice() + "-" + model.getCpShortName() %></td>
					<td><%= model.getDataRows() %></td>
					<td><%= model.getDataRows() - model.getShowDataRows()  %></td>
					<td><%= model.getShowDataRows() %></td>
					<td><%= model.getAmount() %></td>
					<td><%= model.getAmount() - model.getShowAmount() %></td>
					<td><%= model.getShowAmount() %></td>
					<td><%= StringUtil.getPercent(model.getDataRows() - model.getShowDataRows(), model.getDataRows()) %></td>
					<td>
						<a href="recordedit.jsp?mrsummerid=<%= model.getMrSummerId() %>&cpmrsummerid=<%= model.getCpMrSummerId() %>&<%= query %>" >修改</a>&nbsp;&nbsp;
						<a href="#" onclick="delConfirm(<%= model.getMrSummerId() %>,<%= model.getCpMrSummerId() %>)">删除</a>
					</td>
				</tr>
						<%
					}
				%>
			</tbody>
			<tbody>
				<tr>
					<td colspan="11" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>