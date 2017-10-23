package com.pay.channel.controller.payv2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.entity.LineShopVo;
import com.pay.business.payv2.entity.Pavy2PlatformApp;
import com.pay.business.payv2.entity.Payv2AgentPlatform;
import com.pay.business.payv2.entity.Payv2PlatformWay;
import com.pay.business.payv2.mapper.Payv2AgentPlatformMapper;
import com.pay.business.payv2.service.Pavy2PlatformAppService;
import com.pay.business.payv2.service.Payv2AgentPlatformService;
import com.pay.business.payv2.service.Payv2PlatformWayService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.channel.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2AgentPlatformController 
* @Description:代理平台管理
* @author zhoulibo
* @date 2016年12月27日 下午2:40:32
*/
@Controller
@RequestMapping("/Payv2AgentPlatform/*")
public class Payv2AgentPlatformController extends BaseManagerController<Payv2AgentPlatform, Payv2AgentPlatformMapper> {
	private static final Logger logger = Logger.getLogger(Payv2AgentPlatformController.class);
	@Autowired
	private Payv2AgentPlatformService payv2AgentPlatformService;
	@Autowired
	private Payv2PlatformWayService payv2PlatformWayService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Pavy2PlatformAppService pavy2PlatformAppService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	/**
	* @Title: getPavy2PlatformAppList 
	* @Description:获取代理平台列表
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月27日 下午2:42:27 
	* @throws
	*/
	@RequestMapping("getPayv2AgentPlatformList")
	public ModelAndView getPayv2AgentPlatformList(@RequestParam Map<String,Object> map){
		ModelAndView amv=new ModelAndView("platform/Payv2AgentPlatform_list");
		//獲取渠道商ID
		Long channelId=getAdmin().getId();
		//查看渠道商是否開啟代理平台
		map.put("channelId", channelId);
		map.put("isDelete", 2);
		PageObject<Payv2AgentPlatform> pageList= payv2AgentPlatformService.Pagequery(map);
		List<Payv2AgentPlatform> list=pageList.getDataList();
		for (Payv2AgentPlatform payv2AgentPlatform : list) {
			//獲取支持的支付通道個數
			Payv2PlatformWay payv2PlatformWay=new Payv2PlatformWay();
			payv2PlatformWay.setPlatformId(payv2AgentPlatform.getId());
			payv2PlatformWay.setStatus(1);
			int number=	payv2PlatformWayService.getPayv2PlatformWayNumber(payv2PlatformWay);
			payv2AgentPlatform.setNumber(number);
		}
		amv.addObject("list", pageList);
		return amv;
	}
	/**
	* @Title: getPayv2AgentPlatformInfo 
	* @Description:获取详情
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月27日 下午3:30:55 
	* @throws
	*/
	@RequestMapping("getPayv2AgentPlatformInfo")
	public ModelAndView getPayv2AgentPlatformInfo(@RequestParam Map<String,Object> map){
		ModelAndView amv=new ModelAndView("platform/Payv2AgentPlatform_info");
		Payv2AgentPlatform payv2AgentPlatform=new Payv2AgentPlatform();
		payv2AgentPlatform.setId(Long.valueOf(map.get("id").toString()));
		payv2AgentPlatform.setIsDelete(2);
		payv2AgentPlatform=payv2AgentPlatformService.selectSingle(payv2AgentPlatform);
		if(payv2AgentPlatform!=null){
			//获取支持支付通道···
			Payv2PlatformWay payv2PlatformWay=new Payv2PlatformWay();
			payv2PlatformWay.setPlatformId(payv2AgentPlatform.getId());
			payv2PlatformWay.setStatus(1);
			List<Payv2PlatformWay> list=payv2PlatformWayService.selectByObject(payv2PlatformWay);
			for (Payv2PlatformWay payv2PlatformWay2 : list) {
				//获取支付通道名字
				Long id=payv2PlatformWay2.getPayWayId();
				Payv2PayWay payv2PayWay=new Payv2PayWay();
				payv2PayWay.setId(id);
				payv2PayWay.setIsDelete(2);
				payv2PayWay.setStatus(1);
				payv2PayWay=payv2PayWayService.selectSingle(payv2PayWay);
				if(payv2PayWay!=null){
					payv2PlatformWay2.setWayName(payv2PayWay.getWayName());
				}
			}
			amv.addObject("wayList", list);
		}
		amv.addObject("payv2AgentPlatform", payv2AgentPlatform);
		return amv;
	}
	/**
	* @Title: getPayv2AgentPlatformAppList 
	* @Description:获取代理APP列表
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月27日 下午4:11:19 
	* @throws
	*/
	@RequestMapping("getPayv2AgentPlatformAppList")
	public ModelAndView getPayv2AgentPlatformAppList(@RequestParam Map<String,Object> map){
		ModelAndView amv=new ModelAndView("platform/Payv2AgentPlatformApp_list");
		Pavy2PlatformApp pavy2PlatformApp=new Pavy2PlatformApp();
		pavy2PlatformApp.setPlatformId(Long.valueOf(map.get("id").toString()));
		List<Pavy2PlatformApp> list=pavy2PlatformAppService.selectByObject(pavy2PlatformApp);
		for (Pavy2PlatformApp pavy2PlatformApp2 : list) {
			Long appId=	pavy2PlatformApp2.getAppId();
			Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
			payv2BussCompanyApp.setId(appId);
			payv2BussCompanyApp =payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
			if(payv2BussCompanyApp!=null){
				pavy2PlatformApp2.setAppName(payv2BussCompanyApp.getAppName());	
				//查看支持平台
				pavy2PlatformApp2.setIsIos(payv2BussCompanyApp.getIsIos());
				pavy2PlatformApp2.setIsAndroid(payv2BussCompanyApp.getIsAndroid());
				pavy2PlatformApp2.setIsWeb(payv2BussCompanyApp.getIsWeb());
				//app状态
				pavy2PlatformApp2.setStatus(payv2BussCompanyApp.getAppStatus());	
			}
		}
		amv.addObject("platformAppList", list);
		return amv;
	}
	/**
	* @Title: getPayv2AgentPlatformShopList 
	* @Description:获取代理平台线下店铺
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月27日 下午5:57:20 
	* @throws
	*/
	@RequestMapping("getPayv2AgentPlatformShopList")
	public ModelAndView getPayv2AgentPlatformShopList(@RequestParam Map<String,Object> map){
		ModelAndView amv=new ModelAndView("platform/Payv2AgentPlatformShop_list");
		List<LineShopVo> shopVoList=new ArrayList<LineShopVo>();
		//首先获取平台支持的支付通道
		Payv2PlatformWay payv2PlatformWay =new Payv2PlatformWay();
		payv2PlatformWay.setPlatformId(Long.valueOf(map.get("id").toString()));
		payv2PlatformWay.setStatus(1);
		List<Payv2PlatformWay> list=payv2PlatformWayService.selectByObject(payv2PlatformWay);
		for (Payv2PlatformWay payv2PlatformWay2 : list) {
			Payv2PayWay payv2PayWay=new Payv2PayWay();
			payv2PayWay.setId(payv2PlatformWay2.getPayWayId());
			payv2PayWay=payv2PayWayService.selectSingle(payv2PayWay);
			if(payv2PayWay!=null){
				payv2PlatformWay2.setWayName(payv2PayWay.getWayName());
			}
		}
		//获取渠道商所有在线商户
		Payv2BussCompany payv2BussCompany=new Payv2BussCompany();
		payv2BussCompany.setChannelId(getAdmin().getId());
		payv2BussCompany.setIsDelete(2);
		payv2BussCompany.setCompanyStatus(2);
		List<Payv2BussCompany> companyList=payv2BussCompanyService.selectByObject(payv2BussCompany);
		//获取线下店铺
		if(map.get("payWayId")!=null&&map.get("payWayId")!=""){
			List<Payv2BussCompanyShop> shopList=payv2BussCompanyShopService.getLineShopList(map);
			for (Payv2BussCompanyShop payv2BussCompanyShop : shopList) {
				LineShopVo vo=new LineShopVo();
				vo.setCompanyId(payv2BussCompanyShop.getCompanyId());
				vo.setCompanyName(payv2BussCompanyShop.getCompanyName());
				vo.setCreateTime(payv2BussCompanyShop.getCreateTime());
				vo.setId(payv2BussCompanyShop.getId());
				vo.setPayWayId(payv2BussCompanyShop.getPayWayId());
				vo.setShopAddress(payv2BussCompanyShop.getShopAddress());
				vo.setShopName(payv2BussCompanyShop.getShopName());	
				vo.setShopStatus(payv2BussCompanyShop.getShopStatus());
				vo.setWayName(payv2BussCompanyShop.getWayName());
				shopVoList.add(vo);
			}
		}else{
			for (Payv2PlatformWay payv2PlatformWay2 : list) {
				map.put("payWayId", payv2PlatformWay2.getPayWayId());
				List<Payv2BussCompanyShop> shopList=payv2BussCompanyShopService.getLineShopList(map);
				for (Payv2BussCompanyShop payv2BussCompanyShop : shopList) {
					LineShopVo vo=new LineShopVo();
					vo.setCompanyId(payv2BussCompanyShop.getCompanyId());
					vo.setCompanyName(payv2BussCompanyShop.getCompanyName());
					vo.setCreateTime(payv2BussCompanyShop.getCreateTime());
					vo.setId(payv2BussCompanyShop.getId());
					vo.setPayWayId(payv2BussCompanyShop.getPayWayId());
					vo.setShopAddress(payv2BussCompanyShop.getShopAddress());
					vo.setShopName(payv2BussCompanyShop.getShopName());	
					vo.setShopStatus(payv2BussCompanyShop.getShopStatus());
					vo.setWayName(payv2BussCompanyShop.getWayName());
					shopVoList.add(vo);
				}
				//删除
				map.remove("payWayId");
			}
		}
		amv.addObject("shopVoList", shopVoList);
		amv.addObject("map",map);
		amv.addObject("platformWayList",list);
		amv.addObject("companyList",companyList);
		return amv;
	}
}
