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
	String defaultStartDate = StringUtil.getMonthHeadDate() + " 00:00:00";

	String defaultEndDate = StringUtil.getMonthEndDate() + " 23:59:59";
	
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);
	
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone"), -1);
	int synType = StringUtil.getInteger(request.getParameter("syn_type"), 1);
	
	int isLoadData = StringUtil.getInteger(request.getParameter("load_data"), -1);
	
	List<SpModel> spList = new SpServer().loadSp();
	List<CpModel> cpList = new CpServer().loadCp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
	
	List<DetailDataVo> list =  (isLoadData > 0) ? (new MrDetailServer().loadDetailDataByCondition(startDate, endDate, spId, cpId, spTroneId, synType)) : new ArrayList<DetailDataVo>();
	
	String synUrl = ConfigManager.getConfigData("SYN_DATA_URL", "http://h1.n8wan.com:2109/pushagain.ashx");
	
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
		
		//CP的二级联动
		$("#sel_cp").val(<%= cpId %>);	
		$("#sel_syn_type").val(<%= synType %>);
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
	
	function resetCheckBox(data)
	{
		var objs = document.getElementsByName("chk_data");
		for(i=0; i<objs.length; i++)
		{
			objs[i].checked = data;	
		}
	}
	
	function postDataToWhere()
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
		
		if(ids!="")
		{
			if(confirm("如果选择未同步的数据同步过去，会影响扣量数据，是否继续？"))
			{
				ids = ids.substring(0, ids.length-1);
				document.getElementById("hid_data").value = ids;
				document.getElementById("hid_month").value = '<%= StringUtil.getMonthFormat(startDate)  %>';
				document.getElementById("deng_form").submit();
			}
		}
		else
		{
			alert("没有要同步的数据");	
		}
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="syn_data.jsp"  method="post" style="margin-top: 10px" >
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 120px;">
					</dd>
					<dd class="dd01_me">结束日期</dd>
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
					<br /><br /><br />
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp" title="选择CP" style="width: 110px;" onclick="namePicker(this,cpList,onCpDataSelect)">
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
					<dd class="dd01_me">同步类型</dd>
						<dd class="dd04_me">
						<select name="syn_type" id="sel_syn_type" style="width: 100px;">
							<option value="-1">全部</option>
							<option value="0">未同步</option>
							<option value="1">已同步</option>
						</select>
					</dd>
					<input type="hidden" value="1" name="load_data" />					
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit"  />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" value="同 步" type="button" onclick="postDataToWhere()"  />
					</dd>
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
					<td>时间</td>
					<td>省份</td>
					<td>城市</td>
					<td>价格</td>
					<td>指令</td>
					<td>通道号</td>
					<td>已同步</td>
					<td>全选<input type="checkbox" onclick="resetCheckBox(this.checked)" /></td>
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
					<td><%= model.getCreateDate() %></td>
					<td><%= model.getProvinceName() %></td>
					<td><%= model.getCityName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getOrder() %></td>
					<td><%= model.getTroneNum() %></td>
					<td><%= model.getSynFlag()==1 ? "已同步" : "未同步" %></td>
					<td><input type="checkbox" id="<%= model.getId() %>" name="chk_data"></td>
				</tr>
						<%
					}
				%>
			</tbody>
		</table>
	</div>
	<form action="<%= synUrl %>" id="deng_form" method="post" >
		<input type="hidden" value="" id="hid_month" name="month" />
		<input type="hidden" value="" id="hid_data" name="data" />
	</form>
</body>
</html>