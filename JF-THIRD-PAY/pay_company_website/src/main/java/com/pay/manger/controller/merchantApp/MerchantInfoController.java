package com.pay.manger.controller.merchantApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.pay.business.payv2.entity.Payv2BussCompanyShop;
import com.pay.business.payv2.entity.Payv2ShopCode;
import com.pay.business.payv2.service.Payv2BussCompanyService;
import com.pay.business.payv2.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.service.Payv2ShopCodeService;
import com.pay.business.payv2.service.SysConfigSmsService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.annotation.LoginValidate;
import com.pay.manger.controller.admin.LoginVO;
import com.pay.manger.controller.admin.Payv2BussCompanyShopVo;
import com.pay.manger.controller.base.InterfaceBaseController;
import com.pay.manger.util.PhoneCodeTypeUtil;

/**
 * 
 * @ClassName: MerchantInfoController
 * @Description:商户信息--》我的信息模块Controller
 * @author zhoulibo
 * @date 2017年3月6日 下午2:53:35
 */
@Controller
@RequestMapping("/merchantInfo/")
public class MerchantInfoController extends InterfaceBaseController {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;// 商户表
	@Autowired
	private SysConfigSmsService configSmsService;// 发送短信表
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;// 商铺表
	@Autowired
	private Payv2ShopCodeService payv2ShopCodeService;

