<%@page import="com.system.server.CommRightServer"%>
<%@page import="com.system.model.CommRightModel"%>
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
	CommRightModel model = new CommRightServer().loadCommRightById(id);
	if(model==null)
	{
		response.sendRedirect("commright.jsp");
		return;
	}
	int spCommerceId = StringUtil.getInteger(ConfigManager.getConfigData("SP_COMMERCE_GROUP_ID"),-1);
	int cpCommerceId = StringUtil.getInteger(ConfigManager.getConfigData("CP_COMMERCE_GROUP_ID"),-1);
	List<UserModel> commCpList = new UserServer().loadUserByGroupId(cpCommerceId);
	List<UserModel> commSpList = new UserServer().loadUserByGroupId(spCommerceId);
	List<UserModel> commList;
	UserModel user=(UserModel)session.getAttribute("user");
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
	
function joCpChannel(name)
{
	var obj = {};
	obj.name = name;
	return obj;
}
$(function()
		{	
		resetForm();

		});
function resetForm(){
	var rightList = "<%=model.getRightList()%>";
	var rightLists = rightList.split(",");
	<%if(model.getType()==0){%>
	$("#sel_commerce_sp_user_id").val('<%=model.getUserId()%>');
	$("#type").val('<%=model.getType()%>');
	$("#cp_comm").hide();
	$("#sp_comm").show();
	$("#sp_user").show();
	$("#cp_user").hide();
	unAllCkb();
	$('[name=area]:checkbox').each(function() {

		for (k = 0; k < rightLists.length; k++) {
			if (rightLists[k] == this.value) {
				this.checked = true;
				break;
			}
		}
	});
	<%}else{%>
	$("#sel_commerce_cp_user_id").val('<%=model.getUserId()%>');

	$("#type").val('<%=model.getType()%>');
	$("#sp_comm").hide();
	$("#cp_comm").show();
	$("#sp_user").hide();
	$("#cp_user").show();
	unAllCkb();
	$('[name=cparea]:checkbox').each(function() {

		for (k = 0; k < rightLists.length; k++) {
			if (rightLists[k] == this.value) {
				this.checked = true;
				break;
			}
		}
	});
	<%}%>
	$("#type").change(typeChange);
}
function typeChange(){
	var type=$("#type").val();
	if(type=='1'){
		$("#sp_comm").hide();
		$("#cp_comm").show();
		$("#sp_user").hide();
		$("#cp_user").show();

	}else{
		$("#cp_comm").hide();
		$("#sp_comm").show();
		$("#sp_user").show();
		$("#cp_user").hide();

	}
	
}
function subForm() 
{
	var type=$("#type").val();
	if (isNullOrEmpty($("#type").val())||$("#type").val()=='-1') 
	{
		alert("请选择商务类型");
		$("#type").focus();
		return;
	}
	if(type==0){
	if (isNullOrEmpty($("#sel_commerce_sp_user_id").val())||$("#sel_commerce_sp_user_id").val()=='-1') 
	{
		alert("请选择SP商务");
		$("#sel_commerce_sp_user_id").focus();
		return;
	}
	if(getCount('area')<=0)
		{
			alert("请选择授权SP商务");
			return;
		}
	}else{
		if (isNullOrEmpty($("#sel_commerce_cp_user_id").val())||$("#sel_commerce_cp_user_id").val()=='-1') 
		{
			alert("请选择CP商务");
			$("#sel_commerce_cp_user_id").focus();
			return;
		}
		if(getCount('cparea')<=0)
		{
			alert("请选择授权CP商务");
			return;
		}
	}
	var id='<%=id%>';
	var userId=-1;
	if(type=='0'){
		userId=$("#sel_commerce_sp_user_id").val();
	}else{
		userId=$("#sel_commerce_cp_user_id").val();
	}
	var result=ajaxCheck(userId, type,id);
	if(result=='1'){
		alert("数据已存在！");
		return ;
	}
	document.getElementById("addform").submit();
}

