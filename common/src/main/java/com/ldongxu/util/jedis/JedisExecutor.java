package com.ldongxu.util.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author liudongxu06
 * @since 2019-06-12
 */
@FunctionalInterface
public interface JedisExecutor<T> {

    T execute(Jedis jedis);
}
