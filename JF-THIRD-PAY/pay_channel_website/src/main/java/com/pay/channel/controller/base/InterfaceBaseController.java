package com.pay.channel.controller.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.core.teamwork.base.session.MySessionContext;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2Channel;
import com.pay.business.session.service.MySessionContextService;
import com.pay.business.util.ParameterEunm;

/**
 * @ClassName: InterfaceBaseController
 * @Description: 控制器父类
 * @author buyuer
 * @date 2015年11月17日 下午5:38:53
 * 
 */
public class InterfaceBaseController {

	@Autowired
	private MySessionContextService mySessionContextService;

	protected HttpSession getSessionById(String sessionId) {
		return getSessionById(sessionId, true);
	}

	/**
	 * @author cyl
	 * @Title: getSessionById
	 * @Description: 根据sessionid取session值
	 * @param sessionId
	 * @return
	 */
	protected HttpSession getSessionById(String sessionId, boolean create) {
		String redisKey = ReadPro.getValue("session_redis_key");
		HttpSession session = null;
		if (sessionId != null) {
			session = (HttpSession) MySessionContext.getInstance().mymap.get(sessionId);// 先从缓存中取
		}
		if (session == null) {// 如果缓存没有就从redis中取
			session = mySessionContextService.getSession(sessionId, redisKey);
			if (session != null) {
				MySessionContext.getInstance().mymap.put(sessionId, session);// 放缓存
			} else if (session == null && create) {
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				sessionId = request.getSession().getId();// 创建sessionid
				session = mySessionContextService.createSession(sessionId, redisKey);// 创建一个自定义的session
				request.setAttribute(ParameterEunm.SESSION_ID_PARA_NAME, session.getId());
				MySessionContext.getInstance().mymap.put(sessionId, session);
			}
		}
		return session;
	}

	/**
	 * @author cyl
	 * @Title: getSession
	 * @Description: 根据request中的userCertificate 获取session值
	 * @return
	 */
	public HttpSession getSession() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String sessionId = null;
		sessionId = request.getParameter(ParameterEunm.SESSION_ID_PARA_NAME);
		if (sessionId == null) {
			sessionId = (String) request.getAttribute(ParameterEunm.SESSION_ID_PARA_NAME);
		}
		if (sessionId == null && request.getSession(false) != null) {// 支持使用容器自带的sessionid
			sessionId = request.getSession().getId();
		}
		// }
		// 重写的session获取方式得到session对象
		HttpSession session = getSessionById(sessionId, true);
		return session;
	}

	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(String key) {
		HttpSession session = this.getSession();
		return session != null ? (T) session.getAttribute(key) : null;
	}

	public void setSessionAttr(String key, Object value) {
		HttpSession session = this.getSession();
		if (null != session) {
			session.setAttribute(key, value);
			MySessionContext.getInstance().mymap.put(session.getId(), session);
		}

	}

	public void setSessionAttr(String key, Object value, Integer interval) {
		HttpSession session = this.getSession();
		if (null != session) {
			session.setAttribute(key, value);
			session.setMaxInactiveInterval(interval);
			MySessionContext.getInstance().mymap.put(session.getId(), session);
		}

	}

	public void removeSessionAttr(String key) {
		HttpSession session = this.getSession();
		if (session != null)
			session.removeAttribute(key);
	}

	/**
	 * @return
	 * @author buyuer
	 * @Title: getSessionUserId
	 * @Description: 从session中获取已经登录的用户的id
	 */
	protected Long getSessionUserId() {
		Payv2Channel user = getSessionUser();
		if (user != null) {
			return user.getId();
		} else {
			return null;
		}
	}

	/**
	 * @author buyuer
	 * @Title: getSessionUser
	 * @Description: 获取用户
	 * @return
	 */

	protected Payv2Channel getSessionUser() {
		Payv2Channel user = getSessionAttr("user");
		return user;
	}

	protected HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 
	 * @author buyuer
	 * @param <T>
	 * @Title: filterPageObjectList
	 * @Description: 获取分页信息
	 * @param pageObject
	 * @param files
	 * @return
	 */
	protected <T> PageObject<Map<String, Object>> filterPageObjectList(PageObject<T> pageObject, String[] files) {
		PageObject<Map<String, Object>> mapPageObject = new PageObject<Map<String, Object>>();
		mapPageObject.setCurPage(pageObject.getCurPage());
		mapPageObject.setEndPage(pageObject.getEndPage());
		mapPageObject.setPageData(pageObject.getPageData());
		mapPageObject.setDataList(ObjectUtil.getListByFileNames(files, pageObject.getDataList()));
		return mapPageObject;
	}

}