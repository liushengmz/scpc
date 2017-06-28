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
	UserModel sessionUser = (UserModel)session.getAttribute("user");
	if(sessionUser==null)
	{
		response.sendRedirect("login.jsp");
		return;
	}
	UserModel user = new UserServer().getUserModelById(sessionUser.getId());
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

	function endEditInfo()
	{
		var rightTop = $("#rightTop", window.parent.document);
		rightTop.attr("src", "righttop.jsp?menuid=0");
		window.location.href = "../welcome.html";
	}
	
	$.ajaxSetup({   
        async : false  
    });

	function subForm() 
	{
		var userId = <%= sessionUser.getId() %>;
		
		var oldPwd = $("#input_old_pwd").val().trim();
		var newPwd1 = $("#input_new_pwd_1").val().trim();
		var newPwd2 = $("#input_new_pwd_2").val().trim();
		
		var nickName = $("#input_nick_name").val();
		var mail = $("#input_mail").val();
		var qq = $("#input_qq").val();
		var phone = $("#input_phone").val();
		
		if(newPwd1!=newPwd2)
		{
			alert("新密码两次输入不一致！");
			return;
		}
		
		var editPwd = false;
		
		if(!isNullOrEmpty(newPwd1))
			editPwd = true;
		
		if(editPwd)
		{
			if(isNullOrEmpty(oldPwd))
			{
				alert("请输入旧密码");
				return;
			}
		}
		
		var oldPwdMatch = false;
		
		if(editPwd)
		{
			$.post("action.jsp", 
			{
				type : 1,
				pwd : oldPwd,
				id :userId 
			}, 
			function(data) 
			{
				data = $.trim(data);
				if("OK" == data)
				{
					oldPwdMatch = true;	
				}
			});
		}
		
		if(editPwd && !oldPwdMatch)
		{
			alert("原始密码不匹配");
			return;
		}
		
		var editType = 3;
		
		if(editPwd && oldPwdMatch)
		{
			editType = 2;
		}
		
		$.post("action.jsp", 
		{
			type : editType,
			pwd : newPwd1,
			nick_name: nickName,
			mail:mail,
			login_name:'<%= user.getName() %>',
			qq:qq,
			phone:phone,
			id :userId 
		}, 
		function(data) 
		{
			data = $.trim(data);
			
			if("OK" == data)
			{
				$("#input_old_pwd").val("");
				$("#input_new_pwd_1").val("");
				$("#input_new_pwd_2").val("");
				alert("修改成功");
			}
			else
				alert("修改失败");
		});
	}
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="dd01_me">登录用户名</dd>
				<dd class="dd03_me">
					<input type="text" name="login_name" title="登录名称" value="<%= user.getName() %>" readonly="readonly" id="input_login_name" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">旧密码</dd>
				<dd class="dd03_me">
					<input type="password" name="old_pwd"   id="input_old_pwd" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">新密码</dd>
				<dd class="dd03_me">
					<input type="password" name="new_pwd"   id="input_new_pwd_1" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">新密码确认</dd>
				<dd class="dd03_me">
					<input type="password" name="new_pwd"  id="input_new_pwd_2" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">昵称</dd>
				<dd class="dd03_me">
					<input type="text" name="nick_name"  id="input_nick_name" value="<%= user.getNickName() %>" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">Mail</dd>
				<dd class="dd03_me">
					<input type="text" name="mail"  id="input_mail" value="<%= user.getMail() %>" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">QQ</dd>
				<dd class="dd03_me">
					<input type="text" name="qq"  id="input_qq" value="<%= user.getQq()%>" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">电话</dd>
				<dd class="dd03_me">
					<input type="text" name="phone"  id="input_phone" value="<%= user.getPhone() %>" style="width: 200px">
				</dd>


				<br />
				<br />
				<br />
				<dd class="dd00"></dd>
				<dd class="dd00_me"></dd>
				<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
					<input type="button" value="修 改" onclick="subForm()">
				</dd>
				<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
					<input type="button" value="返 回" onclick="endEditInfo()">
				</dd>
			</dl>
		</div>

	</div>
</body>
</html>