<%@page import="com.system.model.SpBillingSptroneDetailModel"%>
<%@page import="com.system.server.SpBillingDetailServer"%>
<%@page import="com.system.server.SpBillingServer"%>
<%@page import="com.system.model.SpBillingModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.CpBillingSptroneDetailModel"%>
<%@page import="com.system.server.CpBillingDetailServer"%>
<%@page import="com.system.server.CpBillingServer"%>
<%@page import="com.system.model.CpBillingModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int spBillingId = StringUtil.getInteger(request.getParameter("spbillingid"), -1);
	SpBillingModel spBillingModel = new SpBillingServer().getSpBillingModel(spBillingId);
	int typeId = StringUtil.getInteger(request.getParameter("type"), -1);
	int reduceType = StringUtil.getInteger(request.getParameter("reduce_type"),0);
	String query = StringUtil.getString(request.getParameter("query"), "");
	SpBillingDetailServer detailServer = new SpBillingDetailServer();
	SpBillingSptroneDetailModel model = detailServer.getSingleSpBillingSpTroneDetailModel(id);
	
	float reduceDataAmount = StringUtil.getFloat(request.getParameter("reduce_data_amount"), 0);
	float reduceMoneyAmount = StringUtil.getFloat(request.getParameter("reduce_money_amount"), 0);
	
	int status = StringUtil.getInteger(request.getParameter("status"), 0);
	String remark = StringUtil.getString(request.getParameter("remark"), "");
	
	if(typeId==1)
	{
		model.setReduceAmount(0);
		model.setStatus(status);
		model.setRemark(remark);
		model.setReduceType(reduceType);
		
		//这里需要重新处理
		model.setReduceDataAmount(model.getAmount() - reduceDataAmount);
		model.setReduceMoneyAmount(reduceDataAmount * model.getRate() - reduceMoneyAmount);
		
		detailServer.updateSingleSpBillingSpTroneDetail(model);
		response.sendRedirect("spbillingdetail.jsp?query=" + Base64UTF.decode(query) + "&spbillingid=" + spBillingId);
		return;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	
	function backDetail()
	{
		window.location.href = "spbillingdetail.jsp?query=<%= Base64UTF.decode(query) %>&spbillingid=<%= spBillingId %>";
	}
	
	$(function(){
		setRadioCheck("status",<%= model.getStatus() %>);
		//setRadioCheck("reduce_type",<%= model.getReduceType() %>);
	});
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:400px">
				<label ><%= spBillingModel.getSpName() + "["+ spBillingModel.getStartDate() +"-"+ spBillingModel.getEndDate() +"]["+ spBillingModel.getJsName() +"]帐单详细" %></label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="spbillingdetailedit.jsp?query=<%= query %>&spbillingid=<%= spBillingId %>" method="post" id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
					<input type="hidden" value="1" name="type" />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">业务名称</dd>
					<dd class="dd03_me">
						<input type="text"  readonly="readonly" value="<%= model.getSpTroneName() %>"
							style="width: 200px;color: #ccc">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">信息费</dd>
					<dd class="dd03_me">
						<input type="text" readonly="readonly" value="<%= model.getAmount() %>"
							style="width: 200px;color: #ccc">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算率</dd>
					<dd class="dd03_me">
						<input type="text" readonly="readonly" value="<%= model.getRate() %>"
							style="width: 200px;color: #ccc">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">上游信息费</dd>
					<dd class="dd03_me">
						<input type="text" name="reduce_data_amount" id="input_reduce_amount" value="<%= StringUtil.getDecimalFormat(model.getAmount() - model.getReduceDataAmount()) %>" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">上游结算款</dd>
					<dd class="dd03_me">
						<input type="text" name="reduce_money_amount" id="input_reduce_amount" value="<%= StringUtil.getDecimalFormat((model.getAmount() - model.getReduceDataAmount())*model.getRate() - model.getReduceMoneyAmount()) %>" style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd03_me">
						<input type="radio" name="status" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">正常</label>
						<input type="radio" name="status" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">不结算</label>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" id="input_remark" style="width: 200px" value="<%= model.getRemark() %>" />
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="submit" value="提 交" >
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="backDetail()">
					</dd>
				</form>
			</dl>
		</div>

	</div>
</body>
</html>