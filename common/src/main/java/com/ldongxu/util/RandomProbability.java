package com.ldongxu.util;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 权重概率随机算法
 * @author liudongxu06
 * @since 2020/10/23
 */
public class RandomProbability<T> {
    private final TreeMap<Double, T> weightMap = new TreeMap<>();
    private Map<T,Double> weightConfigMap;
    private final Map<T,Integer> countMap =new HashMap<>();
    private int balanceFactor = 5;//动态平衡范围因子

    public RandomProbability(Map<T,Double> weightConfig) {
        this.weightConfigMap = weightConfig;
        initWeight();
    }

    public RandomProbability(Map<T,Double> weightConfig,int balanceFactor) {
        this.weightConfigMap = weightConfig;
        this.balanceFactor = balanceFactor;
        initWeight();
    }

    /**
     * 概率随机
     * 结果接近每个元素随机概率
     * @return
     */
    public T random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, T> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }

    /**
     * 平衡随机概率
     * 更精准控制概率精度
     * @return
     */
    public T balanceRandom(){
        T item = random();
        double weight = weightConfigMap.get(item);
        int total = totalCount();
        double rateNum = total*weight;
        Integer count = countMap.get(item);
        if (count>rateNum+balanceFactor){
            return random();
        }
        countMap.put(item,++count);
        return item;
    }
    private void initWeight() {
        for (Map.Entry<T, Double> pair : this.weightConfigMap.entrySet()) {
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey();
            this.weightMap.put(pair.getValue() + lastWeight, pair.getKey());//权重累加
            countMap.put(pair.getKey(),0);
        }
    }

    private int totalCount(){
        int total = 0;
        for (Integer count:countMap.values()){
            total+=count;
        }
        return total;
    }

    public static void main(String[] args) {
        Map<Long,Double> weightConfigMap = new HashMap<>();
        weightConfigMap.put(1L,0.001);
        weightConfigMap.put(2L,0.003);
        weightConfigMap.put(3L,0.0095);
        weightConfigMap.put(4L,0.06);
        weightConfigMap.put(5L,0.197);
        weightConfigMap.put(6L,0.36);
        weightConfigMap.put(7L,0.3695);
        RandomProbability<Long> value = new RandomProbability<>(weightConfigMap);
        for (int i=0;i<10000;i++){
            Long id=value.balanceRandom();
            if (id==2 || id==3 || id==16){
                System.out.println(i+"-"+id+"===========");
            }else {
                System.out.println(i+"-"+id);
            }
        }

        for (Map.Entry<Long,Integer> entry:value.countMap.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }


}
