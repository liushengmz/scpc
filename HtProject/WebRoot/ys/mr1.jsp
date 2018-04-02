<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.yhxf.MrServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.params.ReportParamsModel"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getMonthEndDate();

	String startDate = StringUtil.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil.getString(request.getParameter("enddate"), defaultEndDate);

	int showType = StringUtil.getInteger(request.getParameter("sort_type"), 1);

	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int operatorId = StringUtil.getInteger(request.getParameter("operator"), -1);

	ReportParamsModel params = new ReportParamsModel();
	params.setStartDate(startDate);
	params.setEndDate(endDate);
	params.setShowType(showType);
	params.setCpId(cpId);
	params.setOperatorId(operatorId);
	
	Map<String, Object> map = new MrServer().loadShowData(params);
	
	List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("data");
	
	List<CpModel> cpList = new CpServer().loadCp();

	int rows = (Integer) map.get("rows");
	int dataRows = (Integer) map.get("dataRows");
	Float dataAmount = (Float) map.get("dataAmount");

	String[] titles = {"日期", "月份", "运营商","CP","合作公司-运营商","合作公司"};

	out.clear();
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
//排序 tableId: 表的id,iCol:第几列 ；
  var sortStatus;
  var sortArray;
function TableSorter(tableId, iCol,dataType) {     
    var table = document.getElementById(tableId);     
    var tbody = table.tBodies[0];     
    var colRows = tbody.rows;     
    var aTrs = new Array;   
    //将将得到的列放入数组，备用     
    for (var y=colRows.length-1;y>=0; y--) {     
       var tr=colRows[y];
    	aTrs.push(colRows[y]);
    	tr.parentNode.removeChild(tr);
    } 
    if(sortStatus==null||sortStatus!=iCol){
    	sortArray = Sort(aTrs,iCol,dataType); 
    	sortStatus=iCol;
    }else{
    	sortArray=arrayReverse(sortArray);
    }

    for (var k=0; k < sortArray.length; k++) {  
    	sortArray[k].cells[0].innerHTML=k+1;
    	tbody.appendChild(sortArray[k]);
    }  
}
function Sort(aTrs,col,dataType){
	 var i = aTrs.length, j;
	 var tempExchangVal;
	 if(dataType=='date'){
	    while (i > 0) {
	        for (j = 0; j < i - 1; j++) {
	            if (aTrs[j].cells[col].innerHTML>aTrs[j+1].cells[col].innerHTML) {
	                tempExchangVal = aTrs[j];
	                aTrs[j] = aTrs[j + 1];
	                aTrs[j + 1] = tempExchangVal;
	            }
	        }
	   i--;
	}
}   
	 if(dataType=='float'){
	
		    while (i > 0) {
		        for (j = 0; j < i - 1; j++) {
		        	var strmin=parseFloat(aTrs[j].cells[col].innerHTML);
		        	var strmax=parseFloat(aTrs[j+1].cells[col].innerHTML);
		            if (strmin>strmax) {
		                tempExchangVal = aTrs[j];
		                aTrs[j] = aTrs[j + 1];
		                aTrs[j + 1] = tempExchangVal;
		            }
		        }
		   i--;
		}
	}   
	 if(dataType=='String'){
		    while (i > 0) {
		        for (j = 0; j < i - 1; j++) {
		        	var strmin=aTrs[j].cells[col].innerHTML;
		        	var strmax=aTrs[j+1].cells[col].innerHTML;
		        	strmin=strmin.substr(0,strmin.length-1);
		        	strmax=strmax.substr(0,strmax.length-1);
		            if (parseFloat(strmin)>parseFloat(strmax)) {
		                tempExchangVal = aTrs[j];
		                aTrs[j] = aTrs[j + 1];
		                aTrs[j + 1] = tempExchangVal;
		            }
		        }
		   i--;
		}
	}   
	return aTrs;
}
function arrayReverse(arr) {
	for (var i = 0; i < arr.length / 2; i++) {
	var temp = arr[i]; //交换变量
	arr[i] = arr[arr.length - i - 1];
	arr[arr.length-i-1]=temp;
	}
	return arr;
}


	
	var cpList = new Array();
	<%for (CpModel cpModel : cpList) {%>
		cpList.push(new joSelOption(<%=cpModel.getId()%>,1,'<%=cpModel.getShortName()%>'));
		<%}%>
	
	function onCpDataSelect(joData)
	{
		if(joData.id==-1)
			$("#input_cp").val("");
		else
			$("#input_cp").val(joData.text);
		
		$("#sel_cp").val(joData.id);
	}
	
	$(function()
	{
		$("#sel_cp").val(<%= cpId %>);	
		
		<%
		if(cpId>0)
		{
			%>
			$("#input_cp").val($("#sel_cp").find("option:selected").text());
			<%
		}
		%>
		
		$("#sel_operator").val(<%= operatorId %>);
		$("#sel_sort_type").val(<%= showType %>);
		
	});

</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mr1.jsp" method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})"
							style="width: 100px;">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})"
							style="width: 100px;">
					</dd>
					
					<dd class="dd01_me">CP</dd>
					<dd class="dd03_me">
						<input  type="text" id="input_cp" onclick="namePicker(this,cpList,onCpDataSelect)" style="width: 100px;" readonly="readonly" >
						<select name="cp_id" id="sel_cp" title="选择CP"
							style="width: 110px; display: none" >
							<option value="-1">全部</option>
							<%
								for (CpModel cp : cpList) {
							%>
							<option value="<%=cp.getId()%>"><%=cp.getShortName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					<!--
					<dd class="dd01_me">运营商</dd>
					<dd class="dd04_me">
						<select name="operator" id="sel_operator" style="width: 100px;">
							<option value="-1">全部</option>
							<option value="3">移动</option>
							<option value="1">联通</option>
							<option value="2">电信</option>
						</select>
					</dd>
					-->
					<dd class="dd01_me" style="font-weight: bold; font-size: 14px">展示方式</dd>
					<dd class="dd04_me">
						<select name="sort_type" id="sel_sort_type" title="展示方式"
							style="width: 110px;">
							<option value="1">日期</option>
							<option value="2">月份</option>
							<!--  <option value="3">运营商</option> -->
							<option value="4">CP</option>
							<!--  <option value="5">合作公司-运营商</option> -->
							<option value="6">合作公司</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0" id="table_id" >
			<thead>
				<tr>
					<td>序号</td>
					<td onclick="TableSorter('table_id',1,'date')"><%= titles[showType - 1] %></td>
					<td onclick="TableSorter('table_id',2,'float')">数据量(条)</td>
					<td onclick="TableSorter('table_id',3,'float')">金额(元)</td>
				</tr>
			</thead>
			<tbody>
				<%
					int index = 1;
					for (Map<String,Object> dMap : list ) {
				%>
				<tr>
					<td><%=index++%></td>
					<td><%= dMap.get("showName") %></td>
					<td><%= dMap.get("dataRows") %></td>
					<td><%= StringUtil.getDecimalFormat((Float)dMap.get("dataAmount")) %></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>总数据量(条)：<%= dataRows %></td>
					<td>总金额(元)：<%= StringUtil.getDecimalFormat(dataAmount) %></td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>