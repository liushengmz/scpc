<%@page import="com.system.model.AdAppModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.AdAppServer"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
   // Map<String,Object> name = new AdAppServer().loadApp(1);
	Map<String,Object> name = new AdAppServer().loadApp();
	List<AdAppModel> applist = (List<AdAppModel>)name.get("list");
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
<script type="text/javascript" src="../sysjs/map.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

		$(function()
		{
			
			$("#input_amount").change(function(){
				arrayadd();
				
				alert("hold:"+holdPervent);
				holdPervent = Math.round(holdPervent * 100) / 100;
				alert("hold:"+holdPervent);
				var showamount = $("#input_amount").val();	
				console.log("amount:"+showamount);
			});
			
		});
	
		 		
		
	function arrayadd()
	{
		var map = new Map();
		<%
		
		for(AdAppModel app : applist)
		{
			%>
			map.put(<%=app.getId()%>, <%=app.getHold_percent()%>);
			<%
		}
		%>
		var array = map.keySet();
		var holdPervent ;
		for(var i in array)
		{
			if(array[i]==$('#appname').val())
			{
				holdPervent = map.get(array[i]);
				holdPervent = holdPervent / 100;
				var showamount = $("#input_amount").val();	
				holdPervent = holdPervent*showamount;
				holdPervent = Math.round(holdPervent * 100) / 100;
				$("#input_showamount").val(holdPervent);
			}
		}
	}
	
    //声明整数的正则表达式
    function isNum(a)
	{
		//var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		var reg = /^[0-9]*[1-9][0-9]*$/i;
		return reg.test(a);
	}
	function subForm() 
	{
		if($("#appname").val()<0)
		{
			alert("请选择应用名");
			$("#appname").focus();
			return;
		}
		
		if(isNullOrEmpty($("#input_fee_date").val()))
		{
			alert("请输入计费日期！");
			$("#input_fee_date").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_newuser").val())) 
		{
			alert("请输入新增用户数");
			$("#input_newuser").focus();
			return;
		}
		
		if(!(isNum($("#input_newuser").val()+1)))
		{
			alert("请输入整数！");
			$("#input_newuser").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_amount").val())) 
		{
			alert("请输入每日收入");
			$("#input_amount").focus();
			return;
		}
		
		if(isNaN($("#input_amount").val()+1))
		{
			alert("请输入数字");
			$("#input_amount").focus();
			return;
		}
		
		/*if (isNullOrEmpty($("#input_shownewuser").val())) 
		{
			alert("请输入展示的新增用户数");
			$("#input_shownewuser").focus();
			return;
		}*/
		
		if(!(isNum($("#input_shownewuser").val()+1)))
		{
			alert("请输入整数！");
			$("#input_shownewuser").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_showamount").val())) 
		{
			alert("请输入展示的每日收入");
			$("#input_showamount").focus();
			return;
		}
		
		if(isNaN($("#input_showamount").val()+1))
		{
			alert("请输入数字");
			$("#input_showamount").focus();
			return;
		}
		
		if (isNullOrEmpty($("#input_extendfee").val())) 
		{
			alert("请输入推广费用");
			$("#input_extendfee").focus();
			return;
		}
		
		if(isNaN($("#input_extendfee").val()+1))
		{
			alert("请输入数字");
			$("#input_extendfee").focus();
			return;
		}
		
		
		
		console.log($("#input_remark").val());
		
		/*if($("#type").val()<0)
		{
			//alert("请选择同步类型");
			$("#type").val(0);
			return;
		}*/
		
		document.getElementById("addform").submit();
	}
	
	
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px">
				<label >添加开发者数据</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="cpappaction.jsp" method="post" id="addform">
				
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd04_me">
						<select name="appname" id="appname" style="width: 200px;" title="选择SP">
							<option value="-1">请选择应用名</option>
							<%
							
							for(AdAppModel app : applist)
							{
								%>
							<option value="<%= app.getId() %>"><%= app.getAppname() %><span  type="hidden"><%=app.getHold_percent() %></span></option>	
							
								<%
							}
							%>
						</select>
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">计费日期</dd>
					<dd class="dd03_me">
						<input type="text" name="fee_date" id="input_fee_date" style="width: 200px" onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">新增用户</dd>
					<dd class="dd03_me">
						<input type="text" name="newuser" id="input_newuser"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">当日收入</dd>
					<dd class="dd03_me">
						<input type="text" name="amount" id="input_amount"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">展示新增用户</dd>
					<dd class="dd03_me">
						<input type="text" name="shownewuser" id="input_shownewuser"
							style="width: 200px">
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">展示每日收入</dd>
					<dd class="dd03_me">
						<input type="text" name="showamount" id="input_showamount"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">推广费用</dd>
					<dd class="dd03_me">
						<input type="text" name="extendfee" id="input_extendfee"
							style="width: 200px">
					</dd>
					
					
					<input type="hidden" name="profit" id="input_profit" 
							style="width: 200px">
					
					
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">同步类型</dd>
					<dd class="dd03_me">
						<input type="radio" name="type" style="width: 35px;float:left" value="0" checked="checked" >
						<label style="font-size: 14px;float:left">未同步</label>
						<input type="radio" name="type" style="width: 35px;float:left" value="1" >
						<label style="font-size: 14px;float:left">已同步</label>
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