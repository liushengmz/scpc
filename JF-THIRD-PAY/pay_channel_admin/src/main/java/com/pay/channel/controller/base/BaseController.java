package com.pay.channel.controller.base;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pay.business.merchant.entity.Payv2Channel;

public class BaseController {
	
	/**
     * @Description: 文件临时目录
     * @param @return   
     * @return String  
     * @throws
     * @author pengzhihao
     * @date 2016-7-4下午3:25:54
     */
	protected String getFileTempDir(HttpServletRequest request){
    	 return request.getSession().getServletContext().getRealPath("/") + "temp" + File.separator 
    			+ System.currentTimeMillis() + File.separator;
    }
	
	protected HttpSession getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }
	
	protected Payv2Channel getSessionUser(){
		return (Payv2Channel)getSession().getAttribute("admin");
	}
	
	protected Long getSessionUserId(){
		Payv2Channel admin = getSessionUser();
		if(admin!=null){
			return admin.getId();
		}else{
			return null;
		}
	}
	
	protected Object getSessionAttr(String attr) {
		return getSession().getAttribute(attr);
	}
	
	protected void setSessionUser(HttpServletRequest request,Payv2Channel channel){
		request.getSession().setAttribute("admin", channel);
	}
}
