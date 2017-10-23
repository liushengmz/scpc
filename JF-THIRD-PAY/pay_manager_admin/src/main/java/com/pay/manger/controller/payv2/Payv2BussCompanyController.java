package com.pay.manger.controller.payv2;

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

import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.payv2.entity.Payv2Bank;
import com.pay.business.payv2.entity.Payv2BussTrade;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2BankService;
import com.pay.business.payv2.service.Payv2BussTradeService;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.GenerateUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2BussCompanyController 
* @Description:商户控制前
* @author mofan
* @date 2016年12月8日 下午18:21:52
*/
@Controller
@RequestMapping("/payv2BussCompany/*")
public class Payv2BussCompanyController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{

	private static final Logger logger = Logger.getLogger(Payv2BussCompanyController.class);
	
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	
	@Autowired
	private Payv2BussTradeService payv2BussTradeService;
	
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	@Autowired
	private Payv2ChannelService payv2ChannelService;//渠道商
	
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;//商户支持的支付通道表
	
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	@Autowired
	private Payv2BankService payv2BankService;
	
	/**
	 * 商户列表
	 * @param map
	 * @return
	 */
    @RequestMapping("/companyList")
    public ModelAndView companyList(@RequestParam Map<String, Object> map,HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("payv2/company/pay_company_list");
        map.put("isDelete", 2);//未删除
        //分页列表
        PageObject<Payv2BussCompany> pageObject = payv2BussCompanyService.companyList(map);
        //获取配置支付通道个数
        List<Payv2BussCompany> comapnyList=  pageObject.getDataList();
        for (Payv2BussCompany payv2BussCompany : comapnyList) {
        	Payv2BussSupportPayWay payv2BussSupportPayWay=new Payv2BussSupportPayWay();
        	payv2BussSupportPayWay.setParentId(payv2BussCompany.getId());
        	payv2BussSupportPayWay.setIsDelete(2);
        	List<Payv2BussSupportPayWay> SupportPayWayList=	payv2BussSupportPayWayService.selectByObject(payv2BussSupportPayWay);
        	if(SupportPayWayList.size()>0){
        		payv2BussCompany.setSupportPayWayNum(SupportPayWayList.size());
        	}else{
        		payv2BussCompany.setSupportPayWayNum(0);
        	}
		}
        map.put("parentId", 0);
        //省
    	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
    	if(map.get("companyRangeCity")!=null&&!map.get("companyRangeCity").equals("")){
    		map.put("parentId", map.get("companyRangeProvince"));
    		//市
    		List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
    		mv.addObject("cityList", cityList); 
    	}
    	//获取渠道商列表
    	Payv2Channel payv2Channel=new Payv2Channel();
    	payv2Channel.setIsDelete(2);
    	List<Payv2Channel>	payv2ChannelList=payv2ChannelService.selectByObject(payv2Channel);
    	pageObject.setDataList(comapnyList);
    	mv.addObject("payv2ChannelList", payv2ChannelList); 
        mv.addObject("list", pageObject); 
        mv.addObject("provincesList", provincesList); 
        mv.addObject("map", map); 
        return mv;
    }
    
    /**
     * 省市联动
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/proCity")
    public Map<String,Object> proCity(@RequestParam Map<String, Object> map){
    	Map<String,Object> resultMap = new HashMap<>();
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
     * 查看商户商铺
     * @param map
     * @return
     */
    @RequestMapping("/toViewPayv2BussCompany")
    public ModelAndView toViewPayv2BussCompany(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/company/pay_company_edit");
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		try {
			if (null != map.get("id")) {
				payv2BussCompany = payv2BussCompanyService.detail(map);
				map.remove("id");
	    		//行业
	    		List<Payv2BussTrade> list = payv2BussTradeService.query(map);
	    		if(payv2BussCompany.getCompanyRangeProvince()!=null){
	    			map.put("parentId", payv2BussCompany.getCompanyRangeProvince());
	    			//市
	    			List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
	    			mvc.addObject("cityList", cityList);
	        	}
	        	map.put("parentId", 0);
	        	//省
	        	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
	        	mvc.addObject("tradeList", list);
	        	mvc.addObject("provincesList", provincesList);
				mvc.addObject("obj", payv2BussCompany);
			}
		} catch (Exception e) {
			logger.error(" 查看商户商铺页面报错", e);
		}
		return mvc;
    }
    
