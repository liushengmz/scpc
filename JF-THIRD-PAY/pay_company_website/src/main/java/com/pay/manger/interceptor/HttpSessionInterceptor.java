package com.pay.manger.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.session.MySessionContext;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ValidatorUtil;
import com.pay.business.session.service.MySessionContextService;
import com.pay.business.util.ParameterEunm;

/**
 * @ClassName: IClapHttpSessionInterceptor
 * @Description: 处理session共享的问题
 * @author yangwenguang
 * @date 2015-8-18 上午11:54:47
 * 
 */
public class HttpSessionInterceptor implements HandlerInterceptor {

	@Autowired
	private MySessionContextService mySessionContextService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// HttpSession session = request.getSession(false);
		String sessionId = request.getParameter(ParameterEunm.SESSION_ID_PARA_NAME);
		if (ValidatorUtil.isEmpty(sessionId)) {
			sessionId = request.getHeader(ParameterEunm.SESSION_ID_PARA_NAME);
		}
		if (null != request.getAttribute("session_isChange")
				&& "1".equals(request.getAttribute("session_isChange").toString())) {// 将session从缓存中清除掉
			if (ValidatorUtil.isEmpty(sessionId)) {
				HttpSession session = request.getSession(false);
				sessionId = session.getId();
			}
			HttpSession redisSession = MySessionContext.getInstance().mymap.get(sessionId);
			mySessionContextService.addSession(redisSession, ReadPro.getValue("session_redis_key"));
		}
		if (null != request.getAttribute("session_isDelete")
				&& "1".equals(request.getAttribute("session_isDelete").toString())) {// 将session从缓存中清除掉
			if (ValidatorUtil.isEmpty(sessionId)) {
				HttpSession session = request.getSession(false);
				sessionId = session.getId();
			}
			mySessionContextService.delSession(sessionId, ReadPro.getValue("session_redis_key"));
		}
		if (StringUtils.isNotBlank(sessionId)) {
			MySessionContext.getInstance().mymap.remove(sessionId);// 删除缓存
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
}
