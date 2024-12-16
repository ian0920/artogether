package com.artogether.util.jedis;

import java.time.Duration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
	private static JedisPool pool = null;

	private JedisPoolUtil() {
	}

	public static JedisPool getJedisPool() {
		if (pool == null) { // 避免已有Pool的情況下，還要每次都lock一下資源再解開(而且執行緒還要排隊)
			synchronized (JedisPoolUtil.class) {
				if (pool == null) { // 避免解除lock以後，再度創建新的Pool
					// Pool的初始化設定
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(8);
					config.setMaxIdle(8);
					config.setBlockWhenExhausted(true); // 當連線耗盡時是否阻塞請求
                    config.setMaxWait(Duration.ofMillis(10000)); // 等待連線的最大時間
					pool = new JedisPool(config, "localhost", 6379);
				}
			}
		}
		return pool;
	}

	// 可在ServletContextListener的contextDestroyed裡呼叫此方法註銷Redis連線池
	public static void shutdownJedisPool() {
		if (pool != null)
			pool.destroy();
	}
}
