<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="com.system.model.CpSpTroneRateModel"%>
<%@page import="com.system.server.CpSpTroneRateServer"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String query = StringUtil.getString(request.getParameter("query"), "");
	CpSpTroneRateModel model =new CpSpTroneRateServer().loadCpSpTroneRateById(id);
	if(model==null)
	{
		response.sendRedirect("<script>history.go(-1)</script>");
		return;
	}
	
	List<ProvinceModel> proList = new ProvinceServer().loadProvince();
	
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
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	
	$(function()
	{
		resetForm();
	});
	
	function resetForm()
	{
		$("#input_rate").val("<%= model.getRate() %>");
		$("#input_day_limit").val("<%= model.getDayLimit() %>");
		$("#input_month_limit").val("<%= model.getMonthLimit() %>");
		$("#sel_js_type").val("<%= model.getJsType() %>");
		initData();
	}
	
	function subForm() 
	{
		var limit = parseFloat($("#input_day_limit").val());
		if (isNaN(limit) || limit < 0)
		{
			alert("请输入正确的日限");
			$("#input_day_limit").focus();
			return;
		}
		
		limit = parseFloat($("#input_month_limit").val());
		if (isNaN(limit) || limit < 0)
		{
			alert("请输入正确的月限");
			$("#input_month_limit").focus();
			return;
		}
		
		limit = parseFloat($("#input_rate").val());
		
		if (isNaN(limit) || limit<0 || limit>=10)
		{
			alert("请输入正确的结算率");
			$("#input_rate").focus();
			return;
		}
		
		if(calculateProsData()!=true)
			return;
		
		document.getElementById("addform").submit();
	}
	
</script>

