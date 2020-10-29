package com.ldongxu.luckdraw;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author liudongxu06
 * @since 2020/10/28
 */
@Slf4j
public class LuckyDraw<T> extends AbstractLuckyDraw<T> {

    private TicketItem<T>[] pool;
    private int pos = 0;
    private int poolNum=0;

    public LuckyDraw(int multiple, Map<T, Integer> weightConfigMap, Class<T> tClass, float growPoint) {
        if (multiple < 1 || growPoint > 1) {
            throw new IllegalArgumentException();
        }
        this.multiple = multiple;
        this.weightConfigMap = weightConfigMap;
        this.tClass = tClass;
        this.growPoint = growPoint;
        int total = this.weightConfigMap.values().stream().mapToInt(k -> k).sum();
        this.groupTotal = total * multiple;
        this.pool = new TicketItem[this.groupTotal];
    }

    public LuckyDraw(int multiple, Map<T, Integer> weightConfigMap, Class<T> tClass) {
        this(multiple, weightConfigMap, tClass, 0.8f);
    }

    @Override
    public T getTicket() {
        supplementTickets();
        TicketItem<T> ticket = this.pool[this.pos];
        this.pos++;
        this.poolNum--;
        this.pool[this.pos]=null;
        return ticket.getData();
    }

    @Override
    public int createTickets() {
        TicketItem<T>[] items = generateTickets();
        this.pool = items;
        this.poolNum = this.groupTotal;
        this.pos = 0;
        return items.length;
    }

    @Override
    public void supplementTickets() {
        if (this.pos == this.groupTotal || this.poolNum==0) {
            createTickets();
        }
    }

    @Override
    public long getSize() {
        return this.poolNum;
    }
}
