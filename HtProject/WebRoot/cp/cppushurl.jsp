<%@page import="com.system.model.CpPushUrlModel"%>
<%@page import="com.system.server.CpPushUrlServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);

	Map<String, Object> map = new CpPushUrlServer().loadCpPushUrl(cpId, pageIndex);
		
	List<CpPushUrlModel> cpPushUrlList = (List<CpPushUrlModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	List<CpModel> cpList = new CpServer().loadCp();
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("cp_id", cpId + "");
	
	String pageData = PageUtil.initPageQuery("cppushurl.jsp",params,rowCount,pageIndex);
	
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
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	
	function delTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "troneaction.jsp?did=" + id;	
		}
	}
	
	var cpList = new Array();
	<%
	for(CpModel cpModel : cpList)
	{
		%>
		cpList.push(new joSelOption(<%= cpModel.getId() %>,1,'<%= cpModel.getShortName() %>'));
		<%
	}
	%>
	
	function onCpDataSelect(jo)
	{
		$("#sel_cp").val(jo.id);
	}
	
	$(function(){
		$("#sel_cp").val(<%= cpId %>);
	});
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="cppushurladd.jsp">增  加</a></dd>
				<form action="cppushurl.jsp"  method="post" id="formid">
				<dl>
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp" title="选择CP" onclick="namePicker(this,cpList,onCpDataSelect)">
							<option value="-1">全部</option>
							<%
							for(CpModel cp : cpList)
							{
								%>
							<option value="<%= cp.getId() %>"><%= cp.getShortName() %></option>	
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
					<td>CP</td>
					<td>名字</td>
					<td>URL</td>
					<td>起扣条数</td>
					<td>扣量百分比</td>
					<td>当日最大同步金额</td>
					<td>最后同步日期</td>
					<td>已同步金额</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CpPushUrlModel model : cpPushUrlList)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getName() %></td>
					<td><%= model.getUrl()  %></td>
					<td><%= model.getHoldStartCount() %></td>
					<td><%= model.getHoldPercent() %></td>
					<td><%= model.getHoldAmount()  %></td>
					<td><%= model.getLastDate() %></td>
					<td><%= model.getCurAmount() %></td>
					<td>
						<a href="cppushurledit.jsp?id=<%= model.getId() %>&query=<%= query %>">修改</a>
					</td>
				</tr>
				<%
					}
				%>
			</tbody>	
			<tbody>
				<tr>
					<td colspan="10" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>