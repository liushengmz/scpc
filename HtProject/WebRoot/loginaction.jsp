<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int loginType = StringUtil.getInteger(request.getParameter("type"), 1);
	int reUserName = StringUtil.getInteger(request.getParameter("rename"), -1);
	int rePwd = StringUtil.getInteger(request.getParameter("repwd"), -1);
	if(loginType==1)
	{
		String userName = StringUtil.getString(request.getParameter("username"), "");
		String password = StringUtil.getString(request.getParameter("pwd"), "");
		
		int loginStatus = RightServer.login(session, userName, password);
		out.clear();
		out.print(loginStatus);
		
		if(loginStatus==1)
		{
			if(reUserName==1)
			{
				Cookie cookie= new Cookie("USER_NAME",userName);

				cookie.setMaxAge(604800);

				response.addCookie(cookie);
				
				if(rePwd==1)
				{
					Cookie cookiePwd= new Cookie("USER_PWD",password.length()==32 ? password : StringUtil.getMd5String(password, 32));

					cookiePwd.setMaxAge(604800);

					response.addCookie(cookiePwd);
				}
				else
				{
					Cookie cookies[] = request.getCookies();
					Cookie sCookie = null;
					String sValue = null;
					String sName = null;
					for(int i=0; i<cookies.length; i++)
					{
						sCookie = cookies[i];
						
						if("USER_PWD".equalsIgnoreCase(sCookie.getName()))
						{
							sCookie.setMaxAge(0);   
					        response.addCookie(sCookie); 
					        break;
						}
					}
				}
			}
			else
			{
				Cookie cookies[] = request.getCookies();
				Cookie sCookie = null;
				String sValue = null;
				String sName = null;
				for(int i=0; i<cookies.length; i++)
				{
					sCookie = cookies[i];
					
					if("USER_NAME".equalsIgnoreCase(sCookie.getName()) || "USER_PWD".equalsIgnoreCase(sCookie.getName()))
					{
						sCookie.setMaxAge(0);   
				        response.addCookie(sCookie); 
					}
				}
			}
		}
	}
	else if(loginType==-1)
	{
		session.setAttribute("user", null);
		
		Cookie cookies[] = request.getCookies();
		Cookie sCookie = null;
		String sValue = null;
		String sName = null;
		for(int i=0; i<cookies.length; i++)
		{
			sCookie = cookies[i];
			
			if("USER_NAME".equalsIgnoreCase(sCookie.getName()) || "USER_PWD".equalsIgnoreCase(sCookie.getName()))
			{
				sCookie.setMaxAge(0);   
		        response.addCookie(sCookie); 
			}
		}
		
		out.print("<script>window.location.href='login.jsp?login=out'</script>");
	}
%>