package com.dianfu.web.HttpHandler;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.Logical.MysqlDatabase;
import com.shotgun.Tools.Funcs;

public class SPCallbackHttpFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		String ua = request.getHeader("User-Agent");
		if (!Funcs.isNullOrEmpty(ua)) {
			if (ua.toLowerCase().contains("alibaba.security.heimdall")) {
				// Alibaba.Security.Heimdall 啊里云扫描
				response.setStatus(410);
				response.getWriter().write("Not Welcome");
				return;
			}
		}

		if (JspExist(request)) {// 存在真实的接入文件
			arg2.doFilter(request, response);
			return;
		}
		// System.out.println("http filter");
		com.database.Interface.IDatabase dbase = new MysqlDatabase();
		try {
			SPCallbackAutoProc spcb = CreateProc(request);
			spcb.setDBase(dbase);
			spcb.set_Request(request);
			spcb.set_Response(response);

			spcb.doProc();
		} finally {
			dbase.close();
		}

	}

	/**创建内置的处理器*/
	private SPCallbackAutoProc CreateProc(HttpServletRequest request) {
		String[] path = request.getRequestURI().split("/");
		String filename = path[path.length - 1].toLowerCase();
		//System.out.println("fileName：" + filename);
		if (filename.startsWith("mmbase"))
			return new SPCallbackMMBaseProc();

		return new SPCallbackAutoProc();
	}

	/** 当前请求是否存在实际处理文件 */
	static boolean JspExist(HttpServletRequest request) {
		String path = request.getServletContext().getRealPath(request.getServletPath());
		return new File(path).exists();
	}

}
