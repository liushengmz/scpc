<%@page import="java.net.URLEncoder"%>
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
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String name = StringUtil.getString(request.getParameter("encodeStr"), "");
	int type = StringUtil.getInteger(request.getParameter("type"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	GroupServer groupServer = new GroupServer();
	GroupModel group = groupServer.loadGroupById(id);
	if(group==null)
	{
		response.sendRedirect("group.jsp");
		return;
	}
	String msg = "";
	String encodeStr = URLEncoder.encode(name,"GBK"); 
	if(StringUtil.getInteger(request.getParameter("msg"), -1)==1){
		msg = "alert('修改成功');";
		msg += "window.location.href = 'group.jsp?pageindex="+pageIndex+"&name="+encodeStr+"&type="+type+";'";
		}

	List<UserModel> users=new GroupServer().loadGroupUsersById(id);
	
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
	
	var userArr = new Array();
	<%for(UserModel user : users){%>userArr.push(<%= user.getId() %>);<%}%>
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
		$("[name='user_chex']").removeAttr("checked");
		
		for(i=0; i<userArr.length; i++)
		{
			document.getElementById("user_chex_" + userArr[i]).checked = true;	
		}
	}
	
	function goToMain()
	{
		window.location.href = "group.jsp?pageindex=<%=pageIndex%>&name=<%=encodeStr%>&type=<%=type%>";
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
		<dl>	<dd class="ddbtn" style="width:200px">
					<label><%= group.getName() %></label>
				</dd>
		</dl>
			<dl>
				<form action="groupaction.jsp" method="post" id="addform">
					<input type="hidden" value="<%= group.getId() %>" name="id">
					<input type="hidden" value="2" name="type">
					<input type="hidden" value="<%=pageIndex%>" name="pageindex">
					
					<dd class="dd00"></dd>					
					<dd class="ddtxt03"></dd>
					<dd class="dd01_me">角色用户</dd>
					<dd class="ddtxt02">			
					<%	
								int rowCount = 0;
								for(UserModel user : users)
								{
										rowCount++;
					%>
										<label><input type="checkbox" name="user_chex" id="user_chex_<%= user.getId()%>" checked="checked" value="<%=user.getId() %>" /><%= user.getNickName() %></label>
					<%
										if(rowCount%10==0)
											out.print("</dd><dd class=\"dd00\"></dd><dd class=\"ddtxt03\"></dd><dd style=\"background: transparent;\" class=\"dd01_me\">&nbsp;&nbsp;</dd><dd class=\"ddtxt02\">");
								}
					%>
					</dd>
				
					<div style="clear: both;"><br /></div>
				
					
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="重 置" onclick="resetForm()">
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