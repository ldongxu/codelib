package com.ldongxu.datastructure.sort;

/**
 * Created by 刘东旭 on 2020/12/9.
 */
public class SingleLinkedSort {


    public static void main(String[] args) {
        int[] arr = new int[]{-869091,-860791,-860466,-860166,-767407,-757897,-748249,-693458,-682870,-608888,-484068,-426572,-406609,-362441,-348926,-227942,-170790,-157789,-132713,-100482,-39078,80573,94840,114739,131739,347325,359115,401563,428207,463504,483610,496406,559374,646432,720732,724427,749542,817675,819197,886101,935294};
        ListNode node1 = new ListNode(0);
        ListNode node = node1;
        for (int i=0;i<arr.length;i++){
            node.next= new ListNode(arr[i]);
            node = node.next;
        }
//        ListNode result = new SingleLinkedSort().sortInList(node1.next);
//        while (result!=null){
//            System.out.println(result.val);
//            result=result.next;
//        }
        ListNode res = new SingleLinkedSort().mergeSort(node1.next);
        while (res!=null){
            System.out.println(res.val);
            res=res.next;
        }
    }

    public ListNode mergeSort(ListNode head){
        if (head==null || head.next==null) return head;
        //找中间节点
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next!=null && fast.next.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode mid = slow.next;
        slow.next=null;//断开链表

        ListNode left = mergeSort(head);
        ListNode right = mergeSort(mid);

        //合并链表
        ListNode res = new ListNode(Integer.MIN_VALUE);
        ListNode h = res;
        while (left!=null && right!=null){
            if (left.val<right.val){
                h.next=left;
                left=left.next;
            }else {
                h.next=right;
                right = right.next;
            }
            h = h.next;
        }
        h.next = left==null?right:left;
        return  res.next;
    }

    public ListNode sortInList (ListNode head) {
        //用一个headPre来领起整个链表，确定链表头
        ListNode headPre = new ListNode(Integer.MIN_VALUE);
        headPre.next=head;
        quickSort(headPre,head,null);
        return headPre.next;
    }

    private void quickSort(ListNode headPre,ListNode head,ListNode tail){
        //关键点1、过程不处理tail，到tail前一个节点即可
        if(head==tail || head.next==tail) return;
        ListNode mid = partition(headPre,head,tail);
        quickSort(headPre,headPre.next,mid);
        quickSort(mid,mid.next,tail);
    }

    private ListNode partition(ListNode pPre,ListNode p,ListNode r){
        int v = p.val;//基准点
        ListNode left = new ListNode(Integer.MIN_VALUE);
        ListNode right = new ListNode(Integer.MIN_VALUE);
        ListNode leftn = left;
        ListNode rightn = right;
        ListNode node = p;
        while((node=node.next)!=r){
            if(node.val<v){
                leftn.next=node;
                leftn=node;
            }else{
                rightn.next = node;
                rightn = node;
            }
        }
        rightn.next=r;//过程不处理r节点，所以最后需要把r加上
        leftn.next=p;//基准点在左链表最后一个
        p.next=right.next;//链接左链表和右链表
        pPre.next=left.next;//哨兵节点领起来排序的链表
        return p;//返回基准节点
    }

    static class ListNode {
        int val;
        ListNode next = null;

        public ListNode(int val) {
            this.val = val;
        }
    }
}
