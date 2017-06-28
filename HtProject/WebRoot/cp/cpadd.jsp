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
	List<CpModel> list = new CpServer().loadCp();
	int cpCommerceId = StringUtil.getInteger(ConfigManager.getConfigData("CP_COMMERCE_GROUP_ID"),-1);
	List<UserModel> userList = new UserServer().loadUserByGroupId(cpCommerceId);
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
	
	function joCpChannel(name)
	{
		var obj = {};
		obj.name = name;
		return obj;
	}
	var cpChannelArray = new Array();
	<%
		for(CpModel cp : list)
		{
			%>
			cpChannelArray.push(new joCpChannel('<%= cp.getShortName() %>'));	
			<%
		}
	%>
	
	function subForm() 
	{
		if (isNullOrEmpty($("#input_full_name").val())) 
		{
			alert("请输入CP全称");
			$("#input_full_name").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_short_name").val())) 
		{
			alert("请输入CP简称");
			$("#input_short_name").focus();
			return;
		}
		
		if (!isNullOrEmpty($("#input_short_name").val())) 
		{
			var name = $("#input_short_name").val();
			for(i=0; i<cpChannelArray.length; i++)
			{
				if(cpChannelArray[i].name==name)
				{
					alert("该CP简称已存在");
					$("#input_short_name").focus();
					return;
				}
			}
		}
		
		document.getElementById("addform").submit();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>CP增加</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="action.jsp" method="post" id="addform">
										
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP全称</dd>
					<dd class="dd03_me">
						<input type="text" name="full_name" id="input_full_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP简称</dd>
					<dd class="dd03_me">
						<input type="text" name="short_name" id="input_short_name"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">联系人</dd>
					<dd class="dd03_me">
						<input type="text" name="contract_person" id="input_contract_person"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">商务</dd>
					<dd class="dd04_me">
						<select name="commerce_user_id" id="sel_commerce_user_id">
							<option value="-1">请选择</option>
							<%
							for(UserModel model : userList)
							{
								%>
							<option value="<%= model.getId() %>"><%= model.getNickName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd03_me">
						<input type="radio" name="status" style="width: 35px;float:left" value="1" checked="checked" >
						<label style="font-size: 14px;float:left">开启</label>
						<input type="radio" name="status" style="width: 35px;float:left" value="0" >
						<label style="font-size: 14px;float:left">锁定</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">QQ</dd>
					<dd class="dd03_me">
						<input type="text" name="qq" id="input_qq"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">Email</dd>
					<dd class="dd03_me">
						<input type="text" name="email" id="input_email"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">电话</dd>
					<dd class="dd03_me">
						<input type="text" name="phone" id="input_phone" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">地址</dd>
					<dd class="dd03_me">
						<input type="text" name="address" id="input_address" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">合同起始日</dd>
					<dd class="dd03_me">
						<input type="text" name="contract_start_date" id="input_contract_start_date" style="width: 200px" onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">合同结束日</dd>
					<dd class="dd03_me">
						<input type="text" name="contract_end_date" id="input_contract_end_date" style="width: 200px" onclick="WdatePicker({isShowClear:false,readOnly:true})">
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