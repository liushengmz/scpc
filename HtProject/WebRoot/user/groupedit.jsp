<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 

	request.setCharacterEncoding("UTF-8");
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	GroupModel model = new GroupServer().loadGroupById(id);
	String name = StringUtil.getString(request.getParameter("encodeStr"), "");
	//String encodeStr = URLEncoder.encode(name,"GBK"); 
	String query = request.getQueryString();
	
	if(model==null)
	{
		response.sendRedirect("group.jsp?"+query);
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
	
	$(function() 
	{
		resetForm();
	});
			
	function resetForm()
	{
		$("#input_group_name").val("<%= model.getName() %>");
		$("#input_remark").val("<%= model.getRemark() %>");
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>角色修改</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="groupaction.jsp?name=<%=name %>" method="post" id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
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