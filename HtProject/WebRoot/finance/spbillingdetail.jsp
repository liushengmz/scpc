<%@page import="com.system.server.SpBillingDetailServer"%>
<%@page import="com.system.model.SpBillingSptroneDetailModel"%>
<%@page import="com.system.server.SpBillingServer"%>
<%@page import="com.system.model.SpBillingModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="org.apache.commons.lang.text.StrSubstitutor"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.DecimalFormat" %>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	DecimalFormat df = new DecimalFormat("0.00");
	int spBillingId = StringUtil.getInteger(request.getParameter("spbillingid"), -1);

	int pageType = StringUtil.getInteger(request.getParameter("pagetype"), 0);
	
	SpBillingModel billingModel = new SpBillingServer().getSpBillingModel(spBillingId);
	
	String[] returnUrls = {"spbilling.jsp","cwspbilling.jsp"};
	
	if(billingModel==null)
	{
		response.sendRedirect(returnUrls[pageType]);
		return;
	}
	List<SpBillingSptroneDetailModel> list = new SpBillingDetailServer().loadSpBillingSpTroneDetail(spBillingId);
	String[] statusStr = {"正常","不结算"};
	String[] reduceType = {"信息费","结算款"};
	String query = StringUtil.getString(request.getParameter("query"), "");
	
	boolean isEditAble = billingModel.getStatus()== 0;
	
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
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

	function updateAmount(id)
	{
		window.location.href = "spbillingdetailedit.jsp?query=<%= Base64UTF.encode(query) %>&spbillingid=<%= spBillingId %>&id=" + id;
	}
	
</script>

<style type="text/css">
a:link {
font-size: 14px;
color: #00c0ff;
text-decoration: none;
}
a:visited {
font-size: 14px;
color: #00c0ff;
text-decoration: none;
}
a:hover {
font-size: 14px;
color: #00c0ff;
text-decoration: underline;
}
</style> 

<body>
	<div class="main_content">
		<div class="content" style="margin-left: 20px;font-weight: bold;font-size:small; ;float: left;padding-top: 5px">
			<%= billingModel.getSpName() + "["+ billingModel.getStartDate() +"-"+ billingModel.getEndDate() +"]["+ billingModel.getJsName() +"]帐单详细" %>
			&nbsp;&nbsp;&nbsp; <a href="<%= returnUrls[pageType] %>?type=-1&<%= Base64UTF.decode(query) %>">返  回</a>
		</div>
		<table cellpadding="0" cellspacing="0" style="padding-top: 5px;">
			<thead>
				<tr>
					<td>序号</td>
					<td>业务名称</td>
					<td>信息费</td>
					<td>结算率</td>
					<td>核减信息费</td>
					<td>核减款</td>
					<td>应收</td>
					<td>实际应收</td>
					<td>备注</td>
					<td>状态</td>
					<%= isEditAble ? "<td>操作</td>" : "" %>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SpBillingSptroneDetailModel model : list)
					{
				%>
				<tr>
					<td><%= rowNum++ %></td>
					<td><%= model.getSpTroneName()  %></td>
					<td><%= StringUtil.getDecimalFormat(model.getAmount()) %></td>
					<td><%= model.getRate() %></td>
					<td><%= StringUtil.getDecimalFormat(model.getReduceDataAmount())  %></td>
					<td><%= StringUtil.getDecimalFormat(model.getReduceMoneyAmount()) %></td>
					<td><%= StringUtil.getDecimalFormat(model.getAmount()*model.getRate()) %></td>
					<td><%= StringUtil.getDecimalFormat((model.getAmount() - model.getReduceDataAmount())*model.getRate() - model.getReduceMoneyAmount()) %></td>
					<td><%= model.getRemark() %></td>
					<td><%= statusStr[model.getStatus()] %></td>
					<%=
							isEditAble ? "<td><a href=\"#\" onclick=\"updateAmount(" + model.getId() + ")\">修改</a></td>" : ""
					%>
				</tr>
				<%
					}
				%>
				
			<tbody>
		</table>
	</div>
	
</body>
</html>