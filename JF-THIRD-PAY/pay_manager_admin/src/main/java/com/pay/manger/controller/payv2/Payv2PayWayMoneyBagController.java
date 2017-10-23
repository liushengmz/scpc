
package com.pay.manger.controller.payv2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.sysconfig.entity.SysConfigDictionary;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2PayWayMoneyBagController 
* @Description:支付集钱包管理
* @author mofan
* @date 2016年12月8日 上午10:56:53
*/
@RequestMapping("/payv2PaywayMoneyBag/*")
@Controller
public class Payv2PayWayMoneyBagController extends BaseManagerController<Payv2PayWay, Payv2PayWayMapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Payv2PayWayMoneyBagController.class);
    @Autowired
    private Payv2PayWayService payv2PayWayService;
    @Autowired
    private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
    @Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
    @Autowired
	private Payv2ChannelService payv2ChannelService;//渠道商
    @Autowired
    private SysConfigDictionaryService sysConfigDictionaryService;
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
    /**
    * @Title: getPayv2PayWayMoneyBagList 
    * @Description:获取支付钱包列表
    * @param map
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月8日 上午10:57:52 
    * @throws
    */
    @RequestMapping("/getPayv2PayWayMoneyBagList")
    public ModelAndView getPayv2PayWayMoneyBagList(@RequestParam Map<String, Object> map) {
        ModelAndView andView = new ModelAndView("payv2/moneyBag/payv2payway_moneybag_list");
        map.put("isDelete", 2);
        map.put("wayType", 1);//通道类型,1钱包,2第三方支付'
        PageObject<Payv2PayWay> pageList = payv2PayWayService.getPayv2PayWayList(map);
        andView.addObject("list", pageList);
        andView.addObject("map", map);
    	//获取所有第三方支付钱包
    	map = new HashMap<String, Object>();
    	map.put("wayType", "2");
    	map.put("isDelete", "2");//删除
    	map.put("status", "1");//1启用
    	List<Payv2PayWay> payv2PayWayList = payv2PayWayService.query(map);
    	andView.addObject("payv2PayWayList", payv2PayWayList);
        //省
    	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(new HashMap<String, Object>());
    	andView.addObject("provincesList", provincesList);
        return andView;
    }
    /**
    * @Title: addPayv2PayWayMoneyBagTc 
    * @Description:添加支付钱包弹窗
    * @param map
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月8日 上午11:21:22 
    * @throws
    */
    @RequestMapping("/addPayv2PayWayMoneyBagTc")
    public ModelAndView addPayv2PayWayMoneyBagTc(@RequestParam Map<String, Object> map){
    	ModelAndView andView = new ModelAndView("payv2/moneyBag/payv2payway_moneybag_add");
    	SysConfigDictionary sysConfigDictionary = new SysConfigDictionary();
    	map.put("dictPvalue", -1);
    	map.put("dictValue", "PAY_TYPE");
    	sysConfigDictionary = sysConfigDictionaryService.detail(map);
    	init(sysConfigDictionary,andView,map);
        return andView;
    }
    
    public void init(SysConfigDictionary sysConfigDictionary,ModelAndView andView,Map<String, Object> map){
    	if(sysConfigDictionary != null){
    		map = new HashMap<String, Object>();
    		map.put("dictPvalue", sysConfigDictionary.getId());
			List<SysConfigDictionary> dicList = sysConfigDictionaryService.query(map);
			andView.addObject("dicList", dicList);
    	}
        //省
    	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
    	andView.addObject("provincesList", provincesList);
    	map = new HashMap<String, Object>();
    	map.put("wayType", "2");
    	map.put("isDelete", "2");//删除
    	map.put("status", "1");//1启用
    	List<Payv2PayWay> payv2PayWayList = payv2PayWayService.query(map);
    	andView.addObject("payv2PayWayList", payv2PayWayList);
    }
    
    
    /**
    * @Title: addPayv2PayWayMoneyBag 
    * @Description:添加支付钱包
    * @param map
    * @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @date 2016年12月8日 下午7:57:11 
    * @throws
    */
    @ResponseBody
    @RequestMapping("/addPayv2PayWayMoneyBag")
    public Map<String,Object> addPayv2PayWayMoneyBag(@RequestParam Map<String, Object> map){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	try {
    		filter(map);
    		map.put("createTime", new Date());
    		map.put("wayType", 1);
    		map.put("status", 2);//默认不启动
			payv2PayWayService.add(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("添加支付渠道提交失败",e);
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "添加支付渠道提交失败!");
		}
		return resultMap;
    }
    
    
    /**
    * @Title: updatePayv2PayWayMoneyBag 
    * @Description: 修改支付钱包 启动/停用/删除
    * @param map
    * @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @date 2016年12月8日 上午9:45:03 
    * @throws
    */
    @ResponseBody
    @RequestMapping("updatePayv2PayWayMoneyBag")
    public Map<String,Object> updatePayv2PayWayMoneyBag(@RequestParam Map<String, Object> map){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	try {
    		filter(map);
    		map.put("updateTime", new Date());
			payv2PayWayService.update(map);
			map.put("payType",1);
			//修改旗下的关联
			payv2PayWayService.updatePayWay(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("修改支付渠道 启动/停用失败",e);
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "修改支付渠道 启动/停用失败!");
		}
		return resultMap;
    }
    
    /**
     * @Title: editPayv2PayWayMoneyBag 
     * @Description:修改支付渠道弹窗
     * @param map
     * @return    设定文件 
     * @return ModelAndView    返回类型 
     * @date 2016年12月8日 上午11:21:22 
     * @throws
     */
     @RequestMapping("editPayv2PayWayMoneyBag")
     public ModelAndView editPayv2PayWayMoneyBag(@RequestParam Map<String, Object> map){
		ModelAndView andView = new ModelAndView("payv2/moneyBag/payv2payway_moneybag_edit");
		Payv2PayWay payv2PayWay = new Payv2PayWay();
		payv2PayWay.setId(Long.valueOf(map.get("id").toString()));
		payv2PayWay = payv2PayWayService.selectSingle(payv2PayWay);
		andView.addObject("payv2PayWay", payv2PayWay);
		fillSomething(payv2PayWay,map,andView);
		return andView;
     }
     /**
     * @Title: getCompanyList 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param map
     * @return    设定文件 
     * @return ModelAndView    返回类型 
     * @date 2016年12月9日 上午10:12:36 
     * @throws
     */
     @RequestMapping("getCompanyList")
     public ModelAndView getCompanyList(@RequestParam Map<String, Object> map){
    	ModelAndView andView = new ModelAndView("payv2/moneyBag/pay_company_bag_list");
    	List<Payv2BussCompany> companyList = new ArrayList<Payv2BussCompany>();
    	fill(companyList,map);
    	//获取渠道商列表
     	Payv2Channel payv2Channel=new Payv2Channel();
     	payv2Channel.setIsDelete(2);
     	List<Payv2Channel>	payv2ChannelList=payv2ChannelService.selectByObject(payv2Channel);
     	andView.addObject("payv2ChannelList", payv2ChannelList); 
    	andView.addObject("list",companyList);
    	andView.addObject("map",map);
        return andView;
     }
    
     
     
     private void filter(Map<String, Object> map){
 		if(map.get("prepareNames") != null && !"".equals(map.get("prepareNames"))){
 			String[] prepareNames = map.get("prepareNames").toString().split(",");
 			if(map.get("prepareNums") != null && !"".equals(map.get("prepareNumStr"))){
 				String[] prepareNums = map.get("prepareNums").toString().split(",");
 				if(map.get("prepareIds") != null && !"".equals(map.get("prepareIds"))){
 					String[] prepareIds = map.get("prepareIds").toString().split(",");
 					if(prepareNames.length == prepareNums.length && prepareNames.length== prepareIds.length){
 						String jsonString = "[";
 						for(int i = 0, length = prepareNames.length; i < length ; i++){
 							jsonString += "{\"wayId\":\"" + prepareIds[i] + "\",\"userName\":\""+prepareNames[i]+"\",\"userNum\":\""+prepareNums[i]+"\"}";
 							if(i >= 0 && i < length - 1){
 								jsonString += ",";
 							}
 						}
 						jsonString += "]";
 						map.put("incomeLine", jsonString);
 					}else{
 						map.put("incomeLine", "[]");
 					}
 				}
 			}
 		}

 		if(map.get("shopRangeProvince") != null && !"".equals(map.get("shopRangeProvince").toString())){
 			String[] shopRangeProvinces = map.get("shopRangeProvince").toString().split(",");
 			String[] shopRangeCitys = map.get("shopRangeCity").toString().split(",");
 			if(shopRangeProvinces.length == shopRangeCitys.length ){
 				String jsonString = "[";
 				for(int i = 0, length = shopRangeProvinces.length; i < length ; i++){
 					jsonString += "{\"pro\":\"" + shopRangeProvinces[i] + "\",\"city\":\""+shopRangeCitys[i]+"\"}";
 					if(i >= 0 && i < length - 1){
 						jsonString += ",";
 					}
 				}
 				jsonString += "]";
 				map.put("discoutRegion", jsonString);
 			}else{
 				map.put("discoutRegion", "[]");
 			}
 		}
     }
    
    private void fillSomething(Payv2PayWay payv2PayWay, Map<String, Object> map, ModelAndView andView){
    	
		String disCountRegion = payv2PayWay.getDiscoutRegion();
		if (disCountRegion != null && !"1".equals(disCountRegion) && disCountRegion.length() > 3) {
			JSONArray jsonArray = JSONArray
					.parseArray(disCountRegion);
			andView.addObject("disCountRegionObj", jsonArray);
		}
		//收单机构json
		String inline = payv2PayWay.getIncomeLine();
		if (inline != null && !"1".equals(inline)) {
			JSONArray jsonArray = JSONArray
					.parseArray(inline);
			andView.addObject("inlineObj", jsonArray);
			String jsonId = "";
			for(int i = 0; i < jsonArray.size();i++){
				JSONObject o = (JSONObject) jsonArray.get(i);
				String value = o.getString("wayId");
				jsonId += value+ ",";
			}
			andView.addObject("wayIdObj", jsonId);
		}
		
		//获取配置在数据字典的支付类型
		SysConfigDictionary sysConfigDictionary = new SysConfigDictionary();
		map.remove("id");
		map.put("dictPvalue", -1);
		map.put("dictValue", "PAY_TYPE");
		sysConfigDictionary = sysConfigDictionaryService.detail(map);
		if (sysConfigDictionary != null) {
			map = new HashMap<String, Object>();
			map.put("dictPvalue", sysConfigDictionary.getId());
			List<SysConfigDictionary> dicList = sysConfigDictionaryService
					.query(map);
			andView.addObject("dicList", dicList);
		}
        //省
    	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
    	andView.addObject("provincesList", provincesList);
    	//获取所有第三方支付钱包
    	map = new HashMap<String, Object>();
    	map.put("wayType", "2");
    	map.put("isDelete", "2");//删除
    	map.put("status", "1");//1启用
    	List<Payv2PayWay> payv2PayWayList = payv2PayWayService.query(map);
    	andView.addObject("payv2PayWayList", payv2PayWayList);
    }
    
     
	private void fill(List<Payv2BussCompany> companyList,
			Map<String, Object> map) {
		// 首先获取商户ID
		Payv2BussSupportPayWay payv2BussSupportPayWay = new Payv2BussSupportPayWay();
		payv2BussSupportPayWay.setPayWayId(Long.valueOf(map.get("payWayId")
				.toString()));
		payv2BussSupportPayWay.setPayType(Integer.valueOf(map.get("payType")
				.toString()));
		payv2BussSupportPayWay.setIsDelete(2);
		payv2BussSupportPayWay.setPayWayStatus(1);
		List<Payv2BussSupportPayWay> supportPayWayList = payv2BussSupportPayWayService
				.selectByObject(payv2BussSupportPayWay);

		for (Payv2BussSupportPayWay payv2BussSupportPayWay2 : supportPayWayList) {
			Long id = payv2BussSupportPayWay2.getParentId();
			if (id != null) {
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(id);
				payv2BussCompany.setIsDelete(2);
				if (map.get("companyName") != null && map.get("companyName") != "") {
					payv2BussCompany.setCompanyName(map.get("companyName").toString());
				}
				if (map.get("channelId") != null && map.get("channelId") != "") {
					payv2BussCompany.setChannelId(Long.valueOf(map.get("channelId").toString()));
				}
				if (map.get("licenseType") != null && map.get("licenseType") != "") {
					payv2BussCompany.setLicenseType(Integer.valueOf(map.get("licenseType").toString()));
				}
				if (map.get("companyScale") != null	&& map.get("companyScale") != "") {
					payv2BussCompany.setCompanyScale(Integer.valueOf(map.get("companyScale").toString()));
				}
				if (map.get("companyRangeProvince") != null	&& map.get("companyRangeProvince") != "") {
					payv2BussCompany.setCompanyRangeProvince(Long.valueOf(map.get("companyRangeProvince").toString()));
				}
				if (map.get("companyRangeCity") != null	&& map.get("companyRangeCity") != "") {
					payv2BussCompany.setCompanyRangeCity(Long.valueOf(map.get("companyRangeCity").toString()));
				}
				if (map.get("companyStatus") != null
						&& map.get("companyStatus") != "") {
					payv2BussCompany.setCompanyStatus(Integer.valueOf(map.get("companyStatus").toString()));
				}
				payv2BussCompany = payv2BussCompanyService.getpayv2BussCompanyInfo(payv2BussCompany);
				if (payv2BussCompany != null) {
					companyList.add(payv2BussCompany);
				}
				Payv2BussSupportPayWay payv2BussSupportPayWay3 = new Payv2BussSupportPayWay();
				payv2BussSupportPayWay3.setParentId(id);
				payv2BussSupportPayWay3.setIsDelete(2);
				payv2BussSupportPayWay3.setPayType(1);
				List<Payv2BussSupportPayWay> SupportPayWayList = payv2BussSupportPayWayService
						.selectByObject(payv2BussSupportPayWay3);
				if (SupportPayWayList.size() > 0) {
					payv2BussCompany.setSupportPayWayNum(SupportPayWayList.size());
				} else {
					payv2BussCompany.setSupportPayWayNum(0);
				}
			}
		}
	}
}
