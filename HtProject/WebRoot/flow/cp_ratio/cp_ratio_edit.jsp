<%@page import="com.system.flow.model.CpRatioModel"%>
<%@page import="com.system.flow.server.CpRatioServer"%>
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
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"),0);

	Map<Integer, List<CpRatioModel>> map = new CpRatioServer().loadCpRatioList(cpId);
	
	List<CpRatioModel> unionList = map.get(1);
	List<CpRatioModel> telcomList = map.get(2);
	List<CpRatioModel> mobileList = map.get(3);
	
	String troneIdArrStr = "";
	String ratioValuesStr = "";
	String statusValuesStr = "";
	
	for(CpRatioModel model : unionList)
	{
		troneIdArrStr += model.getId() + ",";
		ratioValuesStr +=  model.getRatio() + ",";
		statusValuesStr += model.getStatus() + ",";
	}
	
	for(CpRatioModel model : telcomList)
	{
		troneIdArrStr += model.getId() + ",";
		ratioValuesStr +=  model.getRatio() + ",";
		statusValuesStr += model.getStatus() + ",";
	}
	
	for(CpRatioModel model : mobileList)
	{
		troneIdArrStr += model.getId() + ",";
		ratioValuesStr +=  model.getRatio() + ",";
		statusValuesStr += model.getStatus() + ",";
	}
	
	troneIdArrStr = troneIdArrStr.substring(0,troneIdArrStr.length()-1);
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
<script type="text/javascript"	>

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
	var ratioValuesArray = [<%= ratioValuesStr %>];
	var statusValuesArray = [<%= statusValuesStr %>];
	
	function setDefaultColor()
	{
		for(var i=0; i<troneIdArray.length; i++)
		{
			var checked = document.getElementById('pro_check_' + troneIdArray[i]).checked;
			
			if(checked)
			{
				$("#pro_td_" + troneIdArray[i]).css("background","#66CD00");
			}
			else
			{
				$("#pro_td_" + troneIdArray[i]).css("background","#fff");
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
	
	function importProvince(item)
	{
		var tmpPro = prompt("请输入省份", "");
		
		if ( tmpPro == null || "" == tmpPro )
			return;
	
		$('[name=' + item + ']:checkbox').each(function() 
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
	
	function applyAllProvince(items)
	{
		var ratio = $("#input_ratio").val();
		
		$('[name=' + items + ']:text').each(function() {
			this.value = ratio;
		});
	}
	
	function subForm() 
	{
		if(confirm("确定修改吗？"))
		{
			fillProRatioCheckValue();

			document.getElementById("addform").submit();
	
		}
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
		for(var i=0; i<troneIdArray.length; i++)
		{
			var checked = document.getElementById('pro_check_' + troneIdArray[i]).checked;
			var ratio = document.getElementById('pro_ratio_' + troneIdArray[i]).value;
			value += troneIdArray[i] + "|"  + ratio + "|" + checked + ","
		}
		value = value.substring(0,value.length -1);
		$("#hid_pro_ratio_check").val(value);
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
		for(var i=0; i<troneIdArray.length; i++)
		{
			$("#pro_ratio_" + troneIdArray[i]).val(ratioValuesArray[i]);
			
			document.getElementById('pro_check_' + troneIdArray[i]).checked = statusValuesArray[i]==1 ? true : false;
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
				<label>修改折扣</label>
				</dd>
			</dl>
			<br/>
			<br/>
			<dl>
				<form action="action.jsp" method="post" id="addform">
					<input type="hidden" name="type" id="hid_type" value="2" alt="修改">
					<input type="hidden" name="pro_ratio_check" id="hid_pro_ratio_check">
					<input type="hidden" name="cp_id" id="hid_cp_id" value="<%= cpId %>">
					
					<dd class="dd00_me"></dd>
					<dd class="dd01_me" >折扣</dd>
					
					<dd class="dd03_me">
						<input  type="text" id="input_ratio" style="width: 50px;"  onchange="autoResetData(this.id,this.value)" />
						<input  type="hidden" id="sel_cp_id" name="cp_id" value="<%= cpId %>" />
					</dd>
					
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="应用到联通" type="button" onclick="applyAllProvince('pro_union_ratio')" >
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="应用到电信" type="button" onclick="applyAllProvince('pro_telcom_ratio')" >
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="应用到移动" type="button" onclick="applyAllProvince('pro_mobile_ratio')" >
					</dd>
					
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>
					
				</form>
					
					<br />
					<br />
					
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">联通省份</dd>
					<div style="margin-left: 95px; width: 600px;text-align: center;" id="pro">
						<table >
							<%
								int rowIndex = 0;
								
								out.print("<tr>");
								
								for(int i=0; i<unionList.size();i++)
								{
									CpRatioModel troneModel = unionList.get(i);
									
									if(i%4==0)
									{
										if(rowIndex==1)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='allCkb(\"pro_union_check\")'>全选</td>");
										}
										else if(rowIndex==3)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='unAllCkb(\"pro_union_check\")'>全不选</td>");
										}
										else if(rowIndex==5)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='inverseCkb(\"pro_union_check\")'>反选</td>");
										}
										else if(rowIndex==7)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='importProvince(\"pro_union_check\")'>导入</td>");
										}
										
										rowIndex++;
										
										out.print("</tr><tr>");
									}
									
									out.print("<td id='pro_td_" + troneModel.getId() + "'  style='border:1px solid #d2d2d2;cursor: pointer;'  onclick='reverseCheckPro(" + troneModel.getId() + ")'><div style='text-align:center;'>");
									out.print(troneModel.getProName());
									out.print("<input type='text'  onchange=autoResetData(this.id,this.value) name='pro_union_ratio' id='pro_ratio_" + troneModel.getId() + "' style='width:40px;background-color:#E3E3E3;text-align:center;margin-left:10px;' value='1000' />");
									out.print("<input type='checkbox' id='pro_check_" + troneModel.getId() + "' name='pro_union_check' title='" + troneModel.getProName() + "' style='margin-left:15px;' onclick='reverseCheckPro(" + troneModel.getId() + ")' />");
									out.print("</div></td>");
								}
								out.print("</tr>");
							%>
						</table>
					</div>
					<dd class="dd01_me">电信省份</dd>
					<div style="margin-left: 95px; width: 600px;text-align: center;" id="pro">
						<table >
							<%
								rowIndex = 0;
								
								out.print("<tr>");
								
								for(int i=0; i<telcomList.size();i++)
								{
									CpRatioModel troneModel = telcomList.get(i);
									
									if(i%4==0)
									{
										if(rowIndex==1)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='allCkb(\"pro_telcom_check\")'>全选</td>");
										}
										else if(rowIndex==3)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='unAllCkb(\"pro_telcom_check\")'>全不选</td>");
										}
										else if(rowIndex==5)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='inverseCkb(\"pro_telcom_check\")'>反选</td>");
										}
										else if(rowIndex==7)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='importProvince(\"pro_telcom_check\")'>导入</td>");
										}
										
										rowIndex++;
										
										out.print("</tr><tr>");
									}
									
									out.print("<td id='pro_td_" + troneModel.getId() + "'  style='border:1px solid #d2d2d2;cursor: pointer;'  onclick='reverseCheckPro(" + troneModel.getId() + ")'><div style='text-align:center;'>");
									out.print(troneModel.getProName());
									out.print("<input type='text'  onchange=autoResetData(this.id,this.value)  name='pro_telcom_ratio' id='pro_ratio_" + troneModel.getId() + "' style='width:40px;background-color:#E3E3E3;text-align:center;margin-left:10px;' value='1000' />");
									out.print("<input type='checkbox' id='pro_check_" + troneModel.getId() + "' name='pro_telcom_check' title='" + troneModel.getProName() + "' style='margin-left:15px;' onclick='reverseCheckPro(" + troneModel.getId() + ")' />");
									out.print("</div></td>");
								}
								out.print("</tr>");
							%>
						</table>
					</div>
					<dd class="dd01_me">移动省份</dd>
					<div style="margin-left: 95px; width: 600px;text-align: center;" id="pro">
						<table >
							<%
								rowIndex = 0;
								
								out.print("<tr>");
								
								for(int i=0; i<mobileList.size();i++)
								{
									CpRatioModel troneModel = mobileList.get(i);
									
									if(i%4==0)
									{
										if(rowIndex==1)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='allCkb(\"pro_mobile_check\")'>全选</td>");
										}
										else if(rowIndex==3)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='unAllCkb(\"pro_mobile_check\")'>全不选</td>");
										}
										else if(rowIndex==5)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='inverseCkb(\"pro_mobile_check\")'>反选</td>");
										}
										else if(rowIndex==7)
										{
											out.print("<td rowspan='2' style='border:1px solid #d2d2d2;cursor: pointer;background-color:#E3E3E3;font-weight:bold;' onclick='importProvince(\"pro_mobile_check\")'>导入</td>");
										}
										
										rowIndex++;
										
										out.print("</tr><tr>");
									}
									
									out.print("<td id='pro_td_" + troneModel.getId() + "'  style='border:1px solid #d2d2d2;cursor: pointer;'  onclick='reverseCheckPro(" + troneModel.getId() + ")'><div style='text-align:center;'>");
									out.print(troneModel.getProName());
									out.print("<input type='text'  onchange=autoResetData(this.id,this.value)  name='pro_mobile_ratio' id='pro_ratio_" + troneModel.getId() + "' style='width:40px;background-color:#E3E3E3;text-align:center;margin-left:10px;' value='1000' />");
									out.print("<input type='checkbox' id='pro_check_" + troneModel.getId() + "' name='pro_mobile_check' title='" + troneModel.getProName() + "' style='margin-left:15px;' onclick='reverseCheckPro(" + troneModel.getId() + ")' />");
									out.print("</div></td>");
								}
								out.print("</tr>");
							%>
						</table>
					</div>
			</dl>
		</div>

	</div>
</body>
</html>