package com.pay.manger.controller.merchantApp;

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
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2BussCompany;
import com.pay.business.payv2.service.Payv2BussCompanyService;
import com.pay.business.payv2.service.SysConfigSmsService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.annotation.LoginValidate;
import com.pay.manger.controller.admin.SessionVO;
import com.pay.manger.controller.base.InterfaceBaseController;
import com.pay.manger.util.PhoneCodeTypeUtil;

/**
 * 
 * @ClassName: LoginAppController
 * @Description:商户APP登录注册相关业务处理Controller
 * @author zhoulibo
 * @date 2017年3月3日 上午9:22:54
 */
@Controller
@RequestMapping("/user/")
public class LoginAppController extends InterfaceBaseController {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;// 商户表
	@Autowired
	private SysConfigSmsService configSmsService;// 发送短信表

	/**
	 * 
	 * @Title: userLogin
	 * @Description:商户登录
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("userLogin")
	public Map<String, Object> userLogin(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "userMobile", "password" }, map);
			if (isNotNull) {
				String userMobile = map.get("userMobile").toString();
				String password = map.get("password").toString();
				password = MD5.GetMD5Code(password);
				// 判断手机号码是否注册过
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setUserName(userMobile);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					// 判断商户是否可以用:
					if (payv2BussCompany.getIsDelete() == 2 && payv2BussCompany.getCompanyStatus() == 2) {
						if (payv2BussCompany.getPassWord().equals(password)) {
							// 登录成功
							// 返回前端用户基础数据
							resultMap.put("userId", payv2BussCompany.getId());
							resultMap.put("userMobile", userMobile);
							resultMap.put("realName", payv2BussCompany.getContactsName());
							resultMap.put("userCertificate", getSession().getId());
							boolean havePayPass=false;
							if (payv2BussCompany.getPayPassWord() != null) {
								havePayPass=true;
								resultMap.put("havePayPass",havePayPass);
							} else {
								havePayPass=false;
								resultMap.put("havePayPass",havePayPass);
							}
							resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
							// 存入session
							// getSession().setAttribute("user",
							// payv2BussCompan);
							SessionVO user = new SessionVO();
							user.setUser(payv2BussCompany);
//							user.setHavePayPass(havePayPass);
							setSessionAttr("user", user, Integer.MAX_VALUE);
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "密码错误");
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "账户已无效，有疑问请致电客户");
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：userLogin", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getSendSMSCode
	 * @Description:忘记密码--》获取短信验证码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("getSendSMSCode")
	public Map<String, Object> getSendSMSCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "userMobile" }, map);
		if (isNotNull) {
			// 查询手机号码是否已经注册
			String userMobile = map.get("userMobile").toString();
			Payv2BussCompany payv2BussCompan = new Payv2BussCompany();
			payv2BussCompan.setUserName(userMobile);
			payv2BussCompan = payv2BussCompanyService.selectSingle(payv2BussCompan);
			if (payv2BussCompan != null) {
				// 判断此商户是否有效
				if (payv2BussCompan.getIsDelete() == 2 && payv2BussCompan.getCompanyStatus() == 2) {
					// 发送短信
					int status = configSmsService.createCode(userMobile, payv2BussCompan.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PWD, (int) 1);
					if (status == 0) {
						// 发送成功
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
					} else if (status == 1 || status == 2 || status == 3 || status == 4) {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "错误次数超限，锁定60秒");
					} else if (status > 4) {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "错误次数超限，锁定600秒");
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "账户已无效，有疑问请致电客户");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
		}
		return resultMap;
	}

	/**
	 * 
	 * @Title: checkSMSCode
	 * @Description:忘记密码--》验证手机验证码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("checkSMSCode")
	public Map<String, Object> checkSMSCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "userMobile", "smsCode" }, map);
		if (isNotNull) {
			// 查询手机号码是否已经注册
			String userMobile = map.get("userMobile").toString();
			String smsCode = map.get("smsCode").toString();
			Payv2BussCompany payv2BussCompan = new Payv2BussCompany();
			payv2BussCompan.setUserName(userMobile);
			payv2BussCompan = payv2BussCompanyService.selectSingle(payv2BussCompan);
			if (payv2BussCompan != null) {
				// 判断此商户是否有效
				if (payv2BussCompan.getIsDelete() == 2 && payv2BussCompan.getCompanyStatus() == 2) {
					// 短信验证验证码
					boolean isOk = configSmsService.checkSms(userMobile, payv2BussCompan.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PWD, smsCode, (int) 1);
					if (isOk) {
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE, null);
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "账户已无效，有疑问请致电客户");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
		}
		return resultMap;
	}

	/**
	 * 
	 * @Title: submitNewPssword
	 * @Description:忘记密码--》新密码数据提交
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("submitNewPssword")
	public Map<String, Object> submitNewPssword(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "userMobile", "smsCode", "newPassword" }, map);
			if (isNotNull) {
				// 查询手机号码是否已经注册
				String userMobile = map.get("userMobile").toString();
				String smsCode = map.get("smsCode").toString();
				String newPassword = map.get("newPassword").toString();
				newPassword = MD5.GetMD5Code(newPassword);
				Payv2BussCompany payv2BussCompan = new Payv2BussCompany();
				payv2BussCompan.setUserName(userMobile);
				payv2BussCompan = payv2BussCompanyService.selectSingle(payv2BussCompan);
				if (payv2BussCompan != null) {
					// 判断此商户是否有效
					if (payv2BussCompan.getIsDelete() == 2 && payv2BussCompan.getCompanyStatus() == 2) {
						// 短信验证验证码
						boolean isOk = configSmsService.checkSms(userMobile, payv2BussCompan.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PWD, smsCode, (int) 1);
						if (isOk) {
							// 判断新旧密码是否一样
							if (!newPassword.equals(payv2BussCompan.getPassWord())) {
								payv2BussCompan.setPassWord(newPassword);
								payv2BussCompanyService.update(payv2BussCompan);
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
							} else {
								resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "不能与历史密码相同");
							}
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE, null);
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "账户已无效，有疑问请致电客户");
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：submitNewPssword", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getResetSendSMSCode
	 * @Description:重置密码--》获取短信验证码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getResetSendSMSCode")
	public Map<String, Object> getResetSendSMSCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = getSessionUserId();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setId(id);
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
		if (payv2BussCompany != null) {
			// 发送短信
			int status = configSmsService.createCode(payv2BussCompany.getUserName(), payv2BussCompany.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PWD, (int) 1);
			if (status == 0) {
				// 发送成功
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} else if (status == 1 || status == 2 || status == 3 || status == 4) {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "错误次数超限，锁定60秒");
			} else if (status > 4) {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "错误次数超限，锁定600秒");
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
			}

		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
		}
		return resultMap;
	}

	/**
	 * @Title: checkResetSMSCode
	 * @Description:重置密码--》验证手机验证码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("checkResetSMSCode")
	public Map<String, Object> checkResetSMSCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "smsCode" }, map);
		if (isNotNull) {
			Long id = getSessionUserId();
			Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
			payv2BussCompany.setId(id);
			payv2BussCompany.setIsDelete(2);
			payv2BussCompany.setCompanyStatus(2);
			payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
			String smsCode = map.get("smsCode").toString();
			if (payv2BussCompany != null) {
				// 判断此商户是否有效
				// 短信验证验证码
				boolean isOk = configSmsService.checkSms(payv2BussCompany.getUserName(), payv2BussCompany.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PWD,
						smsCode, (int) 1);
				if (isOk) {
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE, null);
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
		}
		return resultMap;
	}

	/**
	 * @Title: submitResetNewPssword
	 * @Description:重置密码--》新密码数据提交
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("submitResetNewPssword")
	public Map<String, Object> submitResetNewPssword(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "smsCode", "newPassword" }, map);
			if (isNotNull) {
				// 查询手机号码是否已经注册
				String smsCode = map.get("smsCode").toString();
				String newPassword = map.get("newPassword").toString();
				newPassword=MD5.GetMD5Code(newPassword);
				Long id = getSessionUserId();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					// 短信验证验证码
					boolean isOk = configSmsService.checkSms(payv2BussCompany.getUserName(), payv2BussCompany.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PWD,
							smsCode, (int) 1);
					if (isOk) {
						// 判断新旧密码是否一样
						if (!newPassword.equals(payv2BussCompany.getPassWord())) {
							payv2BussCompany.setPassWord(newPassword);
							payv2BussCompanyService.update(payv2BussCompany);
							resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "不能与历史密码相同");
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE, null);
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "手机号码没有注册");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：submitNewPssword", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: exitUser
	 * @Description:商户退出登录
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("/exitUser")
	public Map<String, Object> exitUser(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		removeSession();
		logger.info("退出成功！");
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		return resultMap;
	}
}
