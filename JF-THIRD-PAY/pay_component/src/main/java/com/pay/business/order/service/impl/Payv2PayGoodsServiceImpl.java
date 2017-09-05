package com.pay.business.order.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.order.entity.Payv2PayGoods;
import com.pay.business.order.mapper.Payv2PayGoodsMapper;
import com.pay.business.order.service.Payv2PayGoodsService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2PayGoodsService")
public class Payv2PayGoodsServiceImpl extends BaseServiceImpl<Payv2PayGoods, Payv2PayGoodsMapper> implements Payv2PayGoodsService {
	// 注入当前dao对象
    @Autowired
    private Payv2PayGoodsMapper payv2PayGoodsMapper;

    public Payv2PayGoodsServiceImpl() {
        setMapperClass(Payv2PayGoodsMapper.class, Payv2PayGoods.class);
    }

    /**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	public Payv2PayGoods selectSingle1(Payv2PayGoods t) {
		return payv2PayGoodsMapper.selectSingle(t);
	}
	/**
	 * 保存商品(先查询是否存在，没有就保存！返回商品id)
	 */
	@Override
	public Long saveGoods(Long appId, String orderName, int type) {
		Payv2PayGoods ppg = new Payv2PayGoods();
		ppg.setAppId(appId);
		ppg.setGoodsName(orderName);
		ppg.setType(type);
		ppg = payv2PayGoodsMapper.selectSingle(ppg);
		if (ppg == null) {
			ppg = new Payv2PayGoods();
			ppg.setAppId(appId);
			ppg.setGoodsName(orderName);
			ppg.setType(type);
			ppg.setCreateTime(new Date());
			payv2PayGoodsMapper.insertBySelect(ppg);
			if(ppg.getId()==null){
				Payv2PayGoods ppg1 = new Payv2PayGoods();
				ppg1.setAppId(appId);
				ppg1.setGoodsName(orderName);
				ppg1.setType(type);
				ppg = payv2PayGoodsMapper.selectSingle(ppg1);
			}else{
				return ppg.getId();
			}
		}
		if(ppg==null){
			return null;
		}
		return ppg.getId();
	}
    
 
}
