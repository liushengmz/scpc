package com.pay.manger.controller.payv2;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ftp.FtpUploadClient;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2BussCompanyShopMapper;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.QRCodeUtilByZXing;
import com.pay.manger.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2BussCompanyShopController 
* @Description:商铺管理
* @author zhoulibo
* @date 2016年12月8日 下午4:30:19
* */
@Controller
@RequestMapping("/payv2BussCompanyShop")
public class Payv2BussCompanyShopController extends BaseManagerController<Payv2BussCompanyShop, Payv2BussCompanyShopMapper>{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	/**
	 * 商户商铺列表
	 * @param map
	 * @param request
	 * @return
	 */
    @RequestMapping("/payv2BussCompanyShopList")
    public ModelAndView payv2BussCompanyAppList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/shop/pay_shop_list");
    	PageObject<Payv2BussCompanyShop> pageObject = payv2BussCompanyShopService.payv2BussCompanyShopList(map);
    	mv.addObject("list", pageObject);
    	Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
    	payv2BussCompany.setIsDelete(2);
    	List<Payv2BussCompany> companyList  = payv2BussCompanyService.selectByObject(payv2BussCompany);
    	mv.addObject("companyList", companyList);
    	mv.addObject("map", map);
    	return mv;
    }
    
	/**
	 * 从商户管理进到商户商铺列表
	 * @param map
	 * @param request
	 * @return
	 */
    @RequestMapping("/toPayv2BussCompanyShopList")
    public ModelAndView toPayv2BussCompanyShopList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payv2/shop/pay_from_company_shop_list");
    	PageObject<Payv2BussCompanyShop> pageObject = payv2BussCompanyShopService.payv2BussCompanyShopList(map);
    	mv.addObject("list", pageObject);
    	Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
    	payv2BussCompany.setIsDelete(2);
    	List<Payv2BussCompany> companyList  = payv2BussCompanyService.selectByObject(payv2BussCompany);
    	mv.addObject("companyList", companyList);
    	mv.addObject("map", map);
    	return mv;
    }
    
    /**
     * 跳转新增商户商铺
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("/toAddPayv2BussCompanyShop")
    public ModelAndView toAddPayv2BussCompanyShop(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("payv2/pay_shop_add");
    	view.addObject("map", map);
    	Payv2Channel pc = (Payv2Channel)request.getSession().getAttribute("admin");
    	Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
    	payv2BussCompany.setChannelId(pc.getId());
    	payv2BussCompany.setIsDelete(2);
    	List<Payv2BussCompany> companyList  = payv2BussCompanyService.selectByObject(payv2BussCompany);
    	view.addObject("companyList", companyList);
    	map.put("parentId", 0);
         //省
     	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
     	view.addObject("provincesList", provincesList); 
    	return view;
    }
    
    /**
     * 添加商户商铺
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/addPayv2BussCompanyShop")
    public Map<String, Object> addPayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	map.put("createTime", new Date());
        	payv2BussCompanyShopService.add(map);
        	resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("添加商户商铺出错！", e);
			e.printStackTrace();
	        resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "添加商户商铺出错！");
		}
        return resultMap;
    }
	
    /**
     * 查看商户商铺审核未通过原因
     * @param map
     * @return
     */
    @RequestMapping("/toViewFailReason")
    public ModelAndView toViewFailReason(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/shop/pay_shop_view");
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
			}
		} catch (Exception e) {
			logger.error(" 查看商户商铺页面报错", e);
			e.printStackTrace();
		}
		return mvc;
    }
    
    /**
     * 初始化 转化数据
     * @param map
     * @param payv2BussCompanyShop
     */
    public void initPayv2BussCompanyShop(Map<String, Object> map,Payv2BussCompanyShop payv2BussCompanyShop,ModelAndView mvc){
		if(payv2BussCompanyShop != null && payv2BussCompanyShop.getShopWeekTime() != null 
				&& !"".equals(payv2BussCompanyShop.getShopWeekTime())){
			String[] weekTimes = payv2BussCompanyShop.getShopWeekTime().split(",");
			payv2BussCompanyShop.setShopWeekTimeStart(weekTimes[0]);
			payv2BussCompanyShop.setShopWeekTimeEnd(weekTimes[weekTimes.length - 1]);
		}
		if(payv2BussCompanyShop.getPayWayId() != null){
			
			map.put("id", payv2BussCompanyShop.getPayWayId());
			Payv2PayWay payv2PayWay = payv2PayWayService.detail(map);
			//钱包支付通道json
			String inline = payv2PayWay.getIncomeLine();
			if (inline != null && inline.length() > 3) {
				JSONArray jsonArray = JSONArray
						.parseArray(inline);
				payv2BussCompanyShop.setInlineObj(jsonArray);
			}
			payv2BussCompanyShop.setPayWayName(payv2PayWay.getWayName() == null ? "" : payv2PayWay.getWayName());
		}
		
		map.remove("id");
		if (payv2BussCompanyShop.getShopRangeProvince() != null) {
			map.put("parentId", payv2BussCompanyShop.getShopRangeProvince());
			// 市
			List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
			mvc.addObject("cityList", cityList);
		}
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyShop(payv2BussCompany);
		mvc.addObject("companyList", companyList);
		map.put("parentId", 0);
		// 省
		List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
		mvc.addObject("provincesList", provincesList);
		// 钱包
		List<Payv2PayWay> paywayList = payv2PayWayService.selectByPayWay(new HashMap<String, Object>());
		mvc.addObject("paywayList", paywayList);
    }
    
    
    
    /**
     * 审核
     * @param map
     * @return
     */
    @RequestMapping("/toApprove")
    public ModelAndView toApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/shop/pay_shop_approve");
		mvc.addObject("map", map);
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
				initPayv2BussCompanyShop(map,payv2BussCompanyShop,mvc);
			}
		} catch (Exception e) {
			logger.error(" 跳转商户商铺审核页面报错", e);
		}
		return mvc;
    }
    
    
    /**
     * 审核拒绝
     * @param map
     * @return
     */
    @RequestMapping("/toRejectApprove")
    public ModelAndView toRejectApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/shop/pay_shop_approve_reject");
		mvc.addObject("map", map);
		return mvc;
    }
    
    /**
     * 审核
     * @param map
     * @return
     */
    @RequestMapping("/toFromCompanyApprove")
    public ModelAndView toFromCompanyApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/shop/pay_from_company_shop_approve");
		mvc.addObject("map", map);
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
				initPayv2BussCompanyShop(map,payv2BussCompanyShop,mvc);
			}
		} catch (Exception e) {
			logger.error(" 跳转商户商铺审核页面报错", e);
		}
		return mvc;
    }
    
    /**
     * 审核拒绝
     * @param map
     * @return
     */
    @RequestMapping("/toFromCompanyRejectApprove")
    public ModelAndView toFromCompanyRejectApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/shop/pay_from_company_shop_approve_reject");
		mvc.addObject("map", map);
		return mvc;
    }
    
   /**
   * @Title: toEditPayv2BussCompanyShop 
   * @Description:获取商铺详情
   * @param map
   * @return    设定文件 
   * @return ModelAndView    返回类型 
   * @date 2016年12月8日 下午5:17:20 
   * @throws
   */
    @RequestMapping("/toEditPayv2BussCompanyShop")
    public ModelAndView toEditPayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/shop/pay_shop_info");
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
				initPayv2BussCompanyShop(map,payv2BussCompanyShop,mvc);
			}
		} catch (Exception e) {
			logger.error(" 获取商铺详情报错", e);
		}
		return mvc;
    }
    
 
    /**
   	 * 编辑商户商铺
   	 * @param map
   	 * @return
   	 */
   	@ResponseBody
   	@RequestMapping(value = "/updatePayv2BussCompanyShop", method = RequestMethod.POST)
   	public Map<String, Object> updatePayv2BussCompanyShop(@RequestParam Map<String, Object> map, HttpServletRequest request) {
   		Map<String, Object> resultMap = new HashMap<String, Object>();
   		if (map.get("id") != null) {
   			try {
   				map.put("updateTime", new Date());
   				payv2BussCompanyShopService.update(map);
   				if(map.get("shopStatus") != null && "2".equals(map.get("shopStatus").toString())){//审核通过
   					buildQRCode(map,request);
   				}
   				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
   			} catch (Exception e) {
   				logger.error("修改商户商铺提交失败", e);
   				e.printStackTrace();
   				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "修改商户商铺出错！");
   			}
   		}
   		resultMap.put("companyId", map.get("companyId"));
   		return resultMap;
   	}
    
   	/**
	 * 生成二维码
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/buildQRCode")
	public Map<String, Object> buildQRCode(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String shopKey =  map.get("shopKey") == null ? "" : map.get("shopKey").toString();
		if("".equals(shopKey)){
			shopKey = "123";
		}
		String content = ReadPro.getValue("qr_code")+"?shopKey=" + shopKey;
		String path = request.getSession().getServletContext().getRealPath("/");
		String name = System.currentTimeMillis() + "";
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		File file1 = new File(path, name + ".jpg");
		try {
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 350, 350, hints);

			QRCodeUtilByZXing zx = new QRCodeUtilByZXing();
			zx.writeToFile(bitMatrix, "jpg", file1);
			
			FtpUploadClient ftp = new FtpUploadClient();
			String ftpDir = System.currentTimeMillis() + File.separator;
			// upGetReady 第一个参数是路径上传路径 第二个参数是文件路径
			Boolean flag = ftp.upGetReady(ftpDir, file1.getPath());
			String url = ReadPro.getValue("ftp.visit.path") + ReadPro.getValue("ftp.upload.path") + ftpDir + file1.getName();
			map.put("shopTwoCodeUrl", url);
			payv2BussCompanyShopService.update(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error(" 生成二维码错误", e);
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "生成二维码错误!");
		}
		if (null != file1 && file1.exists()) {
			file1.delete();
		}
		
		return resultMap;
	}

	
    /**
     * 查看二维码
     * @param map
     * @return
     */
	@RequestMapping("/viewBuildQRCode")
    public ModelAndView viewBuildQRCode(@RequestParam Map<String, Object> map, HttpServletRequest request) {
    	ModelAndView mvc = new ModelAndView("payv2/shop/pay_shop_qrcode_view");
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
			}
		} catch (Exception e) {
			logger.error(" 获取商铺详情报错", e);
		}
		return mvc;
	}
    
}
