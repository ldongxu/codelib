package com.ldongxu.util.jedis;

import java.util.List;

/**
 * @author liudongxu06
 * @since 2020/10/29
 */
public final class JedisUtil {
    private JedisUtil() {
    }

    private static JedisHandler handler = new JedisHandler(RedisConnectionManager.getJedisPool());

    public static  <T> T execute(JedisExecutor<T> jedisExecutor){
        return handler.execute(jedisExecutor);
    }

    public static List<Object> pipeline(List<PipelineExecutor> pipelineExecutors){
        return handler.pipeline(pipelineExecutors);
    }

    public static List<Object> pipeline(PipelineExecutor... pipelineExecutors){
        return handler.pipeline(pipelineExecutors);
    }
}
