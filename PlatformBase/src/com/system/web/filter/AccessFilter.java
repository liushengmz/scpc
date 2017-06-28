package com.system.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.system.cache.RightConfigCacheMgr;
import com.system.model.UserModel;
import com.system.server.RightServer;

public class AccessFilter implements Filter
{

	@Override
	public void destroy()
	{
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String contextpath = request.getContextPath();
		String requestUrl =  request.getRequestURI();
		
		if(requestUrl.equalsIgnoreCase(contextpath + "/"))
		{
			chain.doFilter(servletRequest, servletResponse);
			return;
		}
		
		if(requestUrl.contains(".jsp"))
		{
			requestUrl = requestUrl.replace(contextpath, "");
			
			//如果是公共页面，是不需要登录就可以查看数据的
			if(RightConfigCacheMgr.pubUrlCache.contains(requestUrl))
			{
				chain.doFilter(servletRequest, servletResponse);
				return;
			}
			
			HttpSession session = request.getSession();
			if(session.isNew())
			{
				if(requestUrl.equalsIgnoreCase(contextpath + "/index.jsp"))
				{
					response.sendRedirect(contextpath + "/login.jsp");
					return;
				}
				response.sendRedirect(contextpath + "/syspage/sessionout.html");
				return;
			}
			
			Object obj = session.getAttribute("user");
			if(null==obj)
			{
				response.sendRedirect(contextpath + "/login.jsp");
				return;
			}
			
			if(!RightServer.hadRight((UserModel)obj, requestUrl.substring(1)))
			{
				response.sendRedirect(contextpath + "/syspage/accessdeny.html");
				return;
			}
		}
		
		chain.doFilter(servletRequest, servletResponse);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		
	}
}
