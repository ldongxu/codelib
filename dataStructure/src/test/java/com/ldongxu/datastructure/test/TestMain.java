package com.ldongxu.datastructure.test;

import com.ldongxu.datastructure.linked.Heap;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author liudongxu06
 * @since 2020/12/22
 */
public class TestMain {

    /**
     * 给定一个非空的整数数组，返回其中出现频率前 k 高的元素。
     */
    @Test
    public void test(){

    }


    private int[] topK(int[] arr,int k){
        Map<Integer,Integer> countMap = new HashMap<>();
        for (int i=0;i<arr.length;i++){
            int a = arr[i];
            Integer count = countMap.get(a);
            if (count==null){
                count=0;
            }
            countMap.put(a,count+1);
        }
        Map<Integer,Integer> map = new HashMap<>();
        Heap heap = new Heap(k);
        for (Map.Entry<Integer,Integer> entry:countMap.entrySet()){
            map.put(entry.getValue(),entry.getKey());
            heap.insert(entry.getValue());
        }
        int[] result = new int[k];
        int[] sortArr = heap.arr;
        for (int i=1;i<sortArr.length;i++){
            result[i-1]=map.get(sortArr[i]);
        }
        return result;

    }

    static class Heap{
        int[] arr;
        int index=1;
        int size;

        public Heap(int size) {
            this.size = size;
            arr = new int[size+1];
        }

        public void insert(int data){
            if (data<arr[1] || index>=size){
                return;
            }
            arr[++index]=data;
            healpHeap();
        }

        private void healpHeap(){
            int i = index;
            while (true){
                int f = i/2;
                if (f>0 && arr[f]>arr[i]){
                    swap(i,f);
                }else {
                    break;
                }
            }
        }

        private void swap(int i,int f){
            int tmp = arr[i];
            arr[i] = arr[f];
            arr[f] = tmp;
        }
    }



}
