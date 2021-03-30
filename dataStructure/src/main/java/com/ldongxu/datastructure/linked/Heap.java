package com.ldongxu.datastructure.linked;


/**
 * @author liudongxu06
 * @since 2020/12/22
 */
public class Heap {
    private int size=0;
    private int[] array;
    private static final int DEFAULT_CAPACITY=11;

    public Heap(int initCapacity) {
        array = new int[initCapacity];
    }

    public Heap() {
        this(DEFAULT_CAPACITY);
    }

    public void add(int data){
        if (size>=array.length) return;
        int i=size;
        while (i>0){
            int p = (i-1)>>>1;
            int pe = array[p];
            if (data<pe){
                break;
            }
            array[i]=pe;
            i=p;
        }
        array[i] = data;
    }

    public static void buildHeap(int[] arr){
        int length = arr.length;
        int n = length-1;
        for (int i=(n-1)>>>1;i>=0;--i){
            heapify(arr,i,arr.length);
        }
    }


    public static void sort(int[] arr){
        buildHeap(arr);
        int n = arr.length;
        while (n>0){
            swap(arr,0,n-1);
            n--;
            heapify(arr,0,n);
        }
    }

    private static void heapify(int[] arr,int i,int length){
        while (true){
            int maxPos = i;
            if (2*i+1 <length && arr[2*i+1]>arr[maxPos]){
                maxPos=2*i+1;
            }
            if (2*i+2 <length && arr[2*i+2]>arr[maxPos]){
                maxPos=2*i+2;
            }
            if (maxPos==i){
                break;
            }
            swap(arr,i,maxPos);
            i=maxPos;
        }
    }

    private static void swap(int[] arr,int a,int b){
        int temp = arr[a];
        arr[a]=arr[b];
        arr[b]=temp;
    }

    public static void main(String[] args) {
        int[] array = {2,3,5,6,1,10,4,0};
        sort(array);
        for (int i=0;i<array.length;i++){
            System.out.println(array[i]);

        }
    }
}
