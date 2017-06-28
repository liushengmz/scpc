<%@page import="com.system.server.CommRightServer"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.model.UserModel"%>
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
	
	int userId = ((UserModel)session.getAttribute("user")).getId();
	
	String date = StringUtil.getString(request.getParameter("date"), StringUtil.getDefaultDate());
	int sortType = StringUtil.getInteger(request.getParameter("sort_type"), 5);
	
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone"), -1);
	int troneId = StringUtil.getInteger(request.getParameter("trone"), -1);
	int troneOrderId = StringUtil.getInteger(request.getParameter("trone_order"), -1);
	int provinceId = StringUtil.getInteger(request.getParameter("province"), -1);
	int cityId = StringUtil.getInteger(request.getParameter("city"), -1);
	int spCommerceUserId = StringUtil.getInteger(request.getParameter("commerce_user"), -1);
	int cpCommerceUserId = StringUtil.getInteger(request.getParameter("cp_commerce_user"), -1);
	
	//当前下游用户只能看得到当前自己的数据
	cpCommerceUserId = userId;
	//查询CP权限 1表示CP商务
	String cprigthList=new CommRightServer().getRightListByUserId(userId, 1);
	if(cprigthList==null||"".equals(cprigthList)){
		cprigthList=userId+"";
	}
	
	
	int spCommerceId = StringUtil.getInteger(ConfigManager.getConfigData("SP_COMMERCE_GROUP_ID"),-1);
	List<UserModel> userList = new UserServer().loadUserByGroupId(spCommerceId);
	int cpCommerceId = StringUtil.getInteger(ConfigManager.getConfigData("CP_COMMERCE_GROUP_ID"),-1);
	List<UserModel> cpCommerceUserList = new UserServer().loadUserByGroupId(cpCommerceId);

	Map<String, Object> map =  new MrServer().getMrTodayData(date,spId, spTroneId,troneId, cpId, troneOrderId, provinceId, cityId,spCommerceUserId+"",cprigthList,sortType);
	
	List<SpModel> spList = new ArrayList<SpModel>(); //new SpServer().loadSp();
	List<CpModel> cpList = new CpServer().loadCp();
	List<TroneModel> troneList = new ArrayList<TroneModel>(); //new TroneServer().loadTroneList();
	List<TroneOrderModel> troneOrderList = new ArrayList<TroneOrderModel>(); //new TroneOrderServer().loadTroneOrderList();
	
	List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>(); //new ProvinceServer().loadProvince();
	List<CityModel> cityList = new CityServer().loadCityList();
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
		
	List<MrReportModel> list = (List<MrReportModel>)map.get("list");
	
	int dataRows = (Integer)map.get("datarows");
	int showDataRows = (Integer)map.get("showdatarows");
	double amount = (Double)map.get("amount");
	double showAmount = (Double)map.get("showamount");
	
	String[] titles = {"日期","周数","月份","SP","CP","通道","CP业务","省份","城市","SP业务","小时","SP商务","CP商务"};
	
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
	<%
	for(SpModel spModel : spList)
	{
		%>
		spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
		<%
	}
	%>
	
	var cpList = new Array();
	<%
	for(CpModel cpModel : cpList)
	{
		%>
		cpList.push(new joSelOption(<%= cpModel.getId() %>,1,'<%= cpModel.getShortName() %>'));
		<%
	}
	%>
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp").val(joData.id);
		troneChange();
	}
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp").val(joData.id);
		
		if(joData.id==-1)
			$("#input_cp").val("");
		else
			$("#input_cp").val(joData.text);
		
		troneOrderChange();
		
	}

	function joCity(id,provinceId,name)
	{
		var obj = {};
		obj.id = id;
		obj.provinceId = provinceId;
		obj.name = name;
		return obj;
	}
	
	function joTrone(id,spId,troneName)
	{
		var obj = {};
		obj.id = id;
		obj.spId = spId;
		obj.troneName = troneName;
		return obj;
	}
	
	function joTroneOrder(id,cpId,troneOrderName)
	{
		var obj = {};
		obj.id = id;
		obj.cpId = cpId;
		obj.troneOrderName = troneOrderName;
		return obj;
	}
	
	var cityList = new Array();
	<%for(CityModel city : cityList){%>
	cityList.push(new joCity(<%= city.getId() %>,<%= city.getProvinceId() %>,'<%= city.getName() %>'));<%}%>
		
	var troneList = new Array();
	<%for(TroneModel trone : troneList){%>
	troneList.push(new joTrone(<%= trone.getId() %>,<%= trone.getSpId() %>,'<%= trone.getSpShortName() + "-" +trone.getTroneName() %>'));<%}%>
	
	var troneOrderList = new Array();
	<%for(TroneOrderModel troneOrder : troneOrderList){%>
	troneOrderList.push(new joTroneOrder(<%= troneOrder.getId() %>,<%= troneOrder.getCpId() %>,'<%= troneOrder.getCpShortName() + "-" +troneOrder.getOrderTroneName() %>'));<%}%>
	
	var spTroneArray = new Array();
	<%
	for(SpTroneModel spTroneModel : spTroneList)
	{
		%>
	spTroneArray.push(new joBaseObject(<%= spTroneModel.getId() %>,<%=spTroneModel.getSpId() %>,'<%= spTroneModel.getSpTroneName() %>'));	
		<%
	}
	%>
	
	var npSpTroneArray = new Array();
	
	<%
	for(SpTroneModel spTroneModel : spTroneList)
	{
		%>
		npSpTroneArray.push(new joSelOption(<%= spTroneModel.getId() %>,<%=spTroneModel.getSpId() %>,'<%= spTroneModel.getSpTroneName() %>'));	
		<%
	}
	%>
	
	function npSpTroneChange(jodata)
	{
		$("#sel_sp_trone").val(jodata.id);
	}
	
	$(function()
	{
		$("#sel_sort_type").val(<%= sortType %>);
		
		//SP的二级联动
		$("#sel_sp").val(<%= spId %>);
		//$("#sel_sp").change(troneChange);
		//troneChange();
		$("#sel_sp_trone").val(<%= spTroneId %>);
		$("#sel_trone").val(<%= troneId %>);
		
		//CP的二级联动
		$("#sel_cp").val(<%= cpId %>);	
		
		<%
		if(cpId>0)
		{
			%>
		$("#input_cp").val($("#sel_cp").find("option:selected").text());
			<%
		}
		%>
		
		$("#sel_cp").change(troneOrderChange);
		troneOrderChange();
		$("#sel_trone_order").val(<%= troneOrderId %>);
		
		//省份的二级联动
		$("#sel_province").val(<%= provinceId %>);
		$("#sel_province").change(provinceChange);
		provinceChange();		
		$("#sel_city").val(<%= cityId %>);
		$("#sel_commerce_user").val(<%= spCommerceUserId %>);
		$("#sel_cp_commerce_user").val(<%= cpCommerceUserId %>);
	});
	
	function troneChange()
	{
		var spId = $("#sel_sp").val();
		
		$("#sel_sp_trone").empty(); 
		$("#sel_sp_trone").append("<option value='-1'>全部</option>");
		for(i=0; i<spTroneArray.length; i++)
		{
			if(spTroneArray[i].pid==spId || spId=="-1")
			{
				$("#sel_sp_trone").append("<option value='" + spTroneArray[i].id + "'>" + spTroneArray[i].name + "</option>");
			}
		}
		
		$("#sel_trone").empty(); 
		$("#sel_trone").append("<option value='-1'>全部</option>");
		for(i=0; i<troneList.length; i++)
		{
			if(troneList[i].spId==spId || spId=="-1")
			{
				$("#sel_trone").append("<option value='" + troneList[i].id + "'>" + troneList[i].troneName + "</option>");
			}
		}
	}
	
	function troneOrderChange()
	{
		//选择CP后把CP对应的SP TRONE ID 数据列出来，所以原来的TRONE ORDER ID 就不要了
		/*
		
		$("#sel_trone_order").empty(); 
		$("#sel_trone_order").append("<option value='-1'>全部</option>");
		for(i=0; i<troneOrderList.length; i++)
		{
			if(troneOrderList[i].cpId==cpId || cpId=="-1")
			{
				$("#sel_trone_order").append("<option value='" + troneOrderList[i].id + "'>" + troneOrderList[i].troneOrderName + "</option>");
			}
		}
		*/
		$("#sel_sp_trone").empty(); 
		var cpId = $("#sel_cp").val();
		getAjaxValue("../ajaction.jsp?type=5&cp_id=" + cpId,cpSpTroneChange);
	}
	
	function cpSpTroneChange(data)
	{
		if(isNullOrEmpty(data))
			return;
		
		var spTroneList = data.split(",");
		
		$("#sel_sp_trone").empty(); 
		$("#sel_sp_trone").append("<option value='-1'>全部</option>");
		for(var i=0; i<spTroneList.length; i++)
		{
			for(var j=0; j<spTroneArray.length; j++)
			{
				if(spTroneArray[j].id==spTroneList[i])
				{
					$("#sel_sp_trone").append("<option value='" + spTroneArray[j].id + "'>" + spTroneArray[j].name + "</option>");
				}
			}
		}
		<% if(spTroneId>0){ %> $("#sel_sp_trone").val(<%= spTroneId %>); <% } %>
		
	}
	
	function provinceChange()
	{
		var provinceId = $("#sel_province").val();
		$("#sel_city").empty(); 
		$("#sel_city").append("<option value='-1'>全部</option>");
		for(i=0; i<cityList.length; i++)
		{
			if(cityList[i].provinceId==provinceId || provinceId=="-1")
			{
				$("#sel_city").append("<option value='" + cityList[i].id + "'>" + cityList[i].name + "</option>");
			}
		}
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="mr2_daily_single_cp.jsp"  method="get">
				<dl>
					<dd class="dd01_me" onclick="openDetailData('aaa')">开始日期</dd>
					<dd class="dd03_me">
						<input name="date"  type="text" value="<%=date%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})" style="width: 100px;">
					<!--
					<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp" title="选择SP" onclick="namePicker(this,spList,onSpDataSelect)">
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
					
					<dd class="dd01_me">SP通道</dd>
						<dd class="dd04_me">
						<select name="trone" id="sel_trone" title="请选择通道"></select>
					</dd>
					-->
					<dd class="dd01_me">CP</dd>
					<dd class="dd03_me">
						<input  type="text" id="input_cp" onclick="namePicker(this,cpList,onCpDataSelect)" style="width: 100px;" readonly="readonly" >
						<select name="cp_id" id="sel_cp" title="选择CP" style="display: none;">
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
					<dd class="dd01_me">CP业务</dd>
						<dd class="dd04_me">
						<select name="sp_trone" id="sel_sp_trone" ></select>
					</dd>
					<!--
					<dd>
						<dd class="dd01_me">CP业务</dd>
						<dd class="dd04_me">
						<select name="trone_order" id="sel_trone_order" title="请选择业务"></select>
					</dd>
					-->
					<!-- 暂时先隐藏 -->
					<!-- 
					<dd>
						<dd class="dd01_me">省份</dd>
						<dd class="dd04_me">
						<select name="province" id="sel_province" title="请选择省份">
							<option value="-1">全部</option>
							<%
							for(ProvinceModel province : provinceList)
							{
								%>
							<option value="<%= province.getId() %>"><%= province.getName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd>
						<dd class="dd01_me">城市</dd>
						<dd class="dd04_me">
						<select name="city" id="sel_city" title="请选择城市">
							
						</select>
					</dd>
					-->
					<!--  
					<dd class="dd01_me">SP商务</dd>
						<dd class="dd04_me">
						<select name="commerce_user" id="sel_commerce_user" style="width: 100px;">
							<option value="-1">全部</option>
							<%
							for(UserModel commerceUser : userList)
							{
								%>
							<option value="<%= commerceUser.getId() %>"><%= commerceUser.getNickName() %></option>
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">CP商务</dd>
						<dd class="dd04_me">
						<select name="cp_commerce_user" id="sel_cp_commerce_user" style="width: 100px;">
							<option value="-1">全部</option>
							<%
							for(UserModel commerceUser : cpCommerceUserList)
							{
								%>
							<option value="<%= commerceUser.getId() %>"><%= commerceUser.getNickName() %></option>
								<%
							}
							%>
						</select>
					</dd>
					-->
					<dd class="dd01_me" style="font-weight: bold;font-size: 14px">展示方式</dd>
					<dd class="dd04_me">
						<select name="sort_type" id="sel_sort_type" title="展示方式">
							<option value="11">小时</option>
							<!--  
							<option value="4">SP</option>
							<option value="10">SP业务</option>
							<option value="6">SP通道</option>
							-->
							<option value="5">CP</option>
							<option value="7">CP业务</option>
							<!-- 暂时先隐藏 -->
							<!--
							<option value="2">周数</option>
							<option value="3">月份</option>
							-->
							<option value="8">省份</option>
							<option value="9">城市</option>
							<!--  
							<option value="12">SP商务</option>
							-->
							<option value="13">CP商务</option>
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
					<td onclick="TableSorter('table_id',1,'date')"><%= titles[sortType-1] %></td>
					<td onclick="TableSorter('table_id',2,'float')">推送量(条)</td>
					<td onclick="TableSorter('table_id',3,'float')">推送金额(元)</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(MrReportModel model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><a href="detail.jsp?date=<%= date %>&sp_id=<%= spId %>&cp_id=<%= cpId %>&sp_trone_id=<%= spTroneId %>&trone_id=<%= troneId %>&show_type=<%= sortType %>&joinid=<%= model.getJoinId() %>&title=<%= StringUtil.encodeUrl(model.getTitle1(),"UTF-8") %>" 
					target="_blank"><%= model.getTitle1() %></a></td>
					<td><%= model.getShowDataRows() %></td>
					<td><%= StringUtil.getDecimalFormat(model.getShowAmount()) %></td>
				</tr>
						<%
					}
				%>
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td>总推送量(条)：<%= showDataRows %></td>
					<td>总推送金额(元)：<%= StringUtil.getDecimalFormat(showAmount) %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>