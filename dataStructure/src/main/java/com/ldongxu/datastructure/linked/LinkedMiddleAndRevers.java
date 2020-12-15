package com.ldongxu.datastructure.linked;

/**
 * 链表法判断回文串（链表求中和链表反转）
 * @author liudongxu06
 * @since 2020/12/15
 */
public class LinkedMiddleAndRevers {


    public static void main(String[] args) {
        String str = "abcdcba";
        System.out.println(new LinkedMiddleAndRevers().isHuiWen(str));
    }
    public boolean isHuiWen(String str){
        Node head = new Node();
        for (char c:str.toCharArray()){
            head.next=new Node(c);
        }

        //快慢指针求中点
        //1、节点个数是偶数时，fast!=null && fast.next!=null 条件下，slow落在靠右的中间节点
        //2、节点个数是偶数时，fast.next!=null && fast.next.next!=null 条件下，slow落在靠左的中间节点
        Node slow = head,fast=head;
        while (fast.next!=null && fast.next.next!=null){
            slow=slow.next;
            fast = fast.next.next;
        }
        Node middle = slow;

        //节点反转，需要保留当前节点的上一个节点，
        Node cur = middle.next;
        Node pre = null;
        while (cur!=null){
            Node next = cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        middle.next=pre;//注意连上之前的节点

        Node a = head.next;
        Node b=middle.next;
        while (b!=null){
           if (a.val!=b.val){
               return false;
           }
           a=a.next;
           b=b.next;
        }
        return true;

    }


    static class Node{
        char val;
        Node next;

        public Node(){

        }
        public Node(char val) {
            this.val = val;
        }
    }
}
