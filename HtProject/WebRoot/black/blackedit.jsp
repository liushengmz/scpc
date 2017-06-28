<%@page import="com.system.server.BlackServer"%>
<%@page import="com.system.model.BlackModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	BlackModel model = new BlackServer().loadBlackById(id);
	if(model==null)
	{
		response.sendRedirect("black.jsp");
		return;
	}
	List<BlackModel> list = new BlackServer().loadBlack();
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
		$("#edit_imei").val("<%= model.getImei() %>");
		$("#edit_imsi").val("<%= model.getImsi() %>");
		$("#edit_phone").val("<%= model.getPhone() %>");
		$("#edit_remark").val("<%= model.getRemark() %>");

	}
	
	function subForm() 
	{
		if (isNullOrEmpty($("#edit_imei").val())&isNullOrEmpty($("#edit_imsi").val())&isNullOrEmpty($("#edit_phone").val())) 
		{
			alert("IMEI、IMSI和电话不能全为空！");
			return;
		}
		
		document.getElementById("addform").submit();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
		
				<label>黑名单修改</label>
			
			</dl>
			<br />	<br />		
			<dl>
				<form action="action.jsp?type=1&query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
										
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">IMEI</dd>
					<dd class="dd03_me">
						<input type="text" name="imei" id="edit_imei"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">IMSI</dd>
					<dd class="dd03_me">
						<input type="text" name="imsi" id="edit_imsi"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">电话</dd>
					<dd class="dd03_me">
						<input type="text" name="phone" id="edit_phone"
							style="width: 200px">
					</dd>
					
				
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" id="edit_remark"
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