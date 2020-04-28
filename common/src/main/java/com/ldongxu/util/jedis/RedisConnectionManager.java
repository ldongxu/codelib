package com.ldongxu.util.jedis;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.ldongxu.util.ConfigUtil;
import com.ldongxu.util.gson.JsonUtil;
import com.ldongxu.util.gson.JsonUtil.JsonWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liudongxu06
 * @date 2018/7/25
 */
public class RedisConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(RedisConnectionManager.class);
    private static Map<String, JedisPool> poolMap = new ConcurrentHashMap<String, JedisPool>();
    private static String defaultName = "default";

    public static Jedis getConnection() {
        return getConnection(defaultName);
    }

    public static Jedis getConnection(String name) {
        JedisPool pool;
        if ((pool = getJedisPool(name)) != null) {
            return pool.getResource();
        } else {
            throw new RuntimeException("Can't found this name " + name + "in redis connection pool!");
        }

    }

    public static JedisPool getJedisPool() {
        return getJedisPool(defaultName);
    }

    public static JedisPool getJedisPool(String name) {
        JedisPool pool = poolMap.get(name);
        if (pool == null) {
            throw new NullPointerException("Can't found this name " + name + "in redis connection pool!");
        }
        return pool;
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    private static void init() {
        try {
            String path = ConfigUtil.getAppPath();
            if (path != null) {
                String str = FileUtils.readFileToString(new File(path+"redis.json"), "UTF-8");
                JsonArray arr = JsonUtil.getAsJsonElement(str).getAsJsonArray();
                for (JsonElement element : arr) {
                    JsonWrapper ele = JsonUtil.wrapper(element);
                    String name = ele.getAsString("name").orElseThrow(() -> new RuntimeException("redis配置错误"));
                    String host = ele.getAsString("host").orElseThrow(() -> new RuntimeException("redis配置错误"));
                    int port = ele.getAsInt("port").orElseThrow(() -> new RuntimeException("redis配置错误"));
                    Optional<String> password = ele.getAsString("password");
                    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                    int maxTotal = ele.getAsInt("maxTotal").orElse(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
                    poolConfig.setMaxTotal(maxTotal);
                    int maxIdle = ele.getAsInt("maxIdle").orElse(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
                    poolConfig.setMaxIdle(maxIdle);
                    int minIdle = ele.getAsInt("minIdle").orElse(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
                    poolConfig.setMinIdle(minIdle);
                    long maxWait = ele.getAsLong("maxWait").orElse(GenericObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS);
                    poolConfig.setMaxWaitMillis(maxWait);
                    boolean testOnBorrow = ele.getAsBoolean("testOnBorrow").orElse(GenericObjectPoolConfig.DEFAULT_TEST_ON_BORROW);
                    poolConfig.setTestOnBorrow(testOnBorrow);

                    if (password.isPresent()) {
                        poolMap.put(name, new JedisPool(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, password.get()));
                    } else {
                        poolMap.put(name, new JedisPool(poolConfig, host, port));
                    }
                    logger.info("初始化 redis name:{},host:{},port:{}.......", new Object[]{name, host, port});

                }
            }
        } catch (IOException e) {
            logger.error("加载redis配置失败:{}" + e.getMessage(), e);
        }

    }

    static {
        init();
    }


}