<script type="text/javascript">

	function joSelOption(id,name)
	{
		var obj = {};
		obj.id = id;
		obj.name = name;
		return obj;
	}
	
	//数据库已存在的数据
	var allProsData = "<%= model.getProsData() %>";
	
	//所有省份(数据结构)
	var allProsArr = new Array();
	
	var curRowSelectIndex = -1;
	
	<% 
		for(ProvinceModel proModel :proList)
		{
			out.println("allProsArr.push(new joSelOption(" + proModel.getId() + ",\"" + proModel.getName() + "\"));");
		}
	%>
	
	//未选择的省份
	var unCheckallProsArr = new Array();
	
	//已选择的省份
	var checkallProsArr = new Array();
	
	//初始化数据库数据
	function initData()
	{
		if(allProsData=="")
		{
			//unCheckallProsArr = allProsArr;
			
			for(var i=0; i<allProsArr.length; i++)
			{
				unCheckallProsArr.push(allProsArr[i]);	
			}
			
			return;
		}
			
		
		//分割每一组数据
		var prosData = allProsData.split(";");
		
		for(var i=0; i<prosData.length; i++)
		{
			var prosAndData = prosData[i].split("=");
			var prosArr = prosAndData[0].split(",");
			
			//把分离出来的数据增加进TABLE里去
			addCheckRowToTable(prosArr,prosAndData[1]);
			
			//把存在的省份数据PUSH到已选择的省份数组里去
			for(var j=0; j<prosArr.length; j++)
			{
					for(var k=0; k<allProsArr.length; k++)
					{
						if(allProsArr[k].id == prosArr[j])
						{
							checkallProsArr.push(allProsArr[k]);
							break;	
						}
					}
			}
		}
		
		//把不存在的省份数据PUSH到未选择的省份数组里去
		for(var i=0; i<allProsArr.length; i++)
		{
			var isExist = false;
			for(var j=0; j<checkallProsArr.length; j++)
			{
				if(allProsArr[i].id == checkallProsArr[j].id)
				{
					isExist = true;
					break;
				}
			}
			if(!isExist)
			{
				unCheckallProsArr.push(allProsArr[i]);	
			}
		}
	}
	
	//增加一行数据
	function addCheckRowToTable(prosArr,data)
	{
		var prosHtml = "";
		
		for(var i=0; i<prosArr.length; i++)
		{
			for(var j=0; j<allProsArr.length; j++)
			{
				if(prosArr[i]==allProsArr[j].id)
				{
					prosHtml += "<div class='pro_div' id='" + allProsArr[j].id + "' onclick='delProFromTrTd(this)'><span class='pro_span'>" + allProsArr[j].name + "</span><img class='pro_img' src='../images/close.png' /></div>";	
					break;
				}
			}
		}
		
		$("#table_data").append("<tr><td>" + prosHtml +  "</td><td><input style='width:30px' type='text' value='" + data + "' /></td><td><input type='button' onclick='addProToRowTd(this)' value='增加' /><input type='button' onclick='delTableRow(this)' value='删除' /></td></tr>");
	}
	
	//把省份从对应的数据中删除
	function delProFromTrTd(object)
	{
		addProToUnCheckById(object.id);
		object.parentNode.removeChild(object);
	}
	
	//从未选择的省份删除已选择的省份，再把已选择省份增加进已选择省份的列表
	function addProsToCheckFromUnCheck(prosArrStr)
	{
		var prosArr = prosArrStr.split(",");
		
		for(var i=0; i< prosArr.length; i++)
		{
			var j=0;
			for(;j<unCheckallProsArr.length; j++)
			{
				if(prosArr[i]==unCheckallProsArr[j].id)	
				{
					checkallProsArr.push(unCheckallProsArr[j]);
					break;	
				}
			}
			unCheckallProsArr.splice(j, 1);
		}
	}
	
	//点击省份删除时把省份对象增加未选择列表，把省份从已选择列表删除
	function addProToUnCheckById(ids)
	{
		var idArr = ids.split(",");
		
		for(var k=0; k<idArr.length; k++)
		{
			var i=0;
	    	for(; i<checkallProsArr.length; i++)
	    	{
	    		if(checkallProsArr[i].id==idArr[k])
	    		{
	    			unCheckallProsArr.push(checkallProsArr[i]);
	    			break;
	    		}
	    	}
	    	checkallProsArr.splice(i,1);
		}
	}
	
	//把选择的省份增加进 TABLE TR TD
	function addProsTrTd(prosArrStr)
	{
		var prosArr = prosArrStr.split(",");
		
		for(var i=0; i<prosArr.length; i++)
		{
			for(var j=0; j<allProsArr.length; j++)
			{
				if(allProsArr[j].id == prosArr[i])
				{
					var proHtml = "<div class='pro_div' id='" + allProsArr[j].id + "' onclick='delProFromTrTd(this)'><span class='pro_span'>" + allProsArr[j].name + "</span><img class='pro_img' src='../images/close.png' /></div>";
					
					$("#table_data tr:gt(0):eq(" + (curRowSelectIndex - 1) + ") td:eq(0)").append(proHtml);
					
					break;
				}
			}
		}
	}
	
	//确定选择的省份
	function confirmProsCheck()
	{
		var div_body = document.getElementById("div-pop-box-body");
		var checkboxs = div_body.getElementsByTagName("input");
		 
		var ids = "";
		 
		for(var i=0; i<checkboxs.length; i++)
		{
			if(checkboxs[i].checked == true)
			{
				ids += checkboxs[i].id + ",";
			}
		}
		 
		if(ids!="")
		{
			ids = ids.substring(0, ids.length-1);	 
		}
		 
		hideDiv("pop-div");
		
		addProsToCheckFromUnCheck(ids);
		
		addProsTrTd(ids);
	}
	
	//增加省份到当前选择
	function addProToRowTd(object)
	{
		if(unCheckallProsArr.length<=0)
		{
			alert("没有未分配的省份");
			return;
		}
		
		var tr = object.parentNode.parentNode;
		
		var trs = tr.parentNode.getElementsByTagName("tr");
		
		var i=0;
		
		for(; i<trs.length; i++)
		{
			if(trs[i]==tr)
			{
				break;
			}
		}
		
		if(i<=0)
			return;
		
		curRowSelectIndex = i;
		
		//当前行第一列的HTML
		//console.log($("#table_data tr:gt(0):eq(" + (i-1) + ") td:eq(0)").html());
		
		popupDiv("pop-div");
	}
	
	//弹出省份遮罩层
	function popupDiv(div_id) 
	{
	    var div_obj = $("#"+div_id);
	    var windowWidth = document.documentElement.clientWidth;    
	    var windowHeight = document.documentElement.clientHeight;    
	    var popupHeight = div_obj.height();    
	    var popupWidth = div_obj.width(); 
	    
	    var div_body = document.getElementById("div-pop-box-body");
	    
	    var checkHtml = "";
	    
	    for(var i=0; i<unCheckallProsArr.length; i++)
	    {
	    	checkHtml += "<div class='pro_div' ><label class='pro_span' style='display:block;' >" + unCheckallProsArr[i].name + "<input type='checkbox' id='" + unCheckallProsArr[i].id + "' /></label></div>";
	    	
	    	//checkHtml += "<input type='checkbox' id='" + unCheckallProsArr[i].id + "' />" + unCheckallProsArr[i].name;	
	    	
	    	
	    	if((i+1)%5==0)
	    	{
	    		checkHtml += " <br />";	
	    	}
	    }
	    
	    div_body.innerHTML = checkHtml;
	    
	    //添加并显示遮罩层
	    $("<div id='mask'></div>").addClass("mask")
	                              .width(windowWidth * 0.99)
	                              .height(windowHeight * 0.99)
	                              .click(function() {hideDiv(div_id); })
	                              .appendTo("body")
	                              .fadeIn(200);
	    
	    div_obj.css({"position": "absolute"})
	           .animate({left: windowWidth/2-popupWidth/2, 
	                     top: windowHeight/2-popupHeight/2, opacity: "show" }, "fast");
	    
	    
	    
	}
	
	//隐藏省份遮罩层
	function hideDiv(div_id)
	{
	    $("#mask").remove();
	    $("#" + div_id).animate({left: 0, top: 0, opacity: "hide" }, "fast");
	}
	
	//删除一整行
	function delTableRow(object)
	{
		if(!confirm("确定要删除这一行数据？"))
			return;	
		
		var tr = object.parentNode.parentNode;
		
		var divs = tr.getElementsByTagName("div");
		
		var ids = "";
		
		for(var i=0; i<divs.length; i++)
		{
			if(divs[i].id != "")
				ids += divs[i].id + ",";
		}
		
		if(ids!="")
		{
			ids = ids.substring(0, ids.length -1);
		}
		
		addProToUnCheckById(ids);
		
		tr.parentNode.removeChild(tr);
	}
	
	function addEmptyRowToTable()
	{
		$("#table_data").append("<tr><td></td><td><input style='width:30px' type='text' value='0.0' /></td><td><input type='button' onclick='addProToRowTd(this)' value='增加' /><input type='button' onclick='delTableRow(this)' value='删除' /></td></tr>");
	}
	
	function calculateProsData()
	{
		var table = document.getElementById("table_data");
		
		var trs = table.getElementsByTagName("tr");
		
		var curDataValue = "";
		
		for(var i=1; i<trs.length; i++)
		{
			var curRowData = "";
			var ids = "";
			var tds = trs[i].getElementsByTagName("td");
			var divs = tds[0].getElementsByTagName("div");
			if(divs!=null && divs.length>0)
			{
				for(var j=0; j<divs.length; j++)
	    		{
	    			ids += divs[j].id + ",";	
	    		}
				if(ids!="")
					ids = ids.substring(0, ids.length-1);
			}
			else
				continue;
			
			var dataInput = tds[1].getElementsByTagName("input")[0];
			
			var data = parseFloat(dataInput.value);
			
			if(isNaN(data)|| data<0 || data>1)
			{
				dataInput.focus();
				alert("请输入正确的扣量比");
				return false;	
			}
			
			curDataValue += (ids + "=" + dataInput.value + ";");
		}
		
		if(curDataValue != "")
			curDataValue = curDataValue.substring(0, curDataValue.length-1);
		
		document.getElementById("id_pros_data").value = curDataValue;
			
		return true;
	}

