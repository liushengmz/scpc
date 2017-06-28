<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%
	int menuId = StringUtil.getInteger(request.getParameter("menuid"),0);
	String guildTitle = "";
	
	if(menuId==-1)
		guildTitle = "用户资料修改";
	else
		guildTitle = menuId==0 ? "欢迎页" : new Menu2Server().getMenuGuildTitle(menuId);
	
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="wel_data/right.css" rel="stylesheet" type="text/css">
<title>Insert title here</title>
</head>
<body>
	<div class="main_content" style="margin-top: 0">
		<div class="right_tit">
			<dl>
				<dt></dt>
				<dd><%= guildTitle %></dd>
			</dl>
		</div>
	</div>
</body>
</html>