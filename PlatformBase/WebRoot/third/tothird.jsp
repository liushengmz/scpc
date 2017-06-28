<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
 <%
 	UserModel user = (UserModel)session.getAttribute("user");
 
 	if(user==null)
 	{
 		out.clear();
		out.print("<script>window.location.href='login.jsp'</script>");
		return;
 	}
 	
 	String userName = user.getName();
 	String pwd = user.getPassword();
 	String urlType = StringUtil.getString(request.getParameter("url"), "/sp/tbl_sp_api_urlList.aspx"); 
 	
 	String postUrl = ConfigManager.getConfigData("TO_THIRD_MENU", "http://admin.n8wan.com/passport/login.ashx");
 %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<form action="<%= postUrl %>" method="post" style="display: none">
	<input type="hidden" name="userName" value="<%= userName %>" />
	<input type="hidden" name="password" value="<%= pwd %>" />
	<input type="hidden" name="pType" value="md5" />
	<input type="hidden" name="returnUrl" value="<%= urlType %>" />
	<input type="submit" value="tijiao">
</form>
<script>document.forms[0].submit();</script>	
</body>
</html>