    /**
     * 查看商户商铺
     * @param map
     * @return
     */
    @RequestMapping("/toViewFailReason")
    public ModelAndView toViewFailReason(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/company/pay_company_view");
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		try {
			if (null != map.get("id")) {
				payv2BussCompany = payv2BussCompanyService.detail(map);
				mvc.addObject("payv2BussCompany", payv2BussCompany);
			}
		} catch (Exception e) {
			logger.error(" 查看商户商铺页面报错", e);
		}
		return mvc;
    }
    
    /**
     * 审核
     * @param map
     * @return
     */
    @RequestMapping("/toApprove")
    public ModelAndView toApprove(@RequestParam Map<String, Object> map) {
		ModelAndView mvc = new ModelAndView("payv2/company/pay_company_approve");
		mvc.addObject("map", map);
		return mvc;
    }
    
    /**
     * 查看详情
     * @param map
     * @return
     */
    @RequestMapping("/viewDetail")
    public ModelAndView viewDetail(@RequestParam Map<String, Object> map){
    	ModelAndView mv = new ModelAndView("payv2/company/pay_company_view_handle_new");
    	try {
    		//商户详情
    		Payv2BussCompany obj = payv2BussCompanyService.detail(map);
    		map.remove("id");
    		//行业
    		List<Payv2BussTrade> list = payv2BussTradeService.query(map);
    		if(obj.getCompanyRangeProvince()!=null){
    			map.put("parentId", obj.getCompanyRangeProvince());
    			//市
    			List<Payv2ProvincesCity> cityList = payv2ProvincesCityService.query(map);
    			mv.addObject("cityList", cityList);
        	}
        	map.put("parentId", 0);
        	//省
        	List<Payv2ProvincesCity> provincesList = payv2ProvincesCityService.query(map);
        	
        	//开户机构    银行列表
        	List<Payv2Bank> bankList = payv2BankService.query(map);
        	
        	//商户支持的支付通道和路由
        	Payv2BussSupportPayWay pbspw = new Payv2BussSupportPayWay();
        	pbspw.setParentId(obj.getId());
        	pbspw.setIsDelete(2);
        	pbspw.setPayWayStatus(1);
        	List<Payv2BussSupportPayWay> wayList = payv2BussSupportPayWayService.selectByObject(pbspw);
        	
        	//路由列表
        	Map<String,Object> param = new HashMap<>();
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
    
    @RequestMapping("/companyHandleView")
    public ModelAndView companyHandleView(@RequestParam Long companyId) {
    	//response.setHeader("Access-Control-Allow-Origin", "*" ); 
    	ModelAndView mvc = new ModelAndView("payv2/company/pay_company_view_handle");
    	Map<String,Object> map = new HashMap<>();
    	map.put("id",companyId);
    	Payv2BussCompany detail = payv2BussCompanyService.detail(map);
		mvc.addObject("obj", detail);
		return mvc;
    }
    
    @RequestMapping("/companyHandle")
    @ResponseBody
    public Map<String,Object> companyHandle(HttpServletRequest request,@RequestParam Long companyId,@RequestParam Integer type) {
    	/*Long companyId = NumberUtils.createLong(request.getParameter("companyId"));
    	Integer type = NumberUtils.createInteger(request.getParameter("type"));*/
    	Map<String,Object> resultMap = new HashMap<>();
    	try {
    		if(type==1){
        		//通过
    			resultMap.put("id", companyId);
    			resultMap.put("companyStatus", 2);
        		resultMap.put("updateTime", new Date());
        		resultMap.put("companyKey", GenerateUtil.getRandomString(64));
        		payv2BussCompanyService.update(resultMap);
        		resultMap.clear();
        		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null,"审核通过成功");
        	}else if(type==2){
        		//拒绝
        		resultMap.put("id", companyId);
    			resultMap.put("companyStatus", 3);
        		resultMap.put("updateTime", new Date());
        		payv2BussCompanyService.update(resultMap);
        		resultMap.clear();
        		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null,"审核通过成功");
        	}else{
        		resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null,"非法操作");
        	}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null,"服务器异常,请稍候再试");
		}
		return resultMap;
    }
    
    /**
     * 修改商户
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/updatePayv2BussCompany")
    public Map<String,Object> updatePayv2BussCompany(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<>();
    	try {
    		map.put("updateTime", new Date());
    		payv2BussCompanyService.update(map);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
    
    /**
     * 审核商户通过
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/approveAgreePayv2BussCompany")
    public Map<String,Object> approveAgreePayv2BussCompany(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<>();
    	try {
    		map.put("updateTime", new Date());
    		map.put("companyKey", GenerateUtil.getRandomString(64));
    		payv2BussCompanyService.update(map);
    		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
}
