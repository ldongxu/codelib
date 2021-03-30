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
        ListNode result = new SingleLinkedSort().sortInList(node1.next);
        while (result!=null){
            System.out.println(result.val);
            result=result.next;
        }
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
        rightn.next=r;
        leftn.next=p;
        p.next=right.next;
        pPre.next=left.next;
        return p;
    }

    static class ListNode {
        int val;
        ListNode next = null;

        public ListNode(int val) {
            this.val = val;
        }
    }
}
