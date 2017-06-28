<%@page import="com.system.sdk.server.SdkSpTroneServer"%>
<%@page import="com.system.sdk.model.SdkSpTroneModel"%>
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
	SdkSpTroneModel model = new SdkSpTroneServer().loadSdkSpTroneById(id);
	if(model==null)
	{
		response.sendRedirect("sdksptrone.jsp");
		return;
	}
	List<SdkSpModel> spList=new SdkSpServer().loadSdkSp();
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
function joSpModel(id,fullName,shortName)
{
	var obj = {};
	obj.id = id;
	obj.fullName = fullName;
	obj.shortName = shortName;
	return obj;
}

var spModelList=new Array();
<%for(SdkSpModel sdkSpModel : spList){%>
spModelList.push(new joSpModel(<%=sdkSpModel.getId() %>,'<%= sdkSpModel.getFullName() %>','<%=sdkSpModel.getShortName()%>'));<%}%>

$(function()
{
		resetForm();
		$("#sp_id").change(fullNameChange);

});

function fullNameChange()
{

	var spId = $("#sp_id").val();

	for(var i=0; i<spModelList.length; i++)
	{	
		if(spModelList[i].id==spId)
		{
			$("#short_sp_id").val(spModelList[i].shortName);
		}
	}
}
	
	


	function resetForm()
	{
		$("#sp_id").val("<%= model.getSpId() %>");
		$("#short_sp_id").val("<%= model.getShortName() %>");
		$("#trone_name").val("<%= model.getName()%>");

		
	}
	
	function subForm() 
	{
		if (isNullOrEmpty($("#sp_id").val())) 
		{
			alert("请输入SP全称");
			$("#sp_id").focus();
			return;
		}
		if (isNullOrEmpty($("#short_sp_id").val())) 
		{
			alert("请输入SP简称");
			$("#short_sp_id").focus();
			return;
		}
	
		document.getElementById("addform").submit();
	}
	

	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<label>SP业务修改</label>
			</dl>	
			<dl>
				<form action="action.jsp?query=<%= query %>" method="post"  id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
					<input type="hidden" value="<%= model.getSpTroneId() %>" name="spTroneId" />
					<input type="hidden" value="<%= model.getOperatorId() %>" name="operatorId" />
										
					<dd class="dd01_me">SP全称</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sp_id" title="选择业务" style="width: 200px">
							<option value="-1">请选择SP</option>
							<%
							for(SdkSpModel sdkSpModel : spList)
							{
								%>
							<option value="<%= sdkSpModel.getId() %>"><%= sdkSpModel.getFullName() %></option>	
								<%
							}
							%>
						</select>
					</dd>	
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">SP简称</dd>
					<dd class="dd03_me">
						<input type="text" name="short_sp_id" id="short_sp_id" disabled="readonly" 
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">业务名称</dd>
					<dd class="dd03_me">
						<input type="text" name="trone_name" id="trone_name" unselectable="on" readonly="readonly" value="<%=model.getName()%>"
							style="width: 200px"/>
					</dd>
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