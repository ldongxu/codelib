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

    public int[] insetSort(){
        if (array.length<=1) return array;
        int[] arr = Arrays.copyOf(array,array.length);
        for (int i=1;i<arr.length;i++){
            int val = arr[i];
            int j = i-1;
            for (;j>=0;j--){
                if (arr[j]>val){
                    arr[j+1]=arr[j];
                }else {
                    break;
                }
            }
            arr[j+1]=val;
        }
        return arr;
    }

    public int[] mergeSort(){
        if (array.length<=1) return array;
        int[] arr = Arrays.copyOf(array,array.length);
        doMergeSort(arr,0,arr.length-1);
        return  arr;
    }
    private void doMergeSort(int[] arr,int p,int r){
        if (p>=r) return;
        int mid = (p+r)/2;
        doMergeSort(arr,p,mid);
        doMergeSort(arr,mid+1,r);
        merge(arr,p,mid,mid+1,r);
    }
    private void merge(int[] arr,int p,int a,int b,int r){
        int i=p;
        int j=b;
        int d=0;
        int[] tmp = new int[r-p+1];
        while (i<=a && j<=r){
            if (arr[i]>arr[j]){
                tmp[d++] = arr[j++];
            }else {
                tmp[d++]=arr[i++];
            }
        }
        while (i<=a){
            tmp[d++]=arr[i++];
        }
        while (j<=r){
            tmp[d++]=arr[j++];
        }
        for (int k=0;k<tmp.length;k++){
            arr[p+k]=tmp[k];
        }
    }



    public int[] quickSort(){
        if (array.length<=1) return array;
        int[] arr = Arrays.copyOf(array,array.length);
        doQuickSort(arr,0,arr.length-1);
        return arr;
    }

    private void doQuickSort(int[] arr,int p,int r){
        if (p>=r) return;
        int part = partition(arr,p,r);
        doQuickSort(arr,p,part-1);
        doQuickSort(arr,part+1,r);
    }
    private int partition(int[] arr,int p ,int r){
        int tmp = arr[r];
        int i=p;
        for (int j=p;j<r;j++){
            if (arr[j]<tmp){
                swap(arr,i,j);
                i++;
            }
        }
        swap(arr,i,r);
        return i;
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
        System.out.println(JsonUtil.toJson(arraySort.insetSort()));
        System.out.println(JsonUtil.toJson(arraySort.mergeSort()));
        System.out.println(JsonUtil.toJson(arraySort.quickSort()));

    }
}
