package com.pay.company.controller.online;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2BussTrade;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2BussTradeService;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.mail.SslSmtpMailUtil;
import com.pay.company.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2BussCompanyController
 * @Description:商户控制
 * @author mofan
 * @date 2017年2月23日 上午9:21:52
 */
@Controller
@RequestMapping("/online/payv2BussCompany/*")
public class Payv2BussCompanyController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper> {

	private static final Logger logger = Logger.getLogger(Payv2BussCompanyController.class);

	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	@Autowired
	private Payv2BussTradeService payv2BussTradeService;
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;

	/**
	 * 账单管理列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/accountManager")
	public ModelAndView accountManagerList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("page/accountManager");
		return mv;
	}

	/**
	 * 查看账户信息
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/toViewPayv2BussCompany")
	public Map<String, Object> toViewPayv2BussCompany(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取支付通道
		Payv2BussCompany company = getAdmin();
		try {
			resultMap.put("obj", company);
			if (company.getCompanyTrade() != null) {
				map.put("id", company.getCompanyTrade());
				Payv2BussTrade trade = payv2BussTradeService.detail(map);
				if (trade != null) {
					company.setCompanyTradeVar(trade.getTradeName());// 所属行业
				}
			}
			if (company.getCompanyRangeCity() != null) {
				map.put("id", company.getCompanyRangeCity());
				Payv2ProvincesCity city = payv2ProvincesCityService.detail(map);
				if (city != null) {
					company.setCompanyRangeCityName(city.getName());
				}
			}
		} catch (Exception e) {
			logger.error(" 查看账户信息报错...............", e);
			resultMap = ReMessage.resultBack(ParameterEunm.SHOP_ERROR, null);
		}
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}

	/**
	 * 修改商户
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updatePayv2BussCompany")
	public Map<String, Object> updatePayv2BussCompany(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		if (map.get("id") != null) {
			try {
				Payv2BussCompany company = getAdmin();
				String contactsPhone = map.get("contactsPhone") == null ? "" : map.get("contactsPhone").toString();
				String contactsName = map.get("contactsName") == null ? "" : map.get("contactsName").toString();
				String companyName = map.get("companyName") == null ? "" : map.get("companyName").toString();
				String contactsMail = map.get("contactsMail") == null ? "" : map.get("contactsMail").toString();
				map.put("updateTime", new Date());
				payv2BussCompanyService.update(map);
				if (!StringUtils.isEmpty(contactsPhone)) {
					company.setContactsPhone(contactsPhone);
				}
				if (!StringUtils.isEmpty(contactsName)) {
					company.setContactsName(contactsName);

				}
				if (!StringUtils.isEmpty(companyName)) {
					company.setCompanyName(companyName);
				}
				if (!StringUtils.isEmpty(contactsMail)) {
					company.setContactsMail(contactsMail);
				}
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 获取支付通道及费率列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPaywayInfoList")
	public Map<String, Object> getAppList() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany company = getAdmin();
		Payv2BussSupportPayWay app = new Payv2BussSupportPayWay();
		app.setParentId(company.getId());
		app.setIsDelete(2);
		Payv2BussCompany company1 = new Payv2BussCompany();
		company1.setId(company.getId());
		company = payv2BussCompanyService.selectSingle(company1);
		List<Payv2BussSupportPayWay> paywayList = payv2BussSupportPayWayService.selectByObject(app);
		Map<String, Object> map = new HashMap<String, Object>();
		Payv2PayWayRate payv2PayWayRate = null;
		for (Payv2BussSupportPayWay pay : paywayList) {
			pay.setWeekType(company.getWayArrivalType());// 结算周期
			pay.setWayArrivalValue(company.getWayArrivalValue());
			if (pay.getRateId() != null) {
				map.put("id", pay.getRateId());
				payv2PayWayRate = payv2PayWayRateService.detail(map);
				if (payv2PayWayRate != null) {
					pay.setWayName(payv2PayWayRate.getPayWayName());// 支付通道名称
				}
			}
		}
		resultMap.put("paywayList", paywayList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendEmail")
	public Map<String,Object> sendEmail(@RequestParam Map<String, Object> map, HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<>();
		Payv2BussCompany company = getAdmin();
		if (ObjectUtil.checkObject(new String[] { "id"}, map)) {
			try {
				String email = company.getContactsMail();
				if(!ObjectUtil.isEmpty(email)){
					SslSmtpMailUtil ms = new SslSmtpMailUtil();
			        ms.setSubject("有氧支付");
			        
			        StringBuffer sb = new StringBuffer();
			        String code = RandomUtil.generateString(6);
			        sb.append("重置秘钥验证码为"+code+"，此验证码10分钟有效，秘钥涉及重要支付信息请妥善保管。");
			        RedisDBUtil.redisDao.setString(email+map.get("id").toString(), code,600);
			        ms.setText(sb.toString());
			        ms.setFrom("support@aijinfu.cn");
			        String [] mailFroms = email.split("-");
			        ms.setRecipients(mailFroms, "TO");
			        ms.setSentDate();
			        ms.sendMail();
			        resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
				}else{
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"邮箱不存在，请联系商务！");
				}
			} catch (Exception e) {
				logger.error(" 查看账户信息报错...............", e);
				resultMap = ReMessage.resultBack(ParameterEunm.SHOP_ERROR, null);
			}
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"缺少参数");
		}
		return resultMap;
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkEmailCode")
	public Map<String,Object> checkEmailCode(@RequestParam Map<String, Object> map, HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<>();
		Payv2BussCompany company = getAdmin();
		if (ObjectUtil.checkObject(new String[] { "id","code"}, map)) {
			Long id = Long.valueOf(map.get("id").toString());
			try {
				String email = company.getContactsMail();
				if(!ObjectUtil.isEmpty(email)){
					String code = RedisDBUtil.redisDao.getString(email+id);
					if(!ObjectUtil.isEmpty(code)){
						if(code.toUpperCase().equals(map.get("code").toString().toUpperCase())){
							//生成新的密钥
	    					String updateNewAppSecret = payv2BussCompanyAppService
	    							.updateNewAppSecret(id, company.getId());
	    					
	    					resultMap.put("newAppSecret", updateNewAppSecret);
	    					RedisDBUtil.redisDao.delete(email+id);
							resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
						}else{
							resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"验证码错误!");
						}
					}else{
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"验证码不存在或超时");
					}
			       
				}else{
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"邮箱不存在，请联系商务！");
				}
			} catch (Exception e) {
				logger.error(" 查看账户信息报错...............", e);
				resultMap = ReMessage.resultBack(ParameterEunm.SHOP_ERROR, null);
			}
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"缺少参数");
		}
		return resultMap;
	}
}
