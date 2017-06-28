<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>翔通运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	function subForm() 
	{
		var groupName = $("#input_group_name").val().trim();
		var groupRemarkl = $("#input_remark").val().trim();
		
		if(isNullOrEmpty(groupName))
		{
			alert("组别名称不能为空");
			return;
		}
		
		document.getElementById("addform").submit();
	}
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>增加角色</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="groupaction.jsp" method="post" id="addform">
					<dd class="dd01_me">组别名称</dd>
					<dd class="dd03_me">
						<input type="text" name="group_name" title="登录名称"  id="input_group_name" style="width: 200px">
					</dd>
	
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark"   id="input_remark" style="width: 200px">
					</dd>
	
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