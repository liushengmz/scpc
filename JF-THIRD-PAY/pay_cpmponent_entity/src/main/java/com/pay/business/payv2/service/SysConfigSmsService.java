package com.pay.business.payv2.service;

import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.SysConfigSms;
import com.pay.business.payv2.mapper.SysConfigSmsMapper;

/**
* @ClassName: SysConfigSmsService 
* @Description:(手机短信service) 
* @author zhoulibo
* @date 2016年3月9日 下午4:57:11
*/
public interface SysConfigSmsService extends BaseService<SysConfigSms,SysConfigSmsMapper>  {
	
	/**
     * 获取验证码
     *
     * @param phone 手机号码
     * @param userId 用户id
     * @param type 类型
     * @param appId 项目id
     * @return -1:异常 0:成功 1-4:锁定60秒  >4:锁定600秒
     */
    public int createCode(String phone, Long userId, int type, int appId);
    /**
     * 
     * @author buyuer
     * @Title: checkSms
     * @Description: 从数据库查询验证码
     * @param phone 手机号码
     * @param userId 用户id
     * @param type 类型
     * @param code 验证码
     * @return
     */
    public boolean checkSms(String phone, Long userId, int type, String code, int appId);
    
	/**
	 * @Title: getPageSysConfigSms
	 * @Description: 分页获取手机验证码信息
	 * @param map
	 * @return
	 * @throws Exception
	 *             设定文件
	 * @return PageObject<SysConfigSms> 返回类型
	 * @date 2016年5月24日 下午2:26:44
	 * @throws
	 */
	public PageObject<SysConfigSms> getPageSysConfigSms(
			Map<String, Object> map) throws Exception;
}
