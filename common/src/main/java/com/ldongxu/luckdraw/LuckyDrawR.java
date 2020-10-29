package com.ldongxu.luckdraw;

import com.google.gson.reflect.TypeToken;
import com.ldongxu.util.gson.JsonUtil;
import com.ldongxu.util.jedis.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liudongxu06
 * @since 2020/10/27
 */
@Slf4j
public class LuckyDrawR<T> extends AbstractLuckyDraw<T> {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private String redisKey;
    private String redisBakKey;

    public LuckyDrawR(String key, int multiple, Map<T, Integer> countConfigMap, Class<T> tClass) {
       this(key,multiple,countConfigMap,tClass,0.8f);
    }

    public LuckyDrawR(String key, int multiple, Map<T, Integer> countConfigMap, Class<T> tClass, float growPoint) {
        if (multiple<1 || growPoint>1){
            throw new IllegalArgumentException();
        }
        this.redisKey = key;
        this.redisBakKey = this.redisKey+".bak";
        this.multiple = multiple;
        this.weightConfigMap = countConfigMap;
        int total = this.weightConfigMap.values().stream().mapToInt(k->k).sum();
        this.groupTotal = total*multiple;
        this.tClass = tClass;
        this.growPoint = growPoint;
    }


    @Override
    public T getTicket() {
        String jsonStr = JedisUtil.execute(jedis -> jedis.lpop(redisKey));
        if (StringUtils.isBlank(jsonStr)){
            supplementTickets();
            return getTicket();
        }
        TypeToken<?> type = TypeToken.getParameterized(TicketItem.class,new Type[]{tClass});
        TicketItem<T> item = JsonUtil.fromJson(jsonStr,type.getType());
        if (item.isSupply()){
            supplementTickets();
        }
        log.info("get ticket:{}",item);
        return item.getData();
    }

    @Override
    public int createTickets() {
        TicketItem<T>[] box = generateTickets();
        return push(box,redisKey);
    }

    @Override
    public void supplementTickets() {
        List<String> bakList = JedisUtil.execute(jedis -> jedis.lrange(redisBakKey,0,-1));
        if (bakList==null || bakList.isEmpty()){
            if (getSize()==0){
                createTickets();
            }
        }else {
            JedisUtil.pipeline(jedis -> jedis.rpush(redisKey,bakList.toArray(new String[0])),pipeline -> pipeline.del(redisBakKey));
        }
        executor.execute(this::createBakTickets);
    }

    @Override
    public long getSize() {
        return JedisUtil.execute(jedis -> jedis.llen(redisKey));
    }

    private int createBakTickets(){
        TicketItem<T>[] box = generateTickets();
        return push(box,redisBakKey);
    }

    private int push(TicketItem<T>[] box,String key){
        String[] itemArr = new String[box.length];
        for (int l=0;l< box.length;l++){
            TicketItem<T> item = box[l];
            if (l==Math.round(this.groupTotal*growPoint)){
                item.setSupply(true);
            }
            itemArr[l] = JsonUtil.toJson(item);
        }
        JedisUtil.execute(jedis -> jedis.rpush(key,itemArr));
        log.info("奖组{}奖励个数{}个",key,itemArr.length);
        return itemArr.length;
    }


}
