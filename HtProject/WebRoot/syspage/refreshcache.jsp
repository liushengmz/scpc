<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.system.util.StringUtil"%>
<%@page import="com.system.cache.RightConfigCacheMgr"%>
<%
	if(!StringUtil.isNullOrEmpty(request.getParameter("isfirst")))
	{
		String[] refs = request.getParameterValues("refchk");
		if(null==refs||refs.length==0)
		{
			out.println("<script>alert('没有要刷新的内容！');</script>");
		}
		else
		{
			for(String str : refs)
			{
				if("right".equalsIgnoreCase(str))
				{
					RightConfigCacheMgr.refreshAllCache();
				}
			}
			out.println("<script>alert('刷新缓存成功！');</script>");
		}
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>缓存刷新管理</title>
	<link rel="stylesheet" href="../css/base.css" type="text/css"></link>
	<script type="text/javascript" src="../js/jquery-1.7.js"></script>
	<script type="text/javascript" src="../js/colorTable.js"></script>
	<script type="text/javascript" src="../js/util.js"></script>
	<style type="text/css">
	html,body
	{
		margin:0px;
		height:100%;
	}
	</style>
	<script type="text/javascript">
	
	function subform()
	{
		var objs = document.getElementsByName("refchk");
		var count=0;
		for(i=0; i<objs.length; i++)
		{
			if(objs[i].checked==true)
				count++;
		}
		if(count==0)
		{
			alert("对不起，请选择要刷新的内容！");
			return;
		}
		$("#mainForm").submit();
	}
	
	</script>
</head>
<body>
	<div style="width:100%;height:100%;">
		<div style="width:100%;height:30px;vertical-align: middle;background-image: url(../tabimgs/tab_05.gif)">
			<table style="width:100%;height: 100%">
				<tr>
					<td align="left" >
						<div style="width:100%;text-align: left;">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="95%" >
										<span  style="text-align: left">当前位置</span>：[系统管理]-[缓存刷新管理]
									</td>
								</tr>
							</table>
						</div>
					</td>
					<td></td>
					<td></td>
				</tr>			
			</table>
		</div>
		<div id="divtable" style="width:100%;text-align: center;">
			<form action="refreshcache.jsp" id="mainForm" method="post">
			<input type="hidden" name="isfirst" value="false" />
			<table border="1" bordercolor="#87CEEB" style="border-collapse: collapse;text-align: center;margin: auto;" width="600px">
				<tr>
					<td colspan="2" align="center"><font style="font-size: 20px">选择要刷新的缓存</font></td>
				</tr>
				<tr>
					<td width="150px">权限配置刷新</td>
					<td>配置刷新<input type="checkbox" name="refchk" value="right" /></td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						全选<input type="checkbox" onclick="checkAll(this,'refchk')" value="menu" />
						<input type="button" value="刷  新" onclick="subform()" />
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>

