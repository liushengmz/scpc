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
	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
	List<SpApiUrlModel> spApiUrlList = new SpApiUrlServer().loadSpApiUrl();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	function joSpTrone(id,spId,troneName)
	{
		var obj = {};
		obj.id = id;
		obj.spId = spId;
		obj.troneName = troneName;
		return obj;
	}
	
	var spTroneList = new Array();
	<%for(SpTroneModel spTrone : spTroneList){%>spTroneList.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%= spTrone.getSpName() + "-" +spTrone.getSpTroneName() %>'));<%}%>

	var spApiUrlList = new Array();
	<%for(SpApiUrlModel spTrone : spApiUrlList){%>spApiUrlList.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%=spTrone.getName() %>'));<%}%>
	
	function spTroneChange()
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
	}
	
	$(function()
	{
		//SP的二级联动
		$("#sel_sp").change(spTroneChange);
		//spTroneChange();
	});
	
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
		
		if ($("#sel_api_url").val() == "-1") {
			alert("请选择API URL");
			$("#sel_api_url").focus();
			return;
		}
		
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

		document.getElementById("addform").submit();
	}
	
	
	//声明整数的正则表达式
	function isNum(a)
	{
		var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		return reg.test(a);
	}

</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>增加通道</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="troneaction.jsp" method="post" id="addform">
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" title="选择SP" style="width: 200px">
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
						<input type="checkbox" name="status" style="width: 35px;float:left" checked="checked"  id="chk_status" >
						<label style="font-size: 14px;">启用</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">是否动态</dd>
					<dd class="dd03_me">
						<input type="radio" name="dynamic" style="width: 35px;float:left" value="0" checked="checked" >
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
						<input type="radio" name="match_price" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">否</label>
						<input type="radio" name="match_price" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">是</label>
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