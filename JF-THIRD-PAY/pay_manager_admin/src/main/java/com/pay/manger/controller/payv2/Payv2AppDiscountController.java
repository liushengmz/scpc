package com.pay.manger.controller.payv2;

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

import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2AppDiscount;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.mapper.Payv2AppDiscountMapper;
import com.pay.business.payv2.service.Payv2AppDiscountService;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.manger.controller.admin.BaseManagerController;
/**
* @ClassName: Payv2AppDiscountController 
* @Description:优惠管理
* @author zhoulibo
* @date 2016年12月6日 下午2:36:52
*/
@Controller
@RequestMapping("/Payv2AppDiscount/*")
public class Payv2AppDiscountController extends BaseManagerController<Payv2AppDiscount, Payv2AppDiscountMapper>{

	private static final Logger logger = Logger.getLogger(Payv2AppDiscountController.class);
	@Autowired
	private Payv2AppDiscountService payv2AppDiscountService;
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	/**
	* @Title: getPayv2AppDiscountList 
	* @Description: 获取某一个APP优惠列表
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月6日 下午2:39:35 
	* @throws
	 */
	@RequestMapping("getPayv2AppDiscountList")
	public ModelAndView getPayv2AppDiscountList(@RequestParam Map<String,Object> map){
		ModelAndView av=new ModelAndView("payv2/discount/payv2AppDiscount-list");
		map.put("isDelete", 2);
		PageObject<Payv2AppDiscount> pageList=	payv2AppDiscountService.getPageObject2(map);
		//:状态：1上线2未上线3已下架4已结束 5：等待上线（未到推广日期：开始时间>当前时间；并且状态不等于3,4）
		List<Payv2AppDiscount> list=pageList.getDataList();
		for (Payv2AppDiscount payv2AppDiscount : list) {
			int isShow=payv2AppDiscount.getIsShow();
			if(isShow!=4&&isShow!=3){
				if(payv2AppDiscount.getStartupStartTime()!=null){
					if(payv2AppDiscount.getStartupStartTime().getTime()>new Date().getTime()){
						payv2AppDiscount.setIsShow(5);
					}
				}
			}
		}
		av.addObject("map", map);
		av.addObject("list", pageList);
		
		return av;
	}
	/**
	* @Title: addPayv2AppDiscountTc 
	* @Description:添加某一个app优惠方案页面弹窗
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月6日 下午4:11:08 
	* @throws
	*/
	@RequestMapping("addPayv2AppDiscountTc")
	public ModelAndView addPayv2AppDiscountTc(@RequestParam Map<String,Object> map){
		ModelAndView av=new ModelAndView("payv2/discount/payv2AppDiscount-add");
		Payv2ProvincesCity payv2ProvincesCity=new Payv2ProvincesCity();
		payv2ProvincesCity.setParentId(Long.valueOf(0));
		List<Payv2ProvincesCity> cityList=payv2ProvincesCityService.selectByObject(payv2ProvincesCity);
		av.addObject("map", map);
		av.addObject("cityList", cityList);
		return av;
	}
	/**
	* @Title: addPayv2AppDiscount 
	* @Description:某个APP优惠方案数据提交
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年12月6日 下午5:12:41 
	* @throws
	*/
	@ResponseBody
	@RequestMapping("addPayv2AppDiscountSubmit")
	public Map<String,Object> addPayv2AppDiscountSubmit(@RequestParam Map<String,Object> map){
		Map<String,Object> resultMap=new HashMap<String, Object>();
		try {
			map.put("createTime", new Date());
			map.put("discountStartTime", map.get("time").toString());
			map.put("discountEndTime", map.get("time2").toString());
			map.remove("time");
			map.remove("time2");
			payv2AppDiscountService.add(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			logger.error("某个APP优惠方案数据提交失败", e);
		}
		return resultMap;
	}
	/**
	* @Title: updatePayv2AppDiscount 
	* @Description:某个APP优惠方案下架或者上线/修改优惠方案数据提交
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年12月7日 上午10:02:09 
	* @throws
	*/
	@ResponseBody
	@RequestMapping("updatePayv2AppDiscount")
	public Map<String,Object> updatePayv2AppDiscount(@RequestParam Map<String,Object> map){
		Map<String,Object> resultMap=new HashMap<String, Object>();
		try {
			map.put("updateTime", new Date());
			if(map.get("type")!=null){//修改
				map.put("discountStartTime", map.get("time").toString());
				map.put("discountEndTime", map.get("time2").toString());
				map.remove("time");
				map.remove("time2");
			}
			payv2AppDiscountService.update(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			logger.error("某个APP优惠方案下架或者上线/修改优惠方案数据提交失败", e);
		}
		return resultMap;
	}
	/**
	* @Title: delPayv2AppDiscount 
	* @Description:删除某个APP优惠方案
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年12月7日 上午10:07:00 
	* @throws
	*/
	@ResponseBody
	@RequestMapping("delPayv2AppDiscount")
	public Map<String,Object> delPayv2AppDiscount(@RequestParam Map<String,Object> map){
		Map<String,Object> resultMap=new HashMap<String, Object>();
		try {
			map.put("updateTime", new Date());
			map.put("isDelete", 1);
			payv2AppDiscountService.update(map);
			resultMap.put("resultCode", 200);
		} catch (Exception e) {
			logger.error("删除某个APP优惠方案失败", e);
		}
		return resultMap;
	}
	/**
	* @Title: getPayv2AppDiscountInfo 
	* @Description:查看优惠方案详情
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月7日 上午10:11:14 
	* @throws
	*/
	@RequestMapping("getPayv2AppDiscountInfo")
	public ModelAndView getPayv2AppDiscountInfo(@RequestParam Map<String,Object> map){
		ModelAndView av=new ModelAndView("payv2/discount/payv2AppDiscount-info");
		try {
			Payv2AppDiscount payv2AppDiscount=new Payv2AppDiscount();
			payv2AppDiscount.setId(Long.valueOf(map.get("id").toString()));
			payv2AppDiscount=payv2AppDiscountService.selectSingle(payv2AppDiscount);
			System.out.println(payv2AppDiscount.getDiscountEndTime());
			Payv2ProvincesCity payv2ProvincesCity=new Payv2ProvincesCity();
			payv2ProvincesCity.setParentId(Long.valueOf(0));
			List<Payv2ProvincesCity> cityList=payv2ProvincesCityService.selectByObject(payv2ProvincesCity);
			av.addObject("cityList", cityList);
			av.addObject("payv2AppDiscount", payv2AppDiscount);
			av.addObject("map", map);
		} catch (Exception e) {
			logger.error("查看优惠方案详情失败", e);
		}
		return av;
	}
	/**
	* @Title: getPayv2AppDiscountInfo 
	* @Description:编辑优惠方案弹窗
	* @param map
	* @return    设定文件 
	* @return ModelAndView    返回类型 
	* @date 2016年12月7日 上午10:11:14 
	* @throws
	*/
	@RequestMapping("upatePayv2AppDiscountInfoTc")
	public ModelAndView upatePayv2AppDiscountInfoTc(@RequestParam Map<String,Object> map){
		ModelAndView av=new ModelAndView("payv2/discount/payv2AppDiscount-edit");
		try {
			Payv2AppDiscount payv2AppDiscount=new Payv2AppDiscount();
			payv2AppDiscount.setId(Long.valueOf(map.get("id").toString()));
			payv2AppDiscount=payv2AppDiscountService.selectSingle(payv2AppDiscount);
			Payv2ProvincesCity payv2ProvincesCity=new Payv2ProvincesCity();
			payv2ProvincesCity.setParentId(Long.valueOf(0));
			List<Payv2ProvincesCity> cityList=payv2ProvincesCityService.selectByObject(payv2ProvincesCity);
			av.addObject("cityList", cityList);
			av.addObject("payv2AppDiscount", payv2AppDiscount);
			av.addObject("map", map);
		} catch (Exception e) {
			logger.error("编辑优惠方案弹窗失败", e);
		}
		return av;
	}
}
