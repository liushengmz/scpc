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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	String defaultMonth = sdf.format(new Date());
					
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	
	int chkType = StringUtil.getInteger(request.getParameter("chktype"), 1);
	
	String month = StringUtil.getString(request.getParameter("month"),defaultMonth);
	
	List<DetailDataVo> list = null;
	
	String unMatchKeys = "";
	
	if(!StringUtil.isNullOrEmpty(keyWord))
	{
		String[] keys = keyWord.split("\r\n");
		
		String data = "";
		
		for(String key : keys)
		{
			data += "'" + key + "',";
		}
		
		unMatchKeys = data;
		
		data = data.substring(0,data.length()-1);
		
		System.out.println("data:" + data);
		
		if(keys.length<=1000)
			list = new MrDetailServer().loadDetailDataByPhoneMsg(data, month,chkType);
		
		System.out.println(unMatchKeys);
		
		if(list!=null)
		for(DetailDataVo vo : list)
		{
			if(chkType==1)
			{
				unMatchKeys = unMatchKeys.replaceAll("'" + vo.getMobile() + "'" + ",", "");
			}
			else if(chkType==2)
			{
				unMatchKeys = unMatchKeys.replaceAll("'" + vo.getImei() + "'" + ",", "");
			}
			else if(chkType==3)
			{
				unMatchKeys = unMatchKeys.replaceAll("'" + vo.getImsi() + "'" + ",", "");
			}
		}
		
		if(!StringUtil.isNullOrEmpty(unMatchKeys))
		{
			unMatchKeys = unMatchKeys.replace("'", "");
			unMatchKeys = unMatchKeys.substring(0,unMatchKeys.length()-1);
		}
	}
	
	if(list==null)
		list = new ArrayList<DetailDataVo>();
	
	
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
			<form action="detail.jsp"  method="post" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">月份</dd>
					<dd class="dd03_me">
						<input name="month"  type="text" value="<%=month%>" 
							onclick="WdatePicker({ dateFmt: 'yyyyMM', isShowToday:false, isShowClear:false,readOnly:true })" style="width: 100px;">
					</dd>
					<dd style="margin-left: 10px;">
						<input type="radio" value="1"  name="chktype" checked="checked" />号码
						<input type="radio" value="2"  name="chktype" />IMEI
						<input type="radio" value="3"  name="chktype" />IMSI
					</dd>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<textarea name="keyword"  style="width:120px;height: 25px;"><%= keyWord %></textarea>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" />
					</dd>
				</dl>
			</form>
		</div>
		<%= StringUtil.isNullOrEmpty(unMatchKeys) ? "" : "未匹配关键字：" + unMatchKeys %>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>IMEI</td>
					<td>IMSI</td>
					<td>PHONE</td>
					<td>时间</td>
					<td>省份</td>
					<td>城市</td>
					<td>SP</td>
					<td>业务</td>
					<td>价格</td>
					<td>CP</td>
					<td>指令</td>
					<td>linkid</td>
					<td>同步</td>
				</tr>
			</thead>
			<tbody>		
				<%
					int index = 1;
					for(DetailDataVo model : list)
					{
						%>
				<tr>
					<td><%= index++ %></td>
					<td><%= model.getImei() %></td>
					<td><%= model.getImsi() %></td>
					<td><%= model.getMobile()  %></td>
					<td><%= model.getCreateDate() %></td>
					<td><%= model.getProvinceName() %></td>
					<td><%= model.getCityName() %></td>
					<td><%= model.getSpName() %></td>
					<td><%= model.getSpTroneName() %></td>
					<td><%= model.getPrice() %></td>
					<td><%= model.getCpName() %></td>
					<td><%= model.getOrder() %></td>
					<td><%= model.getLinkId() %></td>
					<td><%= model.getSynFlag()==1 ? "已同步" : "未同步" %></td>
				</tr>
						<%
					}
				%>
			<tbody>
		</table>
	</div>
	
</body>
</html>