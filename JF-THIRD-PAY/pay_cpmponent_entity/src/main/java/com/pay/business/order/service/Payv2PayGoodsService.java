package com.pay.business.order.service;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.order.entity.Payv2PayGoods;
import com.pay.business.order.mapper.Payv2PayGoodsMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayGoodsService extends BaseService<Payv2PayGoods,Payv2PayGoodsMapper>  {
	/**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	public Payv2PayGoods selectSingle1(Payv2PayGoods t);
	/**
	* @Title: saveGoods 
	* @Description: 保存商品(先查询是否存在，没有就保存！返回商品id)
	* @param @param appId
	* @param @param orderName
	* @param @param type
	* @param @return    设定文件 
	* @return Long    返回类型 
	* @throws
	*/
	public Long saveGoods(Long appId, String orderName, int type);
}
