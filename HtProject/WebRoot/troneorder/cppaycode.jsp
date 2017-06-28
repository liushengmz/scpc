<%@page import="com.system.model.PayCodeExportChildModel"%>
<%@page import="com.system.model.PayCodeExportModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.server.TroneServer"%>
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
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
	
	int status = StringUtil.getInteger(request.getParameter("status"), -1);
	
	List<CpModel> cpList = new CpServer().loadCp();
	
	int isLoad = StringUtil.getInteger(request.getParameter("export"), -1);
	
	List<PayCodeExportModel> exportList = new TroneOrderServer().loadPayCodeExportModelListByCpSpTroneId(cpId, spTroneId, status);
	
	if(isLoad>0 && !exportList.isEmpty())
	{
		response.setContentType("application/octet-stream;charset=utf-8");
		
		String cpName = "";
		
		for(int i=0; i<cpList.size(); i++)
		{
			if(cpList.get(i).getId()==cpId)
			{
				cpName = cpList.get(i).getShortName(); 
			}
		}
		
		String fileName = cpName +  "_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt";
		
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
		
		StringBuffer sbData = new StringBuffer();
		
		sbData.append("公司名称：" + cpName + "\r\n\r\n");
		
		for(int i=0; i<exportList.size(); i++)
		{
			PayCodeExportModel exportModel = exportList.get(i);
			
			sbData.append("业务：" + exportModel.getSpTroneName() + "\r\n");
			sbData.append("上量方式:" + exportModel.getUpDataTypeName() + "\r\n");
			sbData.append("省份:" + exportModel.getPrivincesName() + "\r\n");
			sbData.append("下行语:" + exportModel.getRemark() + "\r\n");
			sbData.append("计费点\t价格\t同步指令\t同步端口\r\n");
			
			for(PayCodeExportChildModel childModel : exportModel.getChildList())
			{
				sbData.append((100000 + childModel.getPayCode()) + "\t" + childModel.getPrice() +"\t" + childModel.getOrders() + "\t" + childModel.getTroneNum() + "\r\n");
			}
			
			sbData.append("\r\n");
		}
		
		/*
		
		sbData.append("PayCode\t业务名称\t价格\t状态\t省份\t备注\r\n");
		
		TroneOrderModel orderModel = null;
		
		for(int i=0; i<list.size(); i++)
		{
			orderModel = list.get(i);
			sbData.append((orderModel.getId() + 100000) + "\t" + orderModel.getSpTroneName() + "\t" + orderModel.getPrice() 
			+ "\t" + ((orderModel.getDisable() ==0 || orderModel.getSpTroneStatus() == 1) ? "启用" : "停用") + "\t" + orderModel.getProvinceList() + "\t" + orderModel.getRemark() + "\r\n");
		}
		
		*/
        
        out.clear();
        
        out.print(sbData.toString());
        
        out.flush();
        
        return;
	}
	
	
	List<TroneOrderModel> list = new ArrayList();
	
	if(cpId > 0)
	  	list =  new TroneOrderServer().loadTroneOrderListByCpSpTroneId(cpId, spTroneId, status);
	
	List<SpTroneModel> spTroneList = new ArrayList();
	
	if(cpId>0)
		spTroneList = new SpTroneServer().loadTroneListByCpid(cpId);
	
	
	
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
		$("#sel_cp_id").val(joData.id);
	}
	
	$(function()
	{
		$("#sel_cp_id").val(<%= cpId %>);
		$("#sel_sp_trone_id").val(<%= spTroneId %>);
		$("#sel_status").val(<%= status %>);
	});
	
	function exportPayCode()
	{
		window.location.href = "cppaycode.jsp?export=1&cp_id=<%= cpId %>&sp_trone_id=<%= spTroneId %>&status=<%= status %>";
	}
	
	function refreshPayCode()
	{
		open("<%= ConfigManager.getConfigData("REFRESH_PAY_CODE_URL", "http://thread.n8wan.com/re.jsp") %>");
	}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<form action="cppaycode.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp_id" onclick="namePicker(this,cpList,onCpDataSelect)">
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
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone_id" id="sel_sp_trone_id" >
							<option value="-1">全部</option>
							<%
							for(SpTroneModel spTrone : spTroneList)
							{
								%>
							<option value="<%= spTrone.getId() %>"><%= spTrone.getSpTroneName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd04_me">
						<select name="status" id="sel_status" >
							<option value="-1">全部</option>
							<option value="1">启用</option>
							<option value="0">停用</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查     询" type="submit" />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" value="导    出"  onclick="exportPayCode()"  />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" value="刷   新"  onclick="refreshPayCode()"  />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>PayCode</td>
					<td>业务名称</td>
					<td>价格</td>
					<td>同步指令</td>
					<td>同步端口</td>
					<td>状态</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (TroneOrderModel model : list)
					{
				%>
				<tr <%= model.getDisable() == 1 ? stopStyle : "" %>>
					<td><%= rowNum++ %></td>
					<td><%= model.getId() + 100000 %></td>
					<td><%= (model.getSpId() + 1000) + "-" + model.getSpTroneName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getTroneOrder() %></td>
					<td><%= model.getTroneNum() %></td>
					<td><%= (model.getDisable()==0 && model.getSpTroneStatus()==1)  ? "启用" : "停用" %></td>
				</tr>
				<%
					}
				%>
				
			<tbody>
		</table>
	</div>
	
</body>
</html>