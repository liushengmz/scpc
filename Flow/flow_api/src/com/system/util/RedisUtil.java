
package com.system.util;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil
{
	private static Logger logger  = Logger.getLogger(RedisUtil.class); 
	
	// Redis服务器IP
	private static String		ADDR			= ConfigManager.getConfigData("REDIS_SERVER_ADDR", "192.168.1.11");

	// Redis的端口号
	private static int			PORT			= StringUtil.getInteger(ConfigManager.getConfigData("REDIS_SERVER_PORT", "6379"), 6379);

	// 访问密码
	private static String		AUTH			= ConfigManager.getConfigData("REDIS_SERVER_AUTH", "123456");
	
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int			MAX_ACTIVE		= 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int			MAX_IDLE		= 200;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int			MAX_WAIT		= 10000;

	private static int			TIMEOUT			= 10000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean		TEST_ON_BORROW	= true;

	//REDIS存储对象的生存时间
	public static final int 	REDIS_OBJECT_EXPIRED_SECONDS = StringUtil.getInteger(ConfigManager.getConfigData("REDIS_OBJECT_EXPIRED_SECONDS"), 86400);
	
	private static JedisPool	jedisPool		= null;
	

	/**
	 * 初始化Redis连接池，失败则退出系统
	 */
	static
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxActive(MAX_ACTIVE);
					config.setMaxIdle(MAX_IDLE);
					config.setMaxWait(MAX_WAIT);
					config.setTestOnBorrow(TEST_ON_BORROW);
					jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
					logger.info("REDIS START CONNECT ...");
					jedisPool.getResource();
					logger.info("REDIS CONNECT SUCCESS");
				}
				catch (Exception e)
				{
					logger.info("REDIS START FAIL AND EXIT SYSTEM ...");
					System.exit(1);
				}
			}
		}).start();
		
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis()
	{
		try
		{
			if (jedisPool != null)
			{
				Jedis resource = jedisPool.getResource();
				return resource;
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			logger.info("REDIS CONNECT FAIL ...");
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis)
	{
		if (jedis != null)
		{
			jedisPool.returnResource(jedis);
		}
	}
	
	public static void init()
	{
		
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		RedisUtil.init();
		System.out.println("start..");
		Thread.sleep(2000);
		long oriStartMils = System.currentTimeMillis();
		for(int i=0; i<10000; i++)
		{
			long startMils = System.currentTimeMillis();
			RedisUtil.getJedis().hgetAll("test");
			System.out.println("SpendMils:" + (System.currentTimeMillis() - startMils));
		}
		System.out.println("Last Mils :" + (System.currentTimeMillis() - oriStartMils));
	}
}
