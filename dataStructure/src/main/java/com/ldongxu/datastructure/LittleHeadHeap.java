package com.ldongxu.datastructure;

/**
 * @author liudongxu06
 * @since 2021/4/6
 */
public class LittleHeadHeap {
    private int[] queue;
    private int size =0;
    private int capacity;

    public LittleHeadHeap(int capacity) {
        this.capacity = capacity;
        queue = new int[capacity];
    }

    //小顶堆，保持前k大
    public boolean addAndTopBig(int val){
        if (!add(val)){
            int head = peek();
            if (val<head){
                return false;
            }else {
                queue[0]=val;
                siftDown(0);
                return true;
            }
        }
        return true;
    }

    public boolean add(int val){
        if (size>=capacity){
            return false;
        }
        int i=size;
        size=i+1;
        if (i==0){
            queue[0]=val;
        }else {
            queue[i]=val;
            siftUp(i,val);
        }
        return true;
    }

    public int peek(){
        return size==0?-1:queue[0];
    }

    public int poll(){
        if (size==0){
            return -1;
        }
        int i = --size;
        int r = queue[0];
        if (i!=0){
            queue[0]=queue[size];//将最后节点放到头部
            queue[size]=-1;//最后节点置空
            siftDown(0);//自上而下堆化
        }else {
            queue[0]=-1;
        }
        System.out.println(r);
        return r;
    }

    private void siftUp(int index,int val){
        while (index>0){
            int p = (index-1)/2;//下标从0开始，父节点下标=(index-1)/2
            if (val<queue[p]){
                swap(index,p);
            }
            index = p;
        }
    }

    private void siftDown(int index){
        while (index<size/2){//size=lastIndex-1
            int minPos = index;
            int left = index*2+1;
            int right = index*2+2;
            if (queue[left]<queue[minPos]){
                minPos = left;
            }
            if (right<size && queue[right]<queue[minPos]){
                minPos = right;
            }
            swap(index,minPos);
            index = minPos;
        }
    }

    private void swap(int i,int j){
        int temp = queue[i];
        queue[i]=queue[j];
        queue[j]=temp;
    }

    public void print(){
        for (int a:queue){
            System.out.print(a+",");
        }
        System.out.println();
    }

    /*
            2
           / \
          4   3
         /\   /\
        6  5 7  8
     */
    public static void main(String[] args) {
        LittleHeadHeap littleHeadHeap = new LittleHeadHeap(7);
        int[] array = new int[]{3,4,2,6,5,7,8,9};
        for (int a :array){
            littleHeadHeap.addAndTopBig(a);
        }
        littleHeadHeap.print();
        littleHeadHeap.poll();
        littleHeadHeap.print();

    }
}
