package com.howbuy.onlinecalc.redis;

/**
 * redis�ͻ��˶���
 * @author guangbao.wang
 * 
 */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {

	private static JedisPool pool;

	/**
	 * �������ӳ�
	 * 
	 */
	private static void createJedisPool() {
		// �������ӳ����ò���
		JedisPoolConfig config = new JedisPoolConfig();
		// �������������
		config.setMaxActive(100);
		// �����������ʱ�䣬��ס�Ǻ�����milliseconds
		config.setMaxWait(1000);
		// ���ÿռ�����
		config.setMaxIdle(10);
		// �������ӳ�
		pool = new JedisPool(config, "192.168.220.105", 6379, 5000);

	}

	/**
	 * �ڶ��̻߳���ͬ����ʼ��
	 */
	private static synchronized void poolInit() {
		if (pool == null)
			createJedisPool();
	}

	/**
	 * ��ȡһ��jedis ����
	 * 
	 * @return
	 */
	public static Jedis getJedis() {

		if (pool == null)
			poolInit();
		return pool.getResource();
	}

	/**
	 * �ͷ�����
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
