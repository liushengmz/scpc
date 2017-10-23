package com.system.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.system.model.BaseApiReturnModel;
import com.system.util.ServletUtil;
import com.system.util.StringUtil;

public abstract class BaseHttpServlet extends HttpServlet
{
	protected Logger log = Logger.getLogger(BaseHttpServlet.class);
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
		
		response.setContentType("text/javascript;charset=UTF-8"); 
		
		Map<String, String> params = ServletUtil.getRequestParams(request);
		
		String uri = request.getRequestURI();
		String lastPath = uri.substring(uri.lastIndexOf("/"),uri.length());
		String actionMethod = lastPath.contains(".") ? lastPath.substring(1,lastPath.indexOf(".")) : "";
		
		Object result = null;
		BaseApiReturnModel baseApiReturnModel = new BaseApiReturnModel();
		baseApiReturnModel.setStatus(200);
		
		if(!StringUtil.isNullOrEmpty(actionMethod))
		{
			try
			{
				Method m = this.getClass().getDeclaredMethod(actionMethod,Map.class,HttpServletRequest.class,HttpServletResponse.class);
				result = m.invoke(this,params, request,response);
			}
			catch(Exception ex)
			{
				baseApiReturnModel.setStatus(404);
				baseApiReturnModel.setMsg("不存在该路径");
				System.out.println(ex.getMessage());
				log.error(ex.getMessage());
			}
		}
		else
		{
			result = handleRequest(params,request,response);
		}
		
		baseApiReturnModel.setData(result);
		
		response.getWriter().println(StringUtil.getJsonFormObject(baseApiReturnModel));
	}
	
	protected abstract Object handleRequest(Map<String, String> params,HttpServletRequest request,HttpServletResponse response);

	
	
}
