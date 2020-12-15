package com.ldongxu.datastructure.test;

import org.junit.Test;

/**
 * @author liudongxu06
 * @since 2020/12/13
 */
public class Solution {

    @Test
    public void test1(){
        int[][] matrix = new int[][]{{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20},{21,22,23,24,25}};
        spiralOrder(matrix);
    }

    /**
     * 顺时针打印二纬数组
     * 思路：
     * 1、空值处理
     * 2、初始化：左、右、上、下四个边界
     * 3、循环打印：1、根据边界打印 2、边界内缩1 3、判断是否打印完毕（边界是否相遇）
     *
     * 具体步骤：
     * 1、空值判断，空时直接返回
     * 2、定义矩阵左、右、上、下四个边
     * 3、从左到右遍历打印
     * 4、上边界内缩1，判断上下边界，如果内缩后上边界大于下边界则跳出
     * 5、从上到下遍历打印
     * 6、右边界内缩1，判断左右边界，如果内缩后右边界小于左边界则跳出
     * 7、从右到左打印
     * 8、下边界内缩1，判断上下边界，如果内缩后下边界小于上边界则跳出
     * 9、从下到上打印
     * 10、左边界内缩1，判断左右边界，如果左边界大于右边界则跳出
     * @param matrix
     */
    private void spiralOrder(int[][] matrix){
        if (matrix.length==0) return;
        int l=0,r=matrix[0].length-1;
        int t=0,b=matrix.length-1;
        while (true){
            for (int i=l;i<=r;i++){
                System.out.println(matrix[t][i]);
            }
            if (++t>b) break;
            for (int i=t;i<=b;i++){
                System.out.println(matrix[i][r]);
            }
            if (--r<l) break;
            for (int i=r;i>=l;i--){
                System.out.println(matrix[b][i]);
            }
            if (--b<t) break;
            for (int i=b;i>=t;i--){
                System.out.println(matrix[i][l]);
            }
            if (++l>r) break;
        }

    }

    @Test
    public  void huiwen(){
        String str = "abcddcba";
        int length = str.length();
        char[] chars = str.toCharArray();
        int mid = length/2;
        int a =0;
        int b=chars.length-1;
        boolean flag = true;
        for (int i=0;i<mid;i++){
            if (chars[a++]!=chars[b--]){
                flag = false;
            }
        }
        System.out.println(flag);
    }

    @Test
    public void binarySearch(){

    }

}
