package com.ldongxu.datastructure.test;

import com.ldongxu.util.gson.JsonUtil;
import org.junit.Test;

import java.util.*;

/**
 * @author liudongxu06
 * @since 2020/12/13
 */
public class Solution {

    static class LinkNode{
        int val;
        LinkNode next;

        public LinkNode(int val) {
            this.val = val;
        }
        public static  LinkNode createLink(int[] arr){
            LinkNode p = new LinkNode(0);
            LinkNode node = p;
            for (int i=0;i<arr.length;i++){
                node.next = new LinkNode(arr[i]);
                node = node.next;
            }
            return p.next;
        }

        public void print(){
            LinkNode node = this;
            while (node!=null){
                System.out.print(node.val+",");
                node = node.next;
            }
            System.out.println();
        }
    }

    static class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }
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
    public void ipTransfor(){
        long a = ipToLong("0.255.255.254");
        System.out.println(a);
        System.out.println(longToIp(a));
    }

    private long ipToLong(String ip){
        String[] arr = ip.split("\\.");
        long a =0;
        for(int i=0;i<arr.length;i++){
            int subInt = Integer.parseInt(arr[i]);
            a =(a<<8)|subInt;
        }
        return a;
    }

    private String longToIp(long ipInt){
        long[] arr = new long[4];
        for(int i=0;i<4;i++){
            long a = ipInt & 255;
            arr[3-i]=a;
            ipInt = ipInt>>8;
        }
        StringBuilder sb = new StringBuilder();
        for (int j=0;j<arr.length;j++){
            sb.append(arr[j]);
            if (j!=arr.length-1){
               sb.append(".");
            }
        }
        return sb.toString();
    }


    /**
     * 最大的无重复字串
     *
     */
    @Test
    public void noRepeatStr(){
        String str = "pwwkew";
        int length = str.length();
        int start =0;
        int end = 0;
        for (int i=0;i<length;i++){
            Set<Character> set = new HashSet<>();
            for (int j=i;j<length;j++){
                char c = str.charAt(j);
                if (!set.contains(c)){
                    set.add(c);
                    if (j-i>end-start){
                        start=i;
                        end=j;
                    }
                }else {
                    break;
                }
            }
        }
        System.out.println(str.substring(start,end+1));
    }

    /**
     * 最大的无重复字串
     *  滑动窗口
     */
    @Test
    public void noRepeatStr2(){
        String str = "pwwkew";
        int length = str.length();
        Set<Character> set = new HashSet<>();
        int r=0;
        int start=0;
        int end =0;
        for (int i=0;i<length;i++){
            if (i!=0){
                set.remove(str.charAt(i-1));
            }
            while (r<length && !set.contains(str.charAt(r))){
                set.add(str.charAt(r));
                r++;
            }
            if (r-i>end-start){
                start = i;
                end = r;
            }
        }

        System.out.println(str.substring(start,end));
    }

    @Test
    public void revertKNode(){
        int k = 2;
        int[] arr = new int[]{1,2,3,4,5,6,7,8,9};
        LinkNode head = new LinkNode(0);
        LinkNode node = head;
        for (int a:arr){
            node.next=new LinkNode(a);
            node = node.next;
        }

        //k个一组反转链表
        LinkNode r = revertKGroup(head.next,k);
        while (r!=null){
            System.out.print(r.val+",");
            r=r.next;
        }
    }

    /**
     * k个一组反转链表
     * @param head
     * @param k
     * @return
     */
    private LinkNode revertKGroup(LinkNode head,int k){
        LinkNode hair = new LinkNode(0);//构造起始头
        hair.next=head;
        LinkNode pre = hair;
        while (head!=null){
            LinkNode tail = pre;
            for (int i=0;i<k;i++){//tail走k个位置确定待反转尾部tail
                tail = tail.next;
                if (tail==null){
                    return hair.next;
                }
            }
            LinkNode[] t = myRevert(head,tail);//反转
            head =t[0];//反转之后的新头
            tail = t[1];//反转之后的新尾
            pre.next=head;//反转之后的链表跟之前链表接起来
            pre=tail;//下阶段新的pre节点就是反转之后的尾
            head=tail.next;//下阶段新的head
        }
        return hair.next;
    }
    private LinkNode[] myRevert(LinkNode head,LinkNode tail){
        LinkNode p = tail.next;
        LinkNode n = head;
        while (p!=tail){
            LinkNode next  = n.next;
            n.next=p;
            p=n;
            n=next;
        }
        return new LinkNode[]{tail,head};
    }



    /**
     * 3数之和
     *
     * 你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
     * 注意：答案中不可以包含重复的三元组。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/3sum
     */
    @Test
    public void threeSum(){
        int[] nums = new int[]{-1,0,1,2,-1,-4};
        Arrays.sort(nums);
        List<List<Integer>> lists = new ArrayList<>();
        for (int first=0;first<nums.length;first++){
            if (first>0 && nums[first]==nums[first-1]){
                continue;
            }
            int three = nums.length-1;
            int target = -nums[first];
            for (int second=first+1;second<nums.length;second++){
                if (second>first+1 && nums[second]==nums[second-1]){
                    continue;
                }
                while (three>second && nums[second]+nums[three]>target){
                    three--;
                }
                if (second==three){
                    break;
                }
                if (nums[second]+nums[three]==target){
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[first]);
                    list.add(nums[second]);
                    list.add(nums[three]);
                    lists.add(list);
                }
            }
        }
        System.out.println(JsonUtil.toJson(lists));
    }

    /**
     * 股票最佳买卖时机
     *
     *定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
     * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
     * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock
     */
    @Test
    public void maxProfit(){
        int[] prices = new int[]{7,1,5,3,6,4};
        int max =0;
        int minPrice = Integer.MAX_VALUE;
        for (int i=0;i<prices.length-1;i++){
            if (prices[i]<minPrice){
                minPrice = prices[i];
            }else {
                if (prices[i]-minPrice>max){
                    max = prices[i]-minPrice;
                }
            }
        }
        System.out.println(max);
    }

    /**
     * 数组中的第K个最大元素
     * 在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     */
    @Test
    public void findKLargest(){
        int[] array = new int[]{3,2,3,1,2,4,5,5,6};
        int k =4;
        findKLargeByQuickSort(array,0,array.length-1,k);
    }
    private void findKLargeByQuickSort(int[] arr,int a, int b,int k){
        if (a>=b){
            return;
        }
        int p = pardition(arr,a,b);
        if (arr.length-p==k){
            System.out.println(arr[p]);
            return;
        }
        if (arr.length-p<k){
            findKLargeByQuickSort(arr,a,p-1,k);
        }else {
            findKLargeByQuickSort(arr,p+1,b,k);
        }

    }
    private int pardition(int[] arr,int s,int e){
        int pivot = arr[e];
        int i=s;
        for (int j=s;j<e;j++){
            if (arr[j]<pivot){
                swap(arr,i,j);
                i++;
            }
        }
        swap(arr,i,e);
        return i;
    }
    private void swap(int[] arr,int i,int j){
        int tmp = arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;
    }

    @Test
    public void test(){
        LinkNode node = new LinkNode(1);
        node.next = new LinkNode(8);
        node.next.next = new LinkNode(4);
        node.next.next.next = new LinkNode(5);
        LinkNode a = new LinkNode(4);
        a.next=node;
        LinkNode b = new LinkNode(5);
        b.next = new LinkNode(6);
        b.next.next = node;
        LinkNode res = getIntersectionNode(a,b);
        System.out.println(res);
    }

    public LinkNode getIntersectionNode(LinkNode headA, LinkNode headB) {
        if(headA==null || headB==null){
            return null;
        }
        LinkNode a = headA;
        LinkNode b = headB;
        while(a!=b){
            a = a==null?b:a.next;
            b= b==null?a:b.next;
        }
        return a;
    }

    /**
     * 二叉树最大路径和
     */
    @Test
    public void testMaxPahtSum(){
        TreeNode root = new TreeNode(2);
        root.left= new TreeNode(1);
        root.right = new TreeNode(3);
        maxPathSum(root);
        System.out.println(val);
    }
    private int val = Integer.MIN_VALUE;
    public void maxPathSum(TreeNode root) {
        doMaxPathSum(root);
        System.out.println(val);
    }

    private int doMaxPathSum(TreeNode root) {
        if(root==null) return 0;
        int left = Math.max(0,doMaxPathSum(root.left));
        int right = Math.max(0,doMaxPathSum(root.right));
        int three = root.val+left+right;
        int two = root.val+Math.max(left,right);
        val = Math.max(val,Math.max(two,three));
        return two;
    }

    /**
     * 排序奇升偶降链表
     */
    @Test
    public void sortSpecialLink(){
        LinkNode root = LinkNode.createLink(new int[]{1,8,3,6,5,4,7,2});
        boolean up = true;
        LinkNode pl1 = new LinkNode(0);
        LinkNode pl2=new LinkNode(0);
        LinkNode n1 = pl1;
        LinkNode n2=pl2;
        while (root!=null){
            if (up){
                n1.next = root;
                n1=n1.next;
            }else {
                n2.next=root;
                n2=n2.next;
            }
            root = root.next;
            up=!up;
        }
        n1.next=null;
        n2.next=null;

        LinkNode ouHead = pl2.next;
        LinkNode jiHead = pl1.next;

        LinkNode ou = ouHead;
        LinkNode pre = null;
        while (ou!=null){
            LinkNode nex = ou.next;
            ou.next=pre;
            pre=ou;
            ou=nex;
        }
        ouHead = pre;

        LinkNode res = new LinkNode(0);
        LinkNode r = res;
        while (ouHead!=null && jiHead!=null){
            if (ouHead.val<jiHead.val){
                r.next=ouHead;
                ouHead=ouHead.next;
            }else {
                r.next=jiHead;
                jiHead=jiHead.next;
            }
            r = r.next;
        }
        if (ouHead!=null){
            r.next=ouHead;
        }
        if (jiHead!=null){
            r.next=jiHead;
        }
        res.next.print();

    }

}
