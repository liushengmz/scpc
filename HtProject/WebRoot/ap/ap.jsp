<%@page import="com.system.model.ApModel"%>
<%@page import="com.system.server.TestApServer"%>
<%@page import="com.system.server.xy.XyCpsServer"%>
<%@page import="com.system.model.xy.XyUserCpsModel"%>
<%@page import="com.system.constant.Constant"%><!-- 定义连接数的包  -->
<%@page import="com.system.util.PageUtil"%>  <!-- 定义分页的工具类 -->
<%@page import="java.util.List"%>    
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1); /*设置默认分页*/

	Map<String, Object> map =  new TestApServer().loadAp(pageIndex); /*业务层调用dao层的loadSp方法加载sp数据*/
		
	List<ApModel> list = (List<ApModel>)map.get("list"); /*获得 */
	
	int rowCount = (Integer)map.get("rows");  /*获得最大条目数  */
	
	String pageData = PageUtil.initPageQuery("ap.jsp",null,rowCount,pageIndex); 
	
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
	
	function delTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "delet.jsp?id=" + id;	
		}
	}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="apadd.jsp">增  加</a></dd>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SPID</td>
					<td>全称</td>
					<td>简称</td>
					<td>联系人</td>
					<td>QQ</td>
					<td>电话</td>
					<td>邮箱</td>
					<td>合同起始日</td>
					<td>合同结束日</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (ApModel model : list)
					{
				%>
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%= model.getId() + 1000 %></td>
					<td><%=model.getFullName()%></td>
					<td><%=model.getShortName()%></td>
					<td><%=model.getContactPerson()%></td>
					<td><%=model.getQq()%></td>
					<td><%=model.getPhone() %></td>
					<td><%=model.getMail() %></td>
					<td><%=model.getContractStartDate() %></td>
					<td><%=model.getContractEndDate() %></td>
					<td>
						<a href="apedit.jsp?id=<%= model.getId() %>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="11" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>     
