package com.pay.manger.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.core.teamwork.base.util.ReadProChange;

public class BasePathIntecaptor extends HandlerInterceptorAdapter {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	String scheme = request.getScheme();
    	String serverName = request.getServerName();
    	int port = request.getServerPort();
        String path = request.getContextPath();
        String basePath = scheme + "://" + serverName + ":" + port + path;
        
    	String https_switch = ReadProChange.getValue("https_switch");
    	if(https_switch!=null&&"1".equals(https_switch)){
    		scheme="https";
    		basePath = scheme + "://" + serverName ;
    	}
        request.getSession().getServletContext().setAttribute("basePath", basePath);
        return true;
    }

}