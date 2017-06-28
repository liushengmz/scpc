<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.server.GroupRightServer"%>
<%@page import="com.system.model.GroupRightModel"%>
<%@page import="com.system.dao.GroupRightDao"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<GroupRightModel> list = new GroupRightServer().loadGroup();
	List<GroupModel> groupList = new GroupServer().loadAllGroup();
	
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
	function getProvinceCount(items)
	{
		var i = 0;
		$('[name='+items+']:checkbox').each(function() {
			if(this.checked)
				i++;
		});
		return i;
	}
    //声明整数的正则表达式
    function isNum(a)
	{
		var reg = /^[0-9]*[1-9][0-9]*$/i;
		return reg.test(a);
	}
	function subForm() 
	{
		
		
		if($("#group").val()<0)
		{
			alert("请选择角色");
			$("#group").focus();
			return;
		}
		
		if(getProvinceCount('groupid')<=0)
		{
			alert("请选择授权角色");
			return;
		}
		
		
		if(isNullOrEmpty($("#remark").val()))
		{
			alert("请输入备注！");
			$("#remark").focus();
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
				<label>添加角色</label>
				</dd>
			</dl>
			<br />	<br />		
			<dl>
				<form action="groupRightAction.jsp" method="get" id="addform">
				
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">角色</dd>
					<dd class="dd04_me">
						<select name="group" id="group" style="width: 200px;" title="选择group">
							<option value="-1">请选择角色名</option>
							<%
							
							for(GroupRightModel group : list)
							{
								%>
							<option value="<%= group.getGroupId() %>"><%= group.getName() %></option>	
								<%
							}
							%>
						</select>
					</dd>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">授权角色</dd><br />
					<br />
					<table style="text-align: left">
					<%
					int i=0;
					out.println("<tr>");
					for (GroupModel group : groupList)
					{
						if(i%5==0)
							out.print("</tr><tr>");
						
						out.print("<td style=\"text-align: left\"><input type=\"checkbox\" name=\"groupid\" id=\"groupid_" + group.getId() 
						+ "\" value=\"" + group.getId() + "\"></input>&nbsp;&nbsp;" + group.getName() + "</td>");
						
						i++;
					}
					out.println("</tr>");
					%>
					</table>

					<br />
					<br />
					<br />
					<dd class="dd00_me"></dd>
					<dd class="dd01_me">备注</dd>
					<dd class="dd03_me">
						<input type="text" name="remark" id="remark"
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