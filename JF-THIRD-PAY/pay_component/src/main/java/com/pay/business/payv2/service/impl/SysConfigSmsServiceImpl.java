package com.pay.business.payv2.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ValidatorUtil;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.sms.GetSMSCodeUtil;
import com.pay.business.payv2.entity.SysConfigSms;
import com.pay.business.payv2.mapper.SysConfigSmsMapper;
import com.pay.business.payv2.service.SysConfigSmsService;
/**
* @ClassName: SysConfigSmsServiceImpl 
* @Description:(手机短信信息实现类) 
* @author zhoulibo
* @date 2016年3月9日 下午4:56:48
*/
@Service("sysConfigSmsService")
public class SysConfigSmsServiceImpl extends BaseServiceImpl<SysConfigSms, SysConfigSmsMapper> implements SysConfigSmsService {
	// 注入当前dao对象
    @Autowired
    private SysConfigSmsMapper sysConfigSmsMapper;
    private int firstCodeLockTime = ReadPro.getValue("firstCodeLockTime",1); // 短信验证码的时间间隔 1-4次全部是每1分钟发一次短信

    private int codeLockTime = ReadPro.getValue("codeLockTime",10); // 后4次 每10分钟发送一次短信

    private int smsEffectiveTime = ReadPro.getValue("smsEffectiveTime",300); //短信有效时间（一般验证码验证使用，默认单位 秒）
    public SysConfigSmsServiceImpl() {
        setMapperClass(SysConfigSmsMapper.class, SysConfigSms.class);
    }
    /*
     * (非 Javadoc) <p>Title: createCode</p> <p>Description: </p>
     * 
     * @param phone
     * 
     * @param userId
     * 
     * @param type
     * 
     * @see cn.iyizhan.teamwork.sysconfig.service.SysConfigSmsService#createCode(java.lang.String, int, int)
     */
    public int createCode(String phone, Long userId, int type, int appId) {
        int num = lockSms(userId, type);
        if (num >0) {
            return num; //已锁住
        }
        // 先清空所有之前未验证的短信
        SysConfigSms configSms = new SysConfigSms();
        configSms.setUserId(userId);
        configSms.setSmsType(type);
        configSms.setIsVerification(2);
        sysConfigSmsMapper.updateByEntity(configSms);
        GetSMSCodeUtil smsutil = GetSMSCodeUtil.getInstence();
        String  code = smsutil.sendSSmsDydx(phone.toString()); // 发送短信
        if (ValidatorUtil.isNotEmpty(code)) {
            // 将短信验证码存入数据库
            configSms.setSmsCode(code);
//            configSms.setAppId(appId);
            configSms.setSmsType(type);
            configSms.setIsVerification(0);
            configSms.setSmsEffectiveTime(smsEffectiveTime);
            configSms.setCreateTime(new Date());
            configSms.setPhoneNumber(phone);
            sysConfigSmsMapper.insertByEntity(configSms);
            
            return 0;
        } else {
            return -1;
        }
    }
	/**
	* @Title: checkSms 
	* @Description:短信验证验证码
	* @param phone
	* @param userId
	* @param type
	* @param code
	* @return    设定文件 
	* @return boolean    返回类型 
	* @date 2016年3月10日 上午10:22:01 
	* @throws
	 */
	 public boolean checkSms(String phone, Long userId, int type, String code, int appId){
	        SysConfigSms sms = new SysConfigSms();
//	        sms.setAppId(appId);
	        sms.setUserId(userId);
	        sms.setSmsType(type);
	        sms.setPhoneNumber(phone);
	        sms.setSmsCode(code);
	        sms.setIsVerification(0);
	        sms = sysConfigSmsMapper.selectSingle(sms);
	        if (sms != null) {
	            Long curr_time = System.currentTimeMillis();
	            Long sms_time = sms.getCreateTime().getTime();
	            int  timeNum = sms.getSmsEffectiveTime();
	             if (curr_time > (sms_time + timeNum*1000)) {
	                 sms.setIsVerification(2);
	                 sysConfigSmsMapper.updateByEntity(sms);
	                return false;
	            }else{
	                return true;
	            }
	        }
	        return false;
	    }
	 
	 @Override
		public PageObject<SysConfigSms> getPageSysConfigSms(Map<String, Object> map)
				throws Exception {
			SysConfigSms sysConfigSms = M2O(map);
			int totalData = sysConfigSmsMapper.getCount(sysConfigSms);
			PageHelper pageHelper = new PageHelper(totalData, map);
			List<SysConfigSms> list = sysConfigSmsMapper.pageQueryByObject(pageHelper.getMap());
			PageObject<SysConfigSms> pageObject = pageHelper.getPageObject();
			pageObject.setDataList(list);
			return pageObject;
		}
	/**
     * 
     * @author zhoulibo
     * @Title: lockSms
     * @Description: 判断是否锁定发短信机制
     */
    private int lockSms(Long userId, int type){
        String redisKey = String.format(SysConfigSms.REDIS_ERROR_SMS_KEY, userId, type);
        // 先检查redis中的错误次数
        String numStr = RedisDBUtil.redisDao.get(redisKey);
        RedisDBUtil.redisDao.increase(redisKey);
        if (ValidatorUtil.isNotEmpty(numStr)) {
            Long num = Long.valueOf(numStr);
            // 判断是否大于3， 如果大于3限制10分钟，并且设置最大值为4
            if (num > 3) {
            	RedisDBUtil.redisDao.setString(redisKey, "4", 60*codeLockTime);
                return 4;
            }else{
                RedisDBUtil.redisDao.expire(redisKey, 60*firstCodeLockTime);
                return 1;
            }
        }else{
            RedisDBUtil.redisDao.expire(redisKey, 60*firstCodeLockTime);
            return 0;
        }
    }
    
}
