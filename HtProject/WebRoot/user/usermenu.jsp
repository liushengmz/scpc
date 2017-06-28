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
<%@page import="com.system.model.UserMenuModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int userId = StringUtil.getInteger(request.getParameter("id"),-1);
	List<UserMenuModel> list=new Menu2Server().loadUserMenuByUserId(userId);
	List<Menu1Model> listMenu1=new Menu1Server().loadMenu1ListByUserId(userId);

	
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

	
	
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px">
					<label>用户菜单</label>
				</dd>
			</dl>
			<br /> <br />
			<dl>
				<form action="#" method="post" id="addform">
					<%
					for (Menu1Model menu1Model : listMenu1)
					{
					%>
					<dd class="dd01_me" ><%=menu1Model.getName()%></dd>
					<dd class="ddtxt02">			
					<%
						for(UserMenuModel userMenuModel : list)
						{

							int rowCount=0;
							if(userMenuModel.getMenu_1_id()==menu1Model.getId())
							{
								rowCount++;
					%>
					
					<label class="ddtxt02" ><%=userMenuModel.getMenu_2_name() %></label>

						<%
						if(rowCount%10==0){

							out.print("</dd><dd class=\"dd00\"></dd><dd class=\"ddtxt03\"></dd><dd style=\"background: transparent;\" class=\"dd01_me\">&nbsp;&nbsp;</dd><dd class=\"ddtxt02\">");
						}
							}
						}
					%>
					</dd>
					<div style="clear: both;"><br /></div>
					<%
					}
					%>
					
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
				
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>

				</form>
			</dl>
		</div>

	</div>
</body>
</html>