<%@page import="com.system.server.xy.FeeServer"%>
<%@page import="com.system.server.xy.UserServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), 0);
	int dataRows = StringUtil.getInteger(request.getParameter("datarows"), 0);
	int type = StringUtil.getInteger(request.getParameter("type"), 0);
	float showAmount = StringUtil.getFloat(request.getParameter("showamount"), 0);
	
	if(id==0)
	{
		out.print("NO");
		return;
	}
	
	boolean result = false;
	
	//更新 渠道(CPA) 展示的激活数据
	if(type==0)
		result = new UserServer().updateQdData(id, dataRows);
	
	//更新 游戏CP(CPS) 的展示金额
	if(type==1)
		result = new FeeServer().updateQdFee(id,dataRows);
	
	//更新游戏渠道(CPS)的展示金额
	if(type==2)
		result = new FeeServer().updateChannelFee(id, showAmount);
	
	out.print(result ? "OK" : "NO");
	
 %>



