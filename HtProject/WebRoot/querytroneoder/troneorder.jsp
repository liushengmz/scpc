<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="com.system.server.TroneServer"%>
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
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());

	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	
	int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
	
	int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
	
	int status = StringUtil.getInteger(request.getParameter("trone_status"), -1);
	
	String	keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	Map<String, Object> map =  new TroneOrderServer().loadTroneOrder(spId, spTroneId, cpId,status,pageIndex,keyWord);
		
	List<TroneOrderModel> list = (List<TroneOrderModel>)map.get("list");
	
	List<SpModel> spList = new SpServer().loadSp();
	
	List<CpModel> cpList = new CpServer().loadCp();
	
	List<SpTroneModel> spTroneList = new SpTroneServer().loadSpTroneList();
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String, String> params = new HashMap<String,String>();
	
	params = new HashMap<String,String>();
	params.put("sp_id", spId + "");
	params.put("cp_id", cpId + "");
	params.put("sp_trone_id", spTroneId + "");
	params.put("trone_status",status + "");
	params.put("keyword",keyWord);
	
	String pageData = PageUtil.initPageQuery("troneorder.jsp",params,rowCount,pageIndex);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
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
		spChange();
	}
	
	function onCpDataSelect(joData)
	{
		$("#sel_cp_id").val(joData.id);
	}

	function joSpTrone(id,spId,name)
	{
		var obj = {};
		obj.id = id ;
		obj.spId = spId;
		obj.name = name;
		return obj;
	}
	
	var spTroneArray = new Array();
	<%
		for(SpTroneModel spTrone : spTroneList)
		{
			%>
	spTroneArray.push(new joSpTrone(<%= spTrone.getId() %>,<%= spTrone.getSpId() %>,'<%= spTrone.getSpTroneName() %>'));	
			<%
		}
	%>

	function delTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "troneaction.jsp?did=" + id;	
		}
	}
	
	$(function()
	{
		$("#sel_sp").val(<%= spId %>);
		$("#sel_cp_id").val(<%= cpId %>);
		$("#sel_sp").change(spChange);
		spChange();
		$("#sel_sp_trone_id").val(<%= spTroneId %>);
		$("#sel_trone_status").val(<%= status %>);
	});
	
	function spChange()
	{
		var spId = $("#sel_sp").val();
		$("#sel_sp_trone_id").empty(); 
		$("#sel_sp_trone_id").append("<option value='-1'>请选择</option>");
		for(i=0; i<spTroneArray.length; i++)
		{
			if(spTroneArray[i].spId==spId || spId == "-1")
			{
				$("#sel_sp_trone_id").append("<option value='" + spTroneArray[i].id + "'>" + spTroneArray[i].name + "</option>");
			}
		}
	}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			
			<form action="troneorder.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me" style="margin-left: -16px;">CP</dd>
					<dd class="dd04_me">
						<select name="cp_id" id="sel_cp_id" onclick="namePicker(this,cpList,onCpDataSelect)">
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
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd04_me">
						<select name="sp_trone_id" id="sel_sp_trone_id" ></select>
					</dd>
					<dd class="dd01_me">状态</dd>
					<dd class="dd04_me">
						<select name="trone_status" id="sel_trone_status" >
							<option value="-1">全部</option>
							<option value="0">启用</option>
							<option value="1">停用</option>
						</select>
					</dd>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input type="text" name="keyword" id="sel_keyword" value="<%= keyWord %>" />
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查     询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>CP</td>
					<td>SP名称</td>
					<td>SP业务名称</td>
					<td>通道名称</td>
					<td>价格</td>
					<td>指令</td>
					<td>扣量设置</td>
					<td>扣量比</td>
					<td>同步金额</td>
					<td>起扣数</td>
					<td>模糊</td>
					<td>启用</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (TroneOrderModel model : list)
					{
				%>
				<tr <%= model.getDisable() == 1 ? stopStyle : "" %>>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getCpShortName()%></td>
					<td><%=model.getSpShortName() %></td>
					<td><%=model.getSpTroneName()%></td>
					<td><%=model.getTroneName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%=model.getOrderNum() %></td>
					<td><%=model.getIsHoldCustom()==0 ? "URL" : "当前" %></td>
					<td><%=model.getHoldPercent() %></td>
					<td><%=model.getHoldAmount() %></td>
					<td><%=model.getHoldAcount()%></td>
					<td><%=model.getDynamic()==1 ? "是" : "否" %></td>
					<td><%=model.getDisable() ==0 ? "是" : "否" %></td>
					
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="13" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>