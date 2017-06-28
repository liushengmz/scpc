<%@page import="com.system.server.CpBankInfoServer"%>
<%@page import="com.system.model.CpBankModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");
	int type=StringUtil.getInteger(request.getParameter("type"),-1);
	int status=StringUtil.getInteger(request.getParameter("status"), -1);
	Map<String, Object> map =  new CpBankInfoServer().loadCpBank(pageIndex, keyWord, type, status);
		
	List<CpBankModel> list = (List<CpBankModel>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");

	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	params.put("type", type+"");
	params.put("status", status+"");

	String pageData = PageUtil.initPageQuery("cpbank.jsp",params,rowCount,pageIndex);
	
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
<script type="text/javascript">
$(function(){
			$("#input_keyword").val(<%=keyWord %>);
			$("#type").val(<%=type%>);
			$("#status").val(<%= status %>);
		});
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="cpbankadd.jsp">增  加</a></dd>
				<form action="cpbank.jsp"  method="get" id="formid">
				<dl>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input name="keyword" id="input_keyword" value="<%= keyWord %>" type="text" style="width: 150px">
					</dd>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd04_me">
						<select name="type" id="type" style="width: 110px;" >
							<option value="-1">请选择</option>
							<option value="0">对公</option>
							<option value="1">对私</option>
						</select>
					</dd>
						<dd class="dd01_me">状态</dd>
						<dd class="dd04_me">
						<select name="status" id="status" style="width: 110px;" >
							<option value="-1">请选择</option>
							<option value="1">启用</option>
							<option value="0">停用</option>
						</select>
					</dd>
					<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
						<input class="btn_match" name="search" value="查 询" type="submit" >
					</dd>
				</dl>
				</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>CP简称</td>
					<td>CP全称</td>
					<td>结算类型</td>
					<td>银行</td>
					<td>收款人</td>
					<td>银行账号</td>
					<td>支行</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (CpBankModel model : list)
					{
				%>
					<tr <%= model.getStatus()==0 ? stopStyle : "" %>>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getCpShortName()%></td>
					<td><%=model.getCpFullName()%></td>
					<td><%=model.getTypeName()%></td>
					<td><%=model.getBankName()%></td>
					<td><%=model.getUserName() %></td>
					<td><%=model.getBankAccount() %></td>
					<td><%=model.getBankBranch() %></td>
					<td><%=model.getStatus()==1 ? "开启" : "关闭" %></td>
					<td>
						<a href="cpbankedit.jsp?query=<%= query %>&id=<%= model.getId() %>">修改</a>
						<a href="action.jsp?act=3&id=<%= model.getId() %>" onclick="if(confirm('确定删除?')==false)return false">删除</a>
						
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="12" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>