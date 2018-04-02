<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.CommRightServer"%>
<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
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

	boolean isUpdateStatus = StringUtil.getInteger(request.getParameter("udpate_status"), -1) == 1 ? true : false;

	boolean isUpdateWatchData = StringUtil.getInteger(request.getParameter("udpate_watch_data"), -1) == 1 ? true : false;
	
	if(isUpdateStatus)
	{
		int id = StringUtil.getInteger(request.getParameter("id"), -1);
		int status = StringUtil.getInteger(request.getParameter("status"), -1);
		
		if(id<0)
			return;
		
		new SpTroneServer().updateSpTroneStatus(id, status);
		
		out.print(id + "," + status + ",OK");
		
		return;
	}
	
	if(isUpdateWatchData)
	{
		int id = StringUtil.getInteger(request.getParameter("id"), -1);
		
		int isWatchData = StringUtil.getInteger(request.getParameter("is_watch_data"), 0);
		
		if(id<0)
			return;
		
		new SpTroneServer().updateSpTroneAlarm(id, isWatchData);
		
		out.print(id + "," + isWatchData + ",OK");
		
		return;
	}

	boolean isUpdatePro = StringUtil.getInteger(request.getParameter("update_pro"), -1) == 1 ? true : false;

	if(isUpdatePro)
	{
		int id = StringUtil.getInteger(request.getParameter("id"), -1);
		String pros = StringUtil.getString(request.getParameter("pros"), "");
		if(id<=0 || StringUtil.isNullOrEmpty(pros))
			return;
		
		new SpTroneServer().updateSpTroneProvince(id, pros);
		
		out.print("OK");
		return;
	}

	
	int userId = ((UserModel)session.getAttribute("user")).getId();

	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());
	
	//查询SP权限，0表示SP商务
	String userRightList = new CommRightServer().getRightListByUserId(userId, 0);
	
	if(userRightList == null || "".equals(userRightList))
	{
		userRightList = userId + "";
	}
	
	Map<String, Object> map = new SpTroneServer().loadSpTroneList(pageIndex,keyWord,userRightList);
	
	List<SpModel> spList = new SpServer().loadSp();

	List<SpTroneModel> list = (List<SpTroneModel>) map.get("list");

	int rowCount = (Integer) map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("user_sptrone.jsp", params, rowCount, pageIndex);
	
	String[] troneTypes = {"实时","隔天","IVR","第三方支付"};
	
	//0对公周结；1对公双周结；2对公月结；3对私周结；4对私双周结；5对私月结,6见帐单结,7对公N+1结,8,"对公N+3结"
	String[] jsTypes = {"对公周结","对公双周结","对公N+1结","对私周结","对私双周结","对私月结","见帐单结","对公N+2结","对公N+3结","对公N+5结","对公N+5结","对公N+6结"};
	
	String jiuSuanName = ConfigManager.getConfigData("JIE_SUNA_NAME", "结算率");
	
	List<JsTypeModel> jsTypeList = new JsTypeServer().loadJsType();
	
	List<ProvinceModel> provinceList = new ProvinceServer().loadProvince();
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">


<style type="text/css">

#zz_back 
{
    width:100%;
    height:100%;
    background-color:#000;
    position:absolute;
    top:0;
    left:0;
    z-index:2;
    opacity:0.3;
    /*兼容IE8及以下版本浏览器*/
    filter: alpha(opacity=30);
    display:none;
}

#log_window 
{
    width:585px;
    height:210px;
    margin: auto;
    position: absolute;
    z-index:3;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    border:1px solid black;
    background-color:white;
    display:none;
}

</style>

<script type="text/javascript">

var updateProId = 0;

