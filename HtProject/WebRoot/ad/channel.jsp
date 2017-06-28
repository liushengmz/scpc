<%@page import="com.system.model.UserModel"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="com.system.model.AdAppModel"%>
<%@page import="com.system.server.AdChannelServer"%>
<%@page import="com.system.model.AdChannelModel"%>
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
	int userId =  ((UserModel)session.getAttribute("user")).getId();
    Map<String,Object> name = new AdAppServer().loadApp(1);
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int appnameid = StringUtil.getInteger(request.getParameter("appname"), 0);
	String appkey = StringUtil.getString(request.getParameter("appkey"), "");
	String channel = StringUtil.getString(request.getParameter("channel"), "");
	String channelname = StringUtil.getString(request.getParameter("channelname"), "");
	
	Map<String,Object> map = new AdChannelServer().loadChannel(pageIndex, appnameid, appkey, channel, channelname);	
	List<AdChannelModel> list = (List<AdChannelModel>)map.get("list");
	
	int rowcount = (Integer)map.get("rows");
	
	List<AdAppModel> applist = (List<AdAppModel>)name.get("list");
	
    Map<String,String> params = new HashMap<String,String>();
	
	params.put("appname", appnameid+"");
	params.put("appkey", appkey);
	params.put("channel", channel);
	params.put("channelname", channelname);
	
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
			  console.log(id);
			  window.location.href = "qdaction.jsp?id=" + id+"&type=2";	
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
				<dd class="ddbtn" ><a href="channeladd.jsp?userid=<%=userId%>">增  加</a></dd>
			</dl>
			<form action="channel.jsp"  method="get" id="formid">
				<dl>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd04_me">
						<select name="appname" id="appname" style="width: 150px;" title="选择SP">
							<option value="-1">请选择应用名</option>
							<%
							
							for(AdAppModel app : applist)
							{
								%>
							<option value="<%= app.getId() %>"><%= app.getAppname() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">应用KEY</dd>
					<dd class="dd03_me">
						<input name="appkey" id="input_appkey" value="<%=appkey %>" type="text" style="width: 150px">
					</dd>
					
					<dd class="dd01_me">渠道名</dd>
					<dd class="dd03_me">
						<input name="channelname" id="input_channelname" value="<%=channelname %>" type="text" style="width: 150px">
					</dd>
										
					<dd class="dd01_me">渠道</dd>
					<dd class="dd03_me">
						<input name="channel" id="input_channel" value="<%=channel %>" type="text" style="width: 150px">
					</dd>
					
					<!-- <dd class="dd01_me">同步类型</dd>
					<dd class="dd04_me">
						<select name="type_id" id="typeid" title="请选择同步类型" style="width: 150px">
							<option value="-1">请选择同步类型</option> 
							<option value="1">隔天</option>
							<option value="2">实时</option>
					    </select>
					</dd> -->
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
					<td>渠道名</td>
					<td>渠道</td>
					<td>扣量比</td>
					<td>分成比例</td>
					<td>所属账号</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (AdChannelModel model : list)
					
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppname() %></td>
					<td><%=model.getName() %></td>
					<td><%=model.getChannel() %></td>
					<td><%=model.getHold_percent()%></td>
					<%
						double scale = model.getScale()*100;
						BigDecimal b = new BigDecimal(scale);
						scale = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					%>
					<td><%= scale%>%</td>
					<td><%=model.getCreateName() %></td>
					<td>
						<a href="channeledit.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
						<a href="channelaccount.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>">用户分配</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="7" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>