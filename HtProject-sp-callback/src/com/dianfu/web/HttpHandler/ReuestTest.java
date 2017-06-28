package com.dianfu.web.HttpHandler;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ReuestTest implements Filter {
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) {
//		System.out.println("aa");
//		xRequest request = new xRequest(arg0);
//		request.setDBase(new Mysql());
		 
		try {
			arg2.doFilter(arg0, arg1);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
