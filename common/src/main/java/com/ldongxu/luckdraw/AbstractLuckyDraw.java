package com.ldongxu.luckdraw;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liudongxu06
 * @since 2020/10/28
 */
public abstract class AbstractLuckyDraw<T> implements ILuckyDraw<T> {
    protected int groupTotal;
    protected int multiple;
    protected Map<T, Integer> weightConfigMap;
    protected Class<T> tClass;
    protected float growPoint;


    /**
     * 生成奖组
     * @return
     */
    protected TicketItem<T>[] generateTickets(){
        List<Integer> speed = new ArrayList<>(this.groupTotal);
        for (int i=0;i<this.groupTotal;i++){
            speed.add(i);
        }
        TicketItem<T>[] box = new TicketItem[groupTotal];
        for (Map.Entry<T,Integer> entry:this.weightConfigMap.entrySet()){
            T t = entry.getKey();
            int count = entry.getValue();
            int groupCount = count*this.multiple;
            for (int c=0;c<groupCount;c++){
                int pos = RandomUtils.nextInt(0,speed.size());
                int index = speed.get(pos);
                box[index] = new TicketItem<T>(t);
                speed.remove(pos);
            }
        }
        return box;
    }
}
