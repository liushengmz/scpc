<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.SpBillingModel"%>
<%@page import="com.system.server.SpBillingServer"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.server.SettleAccountServer"%>
<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.model.CpBillingModel"%>
<%@page import="com.system.server.CpBillingServer"%>
<%@page import="com.system.model.SpBillExportModel" %>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.model.SettleAccountModel"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	String startDate=StringUtil.getString(request.getParameter("start_date"), StringUtil.getLastMonthFirstDay());
	String endDate=StringUtil.getString(request.getParameter("end_date"), StringUtil.getDefaultDate());
	String jsTypes=StringUtil.getString(request.getParameter("js_types"), "");
	String status=StringUtil.getString(request.getParameter("status"), "");
	int load=StringUtil.getInteger(request.getParameter("load"), -1);
	List<SpModel> spList = new SpServer().loadSp();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script type="text/javascript">

	var spList = new Array();
	
	<%
	for(SpModel spModel : spList)
	{
		%>
		spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
		<%
	}
	%>
	
	function dataexport() {
		var spId=$("#sp_id").val();
		var startDate=$("#start_date").val();
		var endDate=$("#end_date").val();
		var jstype=[];
		var status=[];
		$('input[name="js_type"]:checked').each(function(){ 
			jstype.push($(this).val()); 
			}); 
		$('input[name="status"]:checked').each(function(){ 
			status.push($(this).val()); 
			}); 
		<!-- 
		ajaxExport(spId,startDate,endDate,jstype,status,1);
		-->
		window.location.href="util.jsp?sp_id=" + spId + "&start_date="+startDate+"&end_date="+endDate+"&js_types="+jstype+"&status_exp="+status+"&load=1";
		
	}
	
	function ajaxExport(spId,startDate,endDate,jstypes,status,load) {
		var result = "";
		$.ajax({
			url : "cwspbillingexport.jsp",
			data : "sp_id=" + spId + "&start_date="+startDate+"&end_date="+endDate+"&js_types="+jstypes+"&status="+status+"&load="+load,
			cache : false,
			async : false,
			success : function(html) {
				result = $.trim(html);
			}
		});
		return result;
	}
		
	function onSpDataSelect(joData)
	{
		$("#sp_id").val(joData.id);
	}
</script>

<style type="text/css">
.ui-button-icon-only .ui-icon{left:0}
.ui-button-icon-only .ui-icon, 
.ui-button-text-icon-primary .ui-icon, 
.ui-button-text-icon-secondary .ui-icon, 
.ui-button-text-icons .ui-icon, 
.ui-button-icons-only .ui-icon
{top:0}
</style>
<style>
.radioclass {
opacity: 0;
cursor: pointer;
-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
filter: alpha(opacity=0);
}
</style>
<body style="min-height: 2000px">
	<div class="main_content">
		<div class="content" >
				<dl>
					<dd class="dd01_me" style="margin-top: 20px">开始日期</dd>
					<dd class="dd03_me" style="margin-top: 20px">
						<input name="startdate" type="text" value="<%=startDate%>" id="start_date"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me" style="margin-top: 20px">结束日期</dd>
					<dd class="dd03_me" style="margin-top: 20px">
						<input name="enddate" type="text" value="<%=endDate%>" id="end_date"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me" style="margin-top: 20px">SP</dd>
					<dd class="dd04_me" style="margin-top: 20px">
						<select name="sp_id" id="sp_id" onclick="namePicker(this,spList,onSpDataSelect)">
						<option value="-1">全部</option>
							<%
							for(SpModel sp : spList)
							{
								%>
							<option value="<%= sp.getId() %>"><%= sp.getShortName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<div style="clear: both;"><br /></div>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd04_me" style="margin-left: 10px">
					<label><input style="" type="checkbox" title="对公周结" class="chpro" name="js_type"value="0" checked="checked">&nbsp;&nbsp;对公周结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对公双周结" class="chpro" name="js_type"value="1" checked="checked">&nbsp;&nbsp;对公双周结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对公N+1结" class="chpro" name="js_type"value="2" checked="checked">&nbsp;&nbsp;对公N+1结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对公N+2结" class="chpro" name="js_type"value="7" checked="checked">&nbsp;&nbsp;对公N+2结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对公N+3结" class="chpro" name="js_type"value="8" checked="checked">&nbsp;&nbsp;对公N+3结</label>&nbsp;&nbsp;
					<label><input style="" type="checkbox" title="对私周结" class="chpro" name="js_type"value="3" checked="checked">&nbsp;&nbsp;对私周结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对私双周结" class="chpro" name="js_type"value="4" checked="checked">&nbsp;&nbsp;对私双周结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对私月结" class="chpro" name="js_type"value="5" checked="checked">&nbsp;&nbsp;对私月结</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="见帐单结" class="chpro" name="js_type"value="6" checked="checked">&nbsp;&nbsp;见帐单结</label>&nbsp&nbsp
					</dd>
					<div style="clear: both;"><br /></div>
					<dd class="dd01_me" >状态</dd>
					<dd class="dd04_me" style="margin-left: 10px">
					<label><input style="" type="checkbox" title="运营发起" class="chpro" name="status"value="0" checked="checked">&nbsp;&nbsp;运营发起</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="运营审核" class="chpro" name="status"value="1" checked="checked">&nbsp;&nbsp;运营审核</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="上游已开票" class="chpro" name="status"value="3" checked="checked">&nbsp;&nbsp;上游已开票</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="结算申请开票" class="chpro" name="status"value="4" checked="checked">&nbsp;&nbsp;结算申请开票</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="财务已开票" class="chpro" name="status"value="5" checked="checked">&nbsp;&nbsp;财务已开票</label>&nbsp&nbsp
					<label><input style="" type="checkbox" title="对帐完成" class="chpro" name="status"value="2" checked="checked">&nbsp;&nbsp;对帐完成</label>&nbsp&nbsp
					</dd>
					<div style="clear: both;"><br /></div>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="export" type="button" id="export" value="导     出" onclick="dataexport()" />
					</dd>
				</dl>
		</div>
	</div>
</body>
</html>
