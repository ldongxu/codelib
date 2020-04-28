package com.ldongxu.util;


import redis.clients.jedis.Jedis;

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

    private Jedis jedis;
    private String lockKey;
    private String requestId;
    private long expireTime;

    public RedisSynLock(Jedis jedis, String lockKey, String requestId, long expireTime) {
        this.jedis = jedis;
        this.lockKey = lockKey+LOCK_SUFFIX;
        this.requestId = requestId;
        this.expireTime = expireTime;
    }



    public RedisSynLock(Jedis jedis,String lockKey, String requestId) {
        this(jedis,lockKey,requestId,default_expireTime);
    }

    public RedisSynLock(Jedis jedis,String lockKey, long expireTime){
        this(jedis,lockKey,UUID.randomUUID().toString(),expireTime);
    }

    public RedisSynLock(Jedis jedis,String lockKey) {
        this(jedis,lockKey,default_expireTime);
    }

    /**
     * 获取分布式锁
     * @return 是否获取成功
     */
    public boolean trylock(){
        String result = this.jedis.set(lockKey,requestId,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME_MILLISECONDS,expireTime);
        return LOCK_SUCCESS.equals(result);
    }

    /**
     * 手动释放本次请求（该requestId）的锁
     * @return
     */
    public boolean unlock(){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = this.jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getRequestId() {
        return requestId;
    }


}
