<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.model.ChannelModel"%>
<%@page import="com.system.server.ChannelServer"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    Map<String,Object> name = new AppServer().loadApp();
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int appnameid = StringUtil.getInteger(request.getParameter("appname"), 0);
	String appkey = StringUtil.getString(request.getParameter("appkey"), "");
	String channel = StringUtil.getString(request.getParameter("channel"), "");
	int type = StringUtil.getInteger(request.getParameter("type_id"),0);
	
	
	Map<String,Object> map = new ChannelServer().loadChannel(pageIndex, appnameid, appkey, channel, type);
	
	List<ChannelModel> list = (List<ChannelModel>)map.get("list");
	
	int rowcount = (Integer)map.get("rows");
	
	
	
	List<AppModel> applist = (List<AppModel>)name.get("list");
	
    Map<String,String> params = new HashMap<String,String>();
	
	params.put("appname", appnameid+"");
	params.put("appkey", appkey);
	params.put("channel", channel);
	params.put("type", type+"");
	
	String pageData = PageUtil.initPageQuery("channel.jsp",params,rowcount,pageIndex); 
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
			  window.location.href = "delet.jsp?id=" + id+"&type=2"+"&pageindex=<%=pageIndex %>&appname=<%=appnameid%>&appkey=<%=appkey%>&channel=<%=channel%>&type_id=<%=type%>";	
		  }
	  }
	  $(function()
		{
			$("#appname").val(<%= StringUtil.getString(params.get("appname"), "-1") %>);
			$("#typeid").val(<%= StringUtil.getString(params.get("type"), "-1") %>);
		});
	  function check() 
	  {
		  console.log($("#input_appname").val());
		  if (isNullOrEmpty($("#input_appname").val())) 
			{
				alert("请输入应用名");
				$("#input_appname").focus();
				return;
			}
		  
		  if(isNullOrEmpty($("#input_appkey").val()))
		  {
			  alert("请输入应用KEY");
			  $("#input_appkey").focus();
			  return;
		  }
		  
		  if(isNullOrEmpty($("#input_channel").val()))
		  {
			  alert("请输入渠道");
			  $("#input_channel").focus();
			  return;
		  }
		  
		  document.getElementById("formid").submit();
		  
	  }
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="channeladd.jsp">增  加</a></dd>
			</dl>
			<form action="channel.jsp"  method="get" id="formid">
				<dl>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd04_me">
						<select name="appname" id="appname" style="width: 150px;" title="选择SP">
							<option value="-1">请选择应用名</option>
							<%
							
							for(AppModel app : applist)
							{
								%>
							<option value="<%= app.getId() %>"><%= app.getAppname() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">渠道</dd>
					<dd class="dd03_me">
						<input name="channel" id="input_channel" value="<%=channel %>" type="text" style="width: 150px">
					</dd>
					<dd class="dd01_me">同步类型</dd>
					<dd class="dd04_me">
						<select name="type_id" id="typeid" title="请选择同步类型" style="width: 150px">
							<option value="-1">请选择同步类型</option> 
							<option value="1">隔天</option>
							<option value="2">实时</option>
					    </select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" >
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>应用名</td>
					<td>渠道</td>
					<td>结算类型</td>
					<td>扣量比</td>
					<td>备注</td>
					<td>同步类型</td>
					<td>帐号名</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (ChannelModel model : list)
					
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppname() %></td>
					<td><%=model.getChannel()%></td>
					<td><%=model.getSettleType()==1?"CPA":"CPS" %></td>
					<td><%=model.getHold_percent()%></td>
					<td><%=model.getRemark()==null?"":model.getRemark()%></td>
					<td><%=model.getSyn_type()==1?"隔天":"实时"%></td>
					<td><%= model.getUserName() %></td>
					<td>
						<a href="channeledit.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>&appname=<%=appnameid%>&appkey=<%=appkey%>&channel=<%=channel%>&type_id=<%=type%>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
						<a href="channelacount.jsp?id=<%= model.getId() %>&query=<%=  Base64UTF.encode(request.getQueryString()) %>">用户分配</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="9" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>