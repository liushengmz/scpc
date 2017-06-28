<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="java.util.List"%>
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
	$.ajaxSetup({   
	    async : false  
	});

	function refreshCache(reType) 
	{
		if(confirm("确定要刷新吗？刷新前请确保更新的数据正确！"))
		{
			$.post("action.jsp", 
			{
				type : reType
			}, 
			function(data) 
			{
				data = $.trim(data);
				
				if("OK" == data)
				{
					alert("刷新成功");
				}
				else
					alert("刷新失败");
			});	
		}
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px">
					<label>缓存刷新</label>
				</dd>
			</dl>
			<br /> <br />
			<dl>
					<dd class="dd01_me">系统权限</dd>
					<dd class="ddbtn" style="margin-left: 10px;">
						<input type="button" value="刷 新" onclick="refreshCache(1)">
					</dd>
					<br /><br />
			</dl>
		</div>

	</div>
</body>
</html>