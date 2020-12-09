package com.ldongxu.datastructure.linked;

import java.util.Arrays;

/**
 * 快速排序，重点在partition函数
 * @author liudongxu06
 * @since 2020/12/2
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {3,4,1,5,2,7,9};
        new QuickSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public void sort(int[] a){
        int length = a.length;
        quickSort(a,0,length-1);
    }

    private void  quickSort(int[] a,int p,int q){
        if (p>=q) return;
        int b = partition(a,p,q);
        quickSort(a,0,b-1);
        quickSort(a,b+1,q);
    }

    private int partition(int[] a,int p,int q){
        int pivot = a[q];
        int i=p;
        for (int j=p;j<q;j++){
            if (a[j]<pivot){
                swap(a,i,j);
                i++;
            }
        }
        swap(a,i,q);
        return i;
    }

    private void swap(int[] a,int i,int j){
        int tem = a[i];
        a[i]=a[j];
        a[j]=tem;
    }
}
