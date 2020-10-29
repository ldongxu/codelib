package com.ldongxu.luckdraw;

/**
 * 概率抽奖
 * 预生成奖组
 * @author liudongxu06
 * @since 2020/10/27
 */
public interface ILuckyDraw<T> {

    T getTicket();

    int createTickets();

    void supplementTickets();

    long getSize();
}
