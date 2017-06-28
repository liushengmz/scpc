<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.net.URLEncoder"%>
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
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
    String appname = StringUtil.getString(request.getParameter("appname"), "");
    String appkey = StringUtil.getString(request.getParameter("appkey"), "");
    String query = Base64UTF.encode(request.getQueryString());
    int appType = StringUtil.getInteger(request.getParameter("app_type"), -1);

	Map<String,Object> map = new AppServer().loadApp(pageIndex, appname, appkey,appType);
	
	List<AppModel> list = (List<AppModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("appname", appname);
	params.put("appkey", appkey);
	params.put("app_type",appType+"");
	
	String pageData = PageUtil.initPageQuery("app.jsp",params,rowCount,pageIndex); 
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
			  window.location.href = "delet.jsp?id=" + id+"&type=1"+"&appname=<%=appname%>&appkey<%=appkey%>";	
		  }
	  }
	  
	  $(function()
	  {
		  $("#sel_app_type").val("<%= appType %>");
	  });
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="appadd.jsp">增  加</a></dd>
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
					<dd class="dd01_me">应用类型</dd>
					<dd class="dd04_me">
						<select name="app_type" id="sel_app_type" style="width: 150px;" title="应用类型">
							<option value="-1">应用类型</option>
							<option value="1">自营应用</option>
							<option value="2">第三方应用</option>
						</select>
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
					<td>应用类型</td>
					<td>信息费扣量比</td>
					<td>备注</td>
					<td>帐号名</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (AppModel model : list)
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppname()%></td>
					<td><%=model.getAppkey()%></td>
					<td><%= model.getAppType()==1 ? "自营" : "第三方" %></td>
					<td><%=model.getHold_percent()%></td>
					<td><%=model.getRemark()==null?"":model.getRemark()%></td>
					<td><%= model.getUserName() %></td>
					<td>
						<%
							String encodeStr = URLEncoder.encode(appname,"GBK"); 
						%>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
						<a href="appedit.jsp?id=<%= model.getId() %>&query=<%= query %>">修改</a>
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