</script>

<style type="text/css">
	.null
	{
		border: 1px solid black;
	}
</style>
<style type="text/css">
	.pro_div
	{
		text-align: center;
		vertical-align:middle;
		background-color: #F7F7F7;
		width: 60px;
		height:22PX;
		cursor: pointer;
		border: solid 1px black;
		margin-left: 2px;
		margin-top:2px;
		float: left;
	}
	.pro_span
	{
		height: 100%;
		display: inline-block;
		vertical-align: middle;
		line-height:22px;
		text-align: center;
	}
	.pro_img
	{
		width: 18px;
		height: 18px;
		vertical-align: middle;
	}
.pop-box {
    z-index: 9999; /*这个数值要足够大，才能够显示在最上层*/
    margin-bottom: 3px;
    display: none;
    position: absolute;
    background: #FFF;
    border:solid 1px #6e8bde;
}

.pop-box h4 {
    color: black;
    cursor:default;
    height: 18px;
    font-size: 14px;
    font-weight:bold;
    text-align: left;
    padding-left: 2px;
    padding-top: 2px;
    padding-bottom: 4px;
    margin:0;
    background-color: #FFF5EE;
}

.pop-box-body {
    clear: both;
    margin: 4px;
    padding: 2px;
}

</style>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label >CP业务数据修改</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="rateaction.jsp?type=6&query=<%= query %>" method="post" id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
					<input type="hidden" value="<%= model.getProsData() %>" name="pros_data"  id="id_pros_data"/>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP业务</dd>
					<dd class="dd03_me">
						<input type="text" name="full_name" id="input_full_name" readonly="readonly" value="<%= model.getCpName() + "-" + model.getSpName() + "-" + model.getSpTroneName() %>"
							style="width: 200px;color: #ccc">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">日限</dd>
					<dd class="dd03_me">
						<input type="text" name="day_limit" value="0" id="input_day_limit"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">月限</dd>
					<dd class="dd03_me">
						<input type="text" name="month_limit" value="0" id="input_month_limit"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算率</dd>
					<dd class="dd03_me">
						<input type="text" name="rate" title="结算率" id="input_rate"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算方式</dd>
					<dd class="dd04_me">
						<select name="js_type" id="sel_js_type" title="结算类型" style="width: 200px" >
							<option value="-1">请选择结算类型</option>
							
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
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">省份扣量列表</dd>
					<dd>
						<table border="1" id="table_data" style="text-align: center;" class="null">
							<tr>
								<td width="400px" >省份</td>
								<td width="100px" >扣量比</td>
								<td width="100px">操作<input type="button" value="增加" onclick="addEmptyRowToTable()" /></td>
							</tr>
						</table>
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="重 置" onclick="resetForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>
				</form>
			</dl>
		</div>

	</div>
	<div id='pop-div' style="width: 300px" class="pop-box" >
    <h4>请选择省份</h4>
    <div class="pop-box-body" id="div-pop-box-body">
    	
    </div>
    <div class='buttonPanel' style="text-align: right" style="text-align: right">
    	<input value="确定" id="btn_confirm"  onclick="confirmProsCheck()"  type="button"  />
        <input value="取消" id="btn_close"  onclick="hideDiv('pop-div');"  type="button"  />
    </div>
    </div>
    
</body>
</html>