<%@page import="com.system.model.ProfitCalModel"%>
<%@page import="com.system.server.ProfitCalServer"%>
<%@page import="com.system.server.CommRightServer"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.CityServer"%>
<%@page import="com.system.model.CityModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneServer"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.MrServer"%>
<%@page import="com.system.model.MrReportModel"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.model.xy.XyUserModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String defaultStartDate = StringUtil.getMonthHeadDate();
	String defaultEndDate = StringUtil.getMonthEndDate();

	String startDate = StringUtil.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil.getString(request.getParameter("enddate"), defaultEndDate);

	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone"), -1);

	List<SpModel> spList = new SpServer().loadSp();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();

	List<ProfitCalModel> list = new ProfitCalServer().loadProfit(startDate, endDate, spId, spTroneId);
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
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
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



	var spList = new Array();
	<%for (SpModel spModel : spList) {%>
		spList.push(new joSelOption(<%=spModel.getId()%>,1,'<%=spModel.getShortName()%>'));
		<%}%>
	
	function onSpDataSelect(joData)
	{
		if(joData.id==-1)
			$("#input_sp").val("");
		else
			$("#input_sp").val(joData.text);
		
		$("#sel_sp").val(joData.id);
		troneChange();
	}
	
	var spTroneArray = new Array();
	<%for (SpTroneModel spTroneModel : spTroneList) {%>
	spTroneArray.push(new joBaseObject(<%=spTroneModel.getId()%>,<%=spTroneModel.getSpId()%>,'<%=spTroneModel.getSpTroneName()%>'));	
		<%}%>
	
	$(function()
	{
		//SP的二级联动
		$("#sel_sp").val(<%=spId%>);
		
		<%
		if(spId>0)
		{
			out.println("fillSpName(" + spId + ");");
		}
		%>
		
		$("#sel_sp").change(troneChange);
		troneChange();
		$("#sel_sp_trone").val(<%=spTroneId%>);
		
	});
	
	function fillSpName(spId)
	{
		if(spId<=0)
			return;
		
		for(var i=0; i<spList.length; i++)
		{
			if(spList[i].id==spId)
			{
				$("#input_sp").val(spList[i].text);
				break;	
			}
		}
	}
	
	
	var npSpTroneArray = new Array();
	
	<%for (SpTroneModel spTroneModel : spTroneList) {%>
		npSpTroneArray.push(new joSelOption(<%=spTroneModel.getId()%>,<%=spTroneModel.getSpId()%>,'<%=spTroneModel.getSpTroneName()%>'));
<%}%>
	function npSpTroneChange(jodata) {
		$("#sel_sp_trone").val(jodata.id);
	}

	function troneChange() 
	{
		var spId = $("#sel_sp").val();
			
		$("#sel_sp_trone").empty();
		
		if(spId<=0)
			return;
		
		$("#sel_sp_trone").append("<option value='-1'>全部</option>");
		for (i = 0; i < spTroneArray.length; i++) {
			if (spTroneArray[i].pid == spId || spId == "-1") {
				$("#sel_sp_trone").append(
						"<option value='" + spTroneArray[i].id + "'>"
								+ spTroneArray[i].name + "</option>");
			}
		}

		$("#sel_trone").empty();
		$("#sel_trone").append("<option value='-1'>全部</option>");
		for (i = 0; i < troneList.length; i++) {
			if (troneList[i].spId == spId || spId == "-1") {
				$("#sel_trone").append(
						"<option value='" + troneList[i].id + "'>"
								+ troneList[i].troneName + "</option>");
			}
		}
	}

</script>
<body>
	<div class="main_content">
		<div class="content">
			<form action="sp_profit.jsp" method="get" style="margin-top: 10px">
				<input type="hidden" name="isfirstload" value="1" />
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
					<dd class="dd01_me">SP</dd>
					<dd class="dd03_me">
						<input  type="text" id="input_sp" onclick="namePicker(this,spList,onSpDataSelect)" style="width: 100px;" readonly="readonly" >
						<input type="hidden" name="sp_id" id="sel_sp" />
					</dd>
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone" id="sel_sp_trone" style="width: 110px;" ></select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0" id="table_id">
			<thead>
				<tr>
					<td>SP</td>
					<td>SP收入</td>
					<td>SP利润</td>
					<td>SP业务</td>
					<td>SP业务收入</td>
					<td>CP业务总支出</td>
					<td>SP业务利润</td>
					<td>CP</td>
					<td>CP业务支出</td>
					
				</tr>
			</thead>
			<tbody>
				<% 
					for(int i=0; i<list.size(); i++)
					{
						ProfitCalModel spModel = list.get(i);
						
						out.println("<tr><td rowspan='" + spModel.spRowsCount + "'>" + spModel.spName + "</td>");
						out.println("<td rowspan='" + spModel.spRowsCount + "'>" + StringUtil.getDecimalFormat(spModel.spMoney) + "</td>");
						out.println("<td rowspan='" + spModel.spRowsCount + "'>" + StringUtil.getDecimalFormat(spModel.spProfit) + "</td>");
						
						for(ProfitCalModel.SpTroneModel spTroneModel : spModel.spTroneList)
						{
							out.println("<td rowspan='" + spTroneModel.spTroneRowsCount + "'>" + spTroneModel.spTroneName + "</td>");
							out.println("<td rowspan='" + spTroneModel.spTroneRowsCount + "'>" + StringUtil.getDecimalFormat(spTroneModel.spTroneMoney) + "</td>");
							out.println("<td rowspan='" + spTroneModel.spTroneRowsCount + "'>" + StringUtil.getDecimalFormat(spTroneModel.spTroneMoney - spTroneModel.spTroneProfit) + "</td>");
							out.println("<td rowspan='" + spTroneModel.spTroneRowsCount + "'>" + StringUtil.getDecimalFormat(spTroneModel.spTroneProfit) + "</td>");
							
							for(ProfitCalModel.SpTroneModel.CpSpTroneModel cpSpTroneModel : spTroneModel.cpSpTroneList)
							{
								out.println("<td>" + cpSpTroneModel.cpName + "</td>");
								out.println("<td>" + StringUtil.getDecimalFormat(cpSpTroneModel.cpMoney) + "</td>");
								out.print("</tr>\r\n");
								out.print("<tr>");
							}
						}
						
						out.println("</tr>");
					}
				
				%>
			<tbody>
		</table>
	</div>

</body>
</html>