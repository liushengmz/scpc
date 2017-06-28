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
	int lineId=StringUtil.getInteger(request.getParameter("product_line"), -1);
	String name=StringUtil.getString(request.getParameter("product_name"), "");
	ProductModel model = new ProductModel();
	
	model.setChildProductId(id);
	model.setChildLineId(lineId);
	model.setChildProductName(name);
	if(id>0)
	{
		new ProductServer().updateProduct(model);
	}
	else
	{
		new ProductServer().addProduct(model);
	}
	
	response.sendRedirect("product.jsp?"+ Base64UTF.decode(query));
	
}
if(type==3){//删除
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	if(id>0){
		new ProductServer().deleteProduct(id);

	}
	response.sendRedirect("product.jsp?"+ Base64UTF.decode(query));

}



	
%>
