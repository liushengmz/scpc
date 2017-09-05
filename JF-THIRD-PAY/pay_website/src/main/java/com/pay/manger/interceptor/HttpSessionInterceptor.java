package com.pay.manger.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.session.MySessionContext;
import com.core.teamwork.base.util.ReadPro;
import com.pay.business.session.service.MySessionContextService;


/** 
 * @ClassName: IClapHttpSessionInterceptor
 * @Description: 处理session共享的问题
 * @author yangwenguang
 * @date 2015-8-18 上午11:54:47
 * 
 */
public class HttpSessionInterceptor implements HandlerInterceptor{

	@Autowired
	private MySessionContextService mySessionContextService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String servletPath = request.getServletPath(); // 请求的相对url
		if(servletPath.startsWith("/payv2BussCompanyShop")){
			
		}else{
			HttpSession session = request.getSession(false);
			if (session != null) {// 将session从缓存中清除掉
				HttpSession redisSession = MySessionContext.getInstance().mymap.get(session.getId());
				mySessionContextService.addSession(redisSession,ReadPro.getValue("session_redis_key"));
				MySessionContext.getInstance().mymap.remove(session.getId());// 删除缓存
				session.invalidate();
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
