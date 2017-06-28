<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String guildTitle = StringUtil
			.getString(request.getParameter("title"), "运营管理平台");

	//guildTitle = new String(guildTitle.getBytes("ISO-8859-1"),"UTF-8");
	
	/*
	String s = guildTitle;
		String[] charcs = {"GBK","GB2312","UTF-8","ISO-8859-1"};
		for(int i=0; i<charcs.length; i++)
		{
			for(int j=charcs.length-1; j>=0;j--)
			{
				if(i!=j)
				{
					System.out.println(i + "--" + j + "--" + new String(s.getBytes(charcs[i]),charcs[j]));
				}
			}
		}
	*/
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<body>
	<div class="main_content">
		<div class="content">
			<dl>
				<form
					action="http://mange.n8wan.com/index.php/Notes/Orders/saveorder"
					method="post" id="form">

					<dd class="dd01">计费指令</dd>
					<dd class="dd03">
						<input type="text" name="zname" title="计费指令" class="">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">计费金额</dd>
					<dd class="dd03">
						<input type="text" name="amount" title="计费金额">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">通道号码</dd>
					<dd class="dd03">
						<input type="text" name="NetWayNum" title="通道号码">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">通道指令</dd>
					<dd class="dd03">
						<input type="text" name="ComText" title="指令内容">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">指令模糊位数</dd>
					<dd class="dd03">
						<input type="text" name="smsplace" title="指令模糊位数" value="0">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">屏蔽号码</dd>
					<dd class="dd03">
						<input type="text" name="passNumber" title="屏蔽号码">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">服务商</dd>
					<dd class="dd04">
						<select name="op" id="op" title="服务商">
							<option value="1">移动</option>
							<option value="2">联通</option>
							<option value="3">电信</option>
						</select>
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">动态地址</dd>
					<dd class="dd03">
						<input type="text" name="url" title="动态地址">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">是否验证IMSI</dd>
					<dd class="dd04">
						是<input type="radio" value="1" name="verifyImsi"> 否<input
							type="radio" value="0" checked="checked" name="verifyImsi">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">是否验证基站</dd>
					<dd class="dd04">
						是<input type="radio" value="1" name="verifyLac"> 否<input
							type="radio" value="0" checked="checked" name="verifyLac">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">是否启用</dd>
					<dd class="dd04">
						是<input type="radio" value="1" name="state"> 否<input
							type="radio" value="0" checked="checked" name="state">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">权重</dd>
					<dd class="dd03">
						<input type="text" value="0" name="roles" title="权重">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">独立日限</dd>
					<dd class="dd03">
						<input type="text" value="0" name="day" title="独立日限">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">独立月限</dd>
					<dd class="dd03">
						<input type="text" value="0" name="month" title="独立月限">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">屏蔽开始时间</dd>
					<dd class="dd03">
						<input type="text" value="0" name="start_hour" title="开始时间">
					</dd>

					<dd class="dd00"></dd>
					<dd class="dd01">屏蔽结束时间</dd>
					<dd class="dd03">
						<input type="text" value="0" name="end_hour" title="结束时间">
					</dd>

					<dd class="dd00"></dd>
					<dd class="ddbtn" style="margin-left: 32px;">
						<input type="submit" value="提 交">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px;">
						<input type="button" value="返 回" onclick="btnChannel();">
					</dd>

				</form>
			</dl>
		</div>

	</div>
</body>
</html>