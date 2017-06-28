<%@page import="com.system.server.AppServer"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String appname = StringUtil.getString(request.getParameter("appname"), "");
	String appkey = StringUtil.getString(request.getParameter("appkey"), "");
	AppModel model = new AppServer().loadAppById(id);
	String query = request.getQueryString();
	
	if(model==null)
	{
		response.sendRedirect("app.jsp?"+query);
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
//声明整数的正则表达式
	function isNum(a)
	{
		//var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		var reg = /^[0-9]*[1-9][0-9]*$/i;
		return reg.test(a);
	}
	
	$(function()
	{
		resetForm();
	});
	
	function resetForm()
	{
		$("#input_app_name").val("<%= model.getAppname() %>");
		$("#input_app_key").val("<%= model.getAppkey() %>");
		setRadioCheck("app_type",<%= model.getAppType() %>);
		$("#input_hold_persent").val("<%= model.getHold_percent() %>");
		$("#input_remark").val("<%= model.getRemark() %>");
		
	}
	
	function subForm() 
	{
		if (isNullOrEmpty($("#input_app_name").val())) 
		{
			alert("请输入应用名");
			$("#input_full_name").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_app_key").val())) 
		{
			alert("请输入应用KEY");
			$("#input_short_name").focus();
			return;
		}
		
		if(!(isNum($("#input_hold_persent").val()+1)))
		{
			alert("请输入整数！");
			$("#input_hold_persent").focus();
			return;
		}
		
		var num = parseInt($("#input_hold_persent").val(), 10);
		
		if(num<0||num>100)
		{
			alert("请输入0~100之间的整数");
			$("#input_hold_persent").focus();
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
				<label>APP修改</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="appaction.jsp?pageindex=<%=pageIndex %>&appname=<%=appname %>&appkey=<%=appkey %>" method="post" id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
					<input type="hidden" 
					  value="<%= StringUtil.getInteger(request.getParameter("pageindex"), 1) %>" 
					  name="pageindex" />
										
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd03_me">
						<input type="text" name="app_name" id="input_app_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">应用KEY</dd>
					<dd class="dd03_me">
						<input type="text" name="app_key" id="input_app_key"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">应用类型</dd>
					<dd class="dd03_me">
						<input type="radio" name="app_type" style="width: 35px;float:left" value="1" checked="checked" >
						<label style="font-size: 14px;float:left">自营</label>
						<input type="radio" name="app_type" style="width: 35px;float:left" value="2" checked="checked">
						<label style="font-size: 14px;float:left">第三方</label>
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">扣量比</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_persent" id="input_hold_persent"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" id="input_remark"
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