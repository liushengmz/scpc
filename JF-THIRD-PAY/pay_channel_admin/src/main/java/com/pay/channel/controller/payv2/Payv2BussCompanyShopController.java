package com.pay.channel.controller.payv2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.core.teamwork.base.util.IdUtils;
import com.core.teamwork.base.util.ObjectUtil;
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
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2BussCompanyShopMapper;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.QRCodeUtilByZXing;
import com.pay.business.util.ZipCompressor;
import com.pay.business.util.qrcode.GetImage;
import com.pay.channel.controller.admin.BaseManagerController;

/**
 * 
 * @ClassName: Payv2BussCompanyShopController
 * @Description: 商户商铺表
 * @author mofan
 * @date 2016年12月6日 15:13:11
 */
@Controller
@RequestMapping("/payv2BussCompanyShop")
public class Payv2BussCompanyShopController extends BaseManagerController<Payv2BussCompanyShop, Payv2BussCompanyShopMapper> {
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
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;

	/**
	 * 商户商铺列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/payv2BussCompanyShopList")
	public ModelAndView payv2BussCompanyAppList(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("companyShop/pay_shop_list");
		Payv2Channel pc = getAdmin();
		map.put("channelId", pc.getId());
		map.put("isDelete", 2);
		PageObject<Payv2BussCompanyShop> pageObject = payv2BussCompanyShopService.payv2BussCompanyShopList(map);
		mv.addObject("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyShop(payv2BussCompany);
		mv.addObject("companyList", companyList);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 从商户管理进入商户商铺列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toPayv2BussCompanyShopList")
	public ModelAndView toPayv2BussCompanyShopList(@RequestParam Map<String, Object> map) {
		ModelAndView mv = new ModelAndView("companyShop/pay_from_company_shop_list");
		Payv2Channel pc = getAdmin();
		map.put("channelId", pc.getId());
		map.put("isDelete", 2);
		PageObject<Payv2BussCompanyShop> pageObject = payv2BussCompanyShopService.payv2BussCompanyShopList(map);
		mv.addObject("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyShop(payv2BussCompany);
		mv.addObject("companyList", companyList);
		mv.addObject("map", map);
		return mv;
	}

	/**
	 * 跳转新增商户商铺
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAddPayv2BussCompanyShop")
	public ModelAndView toAddPayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
		ModelAndView view = new ModelAndView("companyShop/pay_shop_add");
		view.addObject("map", map);
		Payv2Channel pc = getAdmin();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyShop(payv2BussCompany);
		view.addObject("companyList", companyList);
		map.put("parentId", 0);
		// 省
		List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
		view.addObject("provincesList", provincesList);
		// 钱包
		List<Payv2PayWay> paywayList = payv2PayWayService.selectByPayWay(new HashMap<String, Object>());
		view.addObject("paywayList", paywayList);
		return view;
	}

	/**
	 * 从商品管理跳转新增商户商铺
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAddCompanyPayv2BussCompanyShop")
	public ModelAndView toAddCompanyPayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
		ModelAndView view = new ModelAndView("companyShop/pay_from_company_shop_add");
		view.addObject("map", map);
		Payv2Channel pc = getAdmin();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyShop(payv2BussCompany);
		view.addObject("companyList", companyList);
		map.put("parentId", 0);
		// 省
		List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
		view.addObject("provincesList", provincesList);
		// 钱包
		List<Payv2PayWay> paywayList = payv2PayWayService.selectByPayWay(new HashMap<String, Object>());
		view.addObject("paywayList", paywayList);
		return view;
	}

	/**
	 * 添加商户商铺
	 * .
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addPayv2BussCompanyShop")
	public Map<String, Object> addPayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			map.put("shopKey", IdUtils.createId());
			map.put("shopStatus",2);//默认审核通过
			payv2BussCompanyShopService.add(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("添加商户商铺出错！", e);
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "添加商户商铺出错！");
		}
		resultMap.put("companyId", map.get("companyId"));
		return resultMap;
	}

	/**
	 * 查看商户商铺审核未通过原因
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toViewFailReason")
	public ModelAndView toViewFailReason(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyShop/pay_shop_view");
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
			}
		} catch (Exception e) {
			logger.error(" 查看商户商铺页面报错", e);
		}
		return mvc;
	}

	/**
	 * 审核
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toApprove")
	public ModelAndView toApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyShop/pay_shop_approve");
		mvc.addObject("map", map);
		return mvc;
	}

	/**
	 * 编辑商户商铺
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/toEditPayv2BussCompanyShop")
	public ModelAndView toEditPayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyShop/pay_shop_edit");
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
				init(payv2BussCompanyShop,mvc,map);
			}
		} catch (Exception e) {
			logger.error(" 跳转商户商铺编辑页面报错", e);
		}
		return mvc;
	}

	
	private void init(Payv2BussCompanyShop payv2BussCompanyShop,ModelAndView mvc,Map<String, Object> map){
		if(payv2BussCompanyShop != null && payv2BussCompanyShop.getShopWeekTime() != null 
				&& !"".equals(payv2BussCompanyShop.getShopWeekTime())){
			String[] weekTimes = payv2BussCompanyShop.getShopWeekTime().split(",");
			payv2BussCompanyShop.setShopWeekTimeStart(weekTimes[0]);
			payv2BussCompanyShop.setShopWeekTimeEnd(weekTimes[weekTimes.length - 1]);
		}
		map.put("id", payv2BussCompanyShop.getPayWayId());
		Payv2PayWay payv2PayWay = payv2PayWayService.detail(map);
		//钱包支付通道json
		String inline = payv2PayWay.getIncomeLine();
		if (inline != null && inline.length() > 3) {
			JSONArray jsonArray = JSONArray
					.parseArray(inline);
			payv2BussCompanyShop.setInlineObj(jsonArray);
		}
		payv2BussCompanyShop.setPayWayName(payv2PayWay.getWayName());
		map.remove("id");
		if (payv2BussCompanyShop.getShopRangeProvince() != null) {
			map.put("parentId", payv2BussCompanyShop.getShopRangeProvince());
			// 市
			List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
			mvc.addObject("cityList", cityList);
		}
		Payv2Channel pc = getAdmin();
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(pc.getId());
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
	 * 编辑商户商铺
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2BussCompanyShop", method = RequestMethod.POST)
	public Map<String, Object> updatePayv2BussCompanyShop(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				map.put("updateTime", new Date());
				map.put("shopStatus", 1);
				payv2BussCompanyShopService.update(map);
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
	 * 获取配置支付方式，和确认店铺信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/validateBeforeBuildQRcode")
	public ModelAndView validateBeforeBuildQRcode(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("companyShop/pay_shop_qrcode_confirm");
		if (map.get("id") != null) {
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(Long.valueOf(map.get("id").toString()));
			payv2BussAppSupportPayWay.setMerchantType(2);// 商户类型1.app2.店铺
			List<Payv2BussAppSupportPayWay> paywayList = payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
			if (paywayList != null && paywayList.size() > 0) {
				Payv2BussCompanyShop payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
				filter(paywayList,map);
				mvc.addObject("paywayList", paywayList);

			} else {
				mvc.addObject("message", "请先设置支付方式，才能生成二维码！");
			}
		}
		return mvc;
	}

	private void filter(List<Payv2BussAppSupportPayWay> paywayList,Map<String, Object> map){

		for (Payv2BussAppSupportPayWay payWay : paywayList) {
			Payv2BussSupportPayWay pay = new Payv2BussSupportPayWay();
			pay.setId(payWay.getPayWayId());
			pay = payv2BussSupportPayWayService.selectSingle(pay);
			if (pay != null) {
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setId(pay.getPayWayId());
				payv2PayWay = payv2PayWayService.selectSingle(payv2PayWay);
				if (payv2PayWay != null) {
					payWay.setWayName(payv2PayWay.getWayName());
					payWay.setPayWayUserName(pay.getPayWayUserName());
					payWay.setPayWayUserNum(pay.getPayWayUserNum());
				}
			}
		}
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
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/viewBuildQRCode")
	public ModelAndView viewBuildQRCode(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mvc = new ModelAndView("companyShop/pay_shop_qrcode_view");
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

	/**
	 * 
	 * @Title: updatePayv2BussCompanyShopNo
	 * @Description:终止商铺合作
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月21日 上午11:07:18
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2BussCompanyShopNo")
	public Map<String, Object> updatePayv2BussCompanyShopNo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				map.put("updateTime", new Date());
				payv2BussCompanyShopService.updateFor(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("终止商铺合作失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "终止商铺合作出错！");
			}
		}
		return resultMap;
	}

	/**
	 * @Title: updatePayv2BussCompanyShopYes
	 * @Description:商铺恢复合作
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月21日 上午11:13:31
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePayv2BussCompanyShopYes")
	public Map<String, Object> updatePayv2BussCompanyShopYes(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "id" }, map);
		if (isNotNull) {
			try {
				// 首先查询商铺上级：商户是否启动正常状态
				Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
				payv2BussCompanyShop.setId(Long.valueOf(map.get("id").toString()));
				payv2BussCompanyShop.setIsDelete(2);
				payv2BussCompanyShop = payv2BussCompanyShopService.selectSingle(payv2BussCompanyShop);
				if (payv2BussCompanyShop != null) {
					Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
					payv2BussCompany.setId(payv2BussCompanyShop.getCompanyId());
					payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
					if (payv2BussCompany != null) {
						int isDelete = payv2BussCompany.getIsDelete();
						int status = payv2BussCompany.getCompanyStatus();
						if (isDelete == 1) {
							resultMap.put("resultCode", 201);// 已经删除不能启动
							return resultMap;
						}
						if (status == 4) {
							resultMap.put("resultCode", 202);// 已经关闭不能启动
							return resultMap;
						}
					}
				}
				map.put("updateTime", new Date());
				payv2BussCompanyShopService.update(map);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} catch (Exception e) {
				logger.error("恢复合作APP失败", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "修改商户APP出错！");
			}
		}
		return resultMap;
	}
	
	
    /**
     * 根据钱包加载二维码支付通道
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPayWayBy")
    public Map<String,Object> getPayWayBy(@RequestParam Map<String, Object> map){
    	Map<String,Object> resultMap = new HashMap<>();
    	try {
			if (null != map.get("id")) {
				Payv2PayWay payv2PayWay = payv2PayWayService.detail(map);
				//收单机构json
				String inline = payv2PayWay.getIncomeLine();
				if (inline != null && inline.length() > 3) {
					JSONArray jsonArray = JSONArray
							.parseArray(inline);
					resultMap.put("inlineObj", jsonArray);
				}
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "加载二维码支付通道出错！");
		}
    	return resultMap;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
	 * 商户商铺列表
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
    @ResponseBody
	@RequestMapping("/getCompanyShopList")
	public Map<String,Object> getCompanyShopList(@RequestParam Map<String, Object> map) {
		Map<String,Object> resultMap = new HashMap<>();
		Payv2Channel pc = getAdmin();
		map.put("channelId",1);
		map.put("isDelete", 2);
		PageObject<Payv2BussCompanyShop> pageObject = payv2BussCompanyShopService.payv2BussCompanyShopList(map);
		resultMap.put("list", pageObject);
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		payv2BussCompany.setChannelId(1l);
		payv2BussCompany.setIsDelete(2);
		List<Payv2BussCompany> companyList = payv2BussCompanyService.selectForCompanyShop(payv2BussCompany);
		resultMap.put("companyList", companyList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}
    
    /**
	 * 二维码列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/qrcodeList")
	public ModelAndView qrcodeList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mvc = new ModelAndView("qrcode/pay_shop_qrcode");
		try {
			map.put("isDelete", 2);
			map.put("shopStatus", 2);
			PageObject<Payv2BussCompanyShop> list = payv2BussCompanyShopService.qrcodeList(map);
			mvc.addObject("list", list);
			mvc.addObject("map", map);
		} catch (Exception e) {
			logger.error(" 获取商铺详情报错", e);
			e.printStackTrace();
		}
		return mvc;
	}
    
    /**
	 * 上传支付宝收款码
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
    @ResponseBody
	@RequestMapping("/uploadZipCode")
	public Map<String,Object> uploadZipCode(@RequestParam(value = "file", required = false) MultipartFile fileinfo, @RequestParam Map<String, Object> map, HttpServletRequest request) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	if (fileinfo != null) {
    	     try {
    	    	 	String fileName = fileinfo.getOriginalFilename();
    	    	 	byte[] arrays = fileinfo.getBytes();
    	    	 	int index = fileName.lastIndexOf(".");
    	            String indexStr =  fileName.substring(index);
    	            File f = getFile(arrays, request.getSession().getServletContext().getRealPath("/")
    	            		, UUID.randomUUID().toString()+indexStr);
    	    	 	map.put("fileName", f.getPath());
    	    	 	map.put("path", request.getSession().getServletContext().getRealPath("/"));
    	    	 	payv2BussCompanyShopService.uploadZipCode(map);
    	    	 	if(null!=f && f.exists()){
    					f.delete();
    	            }
    	    	 	resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
	    	}catch (Exception e) {
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
				e.printStackTrace();
			}
    	}else{
    		resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, null);
    	}
        return resultMap;
	}
    
    /**
	 * 查看二维码
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/viewBuildQRCode1")
	public ModelAndView viewBuildQRCode1(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mvc = new ModelAndView("qrcode/pay_shop_qrcode_view");
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		try {
			if (null != map.get("id")) {
				payv2BussCompanyShop = payv2BussCompanyShopService.detail(map);
				mvc.addObject("payv2BussCompanyShop", payv2BussCompanyShop);
			}
		} catch (Exception e) {
			logger.error(" 获取商铺详情报错", e);
			e.printStackTrace();
		}
		return mvc;
	}
    
    /**
	 * 
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
    @ResponseBody
	@RequestMapping("/exportZipCode")
	public Map<String,Object> exportZipCode(@RequestParam Map<String, Object> map
			, HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			map.put("isDelete", 2);
			map.put("shopStatus", 2);
			List<Payv2BussCompanyShop> list = payv2BussCompanyShopService.allQrcodeList(map);
			if(list==null&list.size()==0){
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "没有数据");
				return resultMap;
			}
			for (int i = 0; i < list.size(); i++) {
				Payv2BussCompanyShop payv2BussCompanyShop = list.get(i);
				String url = payv2BussCompanyShop.getShopTwoCodeUrl();  
		        byte[] btImg = GetImage.getImageFromNetByUrl(url);  
		        if(null != btImg && btImg.length > 0){  
		            System.out.println("读取到：" + btImg.length + " 字节");  
		            String fileName = System.currentTimeMillis()+""+i+".jpg";  
		            GetImage.writeImageToDisk(btImg, fileName,request.getSession().getServletContext().getRealPath("/")+"export"+File.separator);  
		        }else{  
		            System.out.println("没有从该连接获得内容");  
		        }  
			}
			
            ZipCompressor.getZipFile(request.getSession().getServletContext().getRealPath("/")+"export.zip");  
            ZipCompressor.compressExe(request.getSession().getServletContext().getRealPath("/")+"export"+File.separator);
			
            
            InputStream in = new FileInputStream(request.getSession().getServletContext().getRealPath("/")+"export.zip"); //获取文件的流  
            OutputStream os = response.getOutputStream();
            int len = 0;  
            byte buf[] = new byte[1024];//缓存作用 
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment; filename=\""+new String("export.zip".getBytes("GB2312"),"ISO8859-1")+"\";");// 
            os = response.getOutputStream();//输出流  
            while( (len = in.read(buf)) > 0 ) //切忌这后面不能加 分号 ”;“  
            {  
                 os.write(buf, 0, len);//向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取  
            }
            in.close();
            os.close();
            
            new File(request.getSession().getServletContext().getRealPath("/")+"export.zip").delete();
            deleteFile(new File(request.getSession().getServletContext().getRealPath("/")+"export"+File.separator));
		} catch (Exception e) {
			logger.error(" 获取商铺详情报错", e);
			e.printStackTrace();
		}
    	
        return resultMap;
	}
    
    /** 
     * 通过递归调用删除一个文件夹及下面的所有文件 
     * @param file 
     */  
    public void deleteFile(File file){  
    	
        if(file.isFile()){//表示该文件不是文件夹  
            file.delete();  
        }else{  
            //首先得到当前的路径  
            String[] childFilePaths = file.list();  
            for(String childFilePath : childFilePaths){  
                File childFile=new File(file.getAbsolutePath()+File.separator+childFilePath);  
                deleteFile(childFile);  
            }  
            file.delete();  
        }  
    }
    
    /**
     * 根据byte数组，生成文件
     */
    public File getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
//            file = new File(filePath + "\\" + fileName);
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
}
