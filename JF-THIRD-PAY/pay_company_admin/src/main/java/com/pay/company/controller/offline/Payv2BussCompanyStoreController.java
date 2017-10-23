package com.pay.company.controller.offline;

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

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.controller.admin.BaseManagerController;

@Controller
@RequestMapping("/offline/bussCompanyStore/*")
public class Payv2BussCompanyStoreController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	
	/**
	 * 只获取门店ID 和 门店名称
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("getShopIdNameList")
	@ResponseBody
	public Map<String,Object> getShopIdNameList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("companyId", admin.getId());
		paramMap.put("isDelete", 2);
		try {
			List<Map<String,Object>> resutlList = payv2BussCompanyShopService.getshopIdNameList(map);	
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resutlList);
		} catch (Exception e) {
			logger.error("获取门店号和名称异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 门店列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("storeList")
	@ResponseBody
	public Map<String,Object> storeList(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"curPage","pageData"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Map<String,Object> paramMap = new HashMap<>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		paramMap.put("companyId", admin.getId());
		paramMap.put("isDelete", 2);//未删除
		if(map.containsKey("shopCard") && StringUtils.isNotBlank(map.get("shopCard").toString())){
			paramMap.put("shopCard", map.get("shopCard").toString());
		}
		if(map.containsKey("pageData") && map.containsKey("curPage") && map.get("pageData") != null && map.get("curPage") != null){
			paramMap.put("pageData", NumberUtils.createInteger(map.get("pageData").toString()));
			paramMap.put("curPage", NumberUtils.createInteger(map.get("curPage").toString()));
		}
		try {
			PageObject<Payv2BussCompanyShop> pagequery = payv2BussCompanyShopService.Pagequery(paramMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, pagequery);
		} catch (Exception e) {
			logger.error("获取线下门店列表异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 门店详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("storeDetail")
	@ResponseBody
	public Map<String,Object> storeDetail(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"id"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		try {
			Long id = NumberUtils.createLong(map.get("id").toString());
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("id",id);
			Payv2BussCompanyShop detail = payv2BussCompanyShopService.detail(paramMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, detail);
		} catch (Exception e) {
			logger.error("获取线下门店详情异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 添加门店
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("addStore")
	@ResponseBody
	public Map<String,Object> addStore(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"shopName","provinceId","cityId","shopAddress","shopContacts","shopContactsPhone","shopEmail","shopDayStartTime","shopDayEndTime","shopIcon","shopDesc"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Payv2BussCompanyShop shop = setCompanyShop(map);
		shop.setCompanyId(admin.getId());
		shop.setCreateTime(new Date());
		try {
			payv2BussCompanyShopService.add(shop);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("线下门店添加失败", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 修改门店
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("editStore")
	@ResponseBody
	public Map<String,Object> editStore(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"id","shopName","provinceId","cityId","shopAddress","shopContacts","shopContactsPhone","shopEmail","shopDayStartTime","shopDayEndTime","shopIcon","shopDesc"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Payv2BussCompanyShop shop = setCompanyShop(map);
		shop.setCompanyId(admin.getId());
		shop.setUpdateTime(new Date());
		try {
			payv2BussCompanyShopService.update(shop);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("线下门店修改失败", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	public Payv2BussCompanyShop setCompanyShop(Map<String,Object> map){
		Payv2BussCompanyShop shop = new Payv2BussCompanyShop();
		if(map.containsKey("id") && map.get("id")!=null){
			shop.setId(NumberUtils.createLong(map.get("id").toString()));
		}
		String shopName = map.get("shopName").toString();
		shop.setShopName(shopName);
		Long provinceId = NumberUtils.createLong(map.get("provinceId").toString());
		shop.setShopRangeProvince(provinceId);
		Long cityId = NumberUtils.createLong(map.get("cityId").toString());
		shop.setShopRangeCity(cityId);
		String shopAddress = map.get("shopAddress").toString();
		shop.setShopAddress(shopAddress);
		String shopContacts = map.get("shopContacts").toString();
		shop.setShopContacts(shopContacts);
		String shopContactsPhone = map.get("shopContactsPhone").toString();
		shop.setShopContactsPhone(shopContactsPhone);
		String shopEmail = map.get("shopEmail").toString();
		shop.setShopEmail(shopEmail);
		String shopDayStartTime = map.get("shopDayStartTime").toString();
		shop.setShopDayStartTime(shopDayStartTime);
		String shopDayEndTime = map.get("shopDayEndTime").toString();
		shop.setShopDayEndTime(shopDayEndTime);
		String shopIcon = map.get("shopIcon").toString();
		shop.setShopIcon(shopIcon);
		String shopDesc = map.get("shopDesc").toString();
		shop.setShopDesc(shopDesc);
		if(map.containsKey("shopCard") && StringUtils.isNotBlank(map.get("shopCard").toString())){
			shop.setShopDesc(map.get("shopCard").toString());
		}
		return shop;
	}
	
	/**
	 * 删除门店
	 * @param map
	 * @param request
	 * @return
	 * @throws Throwable 
	 */
	@RequestMapping("deleteStore")
	@ResponseBody
	public Map<String,Object> deleteStore(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"id"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		try {
			Long id = NumberUtils.createLong(map.get("id").toString());
			Payv2BussCompanyShop shop = new Payv2BussCompanyShop();
			shop.setId(id);
			shop.setIsDelete(1);
			payv2BussCompanyShopService.delete(shop);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Throwable e) {
			logger.error("删除线下门店异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 获取省份
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("getProvince")
	@ResponseBody
	public Map<String,Object> getProvince(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("parentId", 0);
		try {
			List<Payv2ProvincesCity> query = payv2ProvincesCityService.query(paramMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, query);
		} catch (Exception e) {
			logger.error("获取省份信息异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 获取城市
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("getCity")
	@ResponseBody
	public Map<String,Object> getCity(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"provinceId"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		Long provinceId = NumberUtils.createLong(map.get("provinceId").toString());
		if(provinceId==0){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数数据错误");
		}
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("parentId",provinceId);
		try {
			List<Payv2ProvincesCity> query = payv2ProvincesCityService.query(paramMap);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, query);
		} catch (Exception e) {
			logger.error("获取城市信息异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
}
