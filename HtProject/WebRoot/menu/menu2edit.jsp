<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	Menu2Model menu2Model =	new Menu2Server().loadMenu2ById(id);
	if(menu2Model==null)
	{
		response.sendRedirect("menu2.jsp");
		return;
	}
	List<MenuHeadModel> menuHeadList = new MenuHeadServer().loadMenuHeadList();	
	List<Menu1Model> menu1List = new Menu1Server().loadMenu1List();
	
	String query = StringUtil.getString(request.getParameter("query"), "");
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

	function menu1Object(menu1Id,menuHeadId,name)
	{
		var obj = {};
		obj.menu1Id = menu1Id;
		obj.menuHeadId = menuHeadId;
		obj.name = name;
		return obj;
	}
	
	var menu1Arr = new Array();
	<%
		for(Menu1Model menu1 : menu1List)
		{
			%>
	menu1Arr.push(new menu1Object(<%= menu1.getId() %>,<%= menu1.getMenuHeadId() %>,'<%= menu1.getName() %>'));
			<%
		}
	%>

	function subForm() 
	{
		var headId = $("#sel_head_id").val();
		var menu1Id = $("#sel_menu_1_id").val();
		var name = $("#input_name").val();
		var url = $("#input_url").val();
		
		if("-1"==headId)
		{
			alert("请选择模块");
			return;
		}
		
		if("-1"==menu1Id)
		{
			alert("请选择菜单");
			return;
		}
		
		if(isNullOrEmpty(name))
		{
			alert("菜单名不能为空");
			return;
		}
		
		if(isNullOrEmpty(url))
		{
			alert("URL不能为空");
			return;
		}
		
		document.getElementById("addform").submit();
	}
	
	$(function()
	{
		$("#sel_head_id").change(menuHeadChange);
	});
	
	function menuHeadChange()
	{
		var headId = $("#sel_head_id").val();
		$("#sel_menu_1_id").empty(); 
		$("#sel_menu_1_id").append("<option value='-1'>请选择</option>");
		for(i=0; i<menu1Arr.length; i++)
		{
			if(menu1Arr[i].menuHeadId==headId)
			{
				$("#sel_menu_1_id").append("<option value='" + menu1Arr[i].menu1Id + "'>" + menu1Arr[i].name + "</option>");
			}
		}
	}
	
	$(function()
	{
		resetForm();
	});
	
	function resetForm()
	{
		 $("#sel_head_id").val("<%= menu2Model.getMenuHeadId() %>");
		 menuHeadChange();
		 $("#sel_menu_1_id").val("<%= menu2Model.getMenu1Id() %>");
		 $("#input_name").val("<%= menu2Model.getName() %>");
		 $("#input_url").val("<%= menu2Model.getUrl() %>");
		 $("#input_action_url").val("<%= menu2Model.getActionUrl() %>");
	}
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px">
				<label>修改子菜单</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="menu2action.jsp?query=<%= query %>" method="post" id="addform">
				<input type="hidden" value="0" name="type">
				<input type="hidden" value="<%= menu2Model.getId() %>" name="id">
				<dd class="dd01_me">模块</dd>
				<dd class="dd03_me">
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
				<dd class="dd01_me">菜单</dd>
				<dd class="dd04_me">
					<select name="menu_1_id" id="sel_menu_1_id"  style="width: 200px"> </select>
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
				<dd class="dd01_me">URL</dd>
				<dd class="dd03_me">
					<input type="text" name="url"  id="input_url" value="" style="width: 200px">
				</dd>
				
				<br />
				<br />
				<br />
				<dd class="dd00_me"></dd>
				<dd class="dd01_me">ACTION URL</dd>
				<dd class="dd03_me">
					<textarea rows="5" cols="30" name="action_url" id="input_action_url" style="width: 200px"></textarea>
				</dd>
<div style="clear: both;"><br /></div>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me"></dd>
					&nbsp;
					&nbsp;
					<textarea name="remark"  style="border:solid 1px black;" overflow-y="auto" overflow-x="hidden" maxlength="1000" cols="38" rows="5"  id="remark" ><%=menu2Model.getRemark() %></textarea>
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