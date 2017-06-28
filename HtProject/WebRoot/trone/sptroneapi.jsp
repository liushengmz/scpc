<%@page import="com.system.model.SpTroneApiModel"%>
<%@page import="com.system.server.SpTroneApiServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String query = Base64UTF.encode(request.getQueryString());

	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	
	Map<String, Object> map = new SpTroneApiServer().loadSpTroneApi(pageIndex, keyWord);
	
	List<SpTroneApiModel> list = (List<SpTroneApiModel>) map.get("list");

	int rowCount = (Integer) map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	String pageData = PageUtil.initPageQuery("sptroneapi.jsp", null, rowCount, pageIndex);
	
	String[] matchFiles = {"LinkId","Msg","SP透参","特殊指令"};
	String[] locateMatchs = {"不需匹配","手机号匹配","IP地区匹配","手机号和IP地区匹配"};
	String[] apiFields = {"IMEI","IMSI","手机号","访问IP","包名","Android版本","网络类型","客户端IP","LAC","CID","ICCID","UserAgent"};
	
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

<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<dd class="ddbtn">
					<a href="sptroneapiadd.jsp">增 加</a>
				</dd>
				<form action="sptroneapi.jsp" method="get" id="formid">
					<dl>
						<dd class="dd01_me">名字</dd>
						<dd class="dd03_me">
							<input name="keyword" id="input_keyword" value="<%= keyWord %>"
								type="text" style="width: 150px">
						</dd>
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
					<td>ID</td>
					<td>名称</td>
					<td>匹配字段</td>
					<td>匹配关键字</td>
					<td>API必须参数</td>
					<td>地区匹配</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SpTroneApiModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex - 1) * Constant.PAGE_SIZE + rowNum++%></td>
					<td><%= model.getId() %></td>
					<td><%=model.getName() %></td>
					<td><%= matchFiles[model.getMatchField()] %></td>
					<td><%=model.getMatchKeyword()%></td>
					<td><%= StringUtil.isNullOrEmpty(model.getApiFields()) ? "" : StringUtil.concatStrings(apiFields, model.getApiFields().split(","), "|") %></td>
					<td><%= locateMatchs[model.getLocateMatch()] %></td>
					<td><a href="sptroneapiedit.jsp?query=<%= query %>&id=<%= model.getId() %>">修改</a></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td colspan="13" class="tfooter" style="text-align: center;"><%=pageData%></td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>