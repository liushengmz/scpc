<%@page import="com.system.sdk.server.SdkTroneServer"%>
<%@page import="com.system.sdk.model.SdkTroneModel"%>
<%@page import="com.system.sdk.server.SdkSpTroneServer"%>
<%@page import="com.system.sdk.model.SdkSpTroneModel"%>
<%@page import="com.system.sdk.server.SdkDataSummerServer"%>
<%@page import="com.system.sdk.model.SdkDataSummerModel"%>
<%@page import="com.system.sdk.server.SdkSpServer"%>
<%@page import="com.system.sdk.model.SdkSpModel"%>
<%@page import="com.system.sdk.server.SdkChannelServer"%>
<%@page import="com.system.sdk.model.SdkChannelModel"%>
<%@page import="com.system.sdk.server.SdkCpServer"%>
<%@page import="com.system.sdk.model.SdkCpModel"%>
<%@page import="com.system.sdk.server.SdkAppServer"%>
<%@page import="com.system.sdk.model.SdkAppModel"%>
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
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int channelId=StringUtil.getInteger(request.getParameter("sdk_channel_id"), -1);
	int appId=StringUtil.getInteger(request.getParameter("app_id"), -1);
	int troneId=StringUtil.getInteger(request.getParameter("trone_id"), -1);
	int spTroneId=StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
	int showType=StringUtil.getInteger(request.getParameter("showType"), 1);
	//添加省份查询
	int provinceId=StringUtil.getInteger(request.getParameter("province_id"), -1);
	// 1:天  2:周  3：月  4：CP 5:APP 6:channel 7:sp_trone_id 8:trone_id 9:province_id
	String[] titles = {"日期", "周数", "月份", "CP", "APP", "渠道", "SP业务", "通道", "省份"};
	List<SdkCpModel>  cpList=new SdkCpServer().loadSdkCp();
	List<SdkAppModel> appList=new SdkAppServer().loadSdkApp();
	List<SdkChannelModel> channelList=new SdkChannelServer().loadSdkChannel();
	List<SdkSpModel> channel=new SdkSpServer().loadSdkSp();
	List<SdkSpTroneModel> spTroneList=new SdkSpTroneServer().loadSdkSpTrone();
	List<SdkTroneModel> sdkTroneList=new SdkTroneServer().loadSdkTrone();
	List<ProvinceModel> provinceList=new ProvinceServer().loadProvince();
	Map<String,Object> map=new SdkDataSummerServer().loadSdkDataSummerModel(cpId, channelId, appId, troneId, spTroneId, startDate, endDate, showType,provinceId);
	List<SdkDataSummerModel> dataSummerList=(List<SdkDataSummerModel>)map.get("list");
	Integer countActRows=(Integer)map.get("countActRows");
	Integer countUserRows=(Integer)map.get("countUserRows"); 
	Integer countTroReqRows=(Integer)map.get("countTroReqRows"); 
	Integer countEffReqRows=(Integer)map.get("countEffReqRows");
	Integer countTroOrdRows=(Integer)map.get("countTroOrdRows"); 
	Integer countMsgRows=(Integer)map.get("countMsgRows");
	Integer countSucRows=(Integer)map.get("countSucRows");
	Float countAmount=(Float)map.get("countAmount");
	Float countEffAmount=(Float)map.get("countEffAmount");
	//List<SdkDataSummerModel> dataSummerList=new SdkDataSummerServer().loadSdkDataSummerModel(cpId,
	//		channelId, appId, 
	//		troneId, spTroneId, 
	//		startDate, endDate, showType);

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
function troneObj(id,spTroneId,name)
{
	var obj = {};
	obj.id = id ;
	obj.spTroneId = spTroneId;
	obj.name = name;
	return obj;
}
<!--SP通道 -->
var troneArray =new Array();
<%
for(SdkTroneModel troneModel : sdkTroneList)
{
	%>
	troneArray.push(new troneObj(<%= troneModel.getTroneId() %>,<%=troneModel.getSpTroneId()%>,'<%=troneModel.getName()%>'));
	<%
}
%>
<!--SP业务 -->
var spTroneList=new Array;
<%
for(SdkSpTroneModel spTroneModel : spTroneList)
{
	%>
	spTroneList.push(new selObj(<%= spTroneModel.getSpTroneId() %>,'<%=spTroneModel.getName()%>','<%=spTroneModel.getName()%>'));
	<%
}
%>
<!--APP-->
var appArray=new Array;
<%
for(SdkAppModel appModel : appList)
{
	%>
	appArray.push(new selObj(<%= appModel.getSdkAppId() %>,'<%=appModel.getName()%>','<%=appModel.getName()%>'));
	<%
}
%>
<!--渠道-->
var channelArray=new Array;
<%
for( SdkChannelModel channelModel : channelList)
{
	%>
	channelArray.push(new selObj(<%= channelModel.getSdkChannelId() %>,'<%=channelModel.getChannelName()%>','<%=channelModel.getChannelName()%>'));
	<%
}
%>

