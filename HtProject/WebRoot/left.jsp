<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URL"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.model.UserRightModel"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	UserModel user = (UserModel) session.getAttribute("user");

	UserRightModel userRightModel = RightServer.loadUserRightModel(user);

	if (userRightModel == null)
		return;

	int headId = StringUtil.getInteger(request.getParameter("head"), 0);

	LinkedHashMap<String, List<Menu2Model>> map = RightServer.loadUserLeftMenu(user, headId);

	if (map == null)
		return;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link href="head_data/public.css" rel="stylesheet" type="text/css" />
<link href="Notes_data/admin.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="Notes_data/jquery-1.js"></script>
<script type="text/javascript" src="Notes_data/Validform_v5.js"></script>

<script type="text/javascript" src="Notes_data/common.js"></script>
<script type="text/javascript" src="Notes_data/kindeditor-all-min.js"></script>
<script type="text/javascript" src="Notes_data/zh_CN.js"></script>
<script type="text/javascript" src="Notes_data/formateditor.js"></script>
<script type="text/javascript" src="Notes_data/admin.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		var rightTop = $("#rightTop", window.parent.document);
		$(document.getElementsByName("leftmenua")).each(function() {
			$(this).click(function() {
				var menuid = $(this).attr("id");
				rightTop.attr("src", "righttop.jsp?menuid=" + menuid);
			});
		});
	});
</script>


<link rel="stylesheet" href="Notes_data/default.css">
</head>

<body class="zz_left" style="background-color: #F7F7F7; width: auto;">

	<div class="left_grxx">
		<div class="leftmenu">

			<%
				for (String key : map.keySet())
				{
			%>
			<ul class="menu">
				<li><a href="javascript:void(0)" class="menu_title"><%=key%></a>
					<ul style="display: none;">
						<li class="last">
							<div class="left_main">
								<dl>
									<%
										for (Menu2Model model : map.get(key))
											{
									%>
									<dd class="dd01">
										<a name="leftmenua" id="<%=model.getId()%>"
											href="./<%=model.getUrl()%>" target="right"><%=model.getName()%></a>
									</dd>
									<%
										}
									%>

								</dl>
							</div>
						</li>
					</ul></li>
			</ul>
			<%
				}
			%>
			<div class="left_bottom"></div>
		</div>
	</div>
</body>
</html>
