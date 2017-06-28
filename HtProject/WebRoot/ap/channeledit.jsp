<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.ChannelServer"%>
<%@page import="com.system.model.ChannelModel"%>
<%@page import="com.system.server.AppServer"%>
<%@page import="com.system.model.AppModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Map<String,Object> name = new AppServer().loadApp();
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	int appnameid = StringUtil.getInteger(request.getParameter("appname"), 0);
	String appkey = StringUtil.getString(request.getParameter("appkey"), "");
	String channel = StringUtil.getString(request.getParameter("channel"), "");
	int type = StringUtil.getInteger(request.getParameter("type_id"),0);
	List<AppModel> applist = (List<AppModel>)name.get("list");
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	int appid = StringUtil.getInteger(request.getParameter("appid"), -1);
	ChannelModel model = new ChannelServer().loadQdById(id);
	String appname = model.getAppname();
	appid = new AppServer().loadIdByName(appname);
	//String appname = StringUtil.getString(request.getParameter("appname"), "");
	
	
	if(model==null)
	{
		response.sendRedirect("channel.jsp");
		return;
	}
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
//声明整数的正则表达式
	function isNum(a)
	{
		//var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
		var reg = /^[0-9]*[1-9][0-9]*$/i;
		return reg.test(a);
	}
	
	$(function()
	{
		resetForm();
	});
	
	function resetForm()
	{
		$("#appid").val("<%= appid %>");
		$("#appname").val("<%= appid %>");
		$("#input_channel").val("<%= model.getChannel() %>");
		$("#input_hold_persent").val("<%= model.getHold_percent() %>");
		$("#input_remark").val("<%= model.getRemark()==null?"": model.getRemark() %>");
		setRadioCheck("settle_type",<%= model.getSettleType()%>);
		setRadioCheck("type",<%= model.getSyn_type()%>);
	}
	
	function subForm() 
	{
		if ($("#input_app_name").val()<0) 
		{
			alert("请选择应用名");
			$("#input_full_name").focus();
			return;
		}
		
		
		if (isNullOrEmpty($("#input_channel").val())) 
		{
			alert("请输入渠道");
			$("#input_channel").focus();
			return;
		} 
		
		if(!(isNum($("#input_hold_persent").val()+1)))
		{
			alert("请输入整数！");
			$("#input_hold_persent").focus();
			return;
		}
		
		var num = parseInt($("#input_hold_persent").val(), 10);
		
		if(num<0||num>100)
		{
			alert("请输入0~100之间的整数");
			$("#input_hold_persent").focus();
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
				<label>渠道更改</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="qdaction.jsp?pageIndex=<%=pageIndex %>&appName=<%=appnameid%>&appKey=<%=appkey%>&Channel=<%=channel%>&Type_Id=<%=type%>" method="post" id="addform">
					<input type="hidden" value="<%= model.getId() %>" name="id" />
					<input type="hidden" value="<%= model.getAppname() %>" name="appid" id="appid"/>
					<input type="hidden" 
					  value="<%= StringUtil.getInteger(request.getParameter("pageindex"), 1) %>" 
					  name="pageindex" />
										
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">应用名</dd>
					<dd class="dd04_me">
						<select name="appname" id="appname" style="width: 200px;" title="选择SP">
							<option value="-1">请选择应用名</option>
							<%
							
							for(AppModel app : applist)
							{
								%>
							<option value="<%= app.getId() %>"><%= app.getAppname() %></option>	
								<%
							}
							%>
						</select>
					</dd>


                    <br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">渠道</dd>
					<dd class="dd03_me">
						<input type="text" name="channel" id="input_channel"
							style="width: 200px">
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd03_me">
						<input type="radio" name="settle_type" style="width: 35px;float:left" value="1" checked="checked" >
						<label style="font-size: 14px;float:left">CPA</label>
						<input type="radio" name="settle_type" style="width: 35px;float:left" value="2" >
						<label style="font-size: 14px;float:left">CPS</label>
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">扣量比</dd>
					<dd class="dd03_me">
						<input type="text" name="hold_persent" id="input_hold_persent"
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
					<dd class="dd01_me">同步类型</dd>
					<dd class="dd03_me">
						<input type="radio" name="type" style="width: 35px;float:left" value="1" checked="checked" >
						<label style="font-size: 14px;float:left">隔天</label>
						<input type="radio" name="type" style="width: 35px;float:left" value="2" >
						<label style="font-size: 14px;float:left">实时</label>
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
						<input type="button" value="重 置" onclick="resetForm()">
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