$(function()
		{
			$("#startdate").val(<%=startDate%>);
			$("#enddate").val(<%=endDate%>);
			$("#sp_trone_id").val(<%=spTroneId%>);
			$("#sp_trone_id").change(spTroneChange);
			spTroneChange();
			$("#trone_id").val(<%=troneId%>);
			$("#cp_id").val(<%=cpId%>);
			$("#app_id").val(<%=appId%>);
			$("#sdk_channel_id").val(<%=channelId%>);
			$("#showType").val(<%=showType%>);
			$("#province_id").val(<%=provinceId%>);

		
		});

function onSpTroneDataSelect(joData)
{
	$("#sp_trone_id").val(joData.id);
	spTroneChange();
}
<!--APP-->
function onAppDataSelect(joData)
{
	$("#app_id").val(joData.id);
}
<!--渠道-->
function onChannelDataSelect(joData)
{
	$("#sdk_channel_id").val(joData.id);
}
function selObj(id,name,text)
{
	var obj = {};
	obj.id = id;
	obj.value = name;
	obj.text = text;
	obj.pyText = pinyin.getCamelChars(text);
	return obj;
}

function spTroneChange()
{
	var spTroneId = $("#sp_trone_id").val();
	$("#trone_id").empty(); 
	$("#trone_id").append("<option value='-1'>请选择</option>");
	for(i=0; i<troneArray.length; i++)
	{
		if(troneArray[i].spTroneId==spTroneId)
		{
			$("#trone_id").append("<option value='" + troneArray[i].id + "'>" + troneArray[i].name + "</option>");
		}
	}
}

