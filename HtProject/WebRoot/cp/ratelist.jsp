<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.model.SingleCpSpTroneRateModel"%>
<%@page import="com.system.server.SingleCpSpTroneRateServer"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String title = StringUtil.getString(request.getParameter("title"), "");
	
	if(!StringUtil.isNullOrEmpty(title))
	{
		title =	URLDecoder.decode(request.getParameter("title"),"UTF-8");
	}

	float defaultRate = StringUtil.getFloat(request.getParameter("rate"), 0.0F);

	String query = StringUtil.getString(request.getParameter("query"), "");
	
	String query2 = Base64UTF.encode(request.getQueryString());

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	
	Map<String, Object> map = new SingleCpSpTroneRateServer().loadSingleCpSpTroneRate(id, pageIndex);
	
	List<SingleCpSpTroneRateModel> list = (List<SingleCpSpTroneRateModel>)map.get("list");
	
	int rowCount = (Integer) map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("title", title);
	params.put("query", query);
	params.put("id","" + id);
	
	String pageData = PageUtil.initPageQuery("ratelist.jsp", params, rowCount, pageIndex);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">

<script type="text/javascript">

	function delCpSpTroneRate(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "rateaction.jsp?type=5&cpsptroneid=<%= id %>&id=" + id + "&query=<%= query2 %>";	
		}
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<dd class="ddbtn" style="width:800px" >
					<label><%= title %> 结算率列表</label>
					&nbsp;&nbsp;&nbsp;
				<a href="ratelistadd.jsp?id=<%= id %>&query2=<%= query2 %>&title=<%= URLEncoder.encode(title,"UTF-8") %>">增  加</a>&nbsp;&nbsp;&nbsp;
				<a href="rate.jsp?<%= Base64UTF.decode(query) %>">返  回</a>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>起始日期</td>
					<td>结算日期</td>
					<td>结算率</td>
					<td>备注</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SingleCpSpTroneRateModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex - 1) * Constant.PAGE_SIZE + rowNum++%></td>
					<td><%= model.getStartDate() %></td>
					<td><%= model.getEndDate() %></td>
					<td><%= model.getRate() %></td>
					<td><%= model.getRemark() %></td>
					<td>
						<a href="ratelistedit.jsp?id=<%= model.getId() %>&query2=<%= query2 %>&title=<%= URLEncoder.encode(title,"UTF-8") %>&query=<%= query %>">修改</a>
						<a href="#" onclick="delCpSpTroneRate(<%=model.getId()%>)">删除</a></td>
				</tr>
				<%
					}
				%>
			<tbody>
				<tr>
					<td colspan="9" class="tfooter" style="text-align: center;"><%=pageData%></td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>