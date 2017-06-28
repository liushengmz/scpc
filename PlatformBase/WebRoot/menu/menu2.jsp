<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	int menuHeadId = StringUtil.getInteger(request.getParameter("head_id"), -1);
	
	int menu1Id = StringUtil.getInteger(request.getParameter("menu_1_id"), -1);
	
	List<MenuHeadModel> menuHeadList = new MenuHeadServer().loadMenuHeadList();
	
	List<Menu1Model> menu1List = new Menu1Server().loadMenu1List();

	Map<String, Object> map =  new Menu2Server().loadMenu2(menuHeadId,menu1Id, pageIndex);
		
	List<Menu2Model> list = (List<Menu2Model>)map.get("list");
	
	int rowCount = (Integer)map.get("rows");
	
	Map<String, String> params = new HashMap<String,String>();
	
	params.put("head_id", menuHeadId + "");
	params.put("menu_1_id", menu1Id + "");
	
	String pageData = PageUtil.initPageQuery("menu2.jsp",params,rowCount,pageIndex);
	
	String query = Base64UTF.encode(request.getQueryString());
	
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

	function menu1Object(menu1Id,menuHeadId,name)
	{
		var obj = {};
		obj.menu1Id = menu1Id;
		obj.menuHeadId = menuHeadId;
		obj.name = name;
		return obj;
	}
	
	var menu1Arr = new Array();
	<%
		for(Menu1Model menu1 : menu1List)
		{
			%>
	menu1Arr.push(new menu1Object(<%= menu1.getId() %>,<%= menu1.getMenuHeadId() %>,'<%= menu1.getName() %>'));
			<%
		}
	%>
	
	$(function()
	{
		$("#sel_head_id").val("<%= menuHeadId %>");
		$("#sel_head_id").change(menuHeadChange);
		menuHeadChange();
		$("#sel_menu_1_id").val("<%= menu1Id %>");
	});
	
	function menuHeadChange()
	{
		var headId = $("#sel_head_id").val();
		$("#sel_menu_1_id").empty(); 
		$("#sel_menu_1_id").append("<option value='-1'>请选择</option>");
		for(i=0; i<menu1Arr.length; i++)
		{
			if(menu1Arr[i].menuHeadId==headId)
			{
				$("#sel_menu_1_id").append("<option value='" + menu1Arr[i].menu1Id + "'>" + menu1Arr[i].name + "</option>");
			}
		}
	}
	
	/* ----------------------------权重修改JS---------------------------- */
	
	function editShowData(editId)
	 {
    	 console.log("aaaaa");
		 var curShowRows = $("#hid_" + editId).val();
		
		 console.log(curShowRows);
		
		 var newHtml = "<input type='text' id='myput_" + editId + "' style='width:30px;background-color:#CDC5BF;text-align:center;' value='"+ curShowRows +"' />";
		
		 newHtml += "<input type='button' value='更新' style='margin-left: 10px' onclick='updateDbData(" + editId + ","+curShowRows+")'/>";
		 
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
	  
	  function updateDbData(editId,oldShowRows)
		{
			var newShowRows = $("#myput_" + editId).val();
			
			var oldRows = oldShowRows;
			
			console.log("id:"+editId);
			
			console.log("value:"+newShowRows);
			
			$.post("menu2action.jsp", 
			{
				value : newShowRows,
				mid :editId,
				type:3
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
					$("#span_" + editId).html(oldRows);
				}
			});
		}
	
</script>

<body>
	<div class="main_content">
		<div class="content" >
			<dl>
				<dd class="ddbtn" ><a href="menu2add.jsp">增  加</a></dd>
			</dl>
			<form action="menu2.jsp"  method="get" style="margin-top: 10px">
				<dl>
					<dd class="dd01_me">模块</dd>
					<dd class="dd04_me">
						<select name="head_id" id="sel_head_id" >
							<option value="-1">全部</option>
							<%
							for(MenuHeadModel model : menuHeadList)
							{
								%>
							<option value="<%= model.getId() %>"><%= model.getName() %></option>	
								<%
							}
							%>
						</select>
					</dd>
					<dd class="dd01_me">组别</dd>
					<dd class="dd04_me">
						<select name="menu_1_id" id="sel_menu_1_id" > </select>
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
					<td>模块名称</td>
					<td>组别名称</td>
					<td>菜单名称</td>
					<td>URL</td>
					<td>备注</td>
					<td>权重</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					for (Menu2Model model : list)
					{
				%>
				<tr>
					<td><%= (pageIndex-1)*Constant.PAGE_SIZE + rowNum++ %></td>
					<td><%=model.getMenuHeadName()%></td>
					<td><%=model.getMenu1Name()%></td>
					<td><%= model.getName() %></td>
					<td><%= model.getUrl() %></td>
					<td><%=model.getRemark()==null?"":model.getRemark()%></td>
					
					<td ondblclick='editShowData(<%=model.getId()%>)'>
						<input type="hidden" id="hid_<%= model.getId() %>" value="<%=model.getSort()%>" />
						<span id="span_<%= model.getId() %>"><%=model.getSort()%></span>
					</td>
					<td>
						<a href="menu2edit.jsp?query=<%= query %>&id=<%= model.getId() %>&pageindex=<%=pageIndex%>">修改</a>
					</td>
				</tr>
				<%
					}
				%>
			<tbody>
				<tr>
					<td colspan="8" class="tfooter" style="text-align: center;"><%= pageData %></td>
				</tr>
			</tbody>
		</table>
	</div>
	
</body>
</html>