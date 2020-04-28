package com.ldongxu.util.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.UUID;

/**
 * redis分布式同步锁
 *
 * 说明：
 * 同步处理完后可以手动及时释放锁也可以利用锁的超时失效时间自动释放锁
 *
 * @author liudongxu06
 * @date 2018/6/20
 */
public class RedisSynLock {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME_MILLISECONDS = "PX";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUFFIX = "_lock";

    private static final long default_expireTime = 2000;//默认锁超时失效时间2秒

    private String lockKey;
    private String requestId;
    private long expireTime;

    private JedisPool jedisPool;

    /**
     * 构造锁对象
     * @param lockKey 锁
     * @param requestId 请求标示
     * @param expireTime 锁超时时间 /毫秒
     */
    public RedisSynLock(JedisPool jedisPool,String lockKey, String requestId, long expireTime) {
        this.jedisPool = jedisPool;
        this.lockKey = lockKey+LOCK_SUFFIX;
        this.requestId = requestId;
        this.expireTime = expireTime;
    }

    public RedisSynLock(String lockKey, JedisPool jedisPool) {
        this.lockKey = lockKey;
        this.jedisPool = jedisPool;
        this.requestId = UUID.randomUUID().toString();
        this.expireTime = default_expireTime;
    }

    /**
     * 获取分布式锁
     * @return 是否获取成功
     */
    public boolean trylock(){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = jedis.set(lockKey,requestId,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME_MILLISECONDS,expireTime);
            return LOCK_SUCCESS.equals(result);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     * 手动释放本次请求（该requestId）的锁
     * @return
     */
    public boolean unlock(){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            return RELEASE_SUCCESS.equals(result);
        } finally {
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "RedisSynLock{" +
                ", lockKey='" + lockKey + '\'' +
                ", requestId='" + requestId + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }


}
