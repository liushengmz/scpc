<%@page import="com.system.server.xy.XyCpsServer"%>
<%@page import="com.system.model.xy.XyUserCpsModel"%>
<%@page import="com.system.server.xy.UserServer"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Map"%>

<%@page import="java.util.List"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- 游戏渠道CPS扣量管理 -->
<%
	String defaultStartDate = StringUtil.getPreDayOfMonth();/* 获取之前一个随机的时间 */
	String defaultEndDate = StringUtil.getDefaultDate();    /* 获得当前系统时间 */
	String appKey = StringUtil.getString(request.getParameter("appkey"),
			""); /* 获取需查询的应用KEY */
	String channelKey = StringUtil
			.getString(request.getParameter("channelkey"), ""); /* 获取渠道ID */
	String startDate = StringUtil
			.getString(request.getParameter("startdate"), defaultStartDate);  /* 获取用户输入开始时间 */
	String endDate = StringUtil
			.getString(request.getParameter("enddate"), defaultEndDate);  /* 获取用户输入的结束时间 */
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
			
    Map<String, Object> map = 
    		new XyCpsServer().loadUserData(startDate,endDate, appKey, channelKey,pageIndex);
    
    List<XyUserCpsModel> list = (List<XyUserCpsModel>)map.get("list");
    
    int rowCount = (Integer)map.get("rows");
    
    Map<String,String> params = new HashMap<String,String>();
	
	params.put("appkey", appKey);
	params.put("channelkey", channelKey);
	params.put("startdate", startDate);
	params.put("enddate", endDate);
	
	String pageData = PageUtil.initPageQuery("qdfee2.jsp",params,rowCount,pageIndex);
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
    <script type="text/javascript">

	//声明整数的正则表达式
	function isNum(a)
	{
		var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		return reg.test(a);
	}
	
	function isDouble(a)
	{
		if(isNum(a)){
			return;
		}else{
			if(!isNaN(a)){
	            var dot = a.indexOf(".");
	            if(dot != -1){
	                var dotCnt = a.substring(dot+1,a.length);
	                if(dotCnt.length > 2){
	                    alert("小数位已超过2位！");
	                    return true;
	                }
	            }
	        }else{
	            alert("数字不合法！");
	            return true;
	        }
		}
		
        
	}

	function editShowData(editId)
	{
		var curShowRows = $("#hid_" + editId).val();
		
		console.log(curShowRows);
		
		var newHtml = "<input type='text' id='myput_" + editId + "' style='width:30px;background-color:#CDC5BF;text-align:center;' value='"+ curShowRows +"' />";
		
		newHtml += "<input type='button' value='更新' style='margin-left: 10px' onclick='updateShowData(" + editId + ")'/>";
		 
		newHtml += "<input type='button' value='取消' style='margin-left: 10px' onclick='cancelShowData(" + editId + ")'/>";
		
		$("#span_" + editId).html(newHtml);
	}
	
	function updateShowData(editId)
	{
		var oldShowRows = $("#ori_hid_" + editId).val();
		var newShowRows = $("#myput_" + editId).val();
		//soldShowRows = parseFloat(oldShowRows);
		//newShowRows = parseFloat(newShowRows);
		console.log(oldShowRows);
		console.log(newShowRows);
		if (isDouble(newShowRows))
		{
			alert("输入数据不正确");
			return;
		}
		
		if(parseFloat(newShowRows)>parseFloat(oldShowRows))
		{
			alert("显示数据不能大于实际数据");
			return;
		}
		
		updateDbData(editId,newShowRows);
	}
	
	function updateDbData(editId,newShowRows)
	{
		var oldShowRows = $("#hid_" + editId).val();
		
		$.post("action.jsp", 
		{
			datafee : newShowRows,
			type : 2,
			id :editId 
		}, 
		function(data) 
		{
			data = $.trim(data);
			if ("OK" == data) 
			{
				$("#hid_" + editId).val(newShowRows);		
				$("#span_" + editId).html(newShowRows);
				$("#show_data_rows_label").html(parseFloat($("#show_data_rows_label").html(),10) - parseFloat(oldShowRows,10) + parseFloat(newShowRows,10));
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
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<form action="qdfee2.jsp"  method="get">
				<dl>
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate"  type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>" 
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">应用包KEY</dd>
					<dd class="dd03_me">
						<input name="appkey" value="<%=appKey%>" type="text">
					</dd>
					<dd class="dd01_me">渠道ID</dd>
					<dd class="dd03_me">
						<input name="channelkey" value="<%=channelKey%>" type="text">
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
					<td  class="or">计费日期</td>
					<td>游戏名</td>
					<td  class="or">应用Key</td>
					<td class="or">渠道ID</td>
					<td class="or">激活数</td>
					<td class="or">费用</td>
					<td class="or">同步费用</td>
					<td class="dd00">状态</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (XyUserCpsModel model : list)
					{
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td><!-- 序号 -->
					<td>
						<%=model.getFee_date()%>
						<input type="hidden" id="hid_<%= model.getId() %>" value="<%= StringUtil.getDecimalFormat((Double)model.getShowAmount()) %>" /><!-- 计费日期 -->
					</td>
					<td class="or"><%=model.getAppName()%></td><!-- APP名字 -->
					<td><%=model.getAppKey()%></td><!-- APPKEY -->
					<td class="or"><%=model.getChannelKey()%></td><!-- 渠道ID -->
					<td>
						<%=model.getDataRows()%><!-- 序号 -->
						 
					</td>
					<td>
					    <%=StringUtil.getDecimalFormat((Double)model.getAmount()) %>
					    <input type="hidden" id="hid_<%= model.getId() %>" value="<%=StringUtil.getDecimalFormat((Double)model.getAmount())%>"/>
					</td>
					<td class="or" <%= model.getStatus()==0 ? "ondblclick='editShowData(" + model.getId() + ")'":"" %> >
						<span id="span_<%= model.getId() %>"><%=StringUtil.getDecimalFormat((Double)model.getShowAmount()) %></span>
					</td>
					<td><%= model.getStatus()==1 ? "已同步":"未同步" %></td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>激活总数:<%= map.get("data_rows") %></td>
					<td>
						<span>总费用:<label ><%= StringUtil.getDecimalFormat((Double)map.get("amount")) %></label></span>
					</td>
					<td>
						<span>总同步费用:<label id="show_data_rows_label"><%= StringUtil.getDecimalFormat((Double)map.get("show_amount")) %></label></span>
					</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="8" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
  </body>
</html>