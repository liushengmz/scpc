package com.pay.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2ChannelMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2ChannelService extends BaseService<Payv2Channel,Payv2ChannelMapper>  {

	public Payv2Channel selectSingle(Payv2Channel t);
	
	public List<Payv2Channel> selectByObject(Payv2Channel t);
	
	/**
     * @author buyuer
     * @Title: loginAdmin
     * @Description: 登录方法
     * @param userName 用户名
     * @param password 密码
     */
	Payv2Channel loginAdmin(String userName, String password);
	
	/**
	 * 渠道商列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2Channel> payv2ChannelList(Map<String, Object> map);
	
	/**
	 * 添加渠道商
	 */
	public Map<String,Object> addChannel(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新渠道商
	 */
	public Map<String,Object> updateChannel(Map<String, Object> map,Payv2Channel payv2Channel) throws Exception;
}
