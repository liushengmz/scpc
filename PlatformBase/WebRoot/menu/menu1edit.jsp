<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	Menu1Model menu1Model = new Menu1Server().loadMenu1ModelById(id);
	if(menu1Model==null)
	{
		response.sendRedirect("menu1.jsp");
		return;
	}
	List<MenuHeadModel> menuHeadList = new MenuHeadServer().loadMenuHeadList();	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>翔通运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript">

	function subForm() 
	{
		var headId = $("#sel_head_id").val();
		var name = $("#input_name").val();
		var remark = $("#input_remark").val();
		
		if("-1"==headId)
		{
			alert("请选择模块");
			return;
		}
		
		if(isNullOrEmpty(name))
		{
			alert("菜单名不能为空");
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
		$("#sel_head_id").val("<%= menu1Model.getMenuHeadId() %>");
		$("#input_name").val("<%= menu1Model.getName() %>");
		$("#input_remark").val("<%= menu1Model.getRemark() %>");
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>修改菜单</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="action.jsp?pageindex=<%=pageIndex %>" method="post" id="addform">
				<input type="hidden" value="0" name="type">
				<input type="hidden" value="<%= id %>" name="id">
				<dd class="dd01_me">模块</dd>
				<dd class="dd04_me">
					<select name="head_id" id="sel_head_id"   style="width: 200px">
							<option value="-1">请选择模块</option>
							<%
								for (MenuHeadModel model : menuHeadList)
								{
							%>
							<option value="<%=model.getId()%>"><%= model.getName() %></option>
							<%
								}
							%>
						</select>
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">名称</dd>
				<dd class="dd03_me">
					<input type="text" name="name"  id="input_name" value="" style="width: 200px">
				</dd>

				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">备注</dd>
				<dd class="dd03_me">
					<input type="text" name="remark"  id="input_remark" value="" style="width: 200px">
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