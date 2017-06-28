<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.model.UserRightModel"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	UserModel user = (UserModel)session.getAttribute("user");

	UserRightModel userRightModel = RightServer.loadUserRightModel(user);
	if(userRightModel==null)
		return;
	
	List<MenuHeadModel> headList = userRightModel.getMenuHeadList();
	if(headList==null || headList.size()==0)
		return;
	
	String logoImg = ConfigManager.getConfigData("SYSTEM_LOGO", "logo.png");
	
	String sysTitle = ConfigManager.getConfigData("SYSTEM_TITLE","运营管理平台");
	
	String sysUser = ConfigManager.getConfigData("SYSTEM_USER","SZHT");
	
	String splitorBand = "";
	
	if("SJHD".equalsIgnoreCase(sysUser))
	{
		splitorBand = "style=\"background-image: url('head_data/splitor_sjhd.png'); background-size:contain;\"";
	}
	else if("SZWX".equalsIgnoreCase(sysUser))
	{
		int[] userIds = {18,19,20};
		
		for(int id : userIds)
		{
			if(id==user.getId())
			{
				logoImg = "";
				break;
			}
		}
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><%= sysTitle %></title>

<link href="head_data/public.css" rel="stylesheet" type="text/css" />

<link href="head_data/nav.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="head_data/jquery-1.js"></script>
<script type="text/javascript" src="sysjs/base.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var leftFrame = $("#leftFrame", window.parent.document);
		var rightTop = $("#rightTop", window.parent.document);
		var rightFrame =  $("#rightFrame", window.parent.document);
		$(".btn_menu").each(function() {
			$(this).click(function() {
				var group = $(this).attr("id");
				leftFrame.attr("src", "./left.jsp?head=" + group);
				rightTop.attr("src", "./righttop.jsp");
				rightFrame.attr("src", "./welcome.html");
			});
		});
	});
	
	function goEditInfo()
	{
		var rightFrame = $("#rightFrame", window.parent.document);
		rightFrame.attr("src", "user/userinfo.jsp");
		
		var rightTop = $("#rightTop", window.parent.document);
		rightTop.attr("src", "righttop.jsp?menuid=-1");
	}
	
</script>

</head>

<body>
	<div class="header" style="background-color: #F7F7F7;height: 50px;">
		<div class="logo" style="float: left">
			<img src="head_data/<%= logoImg %>" style="margin-left: -20px; padding: 0px;" alt="">
		</div>
		<div
			style="float: right;height: 100%;line-height: 50px;text-align: center;font-size: 14px;">
			你好, <strong style="font-size: 16px"><%= user.getNickName() %></strong> 欢迎登陆运营管理平台&nbsp;&nbsp;|&nbsp;&nbsp;
			<script type="text/javascript">
				document.write(getDayDisplay())
			</script>
			&nbsp;&nbsp;|&nbsp;&nbsp;<a href="#" onclick="goEditInfo()">修改资料</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="loginaction.jsp?type=-1" target="_parent">退出</a>&nbsp;&nbsp;
		</div>
	</div>
	<div class="nav" <%= splitorBand %>>
		<div class="nav_left">&nbsp;</div>
		<div class="nav_right">
			<%
				if(headList.size()>1)
				for(MenuHeadModel model : headList)
				{
				%>
					<span><a href="javascript:void(0);" class="btn_menu" id="<%= model.getId() %>"><strong style="font-size: 14px"><%= model.getName() %></strong></a></span>
				<%	
				}
			%>			
		</div>

	</div>


</body>

</html>
