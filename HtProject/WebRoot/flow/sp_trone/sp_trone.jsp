<%@page import="com.system.flow.model.SpTroneModel"%>
<%@page import="com.system.flow.server.SpTroneServer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());
	
	Map<String, Object> map = new SpTroneServer().loadSpTrone(pageIndex, keyWord);

	List<SpTroneModel> list = (List<SpTroneModel>)map.get("list");

	int rowCount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("sp_trone.jsp", params, rowCount, pageIndex);
	
	String[] status = {"停用","启用"};
	
	String[] rangs = {"全国","省内"};
	
	String[] sendSms = {"否","是"};
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../../sysjs/AndyNamePickerV20.js">
</script><link href="../../css/namepicker.css" rel="stylesheet" type="text/css">

<script type="text/javascript">

	
	
</script>
<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<dd class="ddbtn">
					<a style="margin-left: -10px;" href="sp_trone_add.jsp">增 加</a>
				</dd>
				<form action="sp_trone.jsp" method="get" id="formid">
					<dl>					
						<dd class="dd01_me">关键字</dd>
						<dd class="dd03_me">
							<input name="keyword" id="input_keyword" value="<%= keyWord %>"
								type="text" style="width: 100px">
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
					<td>供应商名称</td>
					<td>业务名称</td>
					<td>流量类型</td>
					<td>运营商</td>
					<td>流量包</td>
					<td>价格</td>
					<td>折扣</td>
					<td>发送短信</td>
					<td>开通省份</td>
					<td>备注</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (SpTroneModel model : list)
					{
				%>
				<tr <%= model.getStatus()==0 ? stopStyle : "" %>>
					<td><%=(pageIndex - 1) * Constant.PAGE_SIZE + rowNum++%></td>
					<td><%= model.getSpName()%></td>
					<td><%= model.getSpTroneName() %></td>
					<td><%= rangs[model.getRang()] %></td>
					<td><%= model.getOperatorName() %></td>
					<td><%= model.getFlowName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getRatio() %></td>
					<td><%= sendSms[model.getSendSms()] %></td>
					<td><%= model.getProNames() %></td>
					<td><%= model.getRemark() %></td>
					<td><%= status[model.getStatus()] %></td>
					<td><a href="sp_trone_edit.jsp?query=<%= query %>&id=<%= model.getId() %>">修改</a></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td colspan="14" class="tfooter" style="text-align: center;"><%=pageData%></td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>