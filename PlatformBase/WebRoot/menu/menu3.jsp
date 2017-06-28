<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.dao.MenuHeadDao"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String menuName = StringUtil.getString(request.getParameter("menuName"), "");
	int sort = StringUtil.getInteger(request.getParameter("sort"), -1);
	String sortstr = sort+"";

	Map<String,Object> map = new MenuHeadServer().loadMenuHead(pageIndex,menuName, sort);
	
	List<MenuHeadModel> list = (List<MenuHeadModel>)map.get("list");
	
	int rowcount = (Integer)map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	params.put("menuName", menuName);
	if(sort<0){
		sortstr = "";
	}
	
    
    String pageData = PageUtil.initPageQuery("menu3.jsp",params,rowcount,pageIndex); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>翔通运营管理平台</title>
    <link href="../wel_data/right.css" rel="stylesheet" type="text/css">
    <link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
    <script type="text/javascript">
	
     function editShowData(editId)
	 {
    	 console.log("aaaaa");
		 var curShowRows = $("#hid_" + editId).val();
		
		 console.log(curShowRows);
		
		 var newHtml = "<input type='text' id='myput_" + editId + "' style='width:30px;background-color:#CDC5BF;text-align:center;' value='"+ curShowRows +"' />";
		
		 newHtml += "<input type='button' value='更新' style='margin-left: 10px' onclick='updateShowData(" + editId + ")'/>";
		 
		 newHtml += "<input type='button' value='取消' style='margin-left: 10px' onclick='cancelShowData(" + editId + ")'/>";
		
		 $("#span_" + editId).html(newHtml);
	 }  
    
   //声明整数的正则表达式
 	function isNum(a)
 	{
 		var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
 		return reg.test(a);
 	}
    
        function updateShowData(editId)
 	   {
          console.log("aaa");
 		  var newShowRows = $("#myput_" + editId).val();
 		  
 		  console.log("aaa"+newShowRows);
 		
 		  if (isNum(newShowRows))
 		  {
 			  alert("输入数据不正确");
 			  return;
 		  }
 		
 		  if(parseInt(newShowRows,10)>10000||parseInt(newShowRows,10)<0)
 		  {
 		  	  alert("请输入0~10000之间的正整数");
 		  	  return;
 		  }
 		
 		  updateDbData(editId,newShowRows);
 	  }
     
	  function delTrone(id)
	  {
		  if(confirm('真的要删除吗？'))
		  {
			  window.location.href = "delet.jsp?id=" + id+"&type=3";	
		  }
	  }
	  
	  function cancelShowData(editId)
		{
			$("#span_" + editId).html($("#hid_" + editId).val());
		}
	  
	  function updateDbData(editId,newShowRows)
		{
			var oldShowRows = $("#hid_" + editId).val();
			
			console.log("id:"+editId);
			
			console.log("value:"+newShowRows);
			
			$.post("menu3action.jsp", 
			{
				value : newShowRows,
				mid :editId,
				type:1
			}, 
			function(data) 
			{
				data = $.trim(data);
				if ("OK" == data) 
				{
					$("#hid_" + editId).val(newShowRows);		
					$("#span_" + editId).html(newShowRows);
					$("#show_data_rows_label").html(parseInt($("#show_data_rows_label").html(),10) - parseInt(oldShowRows,10) + parseInt(newShowRows,10));
				} 
				else 
				{
					alert("修改失败！请联系管理员");
					$("#span_" + editId).html($("#hid_" + editId).val());
				}
			});
		}
	
    </script>
  </head>
  <body>
    <div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="menu3add.jsp">增  加</a></dd>
				<form action="menu3.jsp"  method="post" id="formid">
				<dl>
					<dd class="dd01_me">模块名称</dd>
					<dd class="dd03_me">
						<input name="menuName" id="input_menuname"  value="<%=menuName %>" type="text" style="width: 150px">
					</dd>
					<dd class="dd01_me">权重</dd>
					<dd class="dd03_me">
						<input name="sort" id="input_sort" value="<%=sortstr %>" type="text" style="width: 150px">
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
					<td>模块名称</td>
					<td>备注</td>
					<td>权重</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (MenuHeadModel model : list)
					
					{
						
				%>
				
				<tr>
					<td><%=(pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getName()%></td>
					<td><%=model.getRemark()==null?"":model.getRemark()%></td>
					
					<td ondblclick='editShowData(<%=model.getId()%>)'>
						<input type="hidden" id="hid_<%= model.getId() %>" value="<%=model.getSort()%>" />
						<span id="span_<%= model.getId() %>"><%=model.getSort()%></span>
					</td>
					
					<td>
						<a href="menu3edit.jsp?id=<%= model.getId() %>
						&pageindex=<%=StringUtil.getInteger(request.getParameter("pageindex"), 1) %>">修改</a>
						<a onclick="delTrone(<%= model.getId()%>)">删除</a>
					</td>
				</tr>
				<%
					}
				%>
				
			<tbody>
				<tr>
					<td colspan="5" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
  </body>
</html>