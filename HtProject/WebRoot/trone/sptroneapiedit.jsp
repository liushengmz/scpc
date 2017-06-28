<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SpTroneApiServer"%>
<%@page import="com.system.model.SpTroneApiModel"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.model.SettleAccountModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SettleAccountServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<%
	int spTroneApiId = StringUtil.getInteger(request.getParameter("id"), -1);
	SpTroneApiModel model = new SpTroneApiServer().getSpTroneApiById(spTroneApiId);
	String query = StringUtil.getString(request.getParameter("query"), "");
	if(model==null)
	{
		response.sendRedirect("sptroneapi.jsp?" + Base64UTF.decode(query));
		return;
	}
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
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript">

	function subForm() 
	{
		if ($("#input_sp_trone_api_name").val() == "") 
		{
			alert("请输入API名称");
			$("#input_sp_trone_api_name").focus();
			return;
		}
		
		document.getElementById("addform").submit();
	}
	
	$(function()
	{
		$("#input_id").val("<%= model.getId() %>");
		$("#input_sp_trone_api_name").val("<%= model.getName() %>");
		$("#input_match_keyword").val("<%= model.getMatchKeyword() %>");
		setRadioCheck("match_field",<%= model.getMatchField() %>);
		setRadioCheck("locate_match",<%= model.getLocateMatch() %>);
		var apiFildes = "<%= model.getApiFields() %>";
		
		if(apiFildes!="")
		{
			var files = apiFildes.split(",");
			for(i=0; i<files.length; i++)
			{
				document.getElementById("api_fields_" + files[i]).checked = true;	
			}
		}
	});
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>增加业务API</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="sptroneapiaction.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="-1" name="id" id="input_id" />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">API名称</dd>
					<dd class="dd03_me">
						<input type="text" name="sp_trone_api_name" title="API名称" id="input_sp_trone_api_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">匹配字段</dd>
					<dd >
						<input type="radio" name="match_field" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">LinkId</label>
						<input type="radio" name="match_field" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">Msg</label>
						<input type="radio" name="match_field" style="width: 35px;float:left" value="2" >
						<label style="font-size: 14px;float:left">SP透参</label>
						<input type="radio" name="match_field" style="width: 35px;float:left" value="3" >
						<label style="font-size: 14px;float:left">特殊指令</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">匹配关键字</dd>
					<dd class="dd03_me">
						<input type="text" name="match_keyword"  id="input_match_keyword" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">必须参数</dd>
					<dd>&nbsp;&nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_0" value="0">IMEI &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_1" value="1">IMSI &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_2" value="2">手机号码 &nbsp;
						<!--  
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_3" value="3">IP &nbsp;
						-->
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_4" value="4">包名 &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_5" value="5">Android版本 &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_6" value="6">网络类型 &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_7" value="7">客户端IP &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_8" value="8">LAC &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_9" value="9">CID &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_10" value="10">ICCID &nbsp;
						<input type="checkbox" class="chpro" name="api_fields" id="api_fields_11" value="11">UserAgent &nbsp;
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">地区匹配</dd>
					<dd >
						<input type="radio" name="locate_match" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">不匹配</label>
						<input type="radio" name="locate_match" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">手机号匹配</label>
						<input type="radio" name="locate_match" style="width: 35px;float:left" value="2" >
						<label style="font-size: 14px;float:left">IP地区匹配</label>
						<input type="radio" name="locate_match" style="width: 35px;float:left" value="3" >
						<label style="font-size: 14px;float:left">手机和IP地区匹配</label>
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