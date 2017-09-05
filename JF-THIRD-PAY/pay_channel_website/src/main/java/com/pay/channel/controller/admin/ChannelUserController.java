package com.pay.channel.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.encrypt.PasswordEncoder;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2Channel;
import com.pay.business.payv2.service.Payv2ChannelService;
import com.pay.business.util.ParameterEunm;
import com.pay.channel.controller.base.InterfaceBaseController;
import com.pay.channel.util.LoginVO;
import com.pay.channel.util.ReturnMsgTips;

@Controller
@RequestMapping("/channelUser/*")
public class ChannelUserController extends InterfaceBaseController{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2ChannelService payv2ChannelService;

	/**
	 * @Title: channeluserLogin
	 * @Description:渠道商用户登录
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2017年2月23日 上午9:34:37
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/login")
	public Map<String, Object> login(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {
				"admPassword", "admTel"}, map);
		if (isNotNull) {
			String password = map.get("admPassword").toString();
			String userMobile = map.get("admTel").toString();
			
			try {
					password = MD5.GetMD5Code(password);
					Payv2Channel payv2Channel = new Payv2Channel();
					payv2Channel.setChannelContactPhone(userMobile);
					payv2Channel = payv2ChannelService.selectSingle(payv2Channel);
					if (payv2Channel != null) {
						// 有效
						if (payv2Channel.getIsDelete().intValue() == 2) {
							// 判断密码是否相同
							if (payv2Channel.getChannelLoginPwd().equals(password)) {
								// 登录成功，写入日志
								getSession().setAttribute("user", payv2Channel);
								LoginVO vo = new LoginVO();
								vo.setUserCertificate(getSession().getId());
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, vo);
								logger.info("渠道商用户登录成功........");
							} else {
								// 手机号与密码不匹配
								resultMap = ReMessage
										.resultBack(
												ParameterEunm.FAILED_CODE,
												null,ReturnMsgTips.FAIL_PHONE_PASSWORD_NOT_MATCHING);
							}
						} else {
							// 帐号无效或锁定
							resultMap = ReMessage.resultBack(
									ParameterEunm.FAILED_CODE,
									null,ReturnMsgTips.FAIL_ID_IS_LOCK);
						}
					} else {
						// 该手机号未注册
						resultMap = ReMessage.resultBack(
								ParameterEunm.FAILED_CODE,null,
								ReturnMsgTips.FAIL_PHONE_NOT_REG);
					}
			} catch (Exception e) {
				logger.error("渠道商用户登录异常........ ");
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,
						ReturnMsgTips.ERROR_SERVER_ERROR);
			}
		}else{
			logger.info("渠道商用户登录参数无效........");
			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
		}
		return resultMap;
	}

	public static void main(String[] args) {
		System.out.println(MD5.GetMD5Code("123456"));
		System.out.println(MD5.GetMD5Code("e10adc3949ba59abbe56e057f20f883e"));
		System.out.println();
		System.out.println(PasswordEncoder.getPassword("123456"));
		System.out.println(PasswordEncoder.getPassword("e10adc3949ba59abbe56e057f20f883e"));
	}
}
