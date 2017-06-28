<%@page import="com.system.server.FinalcialSpCpDataServer"%>
<%@page import="com.system.model.SpcpProfitModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.util.ArrayList"%>
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
<%@page import="com.system.model.xy.XyUserModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil.getString(request.getParameter("enddate"), StringUtil.getMonthEndDate());
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int spTrone = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);

	int dataType = StringUtil.getInteger(request.getParameter("data_type"), -1);
	int loadData = StringUtil.getInteger(request.getParameter("load"),-1);
	
	List<SpModel> spList = new SpServer().loadSp();
	List<CpModel> cpList = new CpServer().loadCp();
	List<SpTroneModel> spTroneList=new SpTroneServer().loadSpTroneList();
	List<SpcpProfitModel> list = new FinalcialSpCpDataServer().loadProfitData(startDate, endDate, spId, spTrone, cpId);
	
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
	
	var cpList = new Array();
	<%
	for(CpModel cpModel : cpList)
	{
		%>
		cpList.push(new joSelOption(<%= cpModel.getId() %>,1,'<%= cpModel.getShortName() %>'));
		<%
	}
	%>
	
	var spTroneArray = new Array();
	<%
	for(SpTroneModel spTroneModel : spTroneList)
	{
		%>
	spTroneArray.push(new joBaseObject(<%= spTroneModel.getId() %>,<%=spTroneModel.getSpId() %>,'<%= spTroneModel.getSpTroneName() %>'));	
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
	function troneChange()
	{
		var spId = $("#sel_sp").val();
		
		$("#sp_trone_id").empty(); 
		$("#sp_trone_id").append("<option value='-1'>全部</option>");
		for(i=0; i<spTroneArray.length; i++)
		{
			if(spTroneArray[i].pid==spId || spId=="-1")
			{
				$("#sp_trone_id").append("<option value='" + spTroneArray[i].id + "'>" + spTroneArray[i].name + "</option>");
			}
		}
	}
	

	
	function joTroneOrder(id,cpId,troneOrderName)
	{
		var obj = {};
		obj.id = id;
		obj.cpId = cpId;
		obj.troneOrderName = troneOrderName;
		return obj;
	}
	
	$(function()
	{		
		$("#sel_sp").val(<%= spId %>);
		$("#sel_sp").change(troneChange);
		troneChange();
		$("#sel_cp").val(<%=cpId%>);
		$("#sel_data_type").val(<%=dataType%>);
		$("#sp_trone_id").val(<%=spTrone%>);


	});
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mr_lr_spcp.jsp"  method="get" style="margin-top: 10px">
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
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone_id" id="sp_trone_id" title="选择SP业务">
								
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
				
					<dd class="dd00_me"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
					<dd class="dd01_me">
						<a style="color: blue;"
							href="spcpdata.jsp?<%=request.getQueryString()%>">返回</a>
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SP</td>
					<td>SP业务</td>
					<td>收入</td>
					<td>CP</td>
					<td>支出</td>
					<td>利润</td>
				</tr>
			</thead>
			<tbody>
				<%
					int index=1;
					for(SpcpProfitModel model : list)
					{
						out.println("<tr><tr><td rowspan=\"" + model.spRowSpan + "\">" + index++ + "</td><td rowspan=\"" + model.spRowSpan + "\">" + model.spFullName + "</td>");
						double lrcount=0;
						for(SpcpProfitModel.SpTroneModel spTroneModel : model.list)
						{
							double lrAmount=spTroneModel.amount;
							out.println("<td rowspan=\"" + spTroneModel.spTroneRowSpan + "\">" + spTroneModel.spTroneName + "</td>"+"<td rowspan=\"" + spTroneModel.spTroneRowSpan + "\">" + spTroneModel.amount + "</td>");
							for(int i=0;i<spTroneModel.list.size();i++)
							{
								SpcpProfitModel.SpTroneModel.CpModelData cpModelData=spTroneModel.list.get(i);
								if(i==spTroneModel.list.size()-1){
									out.println("<td>" + cpModelData.cpFullName + "</td><td>" + cpModelData.payAmount + "</td>");

								}else{
									out.println("<td>" + cpModelData.cpFullName + "</td><td>" + cpModelData.payAmount + "</td></tr>");
								}
								lrAmount=lrAmount-cpModelData.payAmount;
							}
							int k=0;
							if(spTroneModel.list.size()>1){
								k=1;
							}else{
								k= spTroneModel.spTroneRowSpan;
							}
							out.println("<td text-align: center; rowspan=\"" + k + "\">" +StringUtil.getDecimalFormat(lrAmount)+ "</td></tr>");
							lrcount=lrcount+lrAmount;
						}
						out.println(
								"<tr style='background-color: #E0EEEE;'><td colspan='6' >利润合计</td><td>"
										+ StringUtil.getDecimalFormat(lrcount) + "</td></tr>");
					}
				%>
				</tbody>
		</table>
	</div>
	
</body>
</html>