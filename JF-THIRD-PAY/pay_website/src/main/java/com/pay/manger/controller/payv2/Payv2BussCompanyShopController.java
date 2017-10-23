package com.pay.manger.controller.payv2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.IdUtils;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.entity.Payv2AppDiscount;
import com.pay.business.payv2.entity.Payv2PayShopQrcode;
import com.pay.business.payv2.entity.Payv2WaySdkVersion;
import com.pay.business.payv2.service.Payv2AppDiscountService;
import com.pay.business.payv2.service.Payv2PayShopQrcodeService;
import com.pay.business.payv2.service.Payv2WaySdkVersionService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.IpAddressUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.WebContextUtil;
import com.pay.business.util.wxpay.WeChatPay;
import com.pay.business.util.wxpay.WeChatUtil;

/**
* @ClassName: Payv2BussCompanyShopController 
* @Description:商铺管理
* @author mofan
* @date 2017年1月11日 上午11:30:19
* */
@Controller
@RequestMapping("/payv2BussCompanyShop")
public class Payv2BussCompanyShopController{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Payv2WaySdkVersionService payv2WaySdkVersionService;
	@Autowired
	private Payv2AppDiscountService payv2AppDiscountService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2PayShopQrcodeService payv2PayShopQrcodeService;
	
	/**
	 * 根据shopKey查询店铺以及店铺支持的支付信息
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryShopPayWayInfo")
	public Map<String, Object> queryShopPayWayInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("shopKey") != null && map.get("sdkType") != null) {
			try {
				// 根据shopKey获取店铺信息
				Payv2BussCompanyShop shop = payv2BussCompanyShopService.detail(map);
				if (shop != null) {
					String shopName = shop.getShopName();
					map = new HashMap<String, Object>();
					map.put("id", shop.getPayWayId());
					// 获取支付通道信息
					Payv2PayWay payv2PayWay = payv2PayWayService.detail(map);
					if (payv2PayWay != null) {
						String wayName = payv2PayWay.getWayName();// 钱包名称
						String wayIcon = payv2PayWay.getWayIcon();// icon
						String waySlogan = payv2PayWay.getWaySlogan();// 推广语
						map = new HashMap<String, Object>();
						map.put("payWayId", shop.getPayWayId());
						map.put("sdkType", map.get("sdkType").toString());
						map.put("isOnline", 1);// 上线
						map.put("isDelete", 2);// 未删除
						// 获取最新的版本信息
						Payv2WaySdkVersion payv2WaySdkVersion = payv2WaySdkVersionService.detail(map);
						if (payv2WaySdkVersion != null) {
							String sdkFileUrl = payv2WaySdkVersion.getSdkFileUrl();// apk下载地址
							map = new HashMap<String, Object>();
							map.put("shopName", shopName);
							map.put("wayName", wayName);
							map.put("wayIcon", wayIcon);
							map.put("waySlogan", waySlogan);
							map.put("sdkFileUrl", sdkFileUrl);
							resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, map);
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.PAY_WAY_SDK_VERSION_NOT_EXIST,null);
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_WAY_NOT_EXIST, null);
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.SHOP_NOT_EXIST, null);
				}
			} catch (Exception e) {
				logger.debug(" 根据shopKey查询店铺以及店铺支持的支付信息出现异常" + e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.SHOP_ERROR, null);
			}
		} else {
			logger.debug("shopKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.SHOP_KEY_INVALID,null);
		}

		return resultMap;
	}

	/**
	 * 根据shopKey、金额 查询优惠信息
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDiscountInfo")
	public Map<String, Object> getDiscountInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("shopKey") != null && map.get("payMoney") != null) {
			try {
				// 根据shopKey获取店铺信息
				Payv2BussCompanyShop shop = payv2BussCompanyShopService.detail(map);
				if (shop != null) {
					map = new HashMap<String, Object>();
					map.put("appId", shop.getPayWayId());
					// 获取店铺优惠信息
					Payv2AppDiscount payv2AppDiscount = payv2AppDiscountService.detail(map);
					if (payv2AppDiscount != null) {
						map = new HashMap<String, Object>();
						int discountType = payv2AppDiscount.getDiscountType();// 1.直减 2.折扣
						double discountValue = payv2AppDiscount.getDiscountValue();
						if (discountType == 1) {
							// 如果是直减，优惠金额就是单笔优惠金额
							map.put("discountValue", discountValue);
						} else if (discountType == 2) {
							// 如果是折扣，计算商品单笔金额*折扣的优惠金额，
							// 单笔金额*折扣 > 单笔最高优惠金额 则取单笔最高金额为优惠金额
							// 否则 取单笔金额*折扣 作为优惠金额
							double money = Double.valueOf(map.get("payMoney").toString());
							int singleOrderMaxMoney = payv2AppDiscount.getSingleOrderMaxMoney();
							discountValue = discountValue * money;
							if (discountValue < singleOrderMaxMoney) {
								map.put("discountValue", discountValue);
							} else {
								map.put("discountValue", singleOrderMaxMoney);
							}
						}
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, map);
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_WAY_DISCOUNT_NOT_EXIST, null);
					}

				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.SHOP_NOT_EXIST, null);
				}
			} catch (Exception e) {
				logger.debug(" 根据shopKey、金额 查询优惠信息出现异常" + e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.SHOP_DISCOUNT_ERROR, null);
			}
		} else {
			logger.debug("shopKey 或者 payMoney 参数无效");
			resultMap = ReMessage.resultBack(ParameterEunm.SHOP_KEY_MONEY_INVALID, null);
		}

		return resultMap;
	}
    
	
	/**
	 * 根据shopKey、订单金额、优惠金额、支付方式创建订单，返回签名给请求
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/buildOrderAndReturnSign")
	public void buildOrderAndReturnSign(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> map) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		response.setContentType("text/html;charset=UTF-8");
		if (map.get("shopKey") != null && map.get("payMoney") != null && map.get("discountMoney") != null) {
			
			try {
				resultMap = payv2PayOrderService.buildOrder(map);
				String form = resultMap.get("Data").toString();
				System.out.println(form);
//				form=MD5.GetMD5Code(form);
			    response.getWriter().write(form);//直接将完整的表单html输出到页面
			    response.getWriter().flush();
			} catch (Exception e) {
				logger.debug(" 根据shopKey、订单金额、优惠金额、支付方式创建订单出现异常" + e);
				e.printStackTrace();
			    try {
					response.getWriter().write("根据shopKey、订单金额、优惠金额、支付方式创建订单出现异常！");
					response.getWriter().flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			    
			}
		}else{
			logger.debug("shopKey 或者 payMoney 或者 discountMoney 参数无效");
		}
		
	}
	
	/**
	 * 线下二维码扫描
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/qrcodeRelation")
	public Map<String,Object> qrcodeRelation(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> map){
		Map<String,Object> resultMap = new HashMap<>();
		boolean appKeyCon = ObjectUtil.checkObjectFile(new String[] { "shopKey" }, map);
		if(appKeyCon){
			try {
				Payv2BussCompanyShop payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				Map<String,Object> paramMap = new HashMap<>();
				if(payv2BussCompanyShop==null||payv2BussCompanyShop.getCompanyId()==null){
					resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"未绑定商户,请联系工作人员！");
				}else{
					paramMap.put("id", payv2BussCompanyShop.getCompanyId());
					Payv2BussCompany payv2BussCompany = payv2BussCompanyService.detail(paramMap);
					if(payv2BussCompany==null||payv2BussCompany.getCompanyStatus()!=2){
						resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"未审核或审核失败,请联系工作人员！");
					}else{
						paramMap.clear();
						paramMap.put("shopId", payv2BussCompanyShop.getId());
						Payv2PayShopQrcode payv2PayShopQrcode = payv2PayShopQrcodeService.detail(paramMap);
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, payv2PayShopQrcode);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			}
		}else{
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping("/toWxReadyJSapiPayPage")
	public Map<String, Object> toWxReadyJSapiPayPage(
			@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"shop_key"}, map);
		if (!isNotNull) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null, "参数不允许为空");
		}
		Map<String, Object> paramMap = new HashMap<>();
		String bussCompanyId = map.get("shop_key").toString();
		paramMap.put("shopKey", bussCompanyId);
		try {
			Payv2BussCompanyShop payv2BussCompanyShop = payv2BussCompanyShopService.detail(paramMap);
			if(payv2BussCompanyShop==null || payv2BussCompanyShop.getCompanyId()==null){
				return resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"未绑定商户,请联系工作人员！");
			}
			paramMap.clear();
			paramMap.put("id", payv2BussCompanyShop.getCompanyId());
			Payv2BussCompany payv2BussCompany = payv2BussCompanyService.detail(paramMap);
			if(payv2BussCompany.getCompanyStatus()!=2){
				return resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"未审核或审核失败,请联系工作人员！");
			}
			paramMap.clear();
			paramMap.put("shopId", payv2BussCompanyShop.getId());
			Payv2PayShopQrcode shopQr = payv2PayShopQrcodeService.detail(paramMap);
			if(shopQr==null || StringUtils.isBlank(shopQr.getWechatMchId())){
				return resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"暂不支持微信支付");
			}
			paramMap.clear();
			String sessionId = request.getSession().getId();
			String wx_openId = RedisDBUtil.redisDao.getString(sessionId);
			paramMap.put("shop_key", bussCompanyId);
			if (wx_openId == null) {
				String suffix = "/payv2BussCompanyShop/wxOauth2Notify.do";
				String redirect_uri = WebContextUtil.getBasePath(request, false) + suffix;
				String oauth2 = WeChatUtil.getOauth2(redirect_uri, bussCompanyId);
				paramMap.put("requestUrl", oauth2);
			} else {
				String suffixPage = "/page/pay/tenPay.html";
				String url = WebContextUtil.getBasePath(request, false) + suffixPage+"?shop_key="+bussCompanyId;// 本地输入金额的页面地址
				paramMap.put("requestUrl", url);
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,
					paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		return resultMap;
	}

	@RequestMapping("/wxOauth2Notify")
	public void wxOauth2Notify(@RequestParam Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (map.get("state") != null) {
			String code = map.get("code").toString();
			String openId = WeChatUtil.getOpenId(code);
			System.out.println(openId);
			String state = map.get("state").toString();
			/*String splits = "(*%*)";
			String[] split = StringUtils.split(state, splits);
			String bussCompanyId = split[0];
			String bussCompanyName = split[1];*/
			if (StringUtils.isNotBlank(openId)) {
				String suffixPage = "/page/pay/tenPay.html";
				//request.getSession().setAttribute("wx_openid", openId);
				String sessionId = request.getSession().getId();
				RedisDBUtil.redisDao.setString(sessionId, openId);
				String redirectUrl = WebContextUtil.getBasePath(request) + suffixPage;
				StringBuilder sb = new StringBuilder();
				sb.append(redirectUrl).append("?shop_key=").append(state);
				response.sendRedirect(sb.toString());
			} else {
				// 重新授权或者退出
				String suffix = "/payv2BussCompanyShop/wxOauth2Notify.do";
				String redirect_uri = WebContextUtil.getBasePath(request, false) + suffix;
				/*String states = bussCompanyId + splits + bussCompanyName;*/
				String oauth2 = WeChatUtil.getOauth2(redirect_uri, state);
				response.sendRedirect(oauth2.toString());
			}
		}
	}
	
	/**
	 * 获取商铺名称
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getShopName")
	public Map<String,Object> getShopName(@RequestParam Map<String, Object> map, HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"shop_key"}, map);
		if (!isNotNull) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null, "参数不允许为空");
		}
		String bussCompanyId = map.get("shop_key").toString();
		Map<String,Object> objMap = new HashMap<>();
		objMap.put("shopKey", bussCompanyId);
		try {
			Payv2BussCompanyShop companyShop = payv2BussCompanyShopService.detail(objMap);
			String shopName = companyShop.getShopName();
			String shopAddress = companyShop.getShopAddress();
			Map<String,Object> paMap = new HashMap<>();
			paMap.put("shopName", shopName);
			paMap.put("shopAddress", shopAddress);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,paMap);
		} catch (Exception e) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping("/wxJSapiServicePay")
	public Map<String,Object> wxJSapiServicePay(@RequestParam Map<String, Object> map, HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"shop_key","pay_money"}, map);
		if (!isNotNull) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null, "参数不允许为空");
		}
		//String wx_openid = request.getSession().getAttribute("wx_openid").toString();
		String sessionId = request.getSession().getId();
		String wx_openid = RedisDBUtil.redisDao.getString(sessionId);
		if(StringUtils.isBlank(wx_openid)){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null, "微信openid未获取");
		}
		String pay_money = map.get("pay_money").toString();
		int indexLen = pay_money.indexOf(".");
		if(indexLen>0){
			//有小数点
			int length = pay_money.length();
			if(length-(indexLen+1)>2){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null, "您输入的金额有误");
			}
		}
		int pay_moenyInt = (int) (NumberUtils.createDouble(pay_money) * 100);
		String shop_key = map.get("shop_key").toString();
		//查询该商户对应的微信服务商的子商户ID
		Map<String,Object> objMap = new HashMap<>();
		objMap.put("shopKey", shop_key);
		Payv2BussCompanyShop companyShop = payv2BussCompanyShopService.detail(objMap);
		String shopName = companyShop.getShopName();
		objMap.clear();
		objMap.put("shopId", companyShop.getId());
		Payv2PayShopQrcode shopQr = payv2PayShopQrcodeService.detail(objMap);
		String sub_mch_id = shopQr.getWechatMchId();
		String body = "正在向 "+shopName+" 商家付款";
		try {
			long order_id = IdUtils.createId();
			Map<String, String> jsapiServicePay = WeChatPay.jsapiServicePay(body,String.valueOf(order_id),String.valueOf(pay_moenyInt),IpAddressUtil.getIpAddress(request),sub_mch_id, wx_openid);
	    	System.out.println(jsapiServicePay.toString());
	    	if(jsapiServicePay!=null && !(jsapiServicePay.containsKey("wx_error_code"))){
	    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,
						jsapiServicePay);
	    	}else{
	    		resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
	    	}
		} catch (Exception e) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		return resultMap;
	}
	
	/**
	 * 商家app条码支付
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/barCodePay")
	@ResponseBody
	public Map<String,Object> barCodePay(@RequestParam Map<String, Object> map, HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		try {
			map.put("ip", IpAddressUtil.getIpAddress(request));
			resultMap = payv2PayOrderService.barCodePay(map);
			if(resultMap.containsKey("orderId")){
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,
						resultMap);
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,"商户不支持的支付方式");
			}
		} catch (Exception e) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			e.printStackTrace();
		}
		return resultMap;
	}
}
