package com.pay.business.payway.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.core.teamwork.base.cache.annotations.Cacheable;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.mapper.Payv2BussSupportPayWayMapper;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.PayFinalUtil;

/**
 * @author cyl
 * @version 
 */
@Service("payv2PayWayService")
public class Payv2PayWayServiceImpl extends BaseServiceImpl<Payv2PayWay, Payv2PayWayMapper> implements Payv2PayWayService {
	// 注入当前dao对象
    @Autowired
    private Payv2PayWayMapper payv2PayWayMapper;
    @Autowired
    private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
    @Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
    @Autowired
    private Payv2BussSupportPayWayMapper payv2BussSupportPayWayMapper;
    @Autowired
    private Payv2PayWayRateMapper payv2PayWayRateMapper;
    

    public Payv2PayWayServiceImpl() {
        setMapperClass(Payv2PayWayMapper.class, Payv2PayWay.class);
    }
    
 	public Payv2PayWay selectSingle(Payv2PayWay t) {
		return payv2PayWayMapper.selectSingle(t);
	}

	public List<Payv2PayWay> selectByObject(Payv2PayWay t) {
		return payv2PayWayMapper.selectByObject(t);
	}

	public void updatePayWay(Map<String, Object> map) throws Exception {
		if(map.get("isDelete")!=null||Integer.valueOf(map.get("status").toString())==2){
			Payv2BussSupportPayWay payv2BussSupportPayWay=new Payv2BussSupportPayWay();
			payv2BussSupportPayWay.setPayType(Integer.valueOf(map.get("payType").toString()));
			if(map.get("status")!=null){
				//是启用状态
				payv2BussSupportPayWay.setPayWayStatus(1);
			}
			if(map.get("isDelete")!=null){
				//未删除状态
				payv2BussSupportPayWay.setIsDelete(2);
			}
			//支付渠道ID
			payv2BussSupportPayWay.setPayWayId(Long.valueOf(map.get("id").toString()));
			//查询正常的支付通道
			List<Payv2BussSupportPayWay> supportPayWayList=	payv2BussSupportPayWayService.selectByObject(payv2BussSupportPayWay);
			//停掉或者删除所有的APP或者商铺支持的支付方式
			for (Payv2BussSupportPayWay payv2BussSupportPayWay2 : supportPayWayList) {
				Long payWayId=payv2BussSupportPayWay2.getId();
				Payv2BussAppSupportPayWay payv2BussAppSupportPayWay=new Payv2BussAppSupportPayWay();
				payv2BussAppSupportPayWay.setPayWayId(payWayId);
				if(map.get("status")!=null){//停用
					payv2BussAppSupportPayWay.setStatus(1);
				}
				if(map.get("isDelete")!=null){
					payv2BussAppSupportPayWay.setIsDelete(2);
				}
				//筛选有效的数据
				List<Payv2BussAppSupportPayWay> appSupportPayWayList=payv2BussAppSupportPayWayService.selectByObject(payv2BussAppSupportPayWay);
				for (Payv2BussAppSupportPayWay payv2BussAppSupportPayWay2 : appSupportPayWayList) {
					if(map.get("status")!=null){//停用
						payv2BussAppSupportPayWay2.setStatus(2);
					}
					if(map.get("isDelete")!=null){//删除
						payv2BussAppSupportPayWay2.setIsDelete(1);
					}
					//执行
					payv2BussAppSupportPayWay2.setUpdateTime(new Date());
					payv2BussAppSupportPayWayService.update(payv2BussAppSupportPayWay2);
				}
				if(map.get("status")!=null){//停用
					payv2BussSupportPayWay2.setPayWayStatus(2);
				}
				if(map.get("isDelete")!=null){//删除
					payv2BussSupportPayWay2.setIsDelete(1);
				}
				payv2BussSupportPayWay2.setUpdateTime(new Date());
				//执行：原始数据修改
				payv2BussSupportPayWayService.update(payv2BussSupportPayWay2);	
			}
		}
	}
	
	public List<Payv2PayWay> selectByPayWay(Map<String, Object> map) {
		return payv2PayWayMapper.selectByPayWay(map);
	}

	public PageObject<Payv2PayWay> getPayv2PayWayList(Map<String, Object> map) {
		int totalData = payv2PayWayMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2PayWay> list = payv2PayWayMapper.pageQueryByObject(pageHelper.getMap());
		for(Payv2PayWay way : list){
			String jsonStr = way.getDiscoutRegion();
			if (jsonStr != null && !"1".equals(jsonStr) && jsonStr.length() > 3) {
				JSONArray jsonArray = JSONArray.parseArray(jsonStr);
				way.setJsonArray(jsonArray);
			}
		}
		PageObject<Payv2PayWay> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	/**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	@Cacheable(
			region = "Payv2PayWayService",
			namespace = "selectSingle1",
			fieldsKey = {"#t.id"},
			expire = 300
			)
	public Payv2PayWay selectSingle1(Payv2PayWay t) {
		return payv2PayWayMapper.selectSingle(t);
	}

	/**
	 * 查询支付方式
	 * @param payMoney
	 * @param appId
	 * @param companyId
	 * @param wayType
	 * @param loc
	 * @param rateType
	 * @return
	 */
	@Cacheable(
			region = "Payv2PayWayService",
			namespace = "getWalletConfig",
			fieldsKey = {"#payMoney","#companyId","#wayType","#rateType"},
			expire = 300
			)
	public List<Payv2PayWay> getWalletConfig(Double payMoney,Long companyId, Integer wayType
			, Integer rateType) {
		
		Map<String,Object> map = new HashMap<>();
		map.put("companyId", companyId);
		map.put("wayType", wayType);
		map.put("payWayStatus", PayFinalUtil.PAY_STATUS_ENABLE);
		map.put("isDelete", 2);
		map.put("status", PayFinalUtil.PAY_STATUS_ENABLE);
		map.put("payType", rateType);
		List<Payv2PayWay> list = payv2PayWayMapper.queryCompanyWay(map);
		if(list!=null){
			for (Payv2PayWay payv2PayWay : list) {
				Double lastPayMoeny = payMoney.doubleValue();
				payv2PayWay.setLastPayMoney(lastPayMoeny);
			}
		}
		return list;
	}
	
	/**
	 * 根据渠道商id查询支付方式
	 * @param channelId
	 * @return
	 */
	public List<Payv2PayWay> queryByChannelId(Long channelId){
		return payv2PayWayMapper.queryByChannelId(channelId);
	}
}
