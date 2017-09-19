
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
	private static final String SINGLE_CP_ORDER_REQUEST_PREFIX = "SCOR_PREFIX_";
//	private static final String CP_REMAINING_MONEY_PREFIX  = "CP_MONEY_PREFIX_"; 
//	private static final String SP_REMAINING_MONEY_PREFIX  = "SP_MONEY_PREFIX_"; 
	
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
		map.put(RedisCpSingleOrderModel.MAP_KEY_TEMP_TABLE_ID,"" + model.getTempTableId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_MONTH_NAME,"" + model.getMonthName());
		map.put(RedisCpSingleOrderModel.MAP_KEY_MONTH_TABLE_ID,"" + model.getMonthTableId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_CP_ID,"" + model.getCpId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_CLIENT_ORDER_ID	,"" + model.getClientOrderId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_FLOW_SIZE,"" + model.getFlowSize());
		map.put(RedisCpSingleOrderModel.MAP_KEY_OPERATOR,"" + model.getOperator());
		map.put(RedisCpSingleOrderModel.MAP_KEY_MOBILE,"" + model.getMobile());
		map.put(RedisCpSingleOrderModel.MAP_KEY_RANG,"" + model.getRang());
		map.put(RedisCpSingleOrderModel.MAP_KEY_TIME_TYPE,"" + model.getTimeType());
		map.put(RedisCpSingleOrderModel.MAP_KEY_CP_TRONE_ID,"" + model.getCpTroneId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_TRONE_ID,"" + model.getTroneId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_CP_RATIO,"" + model.getCpRatio());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_RATIO,"" + model.getSpRatio());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_TRONE_ID,"" + model.getSpTroneId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_ID,"" + model.getSpId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_API_ID,"" + model.getSpApiId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SEND_SMS,"" + model.getSendSms());
		map.put(RedisCpSingleOrderModel.MAP_KEY_PRICE,"" + model.getPrice());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SERVER_ORDER_ID,"" + model.getServerOrderId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_BASE_PRICE_ID,"" + model.getBasePriceId());
		map.put(RedisCpSingleOrderModel.MAP_KEY_STATUS,"" + model.getStatus());
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_STATUS,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_SP_ERROR_MSG,"");
		map.put(RedisCpSingleOrderModel.MAP_KEY_CREATE_DATE, model.getCreateDate());
		map.put(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_URL, model.getNotifyUrl());
		map.put(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_STATUS, "" + model.getNotifyStatus());
		map.put(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_URL, model.getNotifyUrl());
		map.put(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_TIMES,"" + model.getNotifyTimes());
		map.put(RedisCpSingleOrderModel.MAP_KEY_LAST_NOTIFY_MILS,"0");
		
		if(!jedis.exists(key))
		{
			jedis.hmset(key, map);
			jedis.expire(key,RedisUtil.REDIS_OBJECT_EXPIRED_SECONDS);
		}
	}
	
	/**
	 * 根据 SP ORDER ID 取得整个MAP
	 * @param spOrderId
	 * @return
	 */
	public static Map<String, String> getSingleCpOrder(String spOrderId)
	{
		Jedis jedis = RedisUtil.getJedis();
		return jedis.hgetAll(SINGLE_CP_ORDER_REQUEST_PREFIX + spOrderId);
	}
	
	
	public static void updateRedisData(int type,String key,Map<String, String> map)
	{
		if(map==null)
			return;
		
		String deskey = "";
		
		if(type==1)
		{
			deskey = SINGLE_CP_ORDER_REQUEST_PREFIX + key;
		}
		else
		{
			return;
		}
		
		Jedis jedis = RedisUtil.getJedis();
		
		jedis.hmset(deskey, map);
		
		jedis.expire(deskey,RedisUtil.REDIS_OBJECT_EXPIRED_SECONDS);
		
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		String spOrderId = "fdsafewq";
		RedisUtil.init();
		Thread.sleep(1000);;
		System.out.println(getSingleCpOrder(spOrderId));
	}
	
}