function editProvince(id)
{
	updateProId = id;
	
	if(isNullOrEmpty(id))
	{
		return;	
	}
	
	var hidPro = document.getElementById("hidden_pro_id_" + updateProId);
	
	if(isNullOrEmpty(hidPro))
	{
		return;	 
	}
	
	var pros = hidPro.value;
	
    var s = document.getElementById("zz_back");
    
    s.style.display = "block";
    
    var l = document.getElementById("log_window");
    l.style.display = "block";
    
    unAllCkb('pro_chk_name');
    
    var porsArray = pros.split(",");
    
    for(var i=0; i<porsArray.length; i++)
    {
    	document.getElementById("pro_chk_id_" + porsArray[i]).checked = true;	
    }
    
}

function cancelEdiProvince()
{
    var s = document.getElementById("zz_back");
    s.style.display = "none";
    
    var l = document.getElementById("log_window");
    l.style.display = "none";
}


function updateProShow()
{
	var proIdArr = new Array();
	
	$('[name=pro_chk_name]:checkbox').each(function() {
		if(this.checked)
		{
			proIdArr.push(this.value);
		}
	});
	
	if(proIdArr.length <=0)
	{
		alert("请选择开通省份");	
		return;
	}
	
	var proIds = "";
	var proNames = "";
	
	for(var i=0; i<proIdArr.length; i++)
	{
		proIds += proIdArr[i] + ",";
		proNames += document.getElementById("pro_span_id_" + proIdArr[i]).innerHTML + ",";
	}
	
	proIds = proIds.substring(0,proIds.length-1);
	proNames = proNames.substring(0,proNames.length-1);
	
	if(document.getElementById("hidden_pro_id_" + updateProId).value==proIds)
	{
		cancelEdiProvince();
		return;
	}
	
	document.getElementById("hidden_pro_id_" + updateProId).value = proIds;
	document.getElementById("span_pro_id_" + updateProId).innerHTML = proNames;
	
	getAjaxValue("user_sptrone.jsp?update_pro=1&id=" + updateProId + "&pros=" + proIds,onSuccess);
	
	cancelEdiProvince();
}

function onSuccess(data)
{
	console.log(data);
}

function importProvince()
{
	var tmpPro = prompt("请输入省份", "");
	
	if ( tmpPro == null || "" == tmpPro )
		return;

	$('[name=pro_chk_name]:checkbox').each(function() 
	{
		if(tmpPro.indexOf(this.title) != -1)
		{
			this.checked = true;
			tmpPro = tmpPro.replace(this.title, "");
		}
		else
		{
			this.checked = false;
		}
	});
	
	if(tmpPro!="")
		alert("未匹配的省份有:" + tmpPro);
}

