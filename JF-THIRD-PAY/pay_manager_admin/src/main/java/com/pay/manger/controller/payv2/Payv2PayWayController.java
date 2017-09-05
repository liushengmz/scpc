
package com.pay.manger.controller.payv2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;
import com.pay.manger.controller.admin.BaseManagerController;

/**
* @ClassName: Payv2PayWayController 
* @Description:支付渠道管理
* @author zhoulibo
* @date 2016年12月6日 上午10:56:53
*/
@RequestMapping("/payv2payway/*")
@Controller
public class Payv2PayWayController extends BaseManagerController<Payv2PayWay, Payv2PayWayMapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Payv2PayWayController.class);
    @Autowired
    private Payv2PayWayService payv2PayWayService;
    @Autowired
    private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
    @Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
    @Autowired
    private SysConfigDictionaryService sysConfigDictionaryService;
    /**
    * @Title: getPayv2PayWayList 
    * @Description:获取支付渠道列表
    * @param map
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月6日 上午10:57:52 
    * @throws
    */
    @RequestMapping("getPayv2PayWayList")
    public ModelAndView getPayv2PayWayList(@RequestParam Map<String, Object> map) {
        ModelAndView andView = new ModelAndView("payv2/payway/payv2payway-list");
        map.put("isDelete", 2);
        map.put("wayType", 2);
        PageObject<Payv2PayWay> pageList = payv2PayWayService.Pagequery(map);
        andView.addObject("list", pageList);
        andView.addObject("map", map);
        return andView;
    }
    /**
    * @Title: addPayv2PayWayTc 
    * @Description:添加支付渠道弹窗
    * @param map
    * @return    设定文件 
    * @return ModelAndView    返回类型 
    * @date 2016年12月6日 上午11:21:22 
    * @throws
    */
    @RequestMapping("addPayv2PayWayTc")
    public ModelAndView addPayv2PayWayTc(@RequestParam Map<String, Object> map){
    	ModelAndView andView = new ModelAndView("payv2/payway/payv2payway-add");
        return andView;
    }
    /**
    * @Title: addPayv2PayWay 
    * @Description:添加支付渠道提交
    * @param map
    * @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @date 2016年12月7日 下午7:57:11 
    * @throws
    */
    @ResponseBody
    @RequestMapping("addPayv2PayWay")
    public Map<String,Object> addPayv2PayWay(@RequestParam Map<String, Object> map){
    	Map<String,Object> returnMap=new HashMap<String, Object>();
    	try {
    		map.put("createTime", new Date());
    		map.put("wayType", 2);
    		map.put("status", 2);//默认不启动
			payv2PayWayService.add(map);
			returnMap.put("resultCode", 200);
		} catch (Exception e) {
			LOGGER.error("添加支付渠道提交失败",e);
		}
		return returnMap;
    }
    /**
    * @Title: updatePayv2PayWay 
    * @Description: 修改支付渠道 启动/停用/删除
    * @param map
    * @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @date 2016年12月8日 上午9:45:03 
    * @throws
    */
    @ResponseBody
    @RequestMapping("updatePayv2PayWay")
    public Map<String,Object> updatePayv2PayWay(@RequestParam Map<String, Object> map){
    	Map<String,Object> returnMap=new HashMap<String, Object>();
    	try {
    		map.put("updateTime", new Date());
			payv2PayWayService.update(map);
			//如果停用/删除:支付渠道：那他旗下的支付方式都停用或者删除;
			map.put("payType", 2);
			//修改旗下的关联
			if(map.get("isDelete")!=null||map.get("status")!=null){
				payv2PayWayService.updatePayWay(map);
			}
			returnMap.put("resultCode", 200);
		} catch (Exception e) {
			LOGGER.error("修改支付渠道 启动/停用失败",e);
		}
   		return returnMap;
    }
    
    /**
     * @Title: addPayv2PayWayTc 
     * @Description:修改支付渠道弹窗
     * @param map
     * @return    设定文件 
     * @return ModelAndView    返回类型 
     * @date 2016年12月6日 上午11:21:22 
     * @throws
     */
     @RequestMapping("editPayv2PayWayTc")
     public ModelAndView editPayv2PayWayTc(@RequestParam Map<String, Object> map){
     	ModelAndView andView = new ModelAndView("payv2/payway/payv2payway-edit");
     	Payv2PayWay payv2PayWay=new Payv2PayWay();
     	payv2PayWay.setId(Long.valueOf(map.get("id").toString()));
     	payv2PayWay=payv2PayWayService.selectSingle(payv2PayWay);
     	andView.addObject("payv2PayWay", payv2PayWay);
        return andView;
     }
}
