<%@page import="com.system.flow.server.SpApiServer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.flow.model.SpApiModel"%>
<%@page import="com.system.flow.server.SpServer"%>
<%@page import="com.system.flow.model.SpModel"%>
<%@page import="com.system.flow.server.SpTroneServer"%>
<%@page import="com.system.flow.model.SpTroneModel"%>
<%@page import="com.system.flow.dao.TroneDao"%>
<%@page import="com.system.flow.model.TroneModel"%>
<%@page import="com.system.flow.server.BasePriceServer"%>
<%@page import="com.system.flow.model.BasePriceModel"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@page import="com.system.server.ProvinceServer"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int spTroneId = StringUtil.getInteger(request.getParameter("id"),0);

	String query = StringUtil.getString(request.getParameter("query"), "");
			

	SpTroneModel spTroneModel = new SpTroneServer().getSpTroneById(spTroneId);

	List<TroneModel> troneList = new TroneDao().loadTroneBySpTroneId(spTroneId);
	
	List<SpModel> spList = new SpServer().loadSp(-1);
	
	List<SpApiModel> spApiList = new SpApiServer().loadSpApiBySpId(spTroneModel.getSpId());
	
	List<BasePriceModel> basePricelist = new BasePriceServer().loadBasePrice(); 
	
	String troneIdArrStr = "";
	String proArrStr = "";
	String ratioValuesStr = "";
	String statusValuesStr = "";
	
	for(TroneModel model : troneList)
	{
		troneIdArrStr += model.getId() + ",";
		proArrStr += model.getProId() + ",";
		ratioValuesStr +=  model.getRatio() + ",";
		statusValuesStr += model.getStatus() + ",";
	}
	
	troneIdArrStr = troneIdArrStr.substring(0,troneIdArrStr.length()-1);
	proArrStr = proArrStr.substring(0,proArrStr.length()-1);
	ratioValuesStr = ratioValuesStr.substring(0,ratioValuesStr.length()-1);
	statusValuesStr = statusValuesStr.substring(0,statusValuesStr.length()-1);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../../sysjs/base.js"></script>
