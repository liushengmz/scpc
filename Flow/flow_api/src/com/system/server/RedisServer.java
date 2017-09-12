
package com.system.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.model.RedisCpSingleOrderModel;
import com.system.model.RedisToBusinessCpOrderModel;
import com.system.util.RedisUtil;
import com.system.util.StringUtil;

import redis.clients.jedis.Jedis;

public class RedisServer
{
	private static final String CP_ORDER_PREFIX = "CP_ORDER_PREFIX_";
	private static final String SINGLE_CP_ORDER_REQUEST_PREFIX = "SINGLE_CP_ORDER_PREFIX_";
	private static final String CP_REMAINING_MONEY_PREFIX  = "CP_REMAINING_MONEY_PREFIX_"; 
	private static final String SP_REMAINING_MONEY_PREFIX  = "SP_REMAINING_MONEY_PREFIX_"; 
	
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
	 * 检查单个CP定单是否存在相同的定单号
	 * @param cpId
	 * @param orderId
	 * @return
	 */
	public static boolean existSingleCpOrder(String serverId)
	{
		//String orderKey  = StringUtil.getS
		//如果为空则不存在，否则存在
		return RedisUtil.getJedis().exists(SINGLE_CP_ORDER_REQUEST_PREFIX + serverId);
	}
	
	/**
	 * 获取CP余额
	 * @param cpId
	 * @return
	 */
	public static int getCpRemainingMoney(int cpId)
	{
		return StringUtil.getInteger(RedisUtil.getJedis().get(CP_REMAINING_MONEY_PREFIX + cpId),0);
	}
	
	/**
	 * 获取SP余额
	 * @param spId
	 * @return
	 */
	public static int getSpRemainingMoney(int spId)
	{
		return StringUtil.getInteger(RedisUtil.getJedis().get(SP_REMAINING_MONEY_PREFIX + spId),0);
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
	
	/**
	 * 把SINGLE CP ORDER 增加进 REDIS 中
	 * @param model
	 */
	public static void setSingleCpOrder(RedisCpSingleOrderModel model)
	{
		Jedis jedis = RedisUtil.getJedis();
		String key = SINGLE_CP_ORDER_REQUEST_PREFIX + model.getServerOrderId();
		Map<String, String> map = new HashMap<String, String>();
		map.put(RedisCpSingleOrderModel.MAP_KEY_TEMP_TABLE_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_MONTH_NAME,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_MONTH_TABLE_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_CP_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_CLIENT_ORDER_ID	,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_FLOW_SIZE,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_OPERATOR,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_MOBILE,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_RANG,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_TIME_TYPE,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_CP_TRONE_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_TRONE_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_CP_RATIO,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_RATIO,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_TRONE_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SEND_SMS,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_PRICE,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SERVER_ORDER_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_BASE_PRICE_ID,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_STATUS,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_ERROR_MSG,"");
		
		if(jedis.hgetAll(key)!=null)
		{
			jedis.hmset(key, map);
			jedis.expire(key,RedisUtil.REDIS_OBJECT_EXPIRED_SECONDS);
		}
	}
	
	
}
