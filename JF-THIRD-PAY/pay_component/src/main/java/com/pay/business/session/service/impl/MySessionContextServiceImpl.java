package com.pay.business.session.service.impl;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.core.teamwork.base.session.RedisSession;
import com.core.teamwork.base.util.SerializableUtils;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.pay.business.session.service.MySessionContextService;

/**
 * @author cyl
 *
 */
@Service("mySessionContextService")
public class MySessionContextServiceImpl implements MySessionContextService {

	/**
	 * @author cyl
	 * @Title: AddSession
	 * @Description: 添加一个session
	 * @param session
	 */
	public void addSession(HttpSession session, String redisKey) {
		if (session != null) {
			session = setSessionToRedis(session, redisKey);// 把session放入redis
		}
	}

	/**
	 * @author cyl
	 * @Title: setSessionToRedis
	 * @Description: 将session放入redis
	 * @param redisSession
	 * @return
	 */
	private HttpSession setSessionToRedis(HttpSession redisSession, String redisKey) {
		String key = String.format(redisKey, redisSession.getId());// 在redis中保存这个session的key
		int maxInactiveInterval = redisSession.getMaxInactiveInterval();
		byte[] bt = SerializableUtils.serialize(redisSession);// 将session序列化
		byte[] btKey = key.getBytes();
		RedisDBUtil.redisDao.set(btKey, bt, maxInactiveInterval);
		return redisSession;
	}

	/**
	 * @author cyl
	 * @Title: getSessionFromRedis
	 * @Description: 从redis中获取session
	 * @param sessionId
	 * @return
	 */
	private HttpSession getSessionFromRedis(String sessionId, String redisKey) {
		String key = String.format(redisKey, sessionId);// 在redis中保存这个session的key
		byte[] btKey = key.getBytes();
		byte[] sessionBt = RedisDBUtil.redisDao.get(btKey);
		if (sessionBt != null && sessionBt.length > 0) {
			RedisSession redisSession = SerializableUtils.deSerialize(sessionBt);// 反序列化
			if (null != redisSession) {
				redisSession.setLastAccessedTime();// 更新这个session的最后访问时间
				return redisSession;
			}
		}
		return null;
	}

	/**
	 * @author cyl
	 * @Title: getSession
	 * @Description: 获取session
	 * @param session_id
	 * @return
	 */
	public HttpSession getSession(String session_id, String redisKey) {
		if (session_id == null)
			return null;
		HttpSession session = getSessionFromRedis(session_id, redisKey);
		return session;
	}

	public HttpSession createSession(String id, String redisKey) {
		HttpSession redisSession = new RedisSession(id);
		addSession(redisSession, redisKey);
		return redisSession;
	}
	/**
	 * 
	 */
	public void delSession(String id, String redisKey) {
		String key = String.format(redisKey, id);// 在redis中保存这个session的key
		byte[] btKey = key.getBytes();
		RedisDBUtil.redisDao.delete(btKey);

	}

}