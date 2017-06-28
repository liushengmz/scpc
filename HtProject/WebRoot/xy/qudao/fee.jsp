<%@page import="com.system.model.xy.XyFeeModel"%>
<%@page import="com.system.server.xy.FeeServer"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.xy.UserServer"%>
<%@page import="com.system.model.xy.XyUserModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 渠道CPS分成管理 -->	
	
<%
	String defaultStartDate = StringUtil.getPreDayOfMonth();

	String defaultEndDate = StringUtil.getDefaultDate();
	
	String keyWord = StringUtil.getString(request.getParameter("keyword"),"");
	
	int appType = StringUtil.getInteger(request.getParameter("app_type"), 0);

	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);
	
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);
	
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	Map<String, Object> map =  new FeeServer().loadChannelAppFee(startDate, endDate, keyWord, pageIndex, appType);
		
	List<XyFeeModel> list = (List<XyFeeModel>)map.get("list");
	
	int rowCount = (Integer)map.get("row");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	params.put("app_type", appType+"");
	
	String pageData = PageUtil.initPageQuery("fee.jsp",params,rowCount,pageIndex);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	$(function()
	{
		$("#sel_app_type").val("<%= appType %>");
	});
	//声明整数的正则表达式
	function isNum(a)
	{
		var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		return reg.test(a);
	}

	function editShowData(editId)
	{
		var curShowRows = $("#hid_" + editId).val();
		
		var newHtml = "<input type='text' id='myput_" + editId + "' style='width:30px;background-color:#CDC5BF;text-align:center;' value='"+ curShowRows +"' />";
		
		newHtml += "<input type='button' value='更新' style='margin-left: 10px' onclick='updateShowData(" + editId + ")'/>";
		 
		newHtml += "<input type='button' value='取消' style='margin-left: 10px' onclick='cancelShowData(" + editId + ")'/>";
		
		$("#span_" + editId).html(newHtml);
	}
	
	function updateShowData(editId)
	{
		var oldShowRows = $("#ori_hid_" + editId).val();
		var newShowRows = $("#myput_" + editId).val();
		
		if (isNum(newShowRows))
		{
			alert("输入数据不正确");
			return;
		}
		
		if(parseInt(newShowRows,10)>parseInt(oldShowRows,10))
		{
			alert("展示数据不能大于实际数据");
			return;
		}
		
		updateDbData(editId,newShowRows);
	}
	
	function updateDbData(editId,newShowRows)
	{
		var oldShowRows = $("#hid_" + editId).val();
		
		$.post("action.jsp", 
		{
			showamount : newShowRows,
			type : 2,
			id :editId 
		}, 
		function(data) 
		{
			data = $.trim(data);
			if ("OK" == data) 
			{
				$("#hid_" + editId).val(newShowRows);		
				$("#span_" + editId).html(newShowRows);
				$("#show_data_rows_label").html(parseInt($("#show_data_rows_label").html(),10) - parseInt(oldShowRows,10) + parseInt(newShowRows,10));
			} 
			else 
			{
				alert("修改失败！请联系管理员");
				$("#span_" + editId).html($("#hid_" + editId).val());
			}
		});
	}
	
	function cancelShowData(editId)
	{
		$("#span_" + editId).html($("#hid_" + editId).val());
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="fee.jsp"  method="get">
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
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input name="keyword" value="<%= keyWord %>" type="text">
					</dd>
					<dd class="dd01_me">应用类型</dd>
					<dd class="dd04_me">
						<select name="app_type" id="sel_app_type" style="width: 150px;" title="应用类型">
							<option value="-1">应用类型</option>
							<option value="1">自营应用</option>
							<option value="2">第三方应用</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit">
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>计费日期</td>
					<td class="or">游戏</td>
					<td class="or">渠道ID</td>
					<td>应用类型</td>
					<td>激活数</td>
					<td class="or">计费金额</td>
					<td class="or">同步金额</td>
					<td>同步状态</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (XyFeeModel model : list)
					{
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td>
						<%=model.getFeeDate()%>
						<input type="hidden" id="hid_<%= model.getId() %>" value="<%= model.getShowAmount() %>" />
					</td>
					<td class="or"><%=model.getAppName() + "("+ model.getAppKey() +")"%></td>
					<td class="or"><%=model.getChannelId()%></td>
					<td><%= model.getAppType()==1 ? "自营" : "第三方" %></td>
					<td>
						<%=model.getDataRows()%>
					</td>
					<td class="or" >
						<input type="hidden" id="ori_hid_<%= model.getId() %>" value="<%=model.getAmount()%>" />
						<%= StringUtil.getDecimalFormat(model.getAmount()) %>
					</td>
					<td class="or" <%= model.getStatus()==0 ? "ondblclick='editShowData(" + model.getId() + ")'":"" %> >
						<span id="span_<%= model.getId() %>"><%=StringUtil.getDecimalFormat(model.getShowAmount())%></span>
					</td>
					<td><%= model.getStatus()==1 ? "已同步":"未同步" %></td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>总激活数:<%= map.get("data_rows") %></td>
					<td>
						<span>总计费额:<label id="amount_label"><%= StringUtil.getDecimalFormat((Float)map.get("total_amount")) %></label></span>
					</td>
					<td>
						<span>总同步额:<label id="show_amount_label"><%= StringUtil.getDecimalFormat((Float)map.get("total_show_amount")) %></label></span>
					</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="9" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>