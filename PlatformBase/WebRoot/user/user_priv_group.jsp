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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int userId = ((UserModel)session.getAttribute("user")).getId();
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	List<GroupModel> groupList = new GroupServer().loadRightGroupByUserId(userId);
	UserServer userServer = new UserServer();
	UserModel model = new UserServer().getUserModelById(id);
	if(model==null)
	{
		response.sendRedirect("user_priv.jsp");
		return;
	}
	
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
<title>翔通运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	var groupArr = new Array();
	
	<%for(int group : userGroupList){%>groupArr.push(<%= group %>);<%}%>

	function subForm() 
	{
		document.getElementById("addform").submit();
	}
	
	$(function() 
	{
		resetForm();
		<%= msg %>
	});
	
	function resetForm()
	{
		$("[name='groupid']").removeAttr("checked");
		
		for(i=0; i<groupArr.length; i++)
		{
			document.getElementById("groupid_" + groupArr[i]).checked = true;	
		}
		
	}
	
	function goToMain()
	{
		window.location.href = "user_priv.jsp?<%= Base64UTF.decode(query) %>";
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px">
					<label>用户角色分配(<%= model.getNickName() %>)</label>
				</dd>
			</dl>
			<br /> <br />
			<dl>
				<form action="user_priv_action.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="<%= id %>" name="id">
					<input type="hidden" value="7" name="type">
					<table style="text-align: left">
					<%
					int i=0;
					out.println("<tr>");
					for (GroupModel group : groupList)
					{
						if(i%5==0)
							out.print("</tr><tr>");
						
						out.print("<td style=\"text-align: left\"><input type=\"checkbox\" name=\"groupid\" id=\"groupid_" + group.getId() 
						+ "\" value=\"" + group.getId() + "\"></input>&nbsp;&nbsp;" + group.getName() + "</td>");
						
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