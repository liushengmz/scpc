<%@page import="java.util.List"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<String> list = new MenuHeadServer().loadMenuName(0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>翔通运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    //声明整数的正则表达式
    function isNum(a)
	{
		//var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		var reg = /^[0-9]*[1-9][0-9]*$/i;
		return reg.test(a);
	}
	function subForm() 
	{
		if (isNullOrEmpty($("#input_menu_name").val())) 
		{
			alert("请输入模块名");
			$("#input_menu_name").focus();
			return;
		}
		
		var arr= new Array();
		<%
			int i=0;
			for (String string : list) {
				%>
				arr[<%=i%>]="<%=string%>";
				<%
				i++;
			}
		%>
		
		for(var i=0;i<arr.length;i++){
		    if($("#input_menu_name").val()==arr[i])
		    {
		    	alert("模块名已存在");
		    	return;
		    }
		}
		
		
		
		console.log(arr);
		
		if(!(isNum($("#input_sort").val())))
		{
			alert("请输入正整数！");
			$("#input_sort").focus();
			return;
		}
		
		var num = parseInt($("#input_sort").val(), 10);
		
		if(num<0||num>10000)
		{
			alert("请输入0~10000之间的整数");
			$("#input_sort").focus();
			return;
		}
		
		document.getElementById("addform").submit();
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" >
				<label>添加模块</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="menu3action.jsp" method="post" id="addform">
				
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">模块名</dd>
					<dd class="dd03_me">
						<input type="text" name="menu_name" id="input_menu_name"
							style="width: 200px">
					</dd>

					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" id="input_remark"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">权重</dd>
					<dd class="dd03_me">
						<input type="text" name="sort" id="input_sort"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="history.go(-1)">
					</dd>
				</form>
			</dl>
		</div>

	</div>
</body>
</html>