</script>
<body>
	<div class="main_content">
		<div class="content">
			<form action="sdkdata.jsp" method="get" style="margin-top: 10px">
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
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone_id" id="sp_trone_id" style="width: 110px;"
							title="选择SP业务" onclick="namePicker(this,spTroneList,onSpTroneDataSelect)">
							<option value="-1">全部</option>
							<%
								for (SdkSpTroneModel spTrone : spTroneList) {
							%>
							<option value="<%=spTrone.getSpTroneId()%>"><%=spTrone.getName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					<dd class="dd01_me">SP通道</dd>
					<dd class="dd04_me">
						<select name="trone_id" id="trone_id" style="width: 110px;">
						
						</select>
						<dd class="dd01_me">省份</dd>
					<dd class="dd04_me">
						<select name="province_id" id="province_id" style="width: 110px;"
							title="选择省份" >
							<option value="-1">全部</option>
							<%
								for (ProvinceModel provinceModel : provinceList) {
							%>
							<option value="<%=provinceModel.getId()%>"><%=provinceModel.getName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					</dd><div style="clear: both;"><br /><div/>
					<dd class="dd01_me">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="cp_id" style="width: 110px;">
						<option value="-1">全部</option>
							<%
								for (SdkCpModel cpModel : cpList) {
							%>
							<option value="<%=cpModel.getCpId()%>"><%=cpModel.getName()+"-"+cpModel.getNickName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					<dd class="dd01_me">APP</dd>
					<dd class="dd04_me">
						<select name="app_id" id="app_id" style="width: 110px;" onclick="namePicker(this,appArray,onAppDataSelect)">
						<option value="-1">全部</option>
							<%
								for (SdkAppModel appModel : appList) {
							%>
							<option value="<%=appModel.getSdkAppId()%>"><%=appModel.getName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					
					<dd class="dd01_me">渠道</dd>
					<dd class="dd04_me">
						<select name="sdk_channel_id" id="sdk_channel_id" title="请选择渠道" onclick="namePicker(this,channelArray,onChannelDataSelect)"
							style="width: 110px;" >
						<option value="-1">全部</option>
							<%
								for (SdkChannelModel channelModel : channelList) {
							%>
							<option value="<%=channelModel.getSdkChannelId()%>"><%=channelModel.getChannelName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					
					<dd class="dd01_me">展示方式</dd>
					<dd class="dd04_me">
						<select name="showType" id="showType" title="展示方式"
							style="width: 110px;">
						<option value="1">日期</option>
						<option value="2">周数</option>
						<option value="3">月份</option>
						<option value="4">CP</option>
						<option value="5">APP</option>
						<option value="6">渠道</option>
						<option value="7">SP业务</option>
						<option value="8">通道</option>
						<option value="9">省份</option>
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
					<td onclick="TableSorter('table_id',1,'date')"><%=titles[showType - 1]%></td>
					<td onclick="TableSorter('table_id',2,'float')">激活用户（个）</td>
					<td onclick="TableSorter('table_id',3,'float')">活跃用户(个)</td>
					<td onclick="TableSorter('table_id',4,'float')">通道请求(条)</td>
					<td onclick="TableSorter('table_id',5,'float')">有效通道请求(条)</td>
					<td onclick="TableSorter('table_id',6,'float')">理论计费金额(元)</td>
					<td onclick="TableSorter('table_id',7,'float')">指令成功(条 )</td>
					<td onclick="TableSorter('table_id',8,'float')">短信成功(条)</td>
					<td onclick="TableSorter('table_id',9,'float')">计费成功(条)</td>
					<td onclick="TableSorter('table_id',10,'float')">计费金额(元)</td>
					<td onclick="TableSorter('table_id',11,'String')">转化率</td>
				</tr>
			</thead>
			<tbody>
				<%
					int index = 1;
					for (SdkDataSummerModel model : dataSummerList) {
				%>
				<tr>
					<td><%=index++%></td>
					<td><%=model.getTitle()%></td>
					<td><%=model.getUserRows()%></td>
					<td><%=model.getActivityRows()%></td>
					<td><%=model.getTroneRequestRows()%></td>
					<td><%=model.getTroneEffectRequestRows()%></td>
					<td><%=StringUtil.getDecimalFormat(model.getEffectAmount())%></td>
					<td><%=model.getTroneOrderRows()%></td>
					<td><%=model.getMsgRows()%></td>
					<td><%=model.getSucRows()%></td>
					<td><%=StringUtil.getDecimalFormat(model.getAmount())%></td>
					<td><%=StringUtil.getPercent(model.getAmount(),model.getEffectAmount())%></td>
				</tr>
				<%
					}
				%>
				</tbody>
						<tr>
					<td></td>
					<td></td>
					<td>激活用户：<%=countUserRows%></td>
					<td>活跃用户：<%=countActRows%></td>
					<td>通道请求：<%=countTroReqRows%></td>
					<td>有效通道请求：<%=countEffReqRows%></td>
					<td>理论金额：<%=StringUtil.getDecimalFormat(countEffAmount)%></td>
					<td>指令成功：<%=countTroOrdRows%></td>
					<td>短信成功：<%=countMsgRows%></td>
					<td>计费成功：<%=countSucRows%></td>
					<td>计费金额：<%=StringUtil.getDecimalFormat(countAmount)%></td>
				</tr>
			</tbody>
				<tbody>
	
			
		</table>
	</div>

</body>
</html>