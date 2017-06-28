<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.CpPushUrlServer"%>
<%@page import="com.system.model.CpPushUrlModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.SpApiUrlServer"%>
<%@page import="com.system.model.SpApiUrlModel"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	
	TroneOrderModel model = new TroneOrderServer().getTroneOrderById(id);
	
	String query = StringUtil.getString(request.getParameter("query"),"");
	
	if(model==null)
	{
		response.sendRedirect("troneorder.jsp");
		return;
	}
	
	List<SpModel> spList = new SpServer().loadSp();
	List<CpModel> cpList = new CpServer().loadCp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
	List<CpPushUrlModel> cpPushUrlList = new CpPushUrlServer().loadcpPushUrl();
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
		spChange();
	}
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp").val(joData.id);
		cpChange();
	}

	function joSpTrone(id,spId,troneName,isUnHoldData)
	{
		var obj = {};
		obj.id = id;
		obj.spId = spId;
		obj.troneName = troneName;
		obj.isUnHoldData = isUnHoldData;
		return obj;
	}
	
	var spTroneList = new Array();
	<%for(SpTroneModel spTrone : spTroneList){
		if(spTrone.getStatus()==1){
	%>spTroneList.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%= spTrone.getSpName() + "-" +spTrone.getSpTroneName() %>',<%= spTrone.getIsUnHoldData() %>));<%}}%>
	
	var cpPushUrlList = new Array();
	<%
		for(CpPushUrlModel cpUrlModel : cpPushUrlList)
		{
	%>
			cpPushUrlList.push(new joBaseObject(<%= cpUrlModel.getId() %>,<%= cpUrlModel.getCpId() %>,'<%= cpUrlModel.getName() + "-" + cpUrlModel.getUrl() %>'));
	<%
		}
	%>
	
	function spChange()
	{
		var spId = $("#sel_sp").val();
		$("#sel_sp_trone").empty();
		$("#sel_sp_trone").append("<option value='-1'>请选择</option>");
		for(i=0; i<spTroneList.length; i++)
		{
			if(spTroneList[i].spId==spId)
			{
				$("#sel_sp_trone").append("<option value='" + spTroneList[i].id + "'>" + spTroneList[i].troneName + "</option>");
			}
		}
	}
	
	function cpChange()
	{
		var cpId = $("#sel_cp").val();
		$("#sel_cp_push_url").empty();
		$("#sel_cp_push_url").append("<option value='-1'>请选择</option>");
		for(i=0; i<cpPushUrlList.length; i++)
		{
			if(cpPushUrlList[i].pid==cpId)
			{
				$("#sel_cp_push_url").append("<option value='" + cpPushUrlList[i].id + "'>" + cpPushUrlList[i].name + "</option>");
			}
		}
	}
	
	var troneArray = new Array();
	
	function spTroneChange()
	{
		troneChange();
		var spTroneId =  $("#sel_sp_trone").val();
		getAjaxValue("../ajaction.jsp?type=1&sptroneid=" + spTroneId + "&arrname=troneArray",onSpTroneChange);
		changeDaoLiangStatus(spTroneId);
	}
	
	function changeDaoLiangStatus(spTroneId)
	{
		console.log("here is spTroneId:" + spTroneId);
		for(var i=0; i<spTroneList.length; i++)
		{
			var spTrone = spTroneList[i];
			if(spTrone.id == spTroneId)
			{
				setRadioCheck("is_unhold_data",spTrone.isUnHoldData);
				break;
			}
		}
	}
	
	var isFirstLoad = true;
	
	function onSpTroneChange(data)
	{
		troneArray.length = 0;
		eval(data);
		$("#sel_trone").empty();
		$("#sel_trone").append("<option value='-1'>请选择</option>");
		
		for(i=0; i<troneArray.length; i++)
		{
			$("#sel_trone").append("<option value='" + troneArray[i].id + "'>" + troneArray[i].name + "</option>");
		}
		
		if(isFirstLoad)
		{
			$("#sel_trone").val("<%=model.getTroneId() %>");
			isFirstLoad = false;
		}
		
	}
	
	var cpTroneOrderArray = new Array();
	
	function troneChange()
	{
		var troneId =  $("#sel_trone").val();
		getAjaxValue("../ajaction.jsp?type=4&troneid=" + troneId,onTroneChange);
	}
	
	function onTroneChange(data)
	{
		cpTroneOrderArray.length = 0;
		
		eval(data);
		
		var cpTroneListDiv = document.getElementById("div_cp_trone_list");
		
		if(cpTroneListDiv!=null)
			cpTroneListDiv.style.display = "none";
		
		if(cpTroneOrderArray.length==0)
			return;
		
		if(cpTroneListDiv==null)
		{
			var parentObj = document.getElementById("sel_trone");
			
			mydiv = document.createElement("div"); 
			
			mydiv.setAttribute("id","div_cp_trone_list"); 
			mydiv.style.position = "absolute";
			mydiv.style.backgroundColor="#F6F5F3";
			mydiv.style.border = "solid 1px #D1CDC5";
			mydiv.style.padding = "5px";
			mydiv.style.lineHeight = "1.5em";
			mydiv.style.fontSize = "14px";
			document.body.appendChild(mydiv);
			//右边显示
			mydiv.style.left =  (getAbsoluteObjectLeft(parentObj) + parentObj.offsetWidth + 2) + "px";
			mydiv.style.top = (getAbsoluteObjectTop(parentObj) - parentObj.offsetHeight) +  "px";
			
			cpTroneListDiv = mydiv;
		}
		
		cpTroneListDiv.innerHTML = "";
		
		var divInnerHtml = "";
		
		var cpTroneOrder = null;
		
		for(i=0; i<cpTroneOrderArray.length; i++)
		{
			cpTroneOrder = cpTroneOrderArray[i]
			divInnerHtml += "<span>"+ cpTroneOrder.cpShortName + "-" + cpTroneOrder.orderNum + "-" + (cpTroneOrder.disable==0 ? "启用" : "停用") +"</span>";	
		}
		
		cpTroneListDiv.innerHTML = divInnerHtml;
		
		cpTroneListDiv.style.display = "block";
	}
	
	$(function()
	{
		$("#sel_cp").change(cpChange);
		
		$("#sel_sp").val("<%= model.getSpId()  %>");
		$("#sel_cp").val("<%= model.getCpId() %>");
		spChange();
		$("#sel_sp_trone").val("<%= model.getSpTroneId() %>");
		spTroneChange();
		cpChange();
		$("#sel_cp_push_url").val("<%= model.getPushUrlId() %>");
		
		$("#input_order_num").val("<%= model.getOrderNum() %>");
		$("#input_hold_percent").val("<%= model.getHoldPercent() %>");
		$("#input_hold_amount").val("<%= model.getHoldAmount() %>");
		$("#input_hold_account").val("<%= model.getHoldAcount() %>");
		
		setRadioCheck("dynamic",<%= model.getDynamic() %>);
		setRadioCheck("status",<%= model.getDisable() %>);
		setRadioCheck("hold_custom",<%= model.getIsHoldCustom() %>);
		setRadioCheck("is_unhold_data",<%= model.getIsUnholdData() %>);
		
		//SP的二级联动
		$("#sel_sp").change(spChange);
		
		$("#sel_sp_trone").change(spTroneChange);
		
		$("#sel_trone").change(troneChange);
		
		
		troneChange();
		
		amountDeduct();
		$("input[name=hold_custom]").click(function(){
			amountDeduct();
		});
	});
	
	function subForm() 
	{
		if ($("#sel_cp").val() == "-1") {
			alert("请选择CP");
			$("#sel_cp").focus();
			return;
		}
		
		if ($("#sel_sp").val() == "-1") {
			alert("请选择SP");
			$("#sel_sp").focus();
			return;
		}
		
		if ($("#sel_sp_trone").val() == "-1") {
			alert("请选择SP业务");
			$("#sel_sp_trone").focus();
			return;
		}
		
		if ($("#sel_trone").val() == "-1") {
			alert("请选择通道");
			$("#sel_trone").focus();
			return;
		}
		
		/*
		if ($("#sel_cp_push_url").val() == "-1") {
			alert("请选择同步 URL");
			$("#sel_cp_push_url").focus();
			return;
		}
		*/
		
		if ($("#input_order_num").val() == "") {
			alert("请输入指令");
			$("#input_order_num").focus();
			return;
		}
		
		/*
		if ($("#input_order_trone_name").val() == "") {
			alert("请输入业务名称");
			$("#input_order_trone_name").focus();
			return;
		}
		*/
		
		document.getElementById("addform").submit();
	}
	
	//声明整数的正则表达式
	function isNum(a)
	{
		var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		return reg.test(a);
	}
	
	function amountDeduct(){
		var radioId=$("input[name=hold_custom]:checked").attr("id");
		if(radioId=="radio_0"){
			$("#input_hold_percent").val(0);
			$("#input_hold_amount").val(0);
			$("#input_hold_account").val(0);
			$("#input_hold_percent").attr("disabled",true);
			$("#input_hold_amount").attr("disabled",true);
			$("#input_hold_account").attr("disabled",true);
			
		}
		if(radioId=="radio_1"){
			$("#input_hold_percent").attr("disabled",false);
			$("#input_hold_amount").attr("disabled",false);
			$("#input_hold_account").attr("disabled",false);
		}
	}

</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>修改CP业务</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="troneorderaction.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" name="id" value="<%= model.getId() %>"/>
					<dd class="dd01_me">CP名称</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp"  style="width: 200px" onclick="namePicker(this,cpList,onCpDataSelect)">
							<option value="-1">请选择CP名称</option>
							<%
								for (CpModel cp : cpList)
								{
							%>
							<option value="<%=cp.getId()%>"><%=cp.getShortName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" title="选择SP" style="width: 200px" onclick="namePicker(this,spList,onSpDataSelect)">
							<option value="-1">请选择SP名称</option>
							<%
								for (SpModel sp : spList)
								{
							%>
							<option value="<%=sp.getId()%>"><%=sp.getShortName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone_id" id="sel_sp_trone" title="选择SP业务" style="width: 200px">
							<option value="-1">请选择SP业务</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务通道</dd>
					<dd class="dd04_me">
						<select name="trone_id" id="sel_trone" style="width: 200px">
							<option value="-1">请选择</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">PUSH_URL</dd>
					<dd class="dd04_me">
						<select name="push_url_id" id="sel_cp_push_url" style="width: 200px">
							<option value="-1">请选择URL</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">指令</dd>
					<dd class="dd03_me">
						<input type="text" name="order_num" title="指令" id="input_order_num"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<!--
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">业务名称</dd>
					<dd class="dd03_me">
						<input type="text" name="order_trone_name"  id="input_order_trone_name"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					 -->
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">是否导量</dd>
					<dd class="dd03_me">
						<input type="radio" name="is_unhold_data" id ="is_unhold_data_0" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">否</label>
						<input type="radio" name="is_unhold_data" id="is_unhold_data_1" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">是</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">扣量方式</dd>
					<dd class="dd03_me">
						<input type="radio" name="hold_custom" id="radio_0" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">URL</label>
						<input type="radio" name="hold_custom" id="radio_1" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">业务</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">扣量百分比</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_percent" title="通道名称" id="input_hold_percent" value="0"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">每天总限额</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_amount" title="通道名称" id="input_hold_amount" value="0"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">起扣数</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_account"  id="input_hold_account" value="0"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd03_me">
						<input type="radio" name="status" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">启用</label>
						<input type="radio" name="status" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">停用</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">是否模糊</dd>
					<dd class="dd03_me">
						<input type="radio" name="dynamic" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">是</label>
						<input type="radio" name="dynamic" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">否</label>
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>
				</form>
			</dl>
		</div>

	</div>
</body>
</html>