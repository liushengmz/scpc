<%@page import="com.system.server.ProductServer"%>
<%@page import="com.system.model.ProductModel"%>
<%@page import="com.system.util.Base64UTF"%>

<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);
	String query = StringUtil.getString(request.getParameter("query"), "");

if(type==-1){//添加或修改
	int id = StringUtil.getInteger(request.getParameter("id"), -1);

	int flag=StringUtil.getInteger(request.getParameter("operator"), -1);
	String name=StringUtil.getString(request.getParameter("productline_name"), "");
	ProductModel model = new ProductModel();
	model.setProductLineId(id);
	model.setOperFlag(flag);
	model.setProductLineName(name);
	if(id>0)
	{
		new ProductServer().updateLineProduct(model);
	}
	else
	{
		new ProductServer().addLineProduct(model);
	}
	
	response.sendRedirect("productline.jsp?"+ Base64UTF.decode(query));
	
}
if(type==3){//删除
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	if(id>0){
		new ProductServer().deleteLineProduct(id);

	}
	response.sendRedirect("productline.jsp?"+ Base64UTF.decode(query));

}



	
%>
