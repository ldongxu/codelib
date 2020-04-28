package com.ldongxu.util.jedis;

import redis.clients.jedis.Pipeline;

/**
 * @author liudongxu06
 * @since 2019-06-12
 */
@FunctionalInterface
public interface PipelineExecutor {

    void load(Pipeline pipeline);
}
