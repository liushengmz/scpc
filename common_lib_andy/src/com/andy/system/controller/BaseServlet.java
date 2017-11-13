package com.andy.system.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.andy.system.util.ServletUtil;
import com.andy.system.util.StringUtil;

public abstract class BaseServlet extends HttpServlet
{
	protected static Logger log = Logger.getLogger(BaseServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		
		Map<String, String> params = ServletUtil.getRequestParams(request,"UTF-8");
		
		String uri = request.getRequestURI();
		String lastPath = uri.substring(uri.lastIndexOf("/"),uri.length());
		String actionMethod = lastPath.contains(".") ? lastPath.substring(1,lastPath.indexOf(".")) : "";
		
		Object result = null;
		
		if(!StringUtil.isNullOrEmpty(actionMethod))
		{
			try
			{
				Method m = this.getClass().getDeclaredMethod(actionMethod,Map.class,HttpServletRequest.class,HttpServletResponse.class);
				result = m.invoke(this,params, request,response);
			}
			catch(Exception ex)
			{
				log.error(ex.getMessage());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", 404);
				map.put("msg", "request not exist");
				result = map;
			}
		}
		else
		{
			result = handleRequest(params,request,response);
		}
		
		response.getWriter().println(result);
	}
	
	protected abstract Object handleRequest(Map<String, String> params,HttpServletRequest request,HttpServletResponse response);

	
	
}
