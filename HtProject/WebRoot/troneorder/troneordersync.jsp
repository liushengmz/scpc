<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.CpSpTroneSynModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	CpSpTroneSynModel model = new TroneOrderServer().loadCpSpTroneSynModelById(id);
	if(model==null)
	{
		out.print("<script>close();</script>");
		return;
	}
	String orderNum = new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date());
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
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

	function refresh()
	{
		location.reload();
	}
	
	function subForm()
	{
		var mobile = $('#input_mobile').val();
		var msg = encodeURI($('#input_order').val());
		var troneNum = "<%= model.getTroneNum() %>";
		var linkid = $("#input_linkid").val();
		var cpParams = encodeURI($("#input_cpparams").val());
		
		var url = "<%= model.getCpUrl() %>?mobile=" + mobile + "&msg=" + msg + "&port=" + troneNum + "&linkid=" + linkid + "&cpparam=" + cpParams + "<%= model.getTroneOrderId() > 0 ? "&paycode=" + (100000+id) + "&ordernum=" + orderNum : "" %>";
		
		open(url);
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px" >
				<label>向CP模拟数据</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="troneorderaction.jsp" method="post" id="addform">
					<dd class="dd01_me">CP名称</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= model.getCpName() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP名称</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= model.getSpName() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">SP业务</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= model.getSpTroneName() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<div style="display: <%= model.getTroneOrderId() > 0 ? "block" : "none" %>">
					<br />
					<br />
					<br />
					<dd class="dd01_me">PayCode</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= 100000 + id %>" readonly="readonly"  style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">订单号</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= orderNum %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					</div>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">指令</dd>
					<dd class="dd03_me">
						<input type="text" id="input_order" value="<%= model.getOrder() %>" style="width: 200px;" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd01_me">通道号</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= model.getTroneNum() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">价格</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= model.getPrice() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">同步URL</dd>
					<dd class="dd03_me">
						<input type="text" value="<%= model.getCpUrl() %>" readonly="readonly" style="width: 200px;color: #ccc" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">手机号</dd>
					<dd class="dd03_me">
						<input type="text" id="input_mobile" value="13911600001" style="width: 200px;" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">linkid</dd>
					<dd class="dd03_me">
						<input type="text" id="input_linkid" value="TEST<%= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) %>" readonly="readonly" style="width: 200px;" >
					</dd>
					
					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">CP透参</dd>
					<dd class="dd03_me">
						<input type="text" id="input_cpparams" value="<%= new SimpleDateFormat("HHmmss").format(new Date()) %>"  style="width: 200px;" >
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="模 拟" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="刷 新" onclick="refresh()">
					</dd>
				</form>
			</dl>
		</div>

	</div>
</body>
</html>