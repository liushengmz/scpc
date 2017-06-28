<%@page import="com.system.constant.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.system.model.SpTroneRateModel"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpTroneRateServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	String query = Base64UTF.encode(request.getQueryString());

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	
	Map<String, Object> map = new SpTroneRateServer().loadSpTroneRate(keyWord, pageIndex);
	
	List<SpTroneRateModel> list = (List<SpTroneRateModel>)map.get("list");
	
	int rowCount = (Integer) map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("sptronerate.jsp", params, rowCount, pageIndex);
	
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
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">

<script type="text/javascript">

	function delSpTroneRate(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "sptronerateaction.jsp?type=2&id=" + id;	
		}
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<dd class="ddbtn">
					<a href="sptronerateadd.jsp">增 加</a>
				</dd>
				<form action="sptronerate.jsp" method="get" id="formid">
					<dl>
						<dd class="dd01_me">关键字</dd>
						<dd class="dd03_me"><input type="text" name="keyword" value="<%= keyWord %>"  /></dd>
						<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
							<input class="btn_match" name="search" value="查 询" type="submit">
						</dd>
					</dl>
				</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SP名称</td>
					<td>业务名称</td>
					<td>起始日期</td>
					<td>结算日期</td>
					<td>结算率</td>
					<td>默认结算率</td>
					<td>备注</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SpTroneRateModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex - 1) * Constant.PAGE_SIZE + rowNum++%></td>
					<td><%= model.getSpName()%></td>
					<td><%= model.getSpTroneName()%></td>
					<td><%= model.getStartDate() %></td>
					<td><%= model.getEndDate() %></td>
					<td><%= model.getRate() %></td>
					<td><%= model.getDefaultRate() %></td>
					<td><%= model.getRemark() %></td>
					<td>
						<a href="sptronerateedit.jsp?id=<%= model.getId() %>&query=<%= query %>">修改</a>
					<a href="#" onclick="delSpTroneRate(<%=model.getId()%>)">删除</a></td>
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