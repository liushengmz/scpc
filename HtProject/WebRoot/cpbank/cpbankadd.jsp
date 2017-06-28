<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<CpModel> cpList=new CpServer().loadCp();
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
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
var cpList = new Array();
<%
for(CpModel cpModel : cpList)
{
	%>
	cpList.push(new joSelOption(<%= cpModel.getId() %>,1,'<%= cpModel.getShortName() %>'));
	<%
}
%>
	function joCpChannel(name)
	{
		var obj = {};
		obj.name = name;
		return obj;
	}
	
	function subForm() 
	{
		var cpId=$("#cp_id").val();
		if ($("#cp_id").val()=='-1') 
		{
			alert("请选择CP！");
			$("#cp_id").focus();
			return;
		}
		
		if (isNullOrEmpty($("#bank_name").val())) 
		{
			alert("请输入银行！");
			$("#bank_name").focus();
			return;
		}
		if (isNullOrEmpty($("#user_name").val())) 
		{
			alert("请输入收款人！");
			$("#user_name").focus();
			return;
		}
		if (isNullOrEmpty($("#bank_account").val())) 
		{
			alert("请输入银行账号！");
			$("#bank_account").focus();
			return;
		}
		
		var status= $('#radio_status input[name="status"]:checked ').val();
		var type= $('#radio_type input[name="type"]:checked ').val();
		var result=ajaxLogin(cpId, type,status);
		if(result=='1'){
			alert("数据已存在！");
			return ;
		}
		document.getElementById("addform").submit();
	}
	function ajaxLogin(cpId, type,status) {
		var result = "";
		$.ajax({
			url : "action.jsp",
			data : "cp_id=" + cpId + "&type=" + type+"&status="+status+"&act=2",
			cache : false,
			async : false,
			success : function(html) {
				result = $.trim(html);
			}
		});
		return result;
	}
	
	function onCpDataSelect(joData)
	{	
		$("#cp_id").val(joData.id);
	}
	//$(function(){ $("#sel_commerce_user_id").val(""); });
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width: 200px">
				<label>CP结款增加</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="action.jsp" method="get" id="addform">
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="cp_id" style="width: 200px" onclick="namePicker(this,cpList,onCpDataSelect)">
							<option value="-1">请选择</option>
							<%
							for(CpModel model : cpList)
							{
								%>
							<option value="<%= model.getId() %>"><%= model.getShortName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd03_me" style="background: none">
					<div id="radio_type">
						<input type="radio" name="type" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">对公</label>
						<input type="radio" name="type" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">对私</label>
					</div>
					</dd>
					<br/>
					<br/>
					<br/>					
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">银行</dd>
					<dd class="dd03_me">
						<input type="text" name="bank_name" id="bank_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">收款人</dd>
					<dd class="dd03_me">
						<input type="text" name="user_name" id="user_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">银行帐号</dd>
					<dd class="dd03_me">
						<input type="text" name="bank_account" id="bank_account"
							style="width: 200px">
					</dd>
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">银行支行</dd>
					<dd class="dd03_me">
						<input type="text" name="bank_branch" id="bank_branch"
							style="width: 200px">
					</dd>
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd03_me" style="background: none">
					<div id="radio_status">
						<input type="radio" name="status" style="width: 35px;float:left" value="1" checked="checked" >
						<label style="font-size: 14px;float:left">启用</label>
						<input type="radio" name="status" style="width: 35px;float:left" value="0" >
						<label style="font-size: 14px;float:left">关闭</label>
					</div>
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