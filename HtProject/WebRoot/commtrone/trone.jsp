<%@page import="com.system.util.Base64UTF"%>
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
<%@page import="com.system.model.UserModel" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserModel user = (UserModel)session.getAttribute("user");
	int userId=-1;
	if(user!=null){
		userId=user.getId();
	}
	int flag=new TroneServer().checkAdd(userId);
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	String query = Base64UTF.encode(request.getQueryString());
	
	Map<String, Object> map =  new TroneServer().loadTrone(pageIndex,keyWord,userId);
		
	int rowCount = (Integer)map.get("rows");
	
	List<TroneModel> list = (List<TroneModel>)map.get("list");
	
	Map<String, String> params = null;
	
	params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("trone.jsp",params,rowCount,pageIndex);
	
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
function checkUser(comUserId){
	var userId='<%=userId%>';
	if(comUserId==userId){
		return true;
	}else{
		alert("你没有权限操作！");
		return false;
	}
	
}
	function checkAdd() {
		var flag='<%=flag%>';
		if(flag==1){
			return true;
		}else{
			alert("你没有权限增加SP通道!");
			return false;
		}
	}
		

</script>
<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="troneadd.jsp" onclick="return checkAdd()">增  加</a></dd>
			</dl>
			<form action="trone.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">关键字</dd>
					<dd class="dd03_me">
						<input name="keyword" id="input_keyword" value="<%= keyWord %>" type="text" style="width: 100px">
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
					<td>SP名称</td>
					<td>SP业务名称</td>
					<td>指令</td>
					<td>通道号</td>
					<td>通道名称</td>
					<td>价格</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (TroneModel model : list)
					{
				%>
				<tr <%= model.getStatus()==0 ? stopStyle : "" %>>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getSpShortName()%></td>
					<td><%=model.getSpTroneName()%></td>
					<td><%=model.getOrders()%></td>
					<td><%=model.getTroneNum() %></td>
					<td><%=model.getTroneName() %></td>
					<td><%=model.getPrice() %></td>
					<td><%=model.getStatus()==1 ? "启用" : "停用" %></td>
					<td>
						<a href="troneedit.jsp?query=<%= query %>&id=<%= model.getId() %>" onclick="return checkUser('<%=model.getCommerceUserId() %>')">修改</a>
						<a href="troneedit.jsp?copy=1&id=<%= model.getId() %>" onclick="return checkUser('<%=model.getCommerceUserId() %>')">复制</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="9" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>