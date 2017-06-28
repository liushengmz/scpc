<%@page import="com.system.model.CpBankModel"%>
<%@page import="com.system.server.CpBankInfoServer"%>
<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.util.Base64UTF"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int act=StringUtil.getInteger(request.getParameter("act"),-1);
 	if(act==2){
		int id = StringUtil.getInteger(request.getParameter("id"), -1);
		int cpId = StringUtil.getInteger(request.getParameter("cp_id"),-1);
		int type = StringUtil.getInteger(request.getParameter("type"), -1);
		int status = StringUtil.getInteger(request.getParameter("status"), -1);
		
		Map<String,Integer> map= CpBankInfoServer.checkData(cpId,type,status,id);
		int flag=(Integer)map.get("flag");
		out.clear();
		out.print(flag); 
	}else if(act==3){
		String query = StringUtil.getString(request.getParameter("query"), "");
		int id=StringUtil.getInteger(request.getParameter("id"), -1);
		if(id>0){
			new CpBankInfoServer().deleteCpBank(id);
		}
		response.sendRedirect("cpbank.jsp?"+ Base64UTF.decode(query));
	}else{
		int id = StringUtil.getInteger(request.getParameter("id"), -1);
		int cpId=StringUtil.getInteger(request.getParameter("cp_id"), -1);
		int type=StringUtil.getInteger(request.getParameter("type"), -1);
		int status=StringUtil.getInteger(request.getParameter("status"), -1);
		String bankName=StringUtil.getString(request.getParameter("bank_name"), "");
		String bankAccount=StringUtil.getString(request.getParameter("bank_account"), "");
		String bankBranch=StringUtil.getString(request.getParameter("bank_branch"), "");
		String userName=StringUtil.getString(request.getParameter("user_name"), "");
		String query = StringUtil.getString(request.getParameter("query"), "");
		CpBankModel model = new CpBankModel();
		model.setId(id);
		model.setCpId(cpId);
		model.setType(type);
		model.setBankName(bankName);
		model.setUserName(userName);
		model.setBankAccount(bankAccount);
		model.setBankBranch(bankBranch);
		model.setStatus(status);
		if(id>0)
		{
			new CpBankInfoServer().updateCpBank(model);
		}
		else
		{
			new CpBankInfoServer().addCpBank(model);
		}
		
		response.sendRedirect("cpbank.jsp?"+ Base64UTF.decode(query));
	}

	
%>
