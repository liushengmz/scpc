<%@page import="com.system.sdk.server.SdkSpServer"%>
<%@page import="com.system.sdk.model.SdkSpModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
    int flag = StringUtil.getInteger(request.getParameter("flag"), -1);
	SdkSpModel model = new SdkSpServer().loadSdkSpById(id);
	if(model==null)
	{
		response.sendRedirect("sdksp.jsp");
		return;
	}
	String query = StringUtil.getString(request.getParameter("query"), "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../../sysjs/base.js"></script>
<script type="text/javascript" src="../../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	
	$(function()
	{
		
		resetForm();
	});
	
	function resetForm()
	{
		$("#input_full_name").val("<%= model.getFullName() %>");
		$("#input_short_name").val("<%= model.getShortName() %>");
		
	}
	
	function subForm() 
	{
		var fullName=$("#input_full_name").val();
		if (isNullOrEmpty($("#input_full_name").val())) 
		{
			alert("请输入SP全称");
			$("#input_full_name").focus();
			return;
		}
		var shortName=$("#input_short_name").val();
		if (isNullOrEmpty($("#input_short_name").val())) 
		{
			alert("请输入SP简称");
			$("#input_short_name").focus();
			return;
		}
		var id='<%=id%>';
		var result=ajaxLogin(fullName, shortName, id);
		if(result==1){
			alert("SP全称已存在！");
			resetForm();
			return;
		}
		if(result==2){
			alert("SP简称已存在！");
			resetForm();
			return;
		}
		if(result==3){
			alert("SP已存在！");
			resetForm();
			return;
		}

		
		
		document.getElementById("addform").submit();
	}
	
	function ajaxLogin(fullName, shortName,id) {
		var result = "";
		$.ajax({
			url : "spajax.jsp",
			data : "fullName=" + fullName + "&shortName=" + shortName+"&id="+id,
			cache : false,
			async : false,
			success : function(html) {
				result = $.trim(html);
			}
		});
		return result;
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<label>SDKSP修改</label>
			</dl>	
			<dl>
				<form action="action.jsp?query=<%= query %>" method="post"  id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
										
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">SP全称</dd>
					<dd class="dd03_me">
						<input type="text" name="full_name" id="input_full_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">SP简称</dd>
					<dd class="dd03_me">
						<input type="text" name="short_name" id="input_short_name"
							style="width: 200px">
					</dd>

					<div style="clear: both;"><br /></div>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me"></dd>
					&nbsp;
					&nbsp;
					<textarea name="remark"  style="border:solid 1px black;" overflow-y="auto" overflow-x="hidden" maxlength="1000" cols="37" rows="10"  id="remark" ><%=model.getRemark() %></textarea>

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