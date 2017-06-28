<%@page import="com.system.server.LvMrServer"%>
<%@page import="com.system.model.LvMrModel"%>
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
	UserModel user=(UserModel)session.getAttribute("user");
	int userId=user.getId();
	String startDate = StringUtil.getString(request.getParameter("startdate"), defaultStartDate);
	String endDate = StringUtil.getString(request.getParameter("enddate"), defaultEndDate);
	int payType=StringUtil.getInteger(request.getParameter("pay_type"),-1);

	Map<String, Object> map = new LvMrServer().getLvMrDaysData(startDate, endDate, payType, userId);
	List<LvMrModel> list=(List<LvMrModel>)map.get("list");
	float allAmount=(Float)map.get("allAmount");
	
	out.clear();
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
	
	$(function()
	{
		$("#pay_type").val(<%=payType%>);
	});
	
</script>
<body>
	<div class="main_content">
		<div class="content">
			<form action="lvmr1.jsp" method="get" style="margin-top: 10px">
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
					<dd class="dd01_me">支付方式</dd>
					<dd class="dd04_me">
						<select name="pay_type" id="pay_type" style="width: 110px;"
							title="请选择支付方式">
							<option value="-1">全部</option>
							<option value="0">支付宝</option>
							<option value="1">微信</option>
						</select>
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
					<td>序号</td>
					<td onclick="TableSorter('table_id',1,'date')">日期</td>
					<td onclick="TableSorter('table_id',2,'float')">收入(元)</td>
				</tr>
			</thead>
			<tbody>
				<%
					int index = 1;
					for (LvMrModel model : list) {
				%>
				<tr>
					<td><%=index++%></td>
					<td><%=model.getDateDay()%></td>
					<td><%=model.getDayAmount()%></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>总收入(元):<%=StringUtil.getDecimalFormat(allAmount)%></td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>