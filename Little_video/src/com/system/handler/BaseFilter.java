
package com.system.handler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.system.api.baseResponse;

public abstract class BaseFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		String method = ((HttpServletRequest) request).getMethod();
		if (!"POST".equals(method))
		{
			chain.doFilter(request, response);
			return;
		}
		int c = request.getContentLength();
		byte[] bin;
		String data;
		if (c > 0)
		{
			ServletInputStream stm = request.getInputStream();
			bin = new byte[c];
			stm.read(bin);
			stm.close();
			data = new String(bin);
		}
		else
			data="{}";
		

		((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse)response).addHeader("Access-Control-Allow-Methods", "POST");
		
		baseResponse result = ProcessReuqest(data);
		if (result == null)
		{
			response.getWriter().print("{}");
			return;
		}
		String json = result.toString();
		bin = json.getBytes("utf-8");
		response.setCharacterEncoding("utf-8");
		response.getOutputStream().write(bin);
	}

	protected abstract baseResponse ProcessReuqest(String s);

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}
