<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.system.model.TroneOrderModel"%>
<%@page import="com.system.server.TroneOrderServer"%>
<%@page import="com.system.model.SpTroneModel"%>
<%@page import="com.system.server.SpTroneServer"%>
<%@page import="com.system.server.TroneServer"%>
<%@page import="com.system.model.TroneModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if(session.getAttribute("user")==null)
	{
		response.sendRedirect("login.jsp");
		return;
	}

	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	
	//取得SP业务对应的通道
	if(type==1)
	{
		String listName = StringUtil.getString(request.getParameter("arrname"), "");
		
		int spTroneId = StringUtil.getInteger(request.getParameter("sptroneid"), -1);
		
		String returnValue = "";
		
		if(StringUtil.isNullOrEmpty(listName) || spTroneId == -1)
		{
			return;
		}
		
		List<TroneModel> list = new TroneServer().loadTrone(spTroneId);
		
		TroneModel troneModel = null;
		
		for(int i=0; i<list.size(); i++)
		{
			troneModel = list.get(i);
			returnValue += listName + ".push(new joBaseSelectOption(" + troneModel.getId() + ",'"+ troneModel.getTroneName() 
				+ "-" + troneModel.getOrders() + "-" + troneModel.getPrice() +"-"+ (troneModel.getStatus()==1 ? "启用":"停用") +"'));";
		}
		
		out.println(returnValue);
	}
	//取得SP对应的业务
	else if(type==2)
	{
		int spId = StringUtil.getInteger(request.getParameter("spid"), -1);
		
		String listName = StringUtil.getString(request.getParameter("arrname"), "");
		
		if(StringUtil.isNullOrEmpty(listName) || spId<0)
			return;
		
		List<SpTroneModel> list = new SpTroneServer().loadSpTroneList(spId);
		
		String returnValue = "";
		
		for(int i=0; i<list.size();i++)
		{
			returnValue += listName + ".push(new joBaseSelectOption(" + list.get(i).getId() + ",'"+ list.get(i).getSpTroneName() + "'));";
		}
		
		out.println(returnValue);
	}
	//取得SP业务对应的CP业务
	else if(type==3)
	{
		int spTroneId = StringUtil.getInteger(request.getParameter("sp_trone_id"), -1);
		
		if(spTroneId<=0)
			return;
		
		String returnValue = "";
		
		List<TroneOrderModel> list = new TroneOrderServer().loadTroneOrderListBySpTroneId(spTroneId);
		
		for(int i=0; i<list.size(); i++)
		{
			returnValue += "troneOrderArray.push(JSON.parse('" + JSONObject.fromObject(list.get(i)) +"'));";
		}
		
		out.println(returnValue);
	}
	//取得该通道已经分配给那些CP，一并列出来
	else if(type==4)
	{
		int troneId = StringUtil.getInteger(request.getParameter("troneid"), -1);
		
		if(troneId<=0)
			return;
		
		String returnValue = "";
		
		List<TroneOrderModel> list = new TroneOrderServer().loadTroneOrderListByTroneId(troneId);
		
		for(int i=0; i<list.size(); i++)
		{
			returnValue += "cpTroneOrderArray.push(JSON.parse('" + JSONObject.fromObject(list.get(i)) +"'));";
		}
		
		out.println(returnValue);
	}
	//取得CP对应的SP业务
	else if(type==5)
	{
		int cpId = StringUtil.getInteger(request.getParameter("cp_id"), -1);
		List<SpTroneModel> list = new TroneOrderServer().loadSpTroneListByCpId(cpId);
		String returnValue = "";
		if(!list.isEmpty())
		{
			for(SpTroneModel model: list)
				returnValue += model.getId() + ",";
			
			returnValue = returnValue.subSequence(0, returnValue.length()-1).toString();
		}
		out.print(returnValue);
	}
	
	
	
%>	



















