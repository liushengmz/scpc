<%@page import="com.system.util.Base64UTF"%>
<%@page import="org.apache.commons.lang.text.StrSubstitutor"%>
<%@page import="com.system.model.CpBillingSptroneDetailModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpBillingDetailServer"%>
<%@page import="com.system.server.CpBillingServer"%>
<%@page import="com.system.model.CpBillingModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

	int cpBillingId = StringUtil.getInteger(request.getParameter("cpbillingid"), -1);

	int pageType = StringUtil.getInteger(request.getParameter("pagetype"), 0);
	
	CpBillingModel billingModel = new CpBillingServer().getCpBillingModel(cpBillingId);
	
	String[] returnUrls = {"cpbilling.jsp","cwcpbilling.jsp"};
	
	if(billingModel==null)
	{
		response.sendRedirect(returnUrls[pageType]);
		return;
	}
	List<CpBillingSptroneDetailModel> list = new CpBillingDetailServer().loadCpBillingSpTroneDetail(cpBillingId);
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
		window.location.href = "cpbillingdetailedit.jsp?query=<%= Base64UTF.encode(query) %>&cpbillingid=<%= cpBillingId %>&id=" + id;
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
			<%= billingModel.getCpName() + "["+ billingModel.getStartDate() +"-"+ billingModel.getEndDate() +"]["+ billingModel.getJsName() +"]帐单详细" %>
			&nbsp;&nbsp;&nbsp; <a href="<%= returnUrls[pageType] %>?type=-1&<%= Base64UTF.decode(query) %>">返  回</a>
		</div>
		<table cellpadding="0" cellspacing="0" style="padding-top: 5px;">
			<thead>
				<tr>
					<td>序号</td>
					<td>业务名称</td>
					<td>信息费</td>
					<td>结算率</td>
					<td>核减费用</td>
					<td>核减类型</td>
					<td>应支付</td>
					<td>备注</td>
					<td>状态</td>
					<%= isEditAble ? "<td>操作</td>" : "" %>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (CpBillingSptroneDetailModel model : list)
					{
				%>
				<tr>
					<td><%= rowNum++ %></td>
					<td><%= model.getSpTroneName()  %></td>
					<td><%= model.getAmount() %></td>
					<td><%= model.getRate() %></td>
					<td><%= model.getReduceAmount()  %></td>
					<td><%= reduceType[model.getReduceType()] %></td>
					<td><%=model.getReduceType()==0?(model.getAmount()-model.getReduceAmount())*model.getRate():model.getAmount()*model.getRate()-model.getReduceAmount() %></td>
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