
package com.system.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.model.RedisToBusinessCpOrderModel;
import com.system.util.RedisUtil;
import com.system.util.StringUtil;

import redis.clients.jedis.Jedis;

public class ToBusinessRedisServer
{
	private static final String CP_ORDER_PREFIX = "CP_ORDER_PREFIX_";
	
	/**
	 * 是否存在 CP ORDER ID 
	 * @param orderId
	 * @return
	 */
	public static boolean existCpOrderId(int cpId,String orderId)
	{
		//如果为空则不存在，否则存在
		return RedisUtil.getJedis().exists(CP_ORDER_PREFIX + cpId + "_" + orderId);
	}
	
	/**
	 * 根据ORDER
	 * @param orderId
	 * @return
	 */
	public static RedisToBusinessCpOrderModel getCpOrder(int cpId,String orderId)
	{
		String key = CP_ORDER_PREFIX + cpId + "_" + orderId;
		List<String> list = RedisUtil.getJedis().hmget(key, 
				RedisToBusinessCpOrderModel.MAP_KEY_ORDER_LIST_SIZE);
		
		if(list!=null && list.size()==1)
		{
			RedisToBusinessCpOrderModel model = new RedisToBusinessCpOrderModel();
			model.setCpId(cpId);
			model.setListSize(StringUtil.getInteger(list.get(0), 0));
			model.setOrderId(orderId);
			return model;
		}
		
		return null;
	}
	
	/**
	 * 存储 CP ID 对应的定单号,其中KEY设置为 CP前缀 加上 CPID 加上 写单号，保证每个CP过来的定单号不会重复
	 * @param model
	 */
	public static void setCpOrder(RedisToBusinessCpOrderModel model)
	{
		Jedis jedis = RedisUtil.getJedis();
		String key = CP_ORDER_PREFIX + model.getCpId() + "_" + model.getOrderId();
		Map<String, String> map = new HashMap<String, String>();
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_TEMP_TABLE_ID,model.getTempTableId() + "");
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_MONTH_NAME, model.getMonthName());
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_MONTH_TABLE_ID, model.getMonthTableId() + "");
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_CP_ORDER, model.getOrderId());
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_CP_ID, model.getCpId() + "");
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_ORDER_LIST_SIZE, model.getListSize()+ "");
		map.put(RedisToBusinessCpOrderModel.MAP_KEY_STATUS, "0");
		jedis.hmset(key,map);
		jedis.expire(key,RedisUtil.REDIS_OBJECT_EXPIRED_SECONDS);
	}
	
	
	
	
}