function ajaxCheck(userId, type,id) {
	var result = "";
	$.ajax({
		url : "action.jsp",
		data : "user_id=" + userId + "&type=" + type+"&id="+id+"&act=2",
		cache : false,
		async : false,
		success : function(html) {
			result = $.trim(html);
		}
	});
	return result;
}

//$(function(){ $("#sel_commerce_user_id").val(""); });
function allCkb(items) {
	$('[name=' + items + ']:checkbox').attr("checked", true);
}
function unAllCkb(items) {
	$('[type=checkbox]:checkbox').attr('checked', false);

}

function inverseCkb(items) {
	$('[name=' + items + ']:checkbox').each(function() {
		this.checked = !this.checked;
	});
}

function getCount(items)
{
	var i = 0;
	$('[name=' + items + ']:checkbox').each(function() {
		if(this.checked)
			i++;
	});
	return i;
}
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width: 100px">
				<label>商务授权修改</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="action.jsp?query=<%= query %>" method="post"  id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
					<dd class="dd01_me">商务类型</dd>
					<dd class="dd04_me">
						<select name="type" id="type" >
						<option value="0">SP商务</option>	
						<option value="1">CP商务</option>
						</select>		
					</dd>
					<div id="sp_user">
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">SP商务</dd>
					<dd class="dd04_me">
						<select name="commerce_sp_user_id" id="sel_commerce_sp_user_id">
							<option value="-1">请选择</option>	
						<%
							for(UserModel comUser : commSpList){
						%>
							<option value="<%= comUser.getId() %>"><%= comUser.getNickName() %></option>	
						<%
							}
						%>
						</select>
					</dd>
					</div>
					<div id="cp_user" style="display: none;">
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP商务</dd>
					<dd class="dd04_me">
						<select name="commerce_cp_user_id" id="sel_commerce_cp_user_id">
							<option value="-1">请选择</option>	
						<%
							for(UserModel comCpUser : commCpList){
						%>
							<option value="<%= comCpUser.getId() %>"><%= comCpUser.getNickName() %></option>	
						<%
							}
						%>
						</select>
					</dd>
					</div>
					<div id="sp_comm">
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">授权SP商务</dd>
					<div style="margin-left: 95px; width: 580px;" id="pro">

						<%
							for (UserModel spUser : commSpList) {
						%>

							<dd class="dd01"><%=spUser.getNickName()%>
							<input style="" type="checkbox" title="<%=spUser.getNickName()%>"
								class="chpro" name="area" value="<%=spUser.getId()%>">
						</dd>
						<%
							}
						%>
						<input type="button" onclick="allCkb('area')"
							style="horve: point;" value="全　选" /> <input type="button"
							onclick="unAllCkb()" style="padding-top: 10px;" value="全不选" /> <input
							type="button" onclick="inverseCkb('area')"
							style="padding-top: 10px;" value="反　选" /> 
					</div>
					</div>
					<div style="clear: both"><br/></div>
					<div id="cp_comm" style="display: none">
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">授权CP商务</dd>
					<div style="margin-left: 95px; width: 580px;" id="pro">

						<%
							for (UserModel cpUser : commCpList) {
						%>
							<dd class="dd01"><%=cpUser.getNickName()%>
							<input style="" type="checkbox" title="<%=cpUser.getNickName()%>"
								class="chpro" name="cparea" value="<%=cpUser.getId()%>">
						</dd>
						<%
							}
						%>
						<input type="button" onclick="allCkb('cparea')"
							style="horve: point;" value="全　选" /> <input type="button"
							onclick="unAllCkb()" style="padding-top: 10px;" value="全不选" /> <input
							type="button" onclick="inverseCkb('cparea')"
							style="padding-top: 10px;" value="反　选" /> 
					</div>
					</div>
					<div style="clear: both;"><br /></div>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me"></dd>
					&nbsp;
					&nbsp;
					<textarea name="remark"  style="border:solid 1px black;" overflow-y="auto" overflow-x="hidden" maxlength="1000" cols="91" rows="10"  id="remark" ><%=model.getRemark() %></textarea>
					<div style="clear: both"><br/></div>
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