</script>

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
	
	function onDataSelect(joData) 
	{
		$("#sel_sp").val(joData.id);
	}

	function delSpTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "sptroneaction.jsp?did=" + id;	
		}
	}
	
	$(function()
	{
		
	});
	
	
	function editShowData(editId)
	{
		var curShowRows = $("#hid_" + editId).val();
		
		var newHtml = "<input type='text' id='myput_" + editId + "' style='width:30px;background-color:#CDC5BF;text-align:center;' value='"+ curShowRows +"' />";
		
		newHtml += "<input type='button' value='更新' style='margin-left: 10px' onclick='updateShowData(" + editId + ")'/>";
		 
		newHtml += "<input type='button' value='取消' style='margin-left: 10px' onclick='cancelShowData(" + editId + ")'/>";
		
		$("#span_" + editId).html(newHtml);
	}
	
	function updateShowData(editId)
	{
		var newShowRows = parseFloat($("#myput_" + editId).val());
		
		if(isNaN(newShowRows) || newShowRows>=1 || newShowRows<=0)
		{
			alert("请输入介于0和1之间的数据");
			return;
		}
		
		updateDbData(editId,newShowRows);
	}
	
	function updateDbData(editId,newShowRows)
	{
		$.post("sptroneaction.jsp", 
		{
			type : 1,
			jiesuanlv : newShowRows,
			id :editId 
		}, 
		function(data) 
		{
			data = $.trim(data);
			if ("OK" == data) 
			{
				$("#hid_" + editId).val(newShowRows);		
				$("#span_" + editId).html(newShowRows);
			} 
			else 
			{
				alert("修改失败！请联系管理员");
				$("#span_" + editId).html($("#hid_" + editId).val());
			}
		});
	}
	
	function cancelShowData(editId)
	{
		$("#span_" + editId).html($("#hid_" + editId).val());
	}
	
	function reverseProCheck(chkId)
	{
		document.getElementById("pro_chk_id_" + chkId).checked = !document.getElementById("pro_chk_id_" + chkId).checked;
	}
	
	function allCkb(items) 
	{
		$('[name=' + items + ']:checkbox').attr("checked", true);
	}

	function unAllCkb(items) 
	{
		$('[name=' + items + ']:checkbox').attr("checked", false);
	}

	function inverseCkb(items) 
	{
		$('[name=' + items + ']:checkbox').each(function() {
			this.checked = !this.checked;
		});
	}
	
	//ID,status,OK 第一个是ID 第二个执行后的状态 第三个是状态 OK是成功其它为否
	function onStatusChangeSuc(data)
	{
		console.log("data:" + data);
		var result = data.split(",");
		
		if(result.length==3)
		{
			if(result[2]=="OK")
			{
				document.getElementById("hid_status_" + result[0]).value = result[1];
				$("#td_status_" + result[0]).html(result[1]==1 ? "开启":"关闭");
				$("#tr_row_" + result[0]).attr("class",(result[1]==1 ? "":"StopStyle"));
			}
		}
	}
	
	function changeSpTroneStatus(spTroneId)
	{
		var status = document.getElementById("hid_status_" + spTroneId).value;
		
		if(status==0)
			status=1
		else
			status=0;
		
		getAjaxValue("user_sptrone.jsp?udpate_status=1&id=" + spTroneId + "&status=" + status,onStatusChangeSuc);
	}
	
	function changeSpTroneWatchData(spTroneId)
	{
		var isWatchData = document.getElementById("hid_is_watch_data_" + spTroneId).value;
		
		if(isWatchData==0)
			isWatchData=1
		else
			isWatchData=0;
		
		getAjaxValue("user_sptrone.jsp?udpate_watch_data=1&id=" + spTroneId + "&is_watch_data=" + isWatchData,onTroneWatchChangeSuc);
	}
	
	function onTroneWatchChangeSuc(data)
	{
		console.log("data:" + data);
		
		var result = data.split(",");
		
		if(result.length==3)
		{
			if(result[2]=="OK")
			{
				document.getElementById("hid_is_watch_data_" + result[0]).value = result[1];
				$("#td_is_watch_data_" + result[0]).html(result[1]==1 ? "是":"否");
			}
		}
	}
	
</script>


