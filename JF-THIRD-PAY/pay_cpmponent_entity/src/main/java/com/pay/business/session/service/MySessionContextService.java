package com.pay.business.session.service;

import javax.servlet.http.HttpSession;

public interface MySessionContextService {
	/**
	 * @Title: addSession 
	 * @Description: 添加一个session在redis
	 * @param @param session
	 * @param @param redisKey    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void addSession(HttpSession session,String redisKey);
	
	/**
	 * @Title: getSession 
	 * @Description:在redis里面获取session
	 * @param @param session_id
	 * @param @param redisKey
	 * @param @return    设定文件 
	 * @return HttpSession    返回类型 
	 * @throws
	 */
	public HttpSession getSession(String session_id,String redisKey);
	/**
	 * @Title: createSession 
	 * @Description:在redis里面创建一个session
	 * @param @param id
	 * @param @param redisKey
	 * @param @return    设定文件 
	 * @return HttpSession    返回类型 
	 * @throws
	 */
	public HttpSession createSession(String id,String redisKey);
	/**
	 * 
	 * @Title: delSession 
	 * @Description:删除redis的session记录
	 * @param @param id
	 * @param @param redisKey    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void delSession(String id,String redisKey);

	
}
