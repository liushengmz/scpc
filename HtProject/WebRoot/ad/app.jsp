<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.model.AdAppModel"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int userId =  ((UserModel)session.getAttribute("user")).getId();
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
    String appname = StringUtil.getString(request.getParameter("appname"), "");
    String appkey = StringUtil.getString(request.getParameter("appkey"), "");
    

    
	Map<String,Object> map = new AdAppServer().loadApp(pageIndex, appname, appkey);
	
	List<AdAppModel> list = (List<AdAppModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("appname", appname);
	params.put("appkey", appkey);
	
	String pageData = PageUtil.initPageQuery("app.jsp",params,rowCount,pageIndex); 
	
	
	String query = Base64UTF.encode(request.getQueryString());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>运营管理平台</title>
    <link href="../wel_data/right.css" rel="stylesheet" type="text/css">
    <link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
    <script type="text/javascript">
	
	  function delTrone(id)
	  {
		  if(confirm('真的要删除吗？'))
		  {
			  window.location.href = "appaction.jsp?id=" + id+"&type=1";	
		  }
	  }
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="appadd.jsp?userid=<%=userId %>">增  加</a></dd>
				<form action="app.jsp"  method="post" id="formid">
				<dl>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd03_me">
						<input name="appname" id="input_appkey" value="<%=appname %>" type="text" style="width: 150px">
					</dd>
					<dd class="dd01_me">应用KEY</dd>
					<dd class="dd03_me">
						<input name="appkey" id="input_appkey" value="<%=appkey %>" type="text" style="width: 150px">
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" >
					</dd>
				</dl>
			</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>应用名</td>
					<td>应用KEY</td>
					<td>扣量比</td>
					<td>所属账号</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (AdAppModel model : list)
					
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppname()%></td>
					<td><%=model.getAppkey()%></td>
					<td><%=model.getHold_percent()%></td>
					<td><%=model.getCreateName()%></td>
					<td>
						<a href="appedit.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
						<a href="appacount.jsp?id=<%= model.getId() %>&query=<%= query %>">用户分配</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="6" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>