<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.server.CpProvinceQueryServer"%>
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
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);

	String[] operaNames = {"联通","电信","移动","虚拟","未知"};

	if(cpId>0)
	{
		String showHtml = "";
		
		Map<Integer,Map<Float,List<Integer>>> operaMap = new CpProvinceQueryServer().loadCpProvince(cpId);

		List<ProvinceModel> proList = new ProvinceServer().loadProvince();
		
		for(int operaId : operaMap.keySet())
		{
			Map<Float,List<Integer>> priceMap = operaMap.get(operaId);
			
			boolean isStart = true;
			
			for(float price: priceMap.keySet())
			{
				String pros = "";
				
				for(int pro : priceMap.get(price))
				{
					for(ProvinceModel proModel : proList)
					{
						if(proModel.getId()==pro)
							pros += proModel.getName() + ",";
					}
				}
				
				if(!StringUtil.isNullOrEmpty(pros))
				{
					pros = pros.substring(0,pros.length()-1);
				}
				
				if(isStart)
				{
					showHtml += "<tr><td rowspan=\"" + priceMap.size() + "\">" + operaNames[operaId-1] + "</td><td>" + price + "</td><td>" + pros + "</td></tr>";
				}
				else
				{
					showHtml += "<tr><td>" + price + "</td><td>" + pros + "</td></tr>";
				}
				
				isStart = false;
				
			}
		}
		
		out.println(showHtml);
		
		return;
	}

	List<CpModel> cpList = new CpServer().loadCp();
	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<script type="text/javascript">
	
	var cpId = 0 ;
	
	var cpList = new Array();
	<%for (CpModel cpModel : cpList) 
	{%>
		cpList.push(new joSelOption(<%=cpModel.getId()%>,1,'<%=cpModel.getShortName()%>'));
	<%}%>
	
	function onCpDataSelect(joData)
	{
		cpId = joData.id;
		$("#input_cp").val(joData.text);
		cpChange();
	}
	
	function cpChange()
	{
		if(cpId<=0)
		{
			$("#data_table_tbody").html("");
			return;
		}
		
		getAjaxValue("cpproquery.jsp?cp_id=" + cpId,onDataChange);
	}
	
	function onDataChange(data)
	{
		$("#data_table_tbody").html(data.trim());
	}
	
	
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="dd01_me" style="margin-left: -16px;">CP</dd>
				<dd class="dd03_me">
					<input name="cp" id="input_cp"  value="未选择" type="text" style="width: 120px" onclick="namePicker(this,cpList,onCpDataSelect)" readonly="readonly">
				</dd>
				<!--  
				<dd class="dd01_me">运营商</dd>
					<dd class="dd04_me">
					<select name="operator" id="sel_operator" style="width: 100px;"></select>
				</dd>
					<dd class="dd01_me">价格</dd>
					<dd class="dd04_me">
					<select name="price" id="sel_price" style="width: 100px;"></select>
				</dd>
				
				<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
					<input class="btn_match" name="search" value="查 询" type="button" >
				</dd>
				-->
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0" >
			<thead>
				<tr>
					<td>运营商</td>
					<td>价格(元)</td>
					<td>开通省份</td>
				</tr>
			</thead>
			<tbody id="data_table_tbody">
			</tbody>	
		</table>
	</div>
	
</body>
</html>