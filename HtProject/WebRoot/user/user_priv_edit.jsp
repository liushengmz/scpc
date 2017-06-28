<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
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
	int userId = StringUtil.getInteger(request.getParameter("id"), -1);
	UserModel model = new UserServer().getUserModelById(userId);
	if(model==null)
	{
		response.sendRedirect("user_priv.jsp");
	}
	String query = StringUtil.getString(request.getParameter("query"),"");
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
<script type="text/javascript">

	function subForm() 
	{
		var pwd = $("#input_new_pwd_1").val().trim();
		var newPwd2 = $("#input_new_pwd_2").val().trim();
		
		var nickName = $("#input_nick_name").val();
		var mail = $("#input_mail").val();
		var qq = $("#input_qq").val();
		var phone = $("#input_phone").val();
		
		if(pwd!=newPwd2)
		{
			alert("新密码两次输入不一致！");
			return;
		}
		
		if(isNullOrEmpty(nickName))
		{
			$("#input_nick_name").focus();
			alert("昵称好歹也填一下吧？");
			return;
		}
		
		document.getElementById("addform").submit();
	}
	
	$(function()
	{
		resetForm();
	});
	
	function resetForm()
	{
		$("#input_nick_name").val("<%= model.getNickName() %>");
		$("#input_mail").val("<%= model.getMail() %>");
		$("#input_qq").val("<%= model.getQq() %>");
		$("#input_phone").val("<%= model.getPhone() %>");
		$("#input_new_pwd_1").val("<%= "" %>");
		$("#input_new_pwd_2").val("<%= "" %>");
		setRadioCheck("status",<%= model.getStatus() %>);
	}
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>修改用户</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="user_priv_action.jsp?query=<%= query %>" method="post" id="addform">
				<input type="hidden" value="5" name="type">
				<input type="hidden" value="<%= userId %>" name="id" />
				<input type="hidden" value="<%= model.getPassword() %>" name="old_pwd">
				<dd class="dd01_me">登录用户名</dd>
				<dd class="dd03_me">
					<input type="text" name="login_name" title="登录名称"  value="<%= model.getName() %>" id="input_login_name" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">密码</dd>
				<dd class="dd03_me">
					<input type="password" name="pwd"   id="input_new_pwd_1" value="<%= model.getPassword() %>" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">密码确认</dd>
				<dd class="dd03_me">
					<input type="password" name="new_pwd_2"  id="input_new_pwd_2" value="<%= model.getPassword() %>" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">昵称</dd>
				<dd class="dd03_me">
					<input type="text" name="nick_name"  id="input_nick_name"  value="<%= model.getNickName() %>" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">Mail</dd>
				<dd class="dd03_me">
					<input type="text" name="mail"  id="input_mail" value="<%= model.getMail() %>" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">QQ</dd>
				<dd class="dd03_me">
					<input type="text" name="qq"  id="input_qq" value="<%= model.getQq() %>" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">电话</dd>
				<dd class="dd03_me">
					<input type="text" name="phone"  id="input_phone" value="<%= model.getPhone() %>" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">状态</dd>
				<dd class="dd03_me">
					<input type="radio" name="status" style="width: 35px;float:left" value="1" >
					<label style="font-size: 14px;float:left">正常</label>
					<input type="radio" name="status" style="width: 35px;float:left" value="0" >
					<label style="font-size: 14px;float:left">停用</label>
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