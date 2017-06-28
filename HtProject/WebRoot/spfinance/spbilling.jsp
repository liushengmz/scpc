<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.SpBillingModel"%>
<%@page import="com.system.server.SpBillingServer"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.model.SettleAccountModel"%>
<%@page import="com.system.server.SettleAccountServer"%>
<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.model.CpBillingModel"%>
<%@page import="com.system.server.CpBillingServer"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.DecimalFormat" %>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	DecimalFormat df = new DecimalFormat("0.00");
	UserModel user=(UserModel)session.getAttribute("user");
	int userId=user.getId();
	int rightType=1;
	int spBillingId = StringUtil.getInteger(request.getParameter("spbillingid"), -1);

	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	SpBillingServer server = new SpBillingServer();
	
	int status = StringUtil.getInteger(request.getParameter("status"), -1);
	
	boolean isRecall = false;
	
	boolean isRecallSuc = false;
	
	//导出EXCEL数据
	if(spBillingId>0 && type ==1)
	{
		List<SettleAccountModel> list = server.exportExcelData(spBillingId);
		
		SpBillingModel spBillingModel = server.getSpBillingModel(spBillingId);
		
		response.setContentType("application/octet-stream;charset=utf-8");
		
		String fileName = spBillingModel.getSpName() + "(" + spBillingModel.getStartDate() + "_" + spBillingModel.getEndDate() + ").xls";
		
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) 
		{
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} 
		else 
		{
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}

		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

		SettleAccountServer accountServer = new SettleAccountServer();
		
		accountServer.exportSettleAccount(1, spBillingModel.getJsType(), spBillingModel.getSpName(), spBillingModel.getStartDate() , spBillingModel.getEndDate(), list,
				response.getOutputStream());
		
		out.clear();
		
		out = pageContext.pushBody();
		
		return;
	}
	//重新生成指定帐单的数据
	else if(type==2 && spBillingId > 0)
	{
		server.reExportSpBillint(spBillingId);
	}
	//删除帐单数据
	else if(type==3 && spBillingId > 0)
	{
		server.delSpBilling(spBillingId);
	}
	//确定对帐单无误，更新对帐单状态
	else  if(type==4 && spBillingId > 0)
	{
		server.updateSpBillingStatus(spBillingId, 1);
	}
	//撤回待运营已审核的帐单
	else if(type==6 && spBillingId > 0)
	{
		isRecall = true;
		isRecallSuc =  server.recallSpBilling(spBillingId);
	}

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());

	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	
	int jsType = StringUtil.getInteger(request.getParameter("js_type"), -1);
	
	String startDate = StringUtil.getString(request.getParameter("startdate"), "");
	
	String endDate = StringUtil.getString(request.getParameter("enddate"), "");

	Map<String, Object> map =  server.loadSpBilling(startDate, endDate, spId, jsType,userId,rightType,status,pageIndex);
		
	List<SpBillingModel> list = (List<SpBillingModel>)map.get("list");
	
	List<SpModel> spList = new SpServer().loadSp();
	
	List<JsTypeModel> jsTypeList = new JsTypeServer().loadJsType();
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String, String> params = new HashMap<String,String>();
	
	params = new HashMap<String,String>();
	params.put("js_type", jsType + "");
	params.put("sp_id", spId + "");
	params.put("startdate", startDate);
	params.put("enddate",endDate);
	params.put("status",""+status);
	
	String pageData = PageUtil.initPageQuery("spbilling.jsp",params,rowCount,pageIndex);
	
	String[] statusData = {"待审核","已审核","已收款"};
	
	String[] btnStrings = {" <a href='#' onclick='sendToFinance(helloisthereany)'>审核</a>&nbsp;&nbsp;<a href='#' onclick='delSpBilling(helloisthereany)'>删除</a>&nbsp;&nbsp;<a href='#' onclick='reExportSpBilling(helloisthereany)'>重新生成</a>",
			"<a href='#' onclick='reCallSpBillingBack(helloisthereany)'>撤回</a>","",""};
	
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

	var spList = new Array();
	
	<%
	for(SpModel spModel : spList)
	{
		%>
		spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
		<%
	}
	%>
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp_id").val(joData.id);
	}

	//删除帐单
	function delSpBilling(id)
	{
		if(confirm('真的要删除帐单吗？删除后可以在SP数据下重新生成帐单'))
		{
			window.location.href = "spbilling.jsp?type=3&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";	
		}
	}
	
	//重新生成帐单
	function reExportSpBilling(id)
	{
		if(confirm("确定是否重新生成帐单？"))
		{
			window.location.href = "spbilling.jsp?type=2&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//审核帐单
	function sendToFinance(id)
	{
		if(confirm("是否确认数据无误？审核后会进入财务结款!"))
		{
			window.location.href = "spbilling.jsp?type=4&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	//召回已发送给财务的帐单
	function reCallSpBillingBack(id)
	{
		if(confirm("确定是否要撤回该SP帐单？"))
		{
			window.location.href = "spbilling.jsp?type=6&spbillingid=" + id + "&<%= Base64UTF.decode(query) %>";
		}
	}
	
	$(function()
	{
		$("#sel_js_type").val(<%= jsType %>);
		$("#sel_sp_id").val(<%= spId %>);
		$("#sel_status").val(<%= status %>);
	});
	
</script>
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="spbilling.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me" style="margin-left: -10px;">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp_id" onclick="namePicker(this,spList,onSpDataSelect)">
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
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd04_me">
						<select name="js_type" id="sel_js_type" >
						<option value="-1">请选择</option>
						<%
						for(JsTypeModel jsTypeModel : jsTypeList)
						{
							%>
							<option value="<%= jsTypeModel.getJsType() %>"><%= jsTypeModel.getJsName() %></option>
							<%
						}
						%>
						</select>
					</dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd04_me">
						<select name="status" id="sel_status" >
						<option value="-1">请选择</option>
						<%
						for(int i=0; i<statusData.length; i++)
						{
							%>
							<option value="<%= i %>"><%= statusData[i] %></option>
							<%
						}
						%>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查     询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SP</td>
					<td>开始时间</td>
					<td>结束时间</td>
					<td>结算类型</td>
					<td>信息费</td>
					<td>应收款</td>
					<td>核减款</td>
					<td>实际应收款</td>
					<td>实际到款</td>
					<td>备注</td>
					<td>创建时间</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (SpBillingModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getSpName() %></td>
					<td><%=model.getStartDate() %></td>
					<td><%=model.getEndDate()%></td>
					<td><%= model.getJsName() %></td>
					<td><%= StringUtil.getDecimalFormat(model.getAmount()) %></td>
					<td><%=StringUtil.getDecimalFormat(model.getPreBilling()) %></td>
					<td><%=StringUtil.getDecimalFormat(model.getReduceAmount()) %></td>
					<td><%= StringUtil.getDecimalFormat(model.getPreBilling() - model.getReduceAmount()) %> </td>
					<td><%= StringUtil.getDecimalFormat(model.getActureBilling()) %></td>
					<td><%=model.getRemark() %></td>
					<td><%= model.getCreateDate() %></td>
					<td><%= statusData[model.getStatus()] %></td>
					<td style="text-align: left">
						<a href="spbillingdetail.jsp?query=<%= query %>&spbillingid=<%= model.getId() %>" >详细</a>
						<%= btnStrings[model.getStatus()].replaceAll("helloisthereany", "" + model.getId()) %>
						<a href="spbilling.jsp?type=1&spbillingid=<%= model.getId() %>">导出</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="100" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>
<%
		if(isRecall)
		{
			if(!isRecallSuc)
			{
				out.println("<script>alert('财务已审核，撤回帐单失败，请找管理员！');</script>");
			}
		}
%>
