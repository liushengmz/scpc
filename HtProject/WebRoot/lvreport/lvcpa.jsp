<%@page import="com.system.model.LvSpecialCpaModel"%>
<%@page import="com.system.server.LvMrServer"%>
<%@page import="com.system.model.UserModel"%>
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
	UserModel user = (UserModel)session.getAttribute("user");
	int userId = user.getId();
	String startDate = StringUtil.getString(request.getParameter("start_date"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil.getString(request.getParameter("end_date"), StringUtil.getDefaultDate());
	
	List<LvSpecialCpaModel> list = new LvMrServer().loadLvSpecialCpaByUserId(startDate, endDate, userId);
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

	});
	
	
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="lvcpa.jsp"  method="get">
				<dl>
					<dd class="dd01_me" >开始日期</dd>
					<dd class="dd03_me">
						<input name="date"  type="text" value="<%=startDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 100px;">
					<dd class="dd01_me" >结束日期</dd>
					<dd class="dd03_me">
						<input name="date"  type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 100px;">
					<!--  
					<dd class="dd01_me">支付方式</dd>
					<dd class="dd04_me">
						<select name="pay_type" id="pay_type" title="选择支付方式" >
							<option value="-1">全部</option>
							<option value="0">支付宝</option>
							<option value="1">微信</option>
						</select>
					</dd>
					-->
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
					<td>日期</td>
					<td>支付方式</td>
					<td>激活数</td>
					<td>有效激活数</td>
					<td>收入</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					int totalDataRows = 0;
					int totalEffectRows = 0;
					int totalMoney = 0;
					int effectRows = 0;
					
					if(list!=null)
					for(LvSpecialCpaModel model : list)
					{
						totalDataRows += model.getDataRows();
						effectRows =  (int)Math.floor(model.getAmount()*0.8/300);
						
						if(effectRows>model.getDataRows())
							effectRows = model.getDataRows();
						
						totalMoney += effectRows*3;
						totalEffectRows += effectRows;
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getActiveDate() %></td>
					<td><%= model.getPayType()==1 ? "微信" : "支付宝" %></td>
					<td><%= model.getDataRows() %></td>
					<td><%= effectRows %></td>
					<td><%= effectRows*3 %></td>
				</tr>
						<%
					}
				%>
			</tbody>
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td>总激活数：<%= totalDataRows %></td>
					<td>总有效激活:<%= totalEffectRows %> </td>
					<td>总额(元):<%= totalMoney %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>