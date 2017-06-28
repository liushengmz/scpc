<%@page import="com.system.server.TronePayCodeServer"%>
<%@page import="com.system.model.TronePayCodeModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.server.TroneServer"%>
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

	String query = StringUtil.getString(request.getParameter("query"),"");

	int troneId = StringUtil.getInteger(request.getParameter("id"), -1);
	int copy = StringUtil.getInteger(request.getParameter("copy"), -1);
	TroneServer troneServer = new TroneServer();
	TroneModel model = troneServer.getTroneById(troneId);
	if(model==null)
	{
		response.sendRedirect("trone.jsp");
		return;
	}
	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
	List<SpApiUrlModel> spApiUrlList = new SpApiUrlServer().loadSpApiUrl();
	
	//把tbl_trone_paycode的数据找出来
	TronePayCodeModel tronePayCodeModel = new TronePayCodeServer().getTronePayCode(model.getId());
	
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

	function joSpTrone(id,spId,troneName,spTroneApiId)
	{
		var obj = {};
		obj.id = id;
		obj.spId = spId;
		obj.troneName = troneName;
		obj.spTroneApiId = spTroneApiId;
		return obj;
	}
	
	var spTroneList = new Array();
	<%for(SpTroneModel spTrone : spTroneList){
		if(spTrone.getStatus()==1){
	%>spTroneList.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%= spTrone.getSpName() + "-" +spTrone.getSpTroneName() %>',<%= spTrone.getTroneApiId() %>));<%}}%>

	var spApiUrlList = new Array();
	<%for(SpApiUrlModel  spTrone : spApiUrlList){%>spApiUrlList.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%=spTrone.getName() %>'));<%}%>
	
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
		
		$("#sel_api_url").empty();
		$("#sel_api_url").append("<option value='-1'>请选择</option>");
		for(i=0; i<spApiUrlList.length; i++)
		{
			if(spApiUrlList[i].spId==spId)
			{
				$("#sel_api_url").append("<option value='" + spApiUrlList[i].id + "'>" + spApiUrlList[i].troneName + "</option>");
			}
		}
		
		showOrHideApiArea(false);
		console.log("here spChange");
	}
	
	function spTroneChange()
	{
		var spTroneId = $("#sel_sp_trone").val();
		showOrHideApiArea(false);
		for(i=0; i<spTroneList.length; i++)
		{
			if(spTroneList[i].id==spTroneId)
			{
				spTroneList[i]
				if(spTroneList[i].spTroneApiId>0)
				{
					showOrHideApiArea(true);	
				}
				break;
			}
		}
		console.log("here spTroneChange");
	}
	
	$(function()
	{
		//SP的二级联动
		$("#sel_sp").change(spChange);
		$("#sel_sp_trone").change(spTroneChange);
		resetForm();
	});
	
	function resetForm()
	{
		$("#sel_sp").val("<%= model.getSpId() %>");
		spChange();
		$("#sel_sp_trone").val("<%= model.getSpTroneId() %>");
		$("#sel_api_url").val("<%= model.getSpApiUrlId() %>");
		$("#input_trone_name").val("<%= model.getTroneName() %>");
		$("#input_trone_order").val("<%= model.getOrders() %>");
		$("#input_trone_num").val("<%= model.getTroneNum() %>");
		$("#input_price").val("<%= model.getPrice() %>");
		
		document.getElementById("chk_status").checked = <%= model.getStatus()==1 ? "true" : "false" %>;
		setRadioCheck("dynamic",<%= model.getDynamic() %>);
		setRadioCheck("match_price",<%= model.getMatchPrice() %>);
		
		$("#hid_exist_pay_code").val("<%= tronePayCodeModel==null ? 0 : 1 %>");
		$("#hid_trone_pay_code_id").val("<%= tronePayCodeModel != null ? tronePayCodeModel.getId() : "" %>");
		
		$("#input_paycode").val("<%= tronePayCodeModel != null ? tronePayCodeModel.getPayCode() : "" %>");
		$("#input_app_id").val("<%= tronePayCodeModel != null ? tronePayCodeModel.getAppId() : "" %>");
		$("#input_channel_id").val("<%= tronePayCodeModel != null ? tronePayCodeModel.getChannelId() : "" %>");
		
		showOrHideApiArea(<%= tronePayCodeModel==null ? false : true %>);
		
		spTroneChange();
	}
	
	function subForm() 
	{
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
		
		/*
		if ($("#sel_api_url").val() == "-1") {
			alert("请选择API URL");
			$("#sel_api_url").focus();
			return;
		}
		*/
		
		if ($("#input_trone_name").val() == "") {
			alert("请输入通道名称");
			$("#input_trone_name").focus();
			return;
		}
		
		if ($("#input_trone_order").val() == "") {
			alert("请输入指令");
			$("#input_trone_order").focus();
			return;
		}
		
		if ($("#input_trone_num").val() == "") {
			alert("请输入通道号");
			$("#input_trone_num").focus();
			return;
		}
		
		var price = $("#input_price").val();
		
		if (price == "")
		{
			alert("请输入价格");
			$("#input_price").focus();
			return;
		}
		
		if(isNum(price))
		{
			alert("价格不正确");
			$("#input_price").focus();
			return;
		}
		
		var existPayCode = document.getElementById("hid_exist_pay_code").value;
		
		if(existPayCode==1)
		{
			var payCode = $("#input_paycode").val();
			var appId = $("#input_app_id").val();
			var channelId = $("#input_channel_id").val();
			
			if(isNullOrEmpty(payCode)&&isNullOrEmpty(appId)&&isNullOrEmpty(channelId))
			{
				alert("兄弟，PayCode、AppId、ChannelId 总得有一个吧？");
				return;	
			}
		}
		
		//alert(document.getElementById("dynamic").value + "---" + document.getElementById("match_price").value);
		
		document.getElementById("addform").submit();
	}
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp").val(joData.id);
		spChange();
	}
	
	
	//声明整数的正则表达式
	function isNum(a)
	{
		var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		return reg.test(a);
	}

	function showOrHideApiArea(isShow)
	{
		document.getElementById("div_sp_trone_api").style.display = isShow ? "block" : "none";
		document.getElementById("hid_exist_pay_code").value = isShow ? 1 : 0 ;
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width: 200px">
				<label>通道<%= copy==1 ? "复制" : "修改"  %></label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="troneaction.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="<%= copy==1 ? -1 : model.getId() %>" name="id" />
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
						<select name="sp_trone_id" id="sel_sp_trone" title="选择SP业务" style="width: 200px" >
							<option value="-1">请选择SP业务</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">API_URL</dd>
					<dd class="dd04_me">
						<select name="api_url_id" id="sel_api_url" title="选择API_URL" style="width: 200px">
							<option value="-1">请选择API_URL</option>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">通道名称</dd>
					<dd class="dd03_me">
						<input type="text" name="trone_name" title="通道名称" id="input_trone_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">指令</dd>
					<dd class="dd03_me">
						<input type="text" name="trone_order" title="指令" id="input_trone_order"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">通道号</dd>
					<dd class="dd03_me">
						<input type="text" name="trone_num" title="通道号" id="input_trone_num"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">价格</dd>
					<dd class="dd03_me">
						<input type="text" name="price" title="价格" id="input_price"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd03_me">
						<input type="checkbox" name="status" style="width: 35px;float:left" checked="checked"  value="1" id="chk_status" >
						<label style="font-size: 14px;">启用</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">是否模糊</dd>
					<dd class="dd03_me">
						<input type="radio" name="dynamic" style="width: 35px;float:left" value="0" >
						<label style="font-size: 14px;float:left">否</label>
						<input type="radio" name="dynamic" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">是</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">匹配价格</dd>
					<dd class="dd03_me">
						<input type="radio" name="match_price" style="width: 35px;float:left" value="0" >
						<label style="font-size: 14px;float:left">否</label>
						<input type="radio" name="match_price" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">是</label>
					</dd>
					
					<div style="clear: both;padding-bottom: 25px"></div>
					
					<div  id="div_sp_trone_api" >
						<input type="hidden" value="1" id="hid_exist_pay_code" name="exist_pay_code" />
						<input type="hidden" value="1" id="hid_trone_pay_code_id" name="trone_pay_code_id" />
						<dd class="dd00_me"></dd>
						<dd class="dd01_me">PayCode</dd>
						<dd class="dd03_me">
							<input type="text" name="paycode" id="input_paycode"
								style="width: 200px">
						</dd>
						
						<div style="clear: both;padding-bottom: 25px"></div>
						
						<dd class="dd00_me"></dd>
						<dd class="dd01_me">AppId</dd>
						<dd class="dd03_me">
							<input type="text" name="appid" id="input_app_id"
								style="width: 200px">
						</dd>
						
						<div style="clear: both;padding-bottom: 25px"></div>
						
						<dd class="dd00_me"></dd>
						<dd class="dd01_me">ChannelId</dd>
						<dd class="dd03_me">
							<input type="text" name="channelid" id="input_channel_id"
								style="width: 200px">
						</dd>
						
						<div style="clear: both;"></div>
					</div>

					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="重 置" onclick="resetForm()">
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