	/**
	 * @Title: getMyInfo
	 * @Description:我的--》获取个人信息资料
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getMyInfo")
	public Map<String, Object> getMyInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = getSessionUserId();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setId(id);
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
		if (payv2BussCompany != null) {
			LoginVO vo = new LoginVO();
			// 用户名字
			vo.setBalance(0.00);
			if (payv2BussCompany.getPayPassWord() != null) {
				vo.setHavePayPass(true);
			} else {
				vo.setHavePayPass(false);
			}
			vo.setMobile(payv2BussCompany.getUserName());
			vo.setRealName(payv2BussCompany.getContactsName());
			vo.setUserIcon(payv2BussCompany.getCompanyPhoto());
			vo.setUserId(payv2BussCompany.getId().toString());
			vo.setAccountBank(payv2BussCompany.getAccountBank());
			vo.setAccountCard(payv2BussCompany.getAccountCard());
			vo.setAccountName(payv2BussCompany.getAccountName());
			vo.setAccountType(payv2BussCompany.getAccountType());
			vo.setAlipayAccountCard(payv2BussCompany.getAlipayAccountCard());
			vo.setWechatAccountCard(payv2BussCompany.getWechatAccountCard());
			resultMap.put("myInfo", vo);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在");
		}
		return resultMap;
	}

	/**
	 * @Title: updateMerchantInfo
	 * @Description: 修改商户图像信息数据提交
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("updateMerchantInfo")
	public Map<String, Object> updateMerchantInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "companyPhoto" }, map);
			if (isNotNull) {
				Long id = getSessionUserId();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					payv2BussCompany.setCompanyPhoto(map.get("companyPhoto").toString());
					payv2BussCompanyService.update(payv2BussCompany);
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：updateMerchantInfo", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getOldPassword
	 * @Description:我的--》重置支付密码--》我记得原密码来进行重置，验证原密码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getOldPayPassword")
	public Map<String, Object> getOldPayPassword(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "oldPayPassword" }, map);
		if (isNotNull) {
			Long id = getSessionUserId();
			Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
			payv2BussCompany.setId(id);
			payv2BussCompany.setIsDelete(2);
			payv2BussCompany.setCompanyStatus(2);
			payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
			if (payv2BussCompany != null) {
				String oldPassword = map.get("oldPayPassword").toString();
				oldPassword = MD5.GetMD5Code(oldPassword);
				if (oldPassword.equals(payv2BussCompany.getPayPassWord())) {
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "密码错误");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在");
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
		}
		return resultMap;
	}

	/**
	 * @Title: getOldPassword
	 * @Description:我的--》重置支付密码--》我记得原密码来进行重置，新密码数据提交
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("setNewPayPassword")
	public Map<String, Object> setNewPayPassword(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "oldPayPassword", "newPayPassword" }, map);
			if (isNotNull) {
				Long id = getSessionUserId();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					String oldPayPassword = map.get("oldPayPassword").toString();
					oldPayPassword = MD5.GetMD5Code(oldPayPassword);
					String newPayPassword = map.get("newPayPassword").toString();
					newPayPassword = MD5.GetMD5Code(newPayPassword);
					if (oldPayPassword.equals(payv2BussCompany.getPayPassWord())) {
						if (!oldPayPassword.equals(newPayPassword)) {
							payv2BussCompany.setPayPassWord(newPayPassword);
							payv2BussCompanyService.update(payv2BussCompany);
							resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "新密码不能与原密码一致");
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "密码错误");
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：setNewPayPassword", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getForgetSendSMSCode
	 * @Description:我的--》我忘记支付密码--》发送短信验证码。。。。流程1
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getForgetSendSMSCode")
	public Map<String, Object> getForgetSendSMSCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 查询手机号码是否已经注册
		Long id = getSessionUserId();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setId(id);
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
		if (payv2BussCompany != null) {
			// 发送短信
			int status = configSmsService.createCode(payv2BussCompany.getUserName(), payv2BussCompany.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PAY_PWD,
					(int) 1);
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
	 * @Title: checkSMSCodeByForget
	 * @Description:我的--》我忘记支付密码--》验证短信验证码。。。。流程2
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("checkSMSCodeByForget")
	public Map<String, Object> checkSMSCodeByForget(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "smsCode" }, map);
		if (isNotNull) {
			// 查询手机号码是否已经注册
			Long id = getSessionUserId();
			Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
			payv2BussCompany.setId(id);
			payv2BussCompany.setIsDelete(2);
			payv2BussCompany.setCompanyStatus(2);
			payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
			String smsCode = map.get("smsCode").toString();
			if (payv2BussCompany != null) {
				// 短信验证验证码
				boolean isOk = configSmsService.checkSms(payv2BussCompany.getUserName(), payv2BussCompany.getId(), PhoneCodeTypeUtil.CODE_TYPE_FIND_PAY_PWD,
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
	 * @Title: getVerifyID
	 * @Description:我的--》我忘记支付密码--》验证身份证号码。。。。流程3
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getVerifyID")
	public Map<String, Object> getVerifyID(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "RMID" }, map);
			if (isNotNull) {
				Long id = getSessionUserId();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					String RMID = map.get("RMID").toString();
					if (RMID.equals(payv2BussCompany.getLegalIdCard())) {
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "身份证不正确");
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：setNewPayPassword", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: setPayNewPasswordByForget
	 * @Description:我的--》我忘记支付密码--》重置新密码数据提交。。。。流程4
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("setPayNewPasswordByForget")
	public Map<String, Object> setPayNewPasswordByForget(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "RMID", "SMSCode", "newPayPassword" }, map);
			if (isNotNull) {
				Long id = getSessionUserId();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				String smsCode = map.get("SMSCode").toString();
				String newPayPassword = map.get("newPayPassword").toString();
				//加密
				newPayPassword=MD5.GetMD5Code(newPayPassword);
				if (payv2BussCompany != null) {
					String RMID = map.get("RMID").toString();
					if (RMID.equals(payv2BussCompany.getLegalIdCard())) {
						// 短信验证验证码
						boolean isOk = configSmsService.checkSms(payv2BussCompany.getUserName(), payv2BussCompany.getId(),
								PhoneCodeTypeUtil.CODE_TYPE_FIND_PAY_PWD, smsCode, (int) 1);
						if (isOk) {
							if (!newPayPassword.equals(payv2BussCompany.getPayPassWord())) {
								// 修改
								payv2BussCompany.setPayPassWord(newPayPassword);
								payv2BussCompanyService.update(payv2BussCompany);
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
							} else {
								resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "新密码不能与原密码一致");
							}
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE, null);
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "身份证不正确");
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在/已无效");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：setNewPayPassword", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: setFirstPayPassword
	 * @Description:我的--》商户首次设置支付密码数据提交接口
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("setFirstPayPassword")
	public Map<String, Object> setFirstPayPassword(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "newPayPassword" }, map);
			if (isNotNull) {
				Long id = getSessionUserId();
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
				String newPayPassword = map.get("newPayPassword").toString();
				//加密
				newPayPassword=MD5.GetMD5Code(newPayPassword);
				if (payv2BussCompany != null) {
					// 修改
					payv2BussCompany.setPayPassWord(newPayPassword);
					payv2BussCompanyService.update(payv2BussCompany);
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户不存在/已无效");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：setNewPayPassword", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getShopList
	 * @Description:我的--》获取商户门店列表：返回了每一个门店详情信息
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getShopList")
	public Map<String, Object> getShopList(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取当前登录商户ID
		Long companyId = getSessionUserId();
		// 获取商户旗下店铺
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		payv2BussCompanyShop.setCompanyId(companyId);
		payv2BussCompanyShop.setIsDelete(2);
		payv2BussCompanyShop.setShopStatus(2);
		List<Payv2BussCompanyShop> shopList = payv2BussCompanyShopService.selectByObject(payv2BussCompanyShop);
		List<Payv2BussCompanyShopVo> shopVoList = new ArrayList<Payv2BussCompanyShopVo>();
		for (Payv2BussCompanyShop payv2BussCompanyShop2 : shopList) {
			Payv2BussCompanyShopVo vo = new Payv2BussCompanyShopVo();
			vo.setId(payv2BussCompanyShop2.getId());
			vo.setCompanyId(payv2BussCompanyShop2.getCompanyId());
			vo.setShopAddress(payv2BussCompanyShop2.getShopAddress());
			vo.setShopCard(payv2BussCompanyShop2.getShopCard());
			vo.setShopContacts(payv2BussCompanyShop2.getShopContacts());
			vo.setShopContactsPhone(payv2BussCompanyShop2.getShopContactsPhone());
			vo.setShopDayEndTime(payv2BussCompanyShop2.getShopDayEndTime());
			vo.setShopDayStartTime(payv2BussCompanyShop2.getShopDayStartTime());
			vo.setShopDesc(payv2BussCompanyShop2.getShopDesc());
			vo.setShopEmail(payv2BussCompanyShop2.getShopEmail());
			vo.setShopIcon(payv2BussCompanyShop2.getShopIcon());
			vo.setShopName(payv2BussCompanyShop2.getShopName());
			vo.setShopRemarks(payv2BussCompanyShop2.getShopRemarks());
			vo.setShopTwoCodeUrl(payv2BussCompanyShop2.getShopTwoCodeUrl());
			shopVoList.add(vo);
		}
		resultMap.put("shopVoList", shopVoList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}

	/**
	 * @Title: shopCodeList
	 * @Description:获取某一个商铺下面的收款二维码列表
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getshopCodeList")
	public Map<String, Object> getshopCodeList(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "shopId" }, map);
		if (isNotNull) {
			List<Payv2ShopCode> shopCodeList = payv2ShopCodeService.query(map);
			resultMap.put("shopCodeList", shopCodeList);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
		}
		return resultMap;
	}

	/**
	 * @Title: delShopCode
	 * @Description:删除商铺下面的某一个二维码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("delShopCode")
	public Map<String, Object> delShopCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "shopCodeId" }, map);
			if (isNotNull) {
				Payv2ShopCode payv2ShopCode = new Payv2ShopCode();
				payv2ShopCode.setId(Long.valueOf(map.get("shopCodeId").toString()));
				payv2ShopCodeService.delete(payv2ShopCode);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：delShopCode", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}
}