<script type="text/javascript" src="../../sysjs/AndyNamePickerV20.js"></script>
<link href="../../css/namepicker.css" rel="stylesheet" type="text/css">
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
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp_name").val(joData.text);
		$("#sel_sp_id").val(joData.id);
		
		$("#sel_sp_api_id").empty(); 
		$("#sel_sp_api_id").append("<option value='-1'>请选择对接文档</option>");
		
		if(joData.id<=0)
			return;
		
		postAjaxValue("action.jsp","type=3&sp_id=" + joData.id,onSpApiDataReturn);
	}
	
	function onSpApiDataReturn(data)
	{
		var spApiJson =JSON.parse(data);
		
		$.each(spApiJson, function(index, content)
		{ 
			$("#sel_sp_api_id").append("<option value='" + content.id + "'>" + content.name + "</option>");
		});
	}

	function allCkb(items) 	
	{
		$('[name=' + items + ']:checkbox').attr("checked", true);
		setDefaultColor();
	}
	
	function unAllCkb(items) 
	{
		$('[name=' + items + ']:checkbox').attr("checked", false);
		setDefaultColor();
	}
	
	function inverseCkb(items) 
	{
		$('[name=' + items + ']:checkbox').each(function() {
			this.checked = !this.checked;
		});
		setDefaultColor();
	}
	
	var troneIdArray = [<%= troneIdArrStr %>];
	var proArray = [<%= proArrStr %>];
	var ratioValuesArray = [<%= ratioValuesStr %>];
	var statusValuesArray = [<%= statusValuesStr %>];
	
	function setDefaultColor()
	{
		for(var i=0; i<proArray.length; i++)
		{
			var checked = document.getElementById('pro_check_' + proArray[i]).checked;
			
			if(checked)
			{
				$("#pro_td_" + proArray[i]).css("background","#66CD00");
			}
			else
			{
				$("#pro_td_" + proArray[i]).css("background","#fff");
			}
		}
	}
	
	function reverseCheckPro(proId)
	{
		var oCheckBox = document.getElementById('pro_check_' + proId);
		
		if(oCheckBox.checked)
		{
			oCheckBox.checked=false;
			$("#pro_td_" + proId).css("background","#fff");
        }
		else
		{
			oCheckBox.checked=true;
			$("#pro_td_" + proId).css("background","#66CD00");
        }
	}
	
	function importProvince()
	{
		var tmpPro = prompt("请输入省份", "");
		
		if ( tmpPro == null || "" == tmpPro )
			return;
	
		$('[name=pro_check]:checkbox').each(function() 
		{
			if(tmpPro.indexOf(this.title) != -1)
			{
				this.checked = true;
				tmpPro = tmpPro.replace(this.title, "");
			}
		});
		
		setDefaultColor();
		
		if(tmpPro!="")
			alert("未匹配数据:" + tmpPro);
	}
	
	function applyAllProvince()
	{
		var ratio = $("#input_ratio").val();
		
		for(var i=0; i<proArray.length; i++)
		{
			$("#pro_ratio_" + proArray[i]).val(ratio);
		}
	}
	
	function subForm() 
	{
		if ($("#sel_sp_id").val() == "-1") 
		{
			alert("请选择SP");
			$("#sel_sp").focus();
			return;
		}
		
		if ($("#sel_sp_api_id").val() == "-1")
		{
			alert("请选择对接文档");
			$("#sel_sp_api_id").focus();
			return;
		}
		
		if ($("#sel_base_price_id").val() == "-1") 
		{
			alert("请选择基础价格");
			$("#sel_base_price_id").focus();
			return;
		}
		
		if ($("#input_sp_trone_name").val() == "") 
		{
			alert("请输入业务名称");
			$("#input_sp_trone_name").focus();
			return;
		}
		
		var ratio = parseFloat($("#input_ratio").val());
		
		if(isNaN(ratio) || ratio>=1000 || ratio<=0)
		{
			alert("折扣只能介于1和1000之间");
			$("#input_ratio").focus();
			return;
		}
		
		if(getProvinceCount('pro_check')<=0)
		{
			alert("请选择省份");
			return;
		}
		
		fillProRatioCheckValue();

		document.getElementById("addform").submit();
	}
	
	function getProvinceCount(items)
	{
		var i = 0;
		$('[name=' + items + ']:checkbox').each(function() {
			if(this.checked)
				i++;
		});
		return i;
	}
	
	function fillProRatioCheckValue()
	{
		var value = "";
		for(var i=0; i<proArray.length; i++)
		{
			var checked = document.getElementById('pro_check_' + proArray[i]).checked;
			var ratio = document.getElementById('pro_ratio_' + proArray[i]).value;
			value += troneIdArray[i] + "|" + proArray[i] + "|" + ratio + "|" + checked + ","
		}
		value = value.substring(0,value.length -1);
		$("#hid_pro_ratio_check").val(value);
	}
	
	function changeSpTroneName()
	{
		$("#input_sp_trone_name").val($("#sel_base_price_id").find("option:selected").text());
	}
	
	function autoResetData(id,value)
	{
		var ratio = parseFloat(value);
		
		if(isNaN(ratio) || ratio>=1000 || ratio<=0)
		{
			$("#" + id).val(0);
			return;
		}
		
		$("#" + id).val(ratio);
	}
	
	function resetForm()
	{
		setRadioCheck("status",<%= spTroneModel.getStatus() %>);
		
		setRadioCheck("send_sms",<%= spTroneModel.getSendSms() %>);
		
		$("#sel_base_price_id").val("<%= spTroneModel.getPriceId() %>");
		
		$("#sel_sp_api_id").val("<%= spTroneModel.getSpApiId() %>");
		
		for(var i=0; i<proArray.length; i++)
		{
			$("#pro_ratio_" + proArray[i]).val(ratioValuesArray[i]);
			
			document.getElementById('pro_check_' + proArray[i]).checked = statusValuesArray[i]==1 ? true : false;
		}
		
		setDefaultColor();
	}
	
	$(function()
	{
		resetForm();
	})
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width: 200px">
				<label>修改业务</label>
				</dd>
			</dl>
			<br />	<br />
			<dl>
				<form action="action.jsp?query=<%= query %>" method="post" id="addform">
					<input type="hidden" name="type" id="hid_type" value="2" alt="修改">
					<input type="hidden" name="pro_ratio_check" id="hid_pro_ratio_check">
					<input type="hidden" name="sp_trone_id" id="hid_sp_trone_id" value="<%= spTroneId %>">
					<dd class="dd01_me">供应商名称</dd>
					<dd class="dd03_me">
						<input  type="text" id="sel_sp_name" value="<%= spTroneModel.getSpName() %>" onclick="namePicker(this,spList,onSpDataSelect)" style="width: 200px;" readonly="readonly" />
						<input  type="hidden" id="sel_sp_id" name="sp_id" value="<%= spTroneModel.getSpId() %>" />
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">对接文档</dd>
					<dd class="dd04_me">
						<select name="sp_api_id" id="sel_sp_api_id"  style="width: 200px">
							<option value="-1">请选择对接文档</option>
							<% 
							for(SpApiModel model : spApiList)
							{
							%>
							<option value="<%= model.getId() %>"><%= model.getName() %></option>
							<%
							}
							%>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">产品价格</dd>
					<dd class="dd04_me">
						<select name="base_price_id" id="sel_base_price_id" onchange="changeSpTroneName()" style="width: 200px">
							<option value="-1">请选择产品</option>
							<%
								for(BasePriceModel basePrice : basePricelist)
								{
									%>
							<option value="<%= basePrice.getId() %>"><%= basePrice.getOperatorName()+ "-" + basePrice.getName() + "-" + basePrice.getPrice()  %></option>		
									<%
								}
							%>
						</select>
					</dd>
					

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">业务名称</dd>
					<dd class="dd03_me">
						<input type="text" name="sp_trone_name" title="业务名称" id="input_sp_trone_name"  style="width: 180px" value="<%= spTroneModel.getSpTroneName() %>">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">折扣</dd>
					<dd class="dd03_me" style="background:url(../fdsws/member_input.gif) top right">
						<input type="text" name="ratio" value="<%= spTroneModel.getRatio() %>"  id="input_ratio" style="width: 100px" onchange="autoResetData(this.id,this.value)">
						<input type="button" onclick="applyAllProvince()"
							style="cursor: pointer;margin-left: 10px;background:url(../images/sssmember_01.gif);background-color:#CD853F;" value="应用到所有省份" />
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">发送短信</dd>
					<dd class="dd03_me">
						<input type="radio" name="send_sms" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">否</label>
						<input type="radio" name="send_sms" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">是</label>
					</dd>
				
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd03_me">
						<input type="radio" name="status" style="width: 35px;float:left" value="1" checked="checked" >
						<label style="font-size: 14px;float:left">开启</label>
						<input type="radio" name="status" style="width: 35px;float:left" value="0" >
						<label style="font-size: 14px;float:left">关闭</label>
					</dd>
			
					
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">省份</dd>
					<div style="margin-left: 95px; width: 600px;text-align: center;" id="pro">
						<table >
							<%
								int rowIndex = 0;
								
								out.print("<tr>");
								
								for(int i=0; i<troneList.size();i++)
								{
									TroneModel troneModel = troneList.get(i);
									
									if(i%4==0)
									{
										if(rowIndex==1)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='allCkb(\"pro_check\")'>全选</td>");
										}
										else if(rowIndex==3)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='unAllCkb(\"pro_check\")'>全不选</td>");
										}
										else if(rowIndex==5)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='inverseCkb(\"pro_check\")'>反选</td>");
										}
										else if(rowIndex==7)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='importProvince()'>导入</td>");
										}
										
										rowIndex++;
										
										out.print("</tr><tr>");
									}
									
									out.print("<td id='pro_td_" + troneModel.getProId() + "'  style='border:1px solid #d2d2d2;cursor: pointer;'  onclick='reverseCheckPro(" + troneModel.getProId() + ")'><div style='text-align:center;'>");
									out.print(troneModel.getProName());
									out.print("<input type='text'  onchange=autoResetData(this.id,this.value)  id='pro_ratio_" + troneModel.getProId() + "' style='width:40px;background-color:#E3E3E3;text-align:center;margin-left:10px;' value='1000' />");
									out.print("<input type='checkbox' id='pro_check_" + troneModel.getProId() + "' name='pro_check' title='" + troneModel.getProName() + "' style='margin-left:15px;' onclick='reverseCheckPro(" + troneModel.getProId() + ")' />");
									out.print("</div></td>");
								}
								out.print("</tr>");
							%>
						</table>
						
					</div>
				<br />
					<div style="clear: both;"><br /></div>
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me"></dd>
					&nbsp;
					&nbsp;
					<textarea name="remark"  style="border:solid 1px black;width: 588px;" overflow-y="auto" overflow-x="hidden" maxlength="1000" cols="91" rows="5"  id="remark" ><%= spTroneModel.getRemark() %></textarea>
					

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>
				</form>
			</dl>
		</div>

	</div>
</body>
</html>