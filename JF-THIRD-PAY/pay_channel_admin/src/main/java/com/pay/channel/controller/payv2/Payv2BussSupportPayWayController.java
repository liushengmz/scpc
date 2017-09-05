package com.pay.channel.controller.payv2;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ObjectUtil;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.mapper.Payv2BussSupportPayWayMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussCompanyShopService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.channel.controller.admin.BaseManagerController;
/**
* @ClassName: Payv2BussSupportPayWayController 
* @Description:商户支付通道配置
* @author zhoulibo
* @date 2016年12月1日 下午5:24:13
*/
@Controller
@RequestMapping("/Payv2BussSupportPayWay/*")
public class Payv2BussSupportPayWayController extends BaseManagerController<Payv2BussSupportPayWay, Payv2BussSupportPayWayMapper>{

	private static final Logger logger = Logger.getLogger(Payv2BussSupportPayWayController.class);
	
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	@Autowired
	private Payv2BussCompanyService  payv2BussCompanyService;//商户表
	@Autowired
	private Payv2PayWayService payv2PayWayServicel;//支付通道表
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;//app支持的支付方式表
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;//商户APP表
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;//商铺
	/**
	* @Title: getSupportPayWayList 
	* @Description:获取某一个商户的支付配置列表
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月1日 下午5:24:39 
	* @throws
	*/
    @RequestMapping("/getSupportPayWayList")
    public ModelAndView getSupportPayWayList(@RequestParam Map<String, Object> map) {
        ModelAndView mv = new ModelAndView("supportPayWay/payv2BussSupportPayWay-list");
        boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "parentId"}, map);
        if(isNotNull){
        	Payv2BussSupportPayWay supportPayWay=new Payv2BussSupportPayWay();
        	supportPayWay.setIsDelete(2);
        	Long parentId=Long.valueOf(map.get("parentId").toString());
        	if(Integer.valueOf(map.get("payWayStatus").toString())>0){
        		supportPayWay.setPayWayStatus(Integer.valueOf(map.get("payWayStatus").toString()));
        	}
        	supportPayWay.setParentId(parentId);
        	List<Payv2BussSupportPayWay> supportPayWayList= payv2BussSupportPayWayService.selectByObject(supportPayWay);
        	for (Payv2BussSupportPayWay payv2BussSupportPayWay : supportPayWayList) {
        		Payv2BussCompany payv2BussCompany=new Payv2BussCompany();
        		payv2BussCompany.setId(parentId);
        		payv2BussCompany.setIsDelete(2);
        		payv2BussCompany=payv2BussCompanyService.selectSingle(payv2BussCompany);
        		//获取商户名字
        		if(payv2BussCompany!=null){
        			payv2BussSupportPayWay.setCompanyName(payv2BussCompany.getCompanyName());
        		}
        		//获取支付通道名字
        		Payv2PayWay payv2PayWay=new Payv2PayWay();
        		payv2PayWay.setId(payv2BussSupportPayWay.getPayWayId());
        		payv2PayWay.setIsDelete(2);
        		payv2PayWay=payv2PayWayServicel.selectSingle(payv2PayWay);
        		if(payv2PayWay!=null){
        			payv2BussSupportPayWay.setWayName(payv2PayWay.getWayName());
        		}
			}
        	   if(supportPayWayList.size()>0){
                   Collections.sort(supportPayWayList, new Comparator<Payv2BussSupportPayWay>(){
                       public int compare(Payv2BussSupportPayWay arg0, Payv2BussSupportPayWay arg1) {
                           Integer a =Integer.valueOf(arg0.getPayWayStatus());
                           Integer b = Integer.valueOf(arg1.getPayWayStatus());
                           return a.compareTo(b);
                           // return b.compareTo(a);
                       }
                   }); 
               }
        	mv.addObject("list", supportPayWayList);
        	mv.addObject("map", map);
        }else{
        	logger.error("获取某一个商户的支付配置列表----》参数有误！");
        }
        return mv;
    }
    /**
    * @Title: updatePayWayStatus 
    * @Description:修改：启动/关闭支付通道||删除支付通道配置（不作物理删除）
    * @param map
    * @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @date 2016年12月1日 下午6:44:18 
    * @throws
    */
    @ResponseBody
    @RequestMapping("/updatePayWayStatus")
    public Map<String,Object> updatePayWayStatus(@RequestParam Map<String,Object> map){
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	try {
    		map.put("updateTime", new Date());
    		//修改：启动/关闭
    		if(map.get("isDelete")==null){
    			int status=Integer.valueOf(map.get("payWayStatus").toString());
    			if(status==1){//启用
    				//判断上级是否是启用状态
    				Payv2BussSupportPayWay payv2BussSupportPayWay=new Payv2BussSupportPayWay();
    				payv2BussSupportPayWay.setId(Long.valueOf(map.get("id").toString()));
    				payv2BussSupportPayWay=payv2BussSupportPayWayService.selectSingle(payv2BussSupportPayWay);
    				if(payv2BussSupportPayWay!=null){
    					Long payWayId=payv2BussSupportPayWay.getPayWayId();
    					Payv2PayWay payv2PayWay=new Payv2PayWay();
    					payv2PayWay.setId(payWayId);
    					payv2PayWay	=payv2PayWayServicel.selectSingle(payv2PayWay);
    					if(payv2PayWay!=null){
    						int isDelete=payv2PayWay.getIsDelete();
    						int payv2PayWayStatus=payv2PayWay.getStatus();
    						if(isDelete==1){
    							resultMap.put("resultCode", 201);//已经删除不能启动
    							return resultMap;
    						}
    						if(payv2PayWayStatus==2){
    							resultMap.put("resultCode", 202);//已经关闭不能启动
    							return resultMap;
    						}
    					}
    				}
    				
    			}
    			//同时修改APP支付方式状态
    			if(map.get("payWayStatus")!=null){
    				if(status==2){
    					//获取支付通道ID
    					Long payWayId=Long.valueOf(map.get("id").toString());
    					Payv2BussAppSupportPayWay payv2BussAppSupportPayWay=new Payv2BussAppSupportPayWay();
    					payv2BussAppSupportPayWay.setPayWayId(payWayId);
    					payv2BussAppSupportPayWay.setStatus(1);
    					payv2BussAppSupportPayWay.setIsDelete(2);
    					List<Payv2BussAppSupportPayWay> appSupportPayWayList=payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
    					for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : appSupportPayWayList) {
    						Map<String, Object> AppMap = new HashMap<String, Object>();
    						AppMap.put("id", payv2BussAppSupportPayWay2.getId());
    						AppMap.put("status", status);
    						AppMap.put("updateTime", new Date());
    						payv2BussAppSupportPayWayService.update(AppMap);
    					}
    					
    				}
    			}
    		}
    		//删除
			if(map.get("isDelete")!=null){
				//获取支付通道ID
				Long payWayId=Long.valueOf(map.get("id").toString());
				Payv2BussAppSupportPayWay payv2BussAppSupportPayWay=new Payv2BussAppSupportPayWay();
				payv2BussAppSupportPayWay.setPayWayId(payWayId);
				payv2BussAppSupportPayWay.setIsDelete(2);
				List<Payv2BussAppSupportPayWay> appSupportPayWayList=payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
				for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : appSupportPayWayList) {
					Map<String, Object> AppMap = new HashMap<String, Object>();
					AppMap.put("id", payv2BussAppSupportPayWay2.getId());
					AppMap.put("isDelete", 1);//删除
					AppMap.put("updateTime", new Date());
					payv2BussAppSupportPayWayService.update(AppMap);
				}
			}
			payv2BussSupportPayWayService.update(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			logger.error("修改：启动/关闭支付通道------>失败", e);
		}
		return resultMap;
    }
    /**
    * @Title: addPayWayStatusTc 
    * @Description:给某个商户添加支付通道配置弹窗
    * @param map
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月1日 下午6:55:08 
    * @throws
    */
    @RequestMapping("/addPayWayStatusTc")
    public ModelAndView addPayWayStatusTc(@RequestParam Map<String,Object> map){
    	 ModelAndView mv = new ModelAndView("supportPayWay/payv2BussSupportPayWay-add");
    	 if(map.get("appId")!=null){
    		 //根据appIdappId获取商户ID
    		 Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
    		 payv2BussCompanyApp.setId(Long.valueOf(map.get("appId").toString()));
    		 payv2BussCompanyApp= payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
    		 if(payv2BussCompanyApp!=null){
    			 map.put("parentId", payv2BussCompanyApp.getCompanyId());
    			 System.out.println("商户ID为：---》"+payv2BussCompanyApp.getCompanyId());
    			 map.remove("appId");
    		 }
    		 
    	 }
    	 Payv2PayWay   payv2PayWay=new Payv2PayWay();
		 payv2PayWay.setStatus(1);
		 payv2PayWay.setIsDelete(2);
		 List<Payv2PayWay> payv2PayWayList=payv2PayWayServicel.selectByObject(payv2PayWay);
		 mv.addObject("list",payv2PayWayList);
    	 mv.addObject("map",map);
		return mv;
    }
    
     /**
     * @Title: addPayWayStatusSubmit 
     * @Description: 给某个商户添加支付通道配置数据提交
     * @param map
     * @return    设定文件 
     * @return Map<String,Object>    返回类型 
     * @date 2016年12月1日 下午6:59:15 
     * @throws
      */
     @ResponseBody
     @RequestMapping("/addPayWayStatusSubmit")
     public Map<String,Object> addPayWayStatusSubmit(@RequestParam Map<String,Object> map){
    	 Map<String, Object> resultMap = new HashMap<String, Object>();
    	 try {
    		 Payv2BussSupportPayWay payv2BussSupportPayWay=new Payv2BussSupportPayWay();
    		 payv2BussSupportPayWay.setParentId(Long.valueOf(map.get("parentId").toString()));
    		 payv2BussSupportPayWay.setPayType(Integer.valueOf(map.get("payType").toString()));
    		 payv2BussSupportPayWay.setPayWayId( Long.valueOf(map.get("payWayId").toString()));
    		 payv2BussSupportPayWay.setIsDelete(2);
    		payv2BussSupportPayWay= payv2BussSupportPayWayService.selectSingle(payv2BussSupportPayWay);
    		 if(payv2BussSupportPayWay==null){
    			 map.put("payWayStatus", 1);
        		 map.put("isDelete", 2); 
        		 map.put("createTime",new Date());
        		 payv2BussSupportPayWayService.add(map);
     			 resultMap.put("resultCode", 200);
    		 }else{
    			 resultMap.put("resultCode", 201);//已經存在，不能添加
    		 }
		} catch (Exception e) {
			logger.error("给某个商户添加支付通道配置数据提交------>失败", e);
		}
		return resultMap;
     }
     /**
     * @Title: getPayv2PayWayList 
     * @Description:根据支付类型不同获取支付方式列表
     * @param map
     * @return    设定文件 
     * @return Map<String,Object>    返回类型 
     * @date 2016年12月1日 下午7:05:34 
     * @throws
     */
     @ResponseBody
     @RequestMapping("/getPayv2PayWayList")
     public Map<String,Object> getPayv2PayWayList(@RequestParam Map<String,Object> map){
    	 Map<String, Object> resultMap = new HashMap<String, Object>();
    	 boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "wayType"}, map);
    	 if(isNotNull){
    		 Payv2PayWay   payv2PayWay=new Payv2PayWay();
    		 payv2PayWay.setStatus(1);
    		 payv2PayWay.setIsDelete(2);
    		 payv2PayWay.setWayType(Integer.valueOf(map.get("wayType").toString()));
    		 List<Payv2PayWay> payv2PayWayList=payv2PayWayServicel.selectByObject(payv2PayWay);
    		 resultMap.put("payv2PayWayList", payv2PayWayList);
    	 }else{
    		 logger.error("根据支付类型不同获取支付方式列表---->参数有误");
    	 }
		return resultMap;
     }
     /**
     * @Title: getAppSupportPayWay 
     * @Description:获取某个APP支付方式列表
     * @param map
     * @return    设定文件 
     * @return ModelAndView    返回类型 
     * @date 2016年12月2日 上午9:56:07 
     * @throws
     */
     @RequestMapping("/getAppSupportPayWay")
     public ModelAndView getAppSupportPayWay(@RequestParam Map<String,Object> map){
    	 ModelAndView mv = new ModelAndView("supportPayWay/Payv2BussAppSupportPayWay-list");
    	 boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "appId"}, map);
    	 if(isNotNull){
    		 //根据appId获取支付方式列表
    		 Payv2BussAppSupportPayWay payv2BussAppSupportPayWay=new Payv2BussAppSupportPayWay();
    		 Long appId=Long.valueOf(map.get("appId").toString());
    		 payv2BussAppSupportPayWay.setAppId(appId);
    		 payv2BussAppSupportPayWay.setIsDelete(2);
    		 if(Integer.valueOf(map.get("status").toString())>0){
    			 payv2BussAppSupportPayWay.setStatus(Integer.valueOf(map.get("status").toString()));
    		 }
    		 int merchantType=Integer.valueOf(map.get("merchantType").toString());
    		 if(merchantType==1){//APP
    			 Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
    			 payv2BussCompanyApp.setId(appId);
    			 payv2BussCompanyApp.setIsDelete(2);
    			 payv2BussCompanyApp=payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
    			 if(payv2BussCompanyApp!=null){
    				 map.put("appOrShopStatus", payv2BussCompanyApp.getAppStatus());//只有通过才能添加支付方式
    			 }
    		 }
    		 if(merchantType==2){//商铺
    			 Payv2BussCompanyShop payv2BussCompanyShop=new Payv2BussCompanyShop();
    			 payv2BussCompanyShop.setId(appId);
    			 payv2BussCompanyShop.setIsDelete(2);
    			 payv2BussCompanyShop=payv2BussCompanyShopService.selectSingle(payv2BussCompanyShop);
    			 if(payv2BussCompanyShop!=null){
    				 map.put("appOrShopStatus", payv2BussCompanyShop.getShopStatus());//只有通过才能添加支付方式
    			 }
    		 }
    		 payv2BussAppSupportPayWay.setMerchantType(merchantType);
    		 List<Payv2BussAppSupportPayWay> appSupportPayWayList= payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
    		 for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : appSupportPayWayList) {
    			 //获取APP名字
    			 if(merchantType==1){
    				 Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
        			 payv2BussCompanyApp.setId(appId);
        			 payv2BussCompanyApp.setIsDelete(2);
        			 payv2BussCompanyApp= payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
        			 if(payv2BussCompanyApp!=null){
        				 payv2BussAppSupportPayWay2.setAppName(payv2BussCompanyApp.getAppName());
        			 }
    			 }else if(merchantType==2){
    				//商铺名字 
    				 Payv2BussCompanyShop payv2BussCompanyShop=new Payv2BussCompanyShop();
    				 payv2BussCompanyShop.setId(appId);
    				 payv2BussCompanyShop.setIsDelete(2);
    				 payv2BussCompanyShop=payv2BussCompanyShopService.selectSingle(payv2BussCompanyShop);
    				 if(payv2BussCompanyShop!=null){
        				 payv2BussAppSupportPayWay2.setAppName(payv2BussCompanyShop.getShopName());
        			 }
    			 }
    			//获取支付通道名字与通道费
    			//支付通道ID
    			Long payWayId= payv2BussAppSupportPayWay2.getPayWayId();
    			//获取支付通道详情
    			Payv2BussSupportPayWay payv2BussSupportPayWay=new Payv2BussSupportPayWay();
    			payv2BussSupportPayWay.setId(payWayId);
    			payv2BussSupportPayWay.setIsDelete(2);
    			payv2BussSupportPayWay.setPayWayStatus(1);
    			payv2BussSupportPayWay=payv2BussSupportPayWayService.selectSingle(payv2BussSupportPayWay);
    			if(payv2BussSupportPayWay!=null){
        			//获取支付通道名字
    				Payv2PayWay payv2PayWay=new Payv2PayWay();
    				payv2PayWay.setIsDelete(2);
      				payv2PayWay.setStatus(1);
       				payv2PayWay.setId(payv2BussSupportPayWay.getPayWayId());
    				payv2PayWay=	payv2PayWayServicel.selectSingle(payv2PayWay);
    				if(payv2PayWay!=null){
    					payv2BussAppSupportPayWay2.setWayName(payv2PayWay.getWayName());
    				}
    			}
			}
    		 if(appSupportPayWayList.size()>0){
                 Collections.sort(appSupportPayWayList, new Comparator<Payv2BussAppSupportPayWay>(){
                     public int compare(Payv2BussAppSupportPayWay arg0, Payv2BussAppSupportPayWay arg1) {
                         Integer a =Integer.valueOf(arg0.getStatus());
                         Integer b = Integer.valueOf(arg1.getStatus());
                         return a.compareTo(b);
                         // return b.compareTo(a);
                     }
                 }); 
             }
    		 if(map.get("companyId")!=null){
    			 System.out.println("商户ID:--------->"+map.get("companyId").toString());
    			 map.put("parentId", map.get("companyId"));
    		 }
    		 mv.addObject("list", appSupportPayWayList);
    		 mv.addObject("map", map);
    	 }else{
    		 logger.error("获取某个APP支付方式列表----->参数有误");
    	 }
		return mv;
     }
     /**
     * @Title: updateAppSupportPayWay 
     * @Description:修改APP支付方式：终止或者恢复支付方式|| 删除支付方式
     * @param map
     * @return    设定文件 
     * @return Map<String,Object>    返回类型 
     * @date 2016年12月2日 上午10:36:44 
     * @throws
     */
     @ResponseBody
     @RequestMapping("/updateAppSupportPayWay")
     public Map<String,Object> updateAppSupportPayWay(@RequestParam Map<String,Object>  map){
    	 Map<String, Object> resultMap = new HashMap<String, Object>();
    	 try {
    		 int status=Integer.valueOf(map.get("status").toString());
    		 if(status==1){
    			 Payv2BussAppSupportPayWay payv2BussAppSupportPayWay=new Payv2BussAppSupportPayWay();
    			 payv2BussAppSupportPayWay.setId(Long.valueOf(map.get("id").toString()));
    			 payv2BussAppSupportPayWay= payv2BussAppSupportPayWayService.selectSingle(payv2BussAppSupportPayWay);
    			 if(payv2BussAppSupportPayWay!=null){
    				Long payWayId =payv2BussAppSupportPayWay.getPayWayId();
    				Payv2BussSupportPayWay payv2BussSupportPayWay=new Payv2BussSupportPayWay();
    				payv2BussSupportPayWay.setId(payWayId);
    				payv2BussSupportPayWay=payv2BussSupportPayWayService.selectSingle(payv2BussSupportPayWay);
    				//支付方式是否关闭
    				if(payv2BussSupportPayWay!=null){
    					int isDelete =payv2BussSupportPayWay.getIsDelete(); //1删除  2不删除
    					int payWayStatus=payv2BussSupportPayWay.getPayWayStatus();//1 启用 2不启用
    					if(isDelete==1){
    						resultMap.put("resultCode", 201);//已经删除不能启动
    						return resultMap;
    					}
    					if(payWayStatus==2){
    						resultMap.put("resultCode", 202);//已经关闭不能启动
    						return resultMap;				
					    }
    					//查询支付通道是否关闭
    					if(isDelete==2||payWayStatus==1){
    						Long pId=payv2BussSupportPayWay.getPayWayId(); 
    						Payv2PayWay payv2PayWay=new Payv2PayWay();
    						payv2PayWay.setId(pId);
    						payv2PayWay=payv2PayWayServicel.selectSingle(payv2PayWay);
    						if(payv2PayWay!=null){
    							int isDelete2=payv2PayWay.getIsDelete();
    							int status2=payv2PayWay.getStatus();
    							if(isDelete2==1){
    	    						resultMap.put("resultCode", 201);//已经删除不能启动
    	    						return resultMap;
    	    					}
    	    					if(status2==2){
    	    						resultMap.put("resultCode", 202);//已经关闭不能启动
    	    						return resultMap;				
    						    }
    						}
    					}
    				}
    				//查询APP或者店铺是否开启，如果没有开启，还是不能恢复启动
    				int merchantType=payv2BussAppSupportPayWay.getMerchantType();
	    			if(merchantType==1){//APP
	    				Payv2BussCompanyApp payv2BussCompanyApp=new Payv2BussCompanyApp();
	    				payv2BussCompanyApp.setId(payv2BussAppSupportPayWay.getAppId());
	    				payv2BussCompanyApp=payv2BussCompanyAppService.selectSingle(payv2BussCompanyApp);
	    				if(payv2BussCompanyApp!=null){
	    					int isDelete2=payv2BussCompanyApp.getIsDelete();
							int status2=payv2BussCompanyApp.getAppStatus();
							if(isDelete2==1){
	    						resultMap.put("resultCode", 203);//已经删除不能启动
	    						return resultMap;
	    					}
	    					if(status2==4){//终止合作
	    						resultMap.put("resultCode", 204);//已经关闭不能启动
	    						return resultMap;				
						    }
	    				}
	    				
	    			}
	    			if(merchantType==2){//店铺
	    				Payv2BussCompanyShop payv2BussCompanyShop=new Payv2BussCompanyShop();
	    				payv2BussCompanyShop.setId(payv2BussAppSupportPayWay.getAppId());
	    				payv2BussCompanyShop=payv2BussCompanyShopService.selectSingle(payv2BussCompanyShop);
	    				if(payv2BussCompanyShop!=null){
	    					int isDelete2=payv2BussCompanyShop.getIsDelete();
							int status2=payv2BussCompanyShop.getShopStatus();
							if(isDelete2==1){
	    						resultMap.put("resultCode", 205);//已经删除不能启动
	    						return resultMap;
	    					}
	    					if(status2==4){//终止合作
	    						resultMap.put("resultCode", 206);//已经关闭不能启动
	    						return resultMap;				
						    }
	    				}
	    			}
    			 }
    		 }
    		 map.put("updateTime", new Date());
    		 payv2BussAppSupportPayWayService.update(map);
    		 resultMap.put("resultCode", 200);
		} catch (Exception e) {
			logger.error("修改APP支付方式：终止或者恢复支付方式|| 删除支付方式------>失败   发生时间："+new Date(), e);
		}
		return resultMap;
     }
     /**
     * @Title: addAppSupportPayWayTc 
     * @Description:给某个商户APP添加支付方式页面弹窗
     * @param map
     * @return    设定文件 
     * @return ModelAndView    返回类型 
     * @date 2016年12月2日 上午10:44:11 
     * @throws
     */
     @RequestMapping("/addAppSupportPayWayTc")
     public ModelAndView addAppSupportPayWayTc(@RequestParam Map<String,Object> map){
    	 ModelAndView mv = new ModelAndView("supportPayWay/Payv2BussAppSupportPayWay-add");
    	 //获取商户支付通道列表
    	 boolean isNotNull = ObjectUtil.checkObjectFile(new String[] { "parentId"}, map);//商户ID
    	 if(isNotNull){
    		Payv2BussSupportPayWay supportPayWay=new Payv2BussSupportPayWay();
         	supportPayWay.setIsDelete(2);
         	Long parentId=Long.valueOf(map.get("parentId").toString());
         	supportPayWay.setPayWayStatus(1);
         	supportPayWay.setParentId(parentId);
         	List<Payv2BussSupportPayWay> supportPayWayList= payv2BussSupportPayWayService.selectByObject(supportPayWay);
         	for (Payv2BussSupportPayWay payv2BussSupportPayWay : supportPayWayList) {
         		//获取支付通道名字
        		Payv2PayWay payv2PayWay=new Payv2PayWay();
        		payv2PayWay.setId(payv2BussSupportPayWay.getPayWayId());
        		payv2PayWay.setIsDelete(2);
        		payv2PayWay=payv2PayWayServicel.selectSingle(payv2PayWay);
        		if(payv2PayWay!=null){
        			payv2BussSupportPayWay.setWayName(payv2PayWay.getWayName());
        		}
			}
         	mv.addObject("list", supportPayWayList);
         	mv.addObject("map", map);
    	 }else{
    		 logger.error("给某个商户APP添加支付方式页面弹窗--->参数有误  发生时间："+new Date());
    	 }
		return mv;
     }
     /**
     * @Title: addAppSupportPayWaySubmit 
     * @Description:给某个商户APP添加支付方式数据提交
     * @param map
     * @return    设定文件 
     * @return Map<String,Object>    返回类型 
     * @date 2016年12月2日 上午10:57:34 
     * @throws
     */
     @ResponseBody
     @RequestMapping("/addAppSupportPayWaySubmit")
     public Map<String,Object> addAppSupportPayWaySubmit(@RequestParam Map<String,Object> map){
    	 Map<String, Object> resultMap = new HashMap<String, Object>();
    	 try {
    		 map.put("createTime", new Date());
    		 map.put("status",1);
    		 map.put("isDelete",2);
			payv2BussAppSupportPayWayService.add(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			logger.error("给某个商户APP添加支付方式数据提交---->失败  发生时间："+new Date(),e);
		}
		return resultMap;
     }
}
