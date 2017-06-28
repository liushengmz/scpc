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
	Map<String,Object> name = new AdAppServer().loadApp(1);
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getMonthEndDate();    
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
    int appid = StringUtil.getInteger(request.getParameter("appname"), -1);
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
    
	
    //Map<String,Object> map = new CpAppServer().loadCpApp(pageIndex);
	Map<String,Object> map = new CpAppServer().loadCpApp(pageIndex, appname, startDate, endDate);
	//Map<String,Object> map = new AdAppServer().loadApp(pageIndex, appname, appkey);
	
	List<CpAppModel> list = (List<CpAppModel>)map.get("list");
	
	List<AdAppModel> applist = (List<AdAppModel>)name.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("appname", appid+"");
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	
	String pageData = PageUtil.initPageQuery("cpapp.jsp",params,rowCount,pageIndex); 
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
	
    function delTrone(id)
	  {
		  if(confirm('真的要删除吗？'))
		  {
			  console.log(id);
			  window.location.href = "cpappaction.jsp?id=" + id+"&emp=2"+"&startdate=<%=startDate%>"+ "&enddate=<%=endDate%>"+"&appnamestr=<%=appid%>"+"&pageindex=<%=pageIndex%>";
		  }
	  }
	
    $(function()
    		{
    			$("#appname").val(<%= StringUtil.getString(params.get("appname"), "-1") %>);
    		});
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="cpappadd.jsp">增  加</a></dd>
				<form action="cpapp.jsp"  method="get" id="formid">
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
					<td>计费日期</td>
					<td>新增用户</td>
					<td>每日收入</td>
					<td>展示新增用户</td>
					<td>展示每日收入</td>
					<td>推广费用</td>
					<td>利润</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CpAppModel model : list)
					
					{
						
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getAppname()%></td>
					<td><%=model.getFeeDate()%></td>
					<td><%=model.getNewUserRows()%></td>
					<td><%=model.getAmount()%></td>
					<td><%=model.getShowNewUserRows()%></td>
					<td><%=model.getShowAmount()%></td>
					<td><%=model.getExtendFee()%></td>
					<td><%=model.getProfit()%></td>
					<td><%=model.getStatus()>0?"已同步":"未同步"%></td>
					
					<td>
						<a href="cpappedit.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>&startdate=<%=startDate%>&appnamestr=<%=appid%>&enddate=<%=endDate%>">修改</a>
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
					<td>
						<span>总用户数:<%=map.get("newUserRows") %></span>
					</td>
					<td>
						<span>总收入:<%=StringUtil.getDecimalFormat((Double)map.get("amountSum"))%></span>
					</td>
					<td>
						<span>新增用户总数:<%=map.get("showUserRows") %></span>
					</td>
					<td>
						<span>每日总收入:<%=StringUtil.getDecimalFormat((Double)map.get("showAmount"))%></span>
					</td>
					<td>
						<span>推广总费用:<%=StringUtil.getDecimalFormat((Double)map.get("extendFee"))%></span>
					</td>
					<td>
						<span>总利润:<%=StringUtil.getDecimalFormat((Double)map.get("profit"))%></span>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="12" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>