package com.pay.business.order.mapper;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.order.entity.Payv2PayGoods;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayGoodsMapper extends BaseMapper<Payv2PayGoods>{
	public void insertBySelect(Payv2PayGoods ppg);
}