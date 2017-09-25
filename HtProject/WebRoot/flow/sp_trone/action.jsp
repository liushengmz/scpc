<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.flow.server.SpApiServer"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.system.flow.model.TroneModel"%>
<%@page import="com.system.flow.server.SpTroneServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.util.JspParamsUtil"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%
	String[] returnUrls = {"sp_trone.jsp"};

	Map<String, String> params = JspParamsUtil.trandPostParams(request);

	int type = StringUtil.getInteger(params.get("type"), 0);

	if(type==1) //增加SP TRONE
	{
		boolean isSucc = new SpTroneServer().addSpTrone(params);
		if(isSucc)
		{
			response.sendRedirect(returnUrls[0]);
		}
		else
		{
			out.println("<script>history.go(-1);alert('增加失败，请联系管理 员')</scirpt>");
		}
		
		return;
	}
	else if(type==2) // 修改 SP TRONE
	{
		Map<String, String> queryMap = JspParamsUtil.trandGetParams(request);
		
		boolean isSucc = new SpTroneServer().updateSpTrone(params);
		
		if(isSucc)
		{
			response.sendRedirect(returnUrls[0] + "?" + Base64UTF.decode(queryMap.get("query")));
		}
		else
		{
			out.println("<script>history.go(-1);alert('修改失败，请联系管理 员')</scirpt>");
		}
		
		return;
		
	}
	else if(type==3) //获取 SP API LIST JSON
	{
		int spId = StringUtil.getInteger(params.get("sp_id"), 0);
		String data = new SpApiServer().loadSpApiJsonBySpId(spId);
		out.print(data);
		return;
	}
	
	out.println("hello boy ~ " + StringUtil.getNowFormat());
%>

