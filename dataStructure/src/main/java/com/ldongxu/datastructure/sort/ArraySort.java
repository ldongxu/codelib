package com.ldongxu.datastructure.sort;

import com.ldongxu.util.gson.JsonUtil;

import java.util.Arrays;

/**
 * @author liudongxu06
 * @since 2021/3/30
 */
public class ArraySort {
    private int[] array;

    public ArraySort(int[] array) {
        this.array = array;
    }

    public int[] bubbleSort(){
        if (array.length<=1) return array;
        int[] arr = Arrays.copyOf(array,array.length);
        for (int i=0;i<arr.length;i++){
            boolean flag = false;
            for (int j=0;j<arr.length-i-1;j++){
                int ai = arr[j];
                int aj = arr[j+1];
                if (ai>aj){
                    swap(arr,j,j+1);
                    flag = true;
                }
            }
            if (flag) break;
        }
        return arr;
    }

    public int[] quickSort(){
        if (array.length<=1) return array;
        int[] arr = Arrays.copyOf(array,array.length);

        return arr;
    }
    private void swap(int[] arr,int i,int j){
        int tmp = arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{5,1,2,4,6,8,7};
        ArraySort arraySort = new ArraySort(arr);
        int[] ba = arraySort.bubbleSort();
        System.out.println(JsonUtil.toJson(ba));
    }
}
