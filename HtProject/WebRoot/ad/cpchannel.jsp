<%@page import="com.system.model.AdChannelModel"%>
<%@page import="com.system.server.AdChannelServer"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.system.server.CpChannelServer"%>
<%@page import="com.system.model.CpChannelModel"%>
<%@page import="com.system.dao.CpChannelDao"%>
<%@page import="com.system.server.CpAppServer"%>
<%@page import="com.system.model.CpAppModel"%>
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
	Map<String,Object> name = new AdAppServer().loadApp();
	String defaultStartDate = StringUtil.getPreDayOfMonth();/* 获取之前一个随机的时间 */
	String defaultEndDate = StringUtil.getMonthEndDate();    /* 获得当前系统时间 */
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
    int appid = StringUtil.getInteger(request.getParameter("appname"), -1);
    int channelid = StringUtil.getInteger(request.getParameter("channel"), -1);
    
    String appkey = StringUtil.getString(request.getParameter("appkey"), "");
    String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);  /* 获取用户输入开始时间 */
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);  /* 获取用户输入的结束时间 */
	String appname = "";
			
			
	if(appid>0)
	{
		AdAppModel appmodel = new AdAppServer().loadAppById(appid);
		appname = appmodel.getAppname();
	}
    
	Map<String,Object> map = new CpChannelServer().loadCpChannel(pageIndex, startDate, endDate, appid, channelid);
    //Map<String,Object> map = new CpChannelServer().loadCpChannel(pageIndex);
    
	//Map<String,Object> map = new CpAppServer().loadCpApp(pageIndex, appname, startDate, endDate);
	//Map<String,Object> map = new AdAppServer().loadApp(pageIndex, appname, appkey);
	Map<String,Object> channel = new AdChannelServer().loadAdChannelName();
	List<AdChannelModel> qdlist = (List<AdChannelModel>)channel.get("list");
	
	List<CpChannelModel> list = (List<CpChannelModel>)map.get("list");
	
	List<AdAppModel> applist = (List<AdAppModel>)name.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	appname = appid+"";
	
	params.put("appname", appname);
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	params.put("channel", channelid+"");
	
	String pageData = PageUtil.initPageQuery("cpchannel.jsp",params,rowCount,pageIndex); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>运营管理平台</title>
    <link href="../wel_data/right.css" rel="stylesheet" type="text/css">
    <link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
    <script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
    	function joCpChannel(id,appId,name)
		{
			var obj = {};
			obj.id = id ;
			obj.appId = appId;
			obj.name = name;
			return obj;
		}
		var cpChannelArray = new Array();
		<%
			for(AdChannelModel cpChannel : qdlist)
			{
				%>
				cpChannelArray.push(new joCpChannel(<%= cpChannel.getId() %>,<%= cpChannel.getAppid() %>,'<%= cpChannel.getName() %>'));	
				<%
			}
		%>
		
		$(function()
				{
					$("#appname").val(<%=appname%>)
					$("#appname").change(channelChange);
					channelChange();
					$("#channel").val(<%=channelid%>)
				});
		
		function channelChange()
		{
			console.log(cpChannelArray);
			var appId = $("#appname").val();
			console.log(appId);
			$("#channel").empty(); 
			$("#channel").append("<option value='-1'>请选择</option>");
			for(i=0; i<cpChannelArray.length; i++)
			{
				if(cpChannelArray[i].appId==appId || appId == "-1")
				{
					$("#channel").append("<option value='" + cpChannelArray[i].id + "'>" + cpChannelArray[i].name + "</option>");
				}
			}
		}
	  function delTrone(id)
	  {
		  if(confirm('真的要删除吗？'))
		  {
			  window.location.href = "cpqdaction.jsp?id=" + id+"&emp=1"+"&startdate=<%=startDate%>"+
			  "&enddate=<%=endDate%>"+"&appnamestr=<%=appid%>"+"&channelidstr=<%=channelid%>"+"&pageindex=<%=pageIndex%>";	
		  }
	  }
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="cpqdadd.jsp">增  加</a></dd>
				<form action="cpchannel.jsp"  method="post" id="formid">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd04_me">
						<select name="appname" id="appname" style="width: 150px;" title="选择SP" >
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
					
					<dd class="dd01_me">渠道名</dd>
					<dd class="dd04_me">
						<select name="channel" id="channel" style="width: 200px;" title="选择SP"></select>
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
					<td>渠道名</td>
					<td>计费日期</td>
					<td>新增用户</td>
					<td>活跃用户</td>
					<td>每日收入</td>
					<td>展示新增用户</td>
					<td>展示活跃用户</td>
					<td>展示收入</td>
					<td>分成比例</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CpChannelModel model : list)
					
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppname()%></td>
					<td><%=model.getChannel() %></td>
					<td><%=model.getFeeDate()%></td>
					<td><%=model.getNewUserRows()%></td>
					<td><%=model.getActiveRows()%></td>
					<td><%=model.getAmount()%></td>
					<td><%=model.getShowNewUserRows()%></td>
					<td><%=model.getShowActiveRows()%></td>
					<td><%=model.getShowAmount()%></td>
					<%
						double num = model.getScale()*100;
						BigDecimal b = new BigDecimal(num);
						num = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
						
					%>
					<td><%=num%>%</td>
					<td><%=model.getStatus()<1?"未同步":"已同步"%></td>
					
					<td>
						<a href="cpqdedit.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>&startdate=<%=startDate%>&enddate=<%=endDate%>&appnamestr=<%=appid%>&channelidstr=<%=channelid%>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>
						<span>总用户数:<%=map.get("newUserRows") %></span>
					</td>
					<td>
						<span>总活跃用户数:<%=map.get("activeRows") %></span>
					</td>
					<td>
						<span>总收入:<%=StringUtil.getDecimalFormat((Double)map.get("amount"))%></span>
					</td>
					<td>
						<span>新增用户总数:<%=map.get("sumUserRows") %></span>
					</td>
					<td>
						<span>展示活跃用户总数:<%=map.get("sumActive") %></span>
					</td>
					<td>
						<span>展示收入总数:<%=StringUtil.getDecimalFormat((Double)map.get("sumAmount"))%></span>
					</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="14" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>