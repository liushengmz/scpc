<%@page import="com.system.server.BlackServer"%>
<%@page import="com.system.model.BlackModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
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
<script type="text/javascript">
	
	
	function subForm() 
	{
	
		if(isNullOrEmpty($("#pl_data").val())){
			alert("IMEI/IMSI/电话不能为空！");
			return ;
		}
		
		document.getElementById("addform").submit();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				
				<label>批量增加</label>
			</dl>
			<dl>
				<form action="action.jsp?type=2" method="post" id="addform">
				
					<dd style="margin-left: 10px;" id="chktype" >
						<input type="radio" value="1"  name="chktype"   checked="checked" />号码
						<input type="radio" value="2"  name="chktype"  />IMEI
						<input type="radio" value="3"  name="chktype"  />IMSI
					</dd>
					<div style="clear: both;"><br /></div>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">IMEI/IMSI/电话</dd>
					<dd class="dd03_me">
					<textarea name="pl_data"  style="width:200px;height: 150px;"   id="pl_data" ></textarea>
					</dd>
					<div style="clear: both;"><br /></div>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" style="margin-bottom 10px">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" id="add_remark"
							style="width: 200px">
					</dd>
					<div style="clear: both;"><br /></div>
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