package com.ldongxu.util.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * @author liudongxu06
 * @since 2019-06-12
 */
public class JedisUtil {

    private JedisPool jedisPool;

    public JedisUtil(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public <T> T execute(JedisExecutor<T> jedisExecutor){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedisExecutor.execute(jedis);
        }
    }

    public List<Object> pipeline(List<PipelineExecutor> pipelineExecutors){
        try (Jedis jedis = jedisPool.getResource()){
            Pipeline pipeline = jedis.pipelined();
            for (PipelineExecutor executor:pipelineExecutors){
                executor.load(pipeline);
            }
            return pipeline.syncAndReturnAll();
        }
    }

    public List<Object> pipeline(PipelineExecutor... pipelineExecutors){
        try (Jedis jedis = jedisPool.getResource()){
            Pipeline pipeline = jedis.pipelined();
            for (PipelineExecutor executor:pipelineExecutors){
                executor.load(pipeline);
            }
            return pipeline.syncAndReturnAll();
        }
    }

}
