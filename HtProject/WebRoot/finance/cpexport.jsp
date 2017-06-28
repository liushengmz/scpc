<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.vmodel.SpFinanceShowModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.model.SettleAccountModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SettleAccountServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil.getString(request.getParameter("enddate"), StringUtil.getMonthEndDate());
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int dateType = StringUtil.getInteger(request.getParameter("datetype"), -1);
	boolean isNotFirstLoad = StringUtil.getInteger(request.getParameter("load"), -1) == -1 ? false : true;
	boolean isExport = StringUtil.getInteger(request.getParameter("export"), -1) == 1 ;
	List<CpModel> cpList = new CpServer().loadCp();
	String display = "";
	Map<String, List<SpFinanceShowModel>> map = null;
	if (isExport) 
	{
		SettleAccountServer accountServer = new SettleAccountServer();
		List<SettleAccountModel> list = accountServer.loadCpSettleAccountList(cpId, startDate, endDate,dateType);
		if (list != null && list.size() > 0) 
		{
			String cpName = "";
			for (CpModel cp : cpList) 
			{
				if (cp.getId() == cpId) 
				{
					cpName = cp.getShortName();
					break;
				}
			}
			response.setContentType("application/octet-stream;charset=utf-8");
			
			String fileName = cpName + "(" + startDate + "_" + endDate + ").xls";
			
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) 
			{
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} 
			else 
			{
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}

			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			accountServer.exportSettleAccount(2, dateType, cpName, startDate , endDate, list,
					response.getOutputStream());
			
			out.clear();
			
			out = pageContext.pushBody();
		} 
		else 
		{
			display = "alert('没有相应的数据');";
		}
	}
	else
	{
		if(isNotFirstLoad)
			map = new SettleAccountServer().loadCpSettleAccountData(startDate, endDate,cpId,dateType);
	}
	List<JsTypeModel> jsTypeList = new JsTypeServer().loadJsType();
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
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

	var cpList = new Array();
	<%
	for(CpModel cpModel : cpList)
	{
		%>
		cpList.push(new joSelOption(<%= cpModel.getId() %>,1,'<%= cpModel.getShortName() %>'));
		<%
	}
	%>
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp").val(joData.id);
	}

	$(function()
	{
		$("#sel_date_type").val("<%= dateType %>");
		$("#sel_cp").val("<%= cpId %>");
	});
	
	function subForm() 
	{
	
		if ($("#sel_date_type").val() == "-1") {
			alert("请选择日期结算类型");
			$("#selsel_date_type_sp").focus();
			return;
		}
	
		document.getElementById("exportform").submit();
	}
	
	function exportCpBillData(startDate,endDate,cpId,jsType)
	{
		window.location.href = "cpexport_all.jsp?export=1&js_type=" + jsType + "&cpid=" + cpId + "&startdate=" + startDate + "&enddate=" + endDate;
	}
	
	function exportBill(startDate,endDate,cpId,jsType)
	{
		getAjaxValue("action.jsp?js_type=" + jsType + "&cpid=" + cpId + "&startdate=" + startDate + "&enddate=" + endDate,onExportBillResult);
	}
	
	function onExportBillResult(data)
	{
		if("OK" == data.trim())
		{
			alert("开始对帐成功");
		}
		else
		{
			alert("已存在相同的对帐单");	
		}
	}
	
</script>
<body style="padding-top: 40px">
	<div class="main_content">
		<div class="content" style="margin-top: 0px">
			<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="cpexport.jsp" method="post" id="exportform">
				<dl>
					<input type="hidden" value="1" name="load" />
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp"  style="width: 120px" onclick="namePicker(this,cpList,onCpDataSelect)">
							<option value="-1">全部</option>
							<%
								for (CpModel cp : cpList)
								{
							%>
							<option value="<%=cp.getId()%>"><%=cp.getShortName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd04_me">
						<select name="datetype" id="sel_date_type" title="选择结算类型" style="width:100px">
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

					<dd class="dd00_me"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 10px;" />
					<input type="button" value="查  询" onclick="subForm()">
					</dd>
				</dl>
			</form>
			</div>
			<br /><br />
			<table cellpadding="0" cellspacing="0">
				<thead>
					<tr>
					<br/>
						<td>CP名称</td>
						<td>业务线</td>
						<td>SP业务名称</td>
						<td>金额</td>
						<td>结算率</td>
						<td>合作方分成</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody>
					<%
						if (map != null)
						{
							SpFinanceShowModel sfsModel = null;
							List<SpFinanceShowModel> tmpList = null;
							for (String key : map.keySet())
							{
								tmpList = map.get(key);
								float totalAmount = 0;
								for (int i = 0; i < tmpList.size(); i++)
								{
									sfsModel = tmpList.get(i);
									totalAmount += (sfsModel.getAmount()
											* sfsModel.getJiesuanlv());
									if (i == 0)
									{
										out.println("<tr><td rowspan='" + tmpList.size()
												+ "'>" + key + "</td><td>"
												+ sfsModel.getOperatorName() + "</td><td>"
												+ sfsModel.getSpTroneName() + "</td><td>"
												+ sfsModel.getAmount() + "</td><td>"
												+ sfsModel.getJiesuanlv() + "</td><td>"
												+ StringUtil.getDecimalFormat(sfsModel.getAmount()
														* sfsModel.getJiesuanlv())
												+ "</td><td rowspan='" + tmpList.size()
												+ "'><a href='#' onclick=exportBill(\'" + startDate + "','"+ endDate +"'," + sfsModel.getSpId() + "," + dateType + ")>对帐</a></td></tr>");
									}
									else
									{
										out.println("<tr><td>" + sfsModel.getOperatorName()
												+ "</td><td>" + sfsModel.getSpTroneName()
												+ "</td><td>" + sfsModel.getAmount()
												+ "</td><td>" + sfsModel.getJiesuanlv()
												+ "</td><td>"
												+ StringUtil.getDecimalFormat(sfsModel.getAmount()
														* sfsModel.getJiesuanlv())
												+ "</td></tr>");
									}
								}
								out.println(
										"<tr style='background-color: #E0EEEE;'><td colspan='5' >合计</td><td>"
												+ StringUtil.getDecimalFormat(totalAmount) + "</td><td></td></tr>");
							}
						}
					%>
				</tbody>
			</table>
		</div>

	</div>
</body>
</html>