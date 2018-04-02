<%@page import="com.system.util.ConfigManager"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.constant.Constant"%>
<%@page import="com.system.util.PageUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserModel user = (UserModel)session.getAttribute("user");
	int userId=-1;
	if(user!=null){
		userId=user.getId();
	}
	int flag=new SpTroneServer().checkAdd(userId);
	String keyWord = StringUtil.getString(request.getParameter("keyword"), "");

	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);

	String query = Base64UTF.encode(request.getQueryString());
	
	Map<String, Object> map = new SpTroneServer().loadSpTroneList(pageIndex,keyWord,userId);
	
	List<SpModel> spList = new SpServer().loadSp();

	List<SpTroneModel> list = (List<SpTroneModel>) map.get("list");

	int rowCount = (Integer) map.get("rows");
	
	Map<String,String> params = new HashMap<String,String>();
	
	params.put("keyword", keyWord);
	
	String pageData = PageUtil.initPageQuery("sptrone.jsp", params, rowCount, pageIndex);
	
	String[] troneTypes = {"实时","隔天","IVR","第三方支付"};
	
	//0对公周结；1对公双周结；2对公月结；3对私周结；4对私双周结；5对私月结,6见帐单结,7对公N+1结,8对公N+3结
	String[] jsTypes = {"对公周结","对公双周结","对公N+1结","对私周结","对私双周结","对私月结","见帐单结","对公N+2结","对公N+3结","对公N+5结","对公N+5结","对公N+6结"};
	
	String jiuSuanName = ConfigManager.getConfigData("JIE_SUNA_NAME", "结算率");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script>
<link href="../css/namepicker.css" rel="stylesheet" type="text/css">

<script type="text/javascript">

	var spList = new Array();
	<%
	for(SpModel spModel : spList)
	{
		%>
		spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
		<%
	}
	%>
	
	function onDataSelect(joData) 
	{
		$("#sel_sp").val(joData.id);
	}

	function delSpTrone(id)
	{
		if(confirm('真的要删除吗？'))
		{
			window.location.href = "sptroneaction.jsp?did=" + id;	
		}
	}
	
	$(function()
	{
		
	});
	
	
	function editShowData(editId)
	{
		var curShowRows = $("#hid_" + editId).val();
		
		var newHtml = "<input type='text' id='myput_" + editId + "' style='width:30px;background-color:#CDC5BF;text-align:center;' value='"+ curShowRows +"' />";
		
		newHtml += "<input type='button' value='更新' style='margin-left: 10px' onclick='updateShowData(" + editId + ")'/>";
		 
		newHtml += "<input type='button' value='取消' style='margin-left: 10px' onclick='cancelShowData(" + editId + ")'/>";
		
		$("#span_" + editId).html(newHtml);
	}
	
	function updateShowData(editId)
	{
		var newShowRows = parseFloat($("#myput_" + editId).val());
		
		if(isNaN(newShowRows) || newShowRows>=1 || newShowRows<=0)
		{
			alert("请输入介于0和1之间的数据");
			return;
		}
		
		updateDbData(editId,newShowRows);
	}
	
	function updateDbData(editId,newShowRows)
	{
		$.post("sptroneaction.jsp", 
		{
			type : 1,
			jiesuanlv : newShowRows,
			id :editId 
		}, 
		function(data) 
		{
			data = $.trim(data);
			if ("OK" == data) 
			{
				$("#hid_" + editId).val(newShowRows);		
				$("#span_" + editId).html(newShowRows);
			} 
			else 
			{
				alert("修改失败！请联系管理员");
				$("#span_" + editId).html($("#hid_" + editId).val());
			}
		});
	}
	
	function cancelShowData(editId)
	{
		$("#span_" + editId).html($("#hid_" + editId).val());
	}
	
	function checkUser(comUserId){
		var userId='<%=userId%>';
		if(comUserId==userId){
			return true;
		}else{
			alert("你没有权限修改！");
			return false;
		}
		
	}
	function checkAdd(){
		var flag='<%=flag%>';
		if(flag==1){
			return true;
		}else{
			alert("你没有权限增加SP业务!");
			return false;
		}
	}
</script>
<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<dd class="ddbtn">
					<a href="sptroneadd.jsp" onclick="return checkAdd()">增 加</a>
				</dd>
				<form action="sptrone.jsp" method="get" id="formid">
						<dl>					
						<dd class="dd01_me">关键字</dd>
						<dd class="dd03_me">
							<input name="keyword" id="input_keyword" value="<%= keyWord %>"
								type="text" style="width: 150px">
						</dd>
						<dd class="ddbtn" style="margin-left: 10px; margin-top: 0px;">
							<input class="btn_match" name="search" value="查 询" type="submit">
						</dd>
					</dl>
				</form>
			</dl>
		</div>
		<table cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<td>序号</td>
					<td>SP名称</td>
					<td>业务线</td>
					<td>业务名称</td>
					<td>商务人员</td>
					<td>数据类型</td>
					<td>结算类型</td>
					<td><%= jiuSuanName %></td>
					<td>日限</td>
					<td>月限</td>
					<td>用户日限</td>
					<td>用户月限</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
				<%
					int rowNum = 1;
					String stopStyle = "class=\"StopStyle\"";
					for (SpTroneModel model : list)
					{
				%>
				<tr <%= model.getStatus()==0 ? stopStyle : "" %>>
					<td><%=(pageIndex - 1) * Constant.PAGE_SIZE + rowNum++%>
						<input type="hidden" id="hid_<%= model.getId() %>" value="<%= model.getJieSuanLv() %>" />
					</td>
					<td><%=model.getSpName()%></td>
					<td><%=model.getServoceCodeName() %></td>
					<td><%=model.getSpTroneName()%></td>
					<td><%= model.getCommerceUserName() %></td>
					<td><%= troneTypes[model.getTroneType()]%></td>
					<td><%= jsTypes[model.getJsTypes()] %></td>
					<td ondblclick="editShowData('<%= model.getId() %>')">
						<span id="span_<%= model.getId() %>"><%= model.getJieSuanLv() %></span>
					</td>
					<td><%= model.getDayLimit() %></td>
					<td><%= model.getMonthLimit() %></td>
					<td><%= model.getUserDayLimit() %></td>
					<td><%= model.getUserMonthLimit() %></td>
					<td><%= model.getStatus()==1 ? "开启" : "关闭" %></td>
					<td><a href="sptroneedit.jsp?query=<%= query %>&id=<%= model.getId() %>"  onclick="return checkUser('<%=model.getCommerceUserId()%>')">修改</a></td>
				</tr>
				<%
					}
				%>
			
			<tbody>
				<tr>
					<td colspan="14" class="tfooter" style="text-align: center;"><%=pageData%></td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>