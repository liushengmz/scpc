<%@page import="com.system.flow.server.CpRatioServer"%>
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
	String[] returnUrls = {"cp_ratio.jsp"};

	Map<String, String> params = JspParamsUtil.trandPostParams(request);

	int type = StringUtil.getInteger(params.get("type"), 0);

	if(type==2) //修改合作CP的折扣
	{
		System.out.println(params);
		
		boolean isSucc = new CpRatioServer().updateCpRation(params);
		
		if(isSucc)
		{
			response.sendRedirect(returnUrls[0] + "?cp_id=" + params.get("cp_id"));
			return;
		}
		else
		{
			out.println("<script>alert('修改失败，请联系管理 员');history.go(-1);</script>");
		}
		
		return;
		
	}
	response.sendRedirect(returnUrls[0]);
%>