<body>
	
	<div class="main_content">
	
	
	<div>
			<div id="zz_back"></div>
			<div id="log_window">
				<div style="width:500px;height:100%;float: left;">
					<%
						for (ProvinceModel province : provinceList)
						{
					%>
						<div onclick="reverseProCheck(<%= province.getId() %>)" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#BFBFBF;margin-top: 4px;line-height: 30px;cursor:pointer;"  >
							<span style="font-size: 16px;" id="pro_span_id_<%= province.getId() %>"><%= province.getName() %></span>
							<input onclick="reverseProCheck(<%= province.getId() %>)" id="pro_chk_id_<%= province.getId() %>" type="checkbox" name="pro_chk_name" value="<%= province.getId() %>" title="<%= province.getName() %>"  />
						</div>
					<%
						}
					%>
					
				</div>
				<div style="width:85px;height:100%;float: left;text-align: center;vertical-align: middle;">
				
					<div onclick="allCkb('pro_chk_name')" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#A1A1A1;margin-top: 4px;line-height: 30px;" >
							<span style="font-size: 16px;cursor: pointer;font-weight: bold;"><%= "全&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选" %></span>
					</div>
					<div onclick="unAllCkb('pro_chk_name')" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#A1A1A1;margin-top: 4px;line-height: 30px;" >
							<span style="font-size: 16px;cursor: pointer;font-weight: bold;"><%= "全&nbsp;不&nbsp;选" %></span>
					</div>
					<div onclick="inverseCkb('pro_chk_name')" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#A1A1A1;margin-top: 4px;line-height: 30px;" >
							<span style="font-size: 16px;cursor: pointer;font-weight: bold;"><%= "反&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选" %></span>
					</div>
					<div onclick="importProvince()" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#A1A1A1;margin-top: 4px;line-height: 30px;" >
							<span style="font-size: 16px;cursor: pointer;font-weight: bold;"><%= "导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入" %></span>
					</div>
					<div onclick="updateProShow()" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#A1A1A1;margin-top: 4px;line-height: 30px;" >
							<span style="font-size: 16px;cursor: pointer;font-weight: bold;"><%= "修&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;改" %></span>
					</div>
					<div onclick="cancelEdiProvince()" style="float: left;margin-left: 3px;height: 30px;width:80px;float: left;background-color:#A1A1A1;margin-top: 4px;line-height: 30px;" >
							<span style="font-size: 16px;cursor: pointer;font-weight: bold;"><%= "取&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;消" %></span>
					</div>
					
				</div>
			</div>
	</div>
		<div class="content">
			<form action="user_sptrone.jsp" method="get" id="formid">
			<dl>
				<dd class="dd01_me" style="margin-left: -15px">关键字</dd>
				<dd class="dd03_me">
					<input name="keyword" id="input_keyword" value="<%= keyWord %>"
						type="text" style="width: 150px">
				</dd>
				<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
					<input class="btn_match" name="search" value="查 询" type="submit">
				</dd>
			</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SP名称</td>
					<td>业务线</td>
					<td>业务名称</td>
					<td>商务人员</td>
					<td>数据类型</td>
					<td>结算类型</td>
					<td>监控</td>
					<td><%= jiuSuanName %></td>
					<td style="max-width: 400px">省份</td>
					<td>状态</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (SpTroneModel model : list)
					{
				%>
				<tr <%= model.getStatus()==0 ? stopStyle : "" %> id="tr_row_<%= model.getId() %>">
					<td><%=(pageIndex - 1) * Constant.PAGE_SIZE + rowNum++%>
						<input type="hidden" id="hid_<%= model.getId() %>" value="<%= model.getJieSuanLv() %>" />
						<input type="hidden" id="hid_status_<%= model.getId() %>" value="<%= model.getStatus() %>" />
						<input type="hidden" id="hid_is_watch_data_<%= model.getId() %>" value="<%= model.getIsWatchData() %>" />
					</td>
					<td><%=model.getSpName()%></td>
					<td><%=model.getServoceCodeName() %></td>
					<td><%=model.getSpTroneName()%></td>
					<td><%= model.getCommerceUserName() %></td>
					<td><%= troneTypes[model.getTroneType()]%></td>
					<td><%= jsTypes[model.getJsTypes()] %></td>
					<td id="td_is_watch_data_<%= model.getId() %>" onclick="changeSpTroneWatchData(<%= model.getId() %>)" ><%= model.getIsWatchData()==1 ? "是" : "否" %></td>
					<td><span id="span_<%= model.getId() %>"><%= model.getJieSuanLv() %></span>
					</td>
					<td style="max-width: 400px"  ondblclick="editProvince('<%= model.getId() %>')">
						<input type="hidden" id="hidden_pro_id_<%= model.getId() %>" value="<%= model.getProvinces() %>" />
						<span id="span_pro_id_<%= model.getId() %>"><%= model.getProvinceList() %></span> 
						</td>
					<td id="td_status_<%= model.getId() %>" ondblclick="changeSpTroneStatus(<%= model.getId() %>)" ><%= model.getStatus()==1 ? "开启" : "关闭" %></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td colspan="14" class="tfooter" style="text-align: center;"><%=pageData%></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>