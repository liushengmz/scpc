<%@page import="com.system.server.ProductServer"%>
<%@page import="com.system.model.ProductModel"%>
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
	ProductModel model = new ProductServer().loadLineProductById(id);
	if(model==null)
	{
		response.sendRedirect("productline.jsp");
		return;
	}
	List<ProductModel> list = new ProductServer().loadProductList();
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
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function()
	{
		resetForm();


	});
	
	function resetForm()
	{
		$("#operator").val("<%= model.getFlag() %>");
		$("#productline_name").val("<%= model.getProductLineName() %>");
	
		
	}
	
	function subForm() 
	{
		if ($("#operator").val() == "-1") {
			alert("请选择运营商");
			$("#operator").focus();
			return;
		}
		if (isNullOrEmpty($("#productline_name").val())) 
		{
			alert("请输入产品线");
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
				<label>修改</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="action.jsp?query=<%= query %>" method="post"  id="addform">
					<input type="hidden" value="<%= model.getProductLineId() %>" name="id" />
										
					<dd class="dd01_me">运营商</dd>
					<dd class="dd04_me">
						<select name="operator" id="operator"  style="width: 200px">
							<option value="-1">请选择</option>
							<%
							for(ProductModel opModel : list)
							{
								%>
							<option value="<%= opModel.getFlag() %>"><%= opModel.getCnName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
							
					

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">产品线</dd>
					<dd class="dd03_me">
						<input type="text" name="productline_name" id="productline_name"
							style="width: 200px">
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