<%@page import="com.system.server.ProductServer"%>
<%@page import="com.system.dao.ProductDao"%>
<%@page import="com.system.model.ProductModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<ProductModel> list = new ProductServer().loadProductList();
	List<ProductModel> linelist = new ProductServer().loadProductLineList();
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
function joProductLine(id,operflag,name)
{
	var obj = {};
	obj.id = id;
	obj.operflag = operflag;
	obj.name = name;
	return obj;
}
var productline=new Array();
<%for(ProductModel productLine : linelist){%>
productline.push(new joProductLine(<%= productLine.getProductLineId() %>,<%= productLine.getOperFlag() %>,'<%=productLine.getProductLineName()%>'));<%}%>
function operatorChange()
{

	var operflag = $("#operator").val();
	$("#product_line").empty();
	$("#product_line").append("<option value='-1'>请选择</option>");

	for(var i=0; i<productline.length; i++)
	{	
		if(productline[i].operflag==operflag)
		{
			$("#product_line").append("<option value='" + productline[i].id + "'>" + productline[i].name + "</option>");
		}
	}
}
	$(function()
			{
				//SP的二级联动
				$("#operator").change(operatorChange);
				//spTroneChange();
			});
	function subForm() 
	{
		if ($("#operator").val() == "-1") {
			alert("请选择运营商");
			$("#operator").focus();
			return;
		}
		if ($("#product_line").val() == "-1") {
			alert("请选择业务线");
			$("#product_line").focus();
			return;
		}
		if (isNullOrEmpty($("#product_name").val())) 
		{
			alert("请输入产品名");
			return;
		}
		document.getElementById("addform").submit();
	}
	
	//$(function(){ $("#sel_commerce_user_id").val(""); });
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>增加</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="action.jsp" method="get" id="addform">
					<dd class="dd01_me">运营商</dd>
					<dd class="dd04_me">
						<select name="operator" id="operator" style="width: 200px">
							<option value="-1">请选择</option>
							<%
							for(ProductModel model : list)
							{
								%>
							<option value="<%= model.getFlag() %>"><%= model.getCnName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
							
					<br />
					<br />
					<br />
					<dd class="dd01_me">业务线</dd>
					<dd class="dd04_me">
						<select name="product_line" id="product_line" title="选择业务" style="width: 200px">
							<option value="-1">请选择业务</option>
						</select>
					</dd>	

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">产品名称</dd>
					<dd class="dd03_me">
						<input type="text" name="product_name" id="product_name"
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