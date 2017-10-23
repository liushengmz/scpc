package com.pay.manger.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.core.teamwork.base.session.MySessionContext;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ValidatorUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.session.service.MySessionContextService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.annotation.LoginValidate;
import com.pay.manger.util.ReturnMsgTips;

/**
 * 
 * @ClassName: SecurityInterceptor
 * @Description: spring拦截器
 * @author buyuer
 * @date 2015年10月8日 下午3:34:53
 * 
 */
public class SecurityInterceptor implements HandlerInterceptor {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MySessionContextService mySessionContextService;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		/*Map<String, Object> resultMap = new HashMap<String, Object>();
		Class<?> targetClass = handler.getClass();
		if (targetClass.isAssignableFrom(HandlerMethod.class)) {
			HandlerMethod targetMethod = (HandlerMethod) handler;
			LoginValidate loginValidate = targetMethod.getMethodAnnotation(LoginValidate.class);
			// 对以@NeedLogin标注的类或方法进行session校验
			if (targetClass.isAnnotationPresent(LoginValidate.class) || loginValidate != null) {
				boolean isLogin = true;
				String sessionId = null;
				sessionId = req.getParameter(ParameterEunm.SESSION_ID_PARA_NAME);
				if (sessionId == null) {
					sessionId = (String) req.getAttribute(ParameterEunm.SESSION_ID_PARA_NAME);
				}
				if (sessionId == null && req.getSession(false) != null) {// 支持使用容器自带的sessionid
					sessionId = req.getSession().getId();
				}
				if (ValidatorUtil.isNotEmpty(sessionId)) {
					String redisKey = ReadPro.getValue("session_redis_key");
					HttpSession session = (HttpSession) MySessionContext.getInstance().mymap.get(sessionId);// 先从缓存中取
					if (session == null) {// 如果缓存没有就从redis中取
						session = mySessionContextService.getSession(sessionId, redisKey);
					}
					if (session == null) {
						isLogin = false;
						logger.error("目前还没有登录");
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, ReturnMsgTips.USER_NOT_LOGIN);
					}
				} else {
					isLogin = false;
					logger.error("目前还没有登录");
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, ReturnMsgTips.USER_NOT_LOGIN);
				}

				if (!isLogin) {
					// // String accept=req.getHeader("Accept");
					// // String url=req.getHeader("Referer");//获取页面地址
					//
					// if(accept.indexOf("application/json")>-1){// json 数据 AJAX请求
					// // Record re = ModelUtil.resultBack(ParameterEunm.ERROR_LOGIN_CODE, null);
					// // ai.getController().renderJson(re);
					// }
					// else if(accept.indexOf("application/xml")>-1){//非AJAX请求
					// // if(StringUtils.isNotBlank(url) && !url.endsWith("regFinish")){
					// // ai.getController().getSession().setAttribute("from", url);
					// // }
					// // ai.getController().forwardAction("/user/loginPage");
					// }
					// else{//AJAX请求
					// // ai.getController().renderHtml("<script>window.location.href='/user/loginPage?from='+window.location.href;</script>");
					// }

					res.setCharacterEncoding("UTF-8");
					res.setContentType("application/json; charset=utf-8");
					PrintWriter out = null;
					try {
						out = res.getWriter();
						out.append(JSON.toJSONString(resultMap));
						return false;
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (out != null) {
							out.close();
						}
					}
					return false;
				}
			}
		}*/
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception arg3) throws Exception {
	}
}
