package com.pay.channel.controller.payv2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2Bank;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.entity.Payv2BussTrade;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2BankService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payv2.service.Payv2BussTradeService;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.GenerateUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.channel.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2BussCompanyController
 * @Description:商户控制前
 * @author qiuguojie
 * @date 2016年11月30日 下午8:21:52
 */
@Controller
@RequestMapping("/payv2BussCompany/*")
public class Payv2BussCompanyController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper> {

	private static final Logger logger = Logger.getLogger(Payv2BussCompanyController.class);

	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;

	@Autowired
	private Payv2BussTradeService payv2BussTradeService;

	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;// 商户支持的支付通道表
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;// APP
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;// 支付方式
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;// 商铺
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	@Autowired
	private Payv2BankService payv2BankService;

	/**
	 * 商户列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/companyList")
	public ModelAndView companyList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("company/buss_company_list");
		Payv2Channel pc = (Payv2Channel) request.getSession().getAttribute("admin");
		map.put("channelId", pc.getId());
		map.put("isDelete", 2);// 未删除
		// 分页列表
		PageObject<Payv2BussCompany> pageObject = payv2BussCompanyService.companyList(map);
		// 获取配置支付通道个数
		List<Payv2BussCompany> comapnyList = pageObject.getDataList();
		for (Payv2BussCompany payv2BussCompany : comapnyList) {
			Payv2BussSupportPayWay payv2BussSupportPayWay = new Payv2BussSupportPayWay();
			payv2BussSupportPayWay.setParentId(payv2BussCompany.getId());
			payv2BussSupportPayWay.setIsDelete(2);
			List<Payv2BussSupportPayWay> SupportPayWayList = payv2BussSupportPayWayService.selectByObject(payv2BussSupportPayWay);
			if (SupportPayWayList.size() > 0) {
				payv2BussCompany.setSupportPayWayNum(SupportPayWayList.size());
			} else {
				payv2BussCompany.setSupportPayWayNum(0);
			}
		}
		map.put("parentId", 0);
		// 省
		List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
		if (map.get("companyRangeCity") != null && !map.get("companyRangeCity").equals("")) {
			map.put("parentId", map.get("companyRangeProvince"));
			// 市
			List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
			mv.addObject("cityList", cityList);
		}
		pageObject.setDataList(comapnyList);
		mv.addObject("list", pageObject);
		mv.addObject("provincesList", provincesList);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 新增页面
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toPayv2BussCompany")
	public ModelAndView toPayv2BussCompany(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("company/buss_company_add_new");
		List<Payv2BussTrade> list = payv2BussTradeService.query(map);

		map.put("parentId", 0);
		List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
		
		
		Payv2Channel pc = (Payv2Channel) request.getSession().getAttribute("admin");
		List<Payv2PayWay> payList = payv2PayWayService.queryByChannelId(pc.getId());
		
		List<Payv2Bank> bankList = payv2BankService.query(map);
		mv.addObject("bankList", bankList);
		mv.addObject("tradeList", list);
		mv.addObject("provincesList", provincesList);
		mv.addObject("payList", payList);
		return mv;
	}
	
	/**
	 * 验证商户是否存在
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateUserName")
	public Map<String, Object> validateUserName(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> param = new HashMap<>();
		param.put("userName", map.get("userName"));
		List<Payv2BussCompany> list = payv2BussCompanyService.query(param);
		if (list != null && list.size() > 0) {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户账号已存在！");
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);	
		}
		return resultMap;
	}

	/**
	 * 添加商户
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addBussCompany")
	public Map<String, Object> addBussCompany(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Payv2Channel pc = (Payv2Channel) request.getSession().getAttribute("admin");
			if (pc != null) {
				// payWayList 格式：1-2-2,3-0-4,5-6-1
				if (map.get("payWayList") != null && !map.get("payWayList").toString().equals("")) {
					Map<String, Object> param = new HashMap<>();
					param.put("userName", map.get("userName"));
					List<Payv2BussCompany> list = payv2BussCompanyService.query(param);
					if (list != null && list.size() > 0) {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "添加失败,商户账号已存在！");
						return resultMap;
					}
					// 添加商户
					map.put("channelId", pc.getId());
					map.put("createTime", new Date());
					String companyKey = MD5.GetMD5Code(map.get("userName").toString());
					map.put("companyKey", companyKey);
					map.put("passWord", MD5.GetMD5Code(map.get("passWord").toString()));
					Payv2BussCompany payv2BussCompany = payv2BussCompanyService.add(map);
					String payWayList = map.get("payWayList").toString();
					// 解析
					String a[] = payWayList.split(",");
					param.clear();
					//添加支付通道
					boolean isOk = addPayWay(a, payv2BussCompany.getId(), param);
					if (isOk) {
						//成功
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);	
					} else {
						//排序重复：但是商户添加成功
						resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, null);	
					}
				} else {
					//支付通道不能为空
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null, "支付通道不能为空！");
				}
			} else {
				//无渠道商
				resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("添加商户失败");
			//异常
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 省市联动
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/proCity")
	public Map<String, Object> proCity(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, provincesList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 编辑详情
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/editDetail")
	public ModelAndView editDetail(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("company/buss_company_edit_new");
		try {
			// 商户详情
			Payv2BussCompany obj = payv2BussCompanyService.detail(map);
			map.remove("id");
			// 行业
			List<Payv2BussTrade> list = payv2BussTradeService.query(map);
			if (obj.getCompanyRangeProvince() != null) {
				map.put("parentId", obj.getCompanyRangeProvince());
				// 市
				List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
				mv.addObject("cityList", cityList);
			}
			map.put("parentId", 0);
			// 省
			List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
			// 开户机构 银行列表
			List<Payv2Bank> bankList = payv2BankService.query(map);

			// 商户支持的支付通道和路由
			Payv2BussSupportPayWay pbspw = new Payv2BussSupportPayWay();
			pbspw.setParentId(obj.getId());
			pbspw.setIsDelete(2);
			pbspw.setPayWayStatus(1);
			List<Payv2BussSupportPayWay> wayList = payv2BussSupportPayWayService.selectByObject(pbspw);
			// 路由列表
			Map<String, Object> param = new HashMap<>();
			String payWayList = "";
			for (Payv2BussSupportPayWay payv2BussSupportPayWay : wayList) {
				if (payv2BussSupportPayWay.getRateId() != null) {
					payWayList += payv2BussSupportPayWay.getPayWayId() + "-" + payv2BussSupportPayWay.getRateId() + "-"
							+ payv2BussSupportPayWay.getPayWayRate()+"-"+payv2BussSupportPayWay.getSortNum()+"-"+payv2BussSupportPayWay.getMaxMoney()+"-"+payv2BussSupportPayWay.getOneMaxMoney() + ",";
				} else {
					payWayList += payv2BussSupportPayWay.getPayWayId() + "-0-" + payv2BussSupportPayWay.getPayWayRate()+"-"+payv2BussSupportPayWay.getSortNum()+"-"+payv2BussSupportPayWay.getMaxMoney()+"-"+payv2BussSupportPayWay.getOneMaxMoney() + ",";
				}
				param.put("payWayId", payv2BussSupportPayWay.getPayWayId());
				param.put("channelId", obj.getChannelId());
				List<Payv2PayWayRate> rateList = payv2PayWayRateService.queryByChannelWayId(param);
				
				payv2BussSupportPayWay.setRateList(rateList);
			}
			if (payWayList != "") {
				payWayList = payWayList.substring(0, payWayList.length() - 1);
			}
			
			List<Payv2PayWay> payList = payv2PayWayService.queryByChannelId(obj.getChannelId());
			mv.addObject("payList", payList);
			mv.addObject("wayList", wayList);
			mv.addObject("bankList", bankList);
			mv.addObject("tradeList", list);
			mv.addObject("provincesList", provincesList);
			mv.addObject("obj", obj);
			mv.addObject("map", map);
			mv.addObject("payWayList", payWayList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 查看详情
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView viewDetail(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("company/buss_company_view_new");
		try {
			// 商户详情
			Payv2BussCompany obj = payv2BussCompanyService.detail(map);
			map.remove("id");
			// 行业
			List<Payv2BussTrade> list = payv2BussTradeService.query(map);
			if (obj.getCompanyRangeProvince() != null) {
				map.put("parentId", obj.getCompanyRangeProvince());
				// 市
				List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
				mv.addObject("cityList", cityList);
			}
			map.put("parentId", 0);
			// 省
			List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);

			// 开户机构 银行列表
			List<Payv2Bank> bankList = payv2BankService.query(map);

			// 商户支持的支付通道和路由
			Payv2BussSupportPayWay pbspw = new Payv2BussSupportPayWay();
			pbspw.setParentId(obj.getId());
			pbspw.setIsDelete(2);
			pbspw.setPayWayStatus(1);
			List<Payv2BussSupportPayWay> wayList = payv2BussSupportPayWayService.selectByObject(pbspw);

			// 路由列表
			Map<String, Object> param = new HashMap<>();
			for (Payv2BussSupportPayWay payv2BussSupportPayWay : wayList) {
				param.put("payWayId", payv2BussSupportPayWay.getPayWayId());
				param.put("channelId", obj.getChannelId());
				List<Payv2PayWayRate> rateList = payv2PayWayRateService.queryByChannelWayId(param);
				
				payv2BussSupportPayWay.setRateList(rateList);
			}
			List<Payv2PayWay> payList = payv2PayWayService.queryByChannelId(obj.getChannelId());
			
			mv.addObject("payList", payList);
			mv.addObject("wayList", wayList);
			mv.addObject("bankList", bankList);
			mv.addObject("tradeList", list);
			mv.addObject("provincesList", provincesList);
			mv.addObject("obj", obj);
			mv.addObject("map", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 修改商户数据提交
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBussCompany")
	public Map<String, Object> updateBussCompany(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if (null != map.get("id")) {
				Map<String, Object> param = new HashMap<>();
				param.put("id", map.get("id"));
				Payv2BussCompany payv2BussCompany = payv2BussCompanyService.detail(param);
				if (!map.get("userName").toString().equals(payv2BussCompany.getUserName())) {
					param.clear();
					param.put("userName", map.get("userName"));
					List<Payv2BussCompany> list = payv2BussCompanyService.query(param);
					if (list != null && list.size() > 0) {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "修改失败,商户账号已存在！");
						return resultMap;
					}
				}

				Payv2Channel pc = (Payv2Channel) request.getSession().getAttribute("admin");
				map.put("channelId", pc.getId());
				// 修改是：重新提交审核
				map.put("companyStatus", 1);

				if (map.get("passWord") != null && !"".equals(map.get("passWord").toString())) {
					if (!map.get("passWord").toString().equals(payv2BussCompany.getPassWord())) {
						// 如果前端传的加密值与预设的加密值一样；证明用户没有修改密码；故设为null;
						if (!String.valueOf(map.get("passWord").toString()).equals("4c6e33e575da86adc6df9d85790c75ce")) {
							String password = MD5.GetMD5Code(map.get("passWord").toString());
							map.put("passWord", password);
						} else {
							map.put("passWord", null);
						}
					}
				}
				// payWayList 格式：1-2-2,3-0-4,5-6-1
				if (map.get("payWayList") != null && !map.get("payWayList").toString().equals("")) {
					param.clear();
					param.put("parentId", map.get("id"));
					// 先删除所有支付通道，再添加
					payv2BussSupportPayWayService.delete(param);
					String payWayList = map.get("payWayList").toString();
					// 解析
					String a[] = payWayList.split(",");
					param.clear();
					//添加支付通道
					boolean isOk =addPayWay(a, payv2BussCompany.getId(), param);
					if (isOk) {
						// 修改商户信息数据提交
						payv2BussCompanyService.update(map);
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
					} else {
						// 排序填写有问题
						resultMap.put("resultCode", 201);
						return resultMap;
					}
				} else {
					// 通道费不能为空
					resultMap.put("resultCode", 401);
					return resultMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("resultCode", 500);
		}
		return resultMap;
	}

	/**
	 * 查看未通过原因
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toViewFailReason")
	public ModelAndView toViewFailReason(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/pay_company_view");
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		try {
			if (null != map.get("id")) {
				payv2BussCompany = payv2BussCompanyService.detail(map);
				mvc.addObject("payv2BussCompany", payv2BussCompany);
			}
		} catch (Exception e) {
			logger.error(" 查看未通过原因报错", e);
		}
		return mvc;
	}

	/**
	 * @Title: updatePayv2BussCompany
	 * @Description:终止合作/恢复合作
	 * @param map
	 * @param request
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2017年2月17日 下午2:58:19
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/updatePayv2BussCompany")
	public Map<String, Object> updatePayv2BussCompany(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if (null != map.get("id")) {
				map.put("updateTime", new Date());
				if (map.get("companyStatus") != null) {
					if (Integer.valueOf(map.get("companyStatus").toString()) == 4) {// 终止合作
						// 需要终止下面的APP与商铺
						Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
						payv2BussCompany.setId(Long.valueOf(map.get("id").toString()));
						payv2BussCompany.setIsDelete(2);
						payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
						if (payv2BussCompany != null) {
							int type = 1;// 终止类型
							// 获取了商户就去修改上户支持的支付通道
							Payv2BussSupportPayWay payv2BussSupportPayWay = new Payv2BussSupportPayWay();
							payv2BussSupportPayWay.setParentId(payv2BussCompany.getId());// 商户ID
							payv2BussSupportPayWay.setIsDelete(2);
							if (type == 1) {
								payv2BussSupportPayWay.setPayWayStatus(1);
							}
							List<Payv2BussSupportPayWay> supportPayWayList = payv2BussSupportPayWayService.selectByObject(payv2BussSupportPayWay);
							for (Payv2BussSupportPayWay payv2BussSupportPayWay2 : supportPayWayList) {
								// 获取支付方式列表
								Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
								payv2BussAppSupportPayWay.setPayWayId(payv2BussSupportPayWay2.getId());
								payv2BussAppSupportPayWay.setIsDelete(2);
								if (type == 1) {
									payv2BussAppSupportPayWay.setStatus(1);
								}
								List<Payv2BussAppSupportPayWay> appSupportPayWayList = payv2BussAppSupportPayWayService
										.selectByObject(payv2BussAppSupportPayWay);
								for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : appSupportPayWayList) {
									payv2BussAppSupportPayWay2.setUpdateTime(new Date());
									// 终止
									if (type == 1) {
										payv2BussAppSupportPayWay2.setStatus(2);
									}// 删除
									if (type == 2) {
										payv2BussAppSupportPayWay2.setIsDelete(1);
									}
									payv2BussAppSupportPayWay2.setStatus(2);
									payv2BussAppSupportPayWayService.update(payv2BussAppSupportPayWay2);
								}
								// 终止
								if (type == 1) {
									payv2BussSupportPayWay2.setPayWayStatus(2);
								}
								// 删除
								if (type == 2) {
									payv2BussSupportPayWay2.setIsDelete(1);
								}
								payv2BussSupportPayWay2.setUpdateTime(new Date());
								payv2BussSupportPayWayService.update(payv2BussSupportPayWay2);
							}
							// 获取商户APP：app不启动
							Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
							payv2BussCompanyApp.setCompanyId(payv2BussCompany.getId());
							payv2BussCompanyApp.setIsDelete(2);
							if (type == 1) {
								payv2BussCompanyApp.setAppStatus(2);
							}
							List<Payv2BussCompanyApp> appList = payv2BussCompanyAppService.selectByObject(payv2BussCompanyApp);
							for (Payv2BussCompanyApp payv2BussCompanyApp2 : appList) {
								// 终止
								if (type == 1) {
									payv2BussCompanyApp2.setAppStatus(4);
								}
								// 删除
								if (type == 2) {
									payv2BussCompanyApp2.setIsDelete(1);
								}
								payv2BussCompanyApp2.setUpdateTime(new Date());
								payv2BussCompanyAppService.update(payv2BussCompanyApp2);

							}
							// 商铺列表:修改商铺为不启动
							Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
							payv2BussCompanyShop.setCompanyId(payv2BussCompany.getId());
							payv2BussCompanyShop.setIsDelete(2);
							if (type == 1) {
								payv2BussCompanyShop.setShopStatus(2);
							}
							List<Payv2BussCompanyShop> ShopList = payv2BussCompanyShopService.selectByObject(payv2BussCompanyShop);
							for (Payv2BussCompanyShop payv2BussCompanyShop2 : ShopList) {
								payv2BussCompanyShop2.setUpdateTime(new Date());
								// 终止
								if (type == 1) {
									payv2BussCompanyShop2.setShopStatus(4);
								}
								// 删除
								if (type == 2) {
									payv2BussCompanyShop2.setIsDelete(1);
								}

								payv2BussCompanyShopService.update(payv2BussCompanyShop2);
							}
						}
					}
				}
				payv2BussCompanyService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 审核商户通过
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/approveAgreePayv2BussCompany")
	public Map<String, Object> approveAgreePayv2BussCompany(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			if (null != map.get("id")) {
				map.put("updateTime", new Date());
				map.put("companyKey", GenerateUtil.getRandomString(64));
				payv2BussCompanyService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping("/getPayTypeName")
	public Map<String, Object> getPayTypeName(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2PayWay payv2PayWay = new Payv2PayWay();
		payv2PayWay.setIsDelete(2);
		payv2PayWay.setStatus(1);
		payv2PayWay.setWayType(Integer.valueOf(map.get("wayType").toString()));
		List<Payv2PayWay> payList = payv2PayWayService.selectByObject(payv2PayWay);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, payList);
		return resultMap;
	}

	@ResponseBody
	@RequestMapping("/getPayWayRate")
	public Map<String, Object> getPayWayRate(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2Channel pc = (Payv2Channel) request.getSession().getAttribute("admin");
		map.put("channelId", pc.getId());
		List<Payv2PayWayRate> payList = payv2PayWayRateService.queryByChannelWayId(map);
		List<Payv2PayWayRate> list = new ArrayList<Payv2PayWayRate>();
		String existIds = map.get("existIds").toString();
		if(existIds.equals("")) {
			list = payList;
		} else {
			List<String> existIdList = Arrays.asList(existIds.split(","));
			for (Payv2PayWayRate rate : payList) {
				if(!existIdList.contains(String.valueOf(rate.getId()))) {
					list.add(rate);
				}
			}
		}
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, list);
		return resultMap;
	}

	// ///////////////////////////////////////////////////////接口///////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @Title: interfacecompanyList
	 * @Description:分页查询获取接口商户列表
	 * @param map
	 * @param request
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2017年2月17日 上午11:19:17
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/interfacecompanyList")
	public Map<String, Object> interfacecompanyList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2Channel pc = (Payv2Channel) request.getSession().getAttribute("admin");
		map.put("channelId", pc.getId());
		map.put("isDelete", 2);// 未删除
		// 分页列表
		PageObject<Payv2BussCompany> pageObject = payv2BussCompanyService.companyList(map);
		// 获取配置支付通道个数
		List<Payv2BussCompany> comapnyList = pageObject.getDataList();
		for (Payv2BussCompany payv2BussCompany : comapnyList) {
			Payv2BussSupportPayWay payv2BussSupportPayWay = new Payv2BussSupportPayWay();
			payv2BussSupportPayWay.setParentId(payv2BussCompany.getId());
			payv2BussSupportPayWay.setIsDelete(2);
			List<Payv2BussSupportPayWay> SupportPayWayList = payv2BussSupportPayWayService.selectByObject(payv2BussSupportPayWay);
			if (SupportPayWayList.size() > 0) {
				payv2BussCompany.setSupportPayWayNum(SupportPayWayList.size());
			} else {
				payv2BussCompany.setSupportPayWayNum(0);
			}
		}
		map.put("parentId", 0);
		// 省
		List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
		if (map.get("companyRangeCity") != null && !map.get("companyRangeCity").equals("")) {
			map.put("parentId", map.get("companyRangeProvince"));
			// 市
			List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
			resultMap.put("cityList", cityList);
		}
		pageObject.setDataList(comapnyList);
		resultMap.put("list", pageObject);
		resultMap.put("provincesList", provincesList);
		resultMap.put("map", map);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}

	/**
	 * @Title: interfaceCompanyDetail
	 * @Description:查看商户详情
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2017年2月17日 下午2:27:45
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/interfaceCompanyDetail")
	public Map<String, Object> interfaceCompanyDetail(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 商户详情
			Payv2BussCompany obj = payv2BussCompanyService.detail(map);
			map.remove("id");
			// 行业
			List<Payv2BussTrade> list = payv2BussTradeService.query(map);
			if (obj.getCompanyRangeProvince() != null) {
				map.put("parentId", obj.getCompanyRangeProvince());
				// 市
				List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
				resultMap.put("cityList", cityList);
			}
			map.put("parentId", 0);
			// 省
			List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
			resultMap.put("tradeList", list);
			resultMap.put("provincesList", provincesList);
			resultMap.put("obj", obj);
			// resultMap.put("map", map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	* @Title: addPayWay 
	* @Description: 添加通道
	* @param @param a
	* @param @param companyId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public boolean addPayWay(String a[], Long companyId, Map<String, Object> param) {
		boolean isOk = false;
		try {
			Payv2BussSupportPayWay payv2BussSupportPayWay = null;
			for (String string : a) {
				String b[] = string.split("-");
				// 支付通道
				Long payWayId = Long.valueOf(b[0]);
				// 支付通道路由 0表示没有
				String rateId = b[1];
				// 通道费
				Double payWayRate = Double.valueOf(b[2]);
				// 排序
				int sortNum = Integer.valueOf(b[3]);
				// 限额
				double maxMoney = Double.valueOf(b[4]);
				// 单笔限额（最小值）
				double oneMinMoney = Double.valueOf(b[5]);
				//每笔限额（最大值）
				double oneMaxMoney = Double.valueOf(b[6]);
				payv2BussSupportPayWay = new Payv2BussSupportPayWay();
				payv2BussSupportPayWay.setIsDelete(2);
				payv2BussSupportPayWay.setParentId(companyId);
				payv2BussSupportPayWay.setPayWayId(payWayId);
				payv2BussSupportPayWay.setSortNum(sortNum);
				// 查询排序是否存在：
				Payv2BussSupportPayWay payv2BussSupportPayWay1 = new Payv2BussSupportPayWay();
				payv2BussSupportPayWay1 = payv2BussSupportPayWayService.selectSingle(payv2BussSupportPayWay);
				if (payv2BussSupportPayWay1 == null) {
					isOk = true;
				} else {
					isOk = false;
					break;
				}
				if(isOk){
					param.put("id", payWayId);
					Payv2PayWay ppw = payv2PayWayService.detail(param);
					if (!"0".equals(rateId)) {
						payv2BussSupportPayWay.setRateId(Long.valueOf(rateId));

					}
					payv2BussSupportPayWay.setPayType(ppw.getWayType());
					payv2BussSupportPayWay.setPayWayRate(payWayRate);
					payv2BussSupportPayWay.setPayWayStatus(1);
					payv2BussSupportPayWay.setCreateTime(new Date());
					payv2BussSupportPayWay.setMaxMoney(maxMoney);
					payv2BussSupportPayWay.setOneMinMoney(oneMinMoney);
					payv2BussSupportPayWay.setOneMaxMoney(oneMaxMoney);
					payv2BussSupportPayWayService.add(payv2BussSupportPayWay);
					logger.info("支付通道添加成功");
				}
				
			}
		} catch (Exception e) {
			logger.info("支付通道添加失败");
			e.printStackTrace();
		}
		return isOk;
	}
}
