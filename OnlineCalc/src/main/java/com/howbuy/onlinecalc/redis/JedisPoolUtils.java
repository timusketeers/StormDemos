package com.howbuy.onlinecalc.redis;

/**
 * redis客户端对象。
 * @author guangbao.wang
 * 
 */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {

	private static JedisPool pool;

	/**
	 * 建立连接池
	 * 
	 */
	private static void createJedisPool() {
		// 建立连接池配置参数
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxActive(100);
		// 设置最大阻塞时间，记住是毫秒数milliseconds
		config.setMaxWait(1000);
		// 设置空间连接
		config.setMaxIdle(10);
		// 创建连接池
		pool = new JedisPool(config, "192.168.220.105", 6379, 5000);

	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (pool == null)
			createJedisPool();
	}

	/**
	 * 获取一个jedis 对象
	 * 
	 * @return
	 */
	public static Jedis getJedis() {

		if (pool == null)
			poolInit();
		return pool.getResource();
	}

	/**
	 * 释放连接
	 * 
	 * @param jedis
	 */
	public static void returnRes(Jedis jedis) {
		pool.returnResource(jedis);
	}
	
	public static void main(String[] args) {
		Jedis jedis = getJedis();
		System.out.println(jedis.get("3d6f04de-a101-40ca-9911-44d16401c856"));
		returnRes(jedis);
	}
}
