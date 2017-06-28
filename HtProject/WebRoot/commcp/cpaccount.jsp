<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.model.AdAppModel"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);

	int groupId = StringUtil.getInteger(ConfigManager.getConfigData("DF_CP_GROUP_ID"),-1);
	
	CpModel cpModel = new CpServer().loadCpById(id);
	
	UserServer userServer = new UserServer();
	
	List<UserModel> userList = userServer.loadUserByGroupId(groupId);
	
	if(cpModel==null)
	{
		response.sendRedirect("cp.jsp");
		return;
	}
	
	UserModel emptyUser = new UserModel();
	
	emptyUser.setId(0);
	emptyUser.setName("unknow");
	emptyUser.setNickName("未知");
	
	userList.add(emptyUser);
	
	String msg = "";
	
	if(StringUtil.getInteger(request.getParameter("msg"), -1)==1)
		msg = "alert('修改成功');";
		
	List<Integer> userGroupList = userServer.loadUserGroup(id);
	
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

	function subForm() 
	{
		document.getElementById("addform").submit();
	}
	
	function goToMain()
	{
		window.location.href = "cp.jsp?<%= Base64UTF.decode(query) %>";
	}
	
	$(function()
	{
		setRadioCheck("userid",<%= cpModel.getUserId() %>);
		<%= msg %>
	});
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:500px">
					<label>垫付CP登录分配(<%= cpModel.getShortName() %>)</label>
				</dd>
			</dl>
			<br /> <br />
			<dl>
				<form action="action.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="<%= id %>" name="id">
					<input type="hidden" value="1" name="accounttype">
					<table style="text-align: left">
					<%
					int i=0;
					out.println("<tr>");
					for (UserModel user : userList)
					{
						if(i%5==0)
							out.print("</tr><tr>");
						
						out.print("<td style=\"text-align: left\"><input type=\"radio\" name=\"userid\" id=\"userid_" + user.getId() 
						+ "\" value=\"" + user.getId() + "\"></input>&nbsp;&nbsp;" + user.getName() + "[" + user.getNickName() + "]" + "</td>");
						
						i++;
					}
					out.println("</tr>");
					%>
					</table>
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="goToMain()">
					</dd>

				</form>
			</dl>
		</div>

	</div>
</body>
</html>