package com.ldongxu.datastructure.linked;


/**
 * @author liudongxu06
 * @since 2020/9/1
 */
public class LinkedPractice {


    public boolean palindromeString(String str){
        SingleLinked<Character> linked = new SingleLinked<Character>();
        for (char c:str.toCharArray()){
            linked.add(c);
        }
        //快慢指针找中间节点
        SingleLinked.Node<Character> slow =linked.head,fast = linked.head;
        while (fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
        }
        SingleLinked.Node<Character> middle = slow;

        //反转中间节点之后的节点
        SingleLinked.Node<Character> cur = middle.next;
        SingleLinked.Node<Character> pre = null;
        while (cur!=null){
            SingleLinked.Node<Character> next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        middle.next = pre;
        System.out.println(linked);

        //从第一个点和中间接口之后的节点开始遍历比较
        SingleLinked.Node<Character> a = linked.head,b =middle;
        while (b.next!=null ){
            if (!a.next.item.equals(b.next.item)){
                return false;
            }
            a = a.next;
            b = b.next;
        }
        return true;
    }


    public static void main(String[] args) {
      boolean s =  new LinkedPractice().palindromeString("abcdcba");
        System.out.println(s);
    }
}
