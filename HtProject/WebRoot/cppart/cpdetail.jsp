<%@page import="java.util.Calendar"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.server.CpDetailServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.system.server.MrDetailServer"%>
<%@page import="com.system.vo.DetailDataVo"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int userId = ((UserModel)session.getAttribute("user")).getId();

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	
	Calendar ca = Calendar.getInstance();
	
	ca.add(Calendar.DAY_OF_YEAR, -1);
	
	String yesDate = StringUtil.getDateFormat(ca.getTime());

	String startDate = StringUtil.getString(request.getParameter("startDate"), yesDate + " 00:00:00");
	
	String endDate = StringUtil.getString(request.getParameter("endDate"), yesDate + " 23:59:59");
					
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	
	int chkType = StringUtil.getInteger(request.getParameter("chktype"), 1);
	
	Map<String,Object> map = new CpDetailServer().loadDetailDataByMsg(pageIndex, userId, startDate, endDate, chkType, keyWord);
	
	int rowCount = (Integer)map.get("rows");
	
	List<DetailDataVo> list = (List<DetailDataVo>)map.get("list");
	
	if(list==null)
		list = new ArrayList<DetailDataVo>();
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	params.put("startDate", startDate);
	
	params.put("endDate", endDate);
	
	String pageData = PageUtil.initPageQuery("cpdetail.jsp",params,rowCount,pageIndex);
	
	String query = Base64UTF.encode(request.getQueryString());
	
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
		setRadioCheck("chktype",<%= chkType %>);
	});
	
</script>
<body>
	<div class="main_content">
		<div class="content" >
			<form action="cpdetail.jsp"  method="post" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">开始时间</dd>
					<dd class="dd03_me">
						<input name="startDate"  type="text" value="<%= startDate %>" 
							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'<%= yesDate + " 00:00:00" %>', isShowToday:false, isShowClear:false,readOnly:true })" style="width: 150px;">
					</dd>
					<dd class="dd01_me">结束时间</dd>
					<dd class="dd03_me">
						<input name="endDate"  type="text" value="<%= endDate %>" 
							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'<%= yesDate  + " 23:59:59" %>', isShowToday:false, isShowClear:false,readOnly:true })" style="width: 150px;">
					</dd>
					<dd style="margin-left: 10px;">
						<input type="radio" value="1"  name="chktype" checked="checked" />号码
						<input type="radio" value="2"  name="chktype" />IMEI
						<input type="radio" value="3"  name="chktype" />IMSI
					</dd>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input type="text" name="keyword"  style="width:120px;"><%= keyWord %></input>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>IMEI</td>
					<td>IMSI</td>
					<td>PHONE</td>
					<td>时间</td>
					<!-- <td>省份</td> -->
					<!-- <td>城市</td> -->
					<td>价格</td>
					<td>CP</td>
					<td>指令</td>
					<td>linkid</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(DetailDataVo model : list)
					{
						%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + index++ %></td>
					<td><%= model.getImei() %></td>
					<td><%= model.getImsi() %></td>
					<td><%= model.getMobile()  %></td>
					<td><%= model.getCreateDate() %></td>
					<!--  
					<td><%= model.getProvinceName() %></td>
					<td><%= model.getCityName() %></td>
					-->
					<td><%= model.getPrice() %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getOrder() %></td>
					<td><%= model.getLinkId() %></td>
				</tr>
						<%
					}
				%>
			<tbody>
			<tbody>
				<tr>
					<td colspan="12" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>