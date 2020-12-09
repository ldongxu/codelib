package com.ldongxu.datastructure.linked;

import java.util.Arrays;

/**
 * 归并排序
 * @author liudongxu06
 * @since 2020/12/2
 */
public class MergeSort {


    public static void main(String[] args) {
        int[] arr = {3,4,1,5,2,7,9};
        new MergeSort().sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public void sort(int[] arr){
        mergeSort(arr,0,arr.length-1);
    }

    private void mergeSort(int[] arr,int p,int r){
        if (p>=r) return;
        int mid = (p+r)/2;
        mergeSort(arr,p,mid);
        mergeSort(arr,mid+1,r);
        merge(arr,p,mid,mid+1,r);

    }
    private void merge(int[] arr,int a,int b,int e,int f){
        int i=a;
        int j=e;
        int d =0;
        int[] temp = new int[f-a+1];
        while (i<=b && j<=f){
            if (arr[i]<=arr[j]){
                temp[d++]=arr[i++];
            }else {
                temp[d++]=arr[j++];
            }
        }

        while (i<=b){
            temp[d++]=arr[i++];
        }

        while (j<=f){
            temp[d++]=arr[j++];
        }

        for (int k=0;k<temp.length;k++){
            arr[a+k]=temp[k];
        }
    }
}
