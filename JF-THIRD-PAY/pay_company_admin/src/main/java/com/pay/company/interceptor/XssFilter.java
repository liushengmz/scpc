package com.pay.company.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @Title: XssFilter.java 
* @Package com.pay.company.interceptor 
* @Description: 防止XSS攻击
* @author ZHOULIBO   
* @date 2017年5月27日 下午5:41:33 
* @version V1.0
*/
public class XssFilter implements Filter {
	private static final String X_FRAME_VALUE = "SAMEORIGIN";
	private static final String X_FRAME_HEADER = "X-FRAME-OPTIONS";

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		filterClickJack(servletResponse);
		XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
		filterChain.doFilter(wrapper, servletResponse);
	}

	// Prevent to load in iframe
	private void filterClickJack(ServletResponse servletResponse) {
		if (servletResponse instanceof HttpServletResponse) {
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			if (!httpServletResponse.containsHeader(X_FRAME_HEADER)) {
				httpServletResponse.addHeader(X_FRAME_HEADER, X_FRAME_VALUE);
			}
		}
	}
}
