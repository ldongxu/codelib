package com.ldongxu.datastructure.linked;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * @author liudongxu06
 * @since 2020/9/7
 */
public class SingleLinked<E> implements Linked<E>, Serializable {
    transient int size = 0;
    transient Node<E> head;
    transient Node<E> tail;

    public SingleLinked() {
        this.head = this.tail = new Node<E>(null, null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E data) {
       indexLast(data);
       size++;
    }

    public void insert(int p, E data) {
        checkPosition(p);
        if (p == 0) {
            head.next = new Node<E>(data, head.next);
        }else if(p==size){
            indexLast(data);
        } else {
            Node<E> pre = node(p - 1);
            pre.next = new Node<E>(data,pre.next);
        }
        size++;
    }

    public void set(int p, E data) {
        checkElementIndex(p);
        if (p==0){
            head.next.item=data;
        }else if(p==size-1){
            tail.item = data;
        }else {
            Node<E> n = node(p);
            n.item = data;
        }
    }

    public void delete(int p) {
        checkElementIndex(p);
        if (p==0){
           Node<E> first = head.next;
           head.next = head.next.next;
           first.item = null;
           first.next = null;
        }else {
            Node<E> pre = node(p-1);
            Node<E> cur = pre.next;
            pre.next = cur.next;
            cur.item = null;
            cur.next = null;
        }
        size--;
    }

    public E getFirst() {
        if (head.next==null){
            throw new NoSuchElementException();
        }
        return head.next.item;
    }

    public E getLast() {
        if (head.next==null){
            throw new NoSuchElementException();
        }
        return tail.item;
    }

    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    public void clear() {
        for (Node<E> x=head.next;x!=null;){
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x = next;
        }
        head.next =null;
        tail = head;
        size=0;
    }

    private void indexLast(E data){
        Node<E> n = new Node<E>(data,null);
        tail.next = n;
        tail = n;
    }

    private Node<E> node(int index) {
        Node<E> x = head;
        for (int i = 0; i <= index; i++) {
            x = x.next;
        }
        return x;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private void checkPosition(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }


    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        for (Node<E> x = head; x != null; x = x.next) {
            s.writeObject(x.item);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int l = s.readInt();
        for (int i = 0; i < l; i++) {
            indexLast((E) s.readObject());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> cur = head.next;
        while (cur!=null){
            sb.append(cur.item).append(",");
            cur = cur.next;
        }
        return sb.toString();
    }

    public static class Node<E> {
        E item;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.item = data;
            this.next = next;
        }
    }

    public void reverse(){
        Node<E> pre = null;
        Node<E> cur = head.next;
        while (cur!=null){
            Node<E> next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;

        }
        head.next = pre;
    }

    public static void main(String[] args) {
        SingleLinked<String> linked = new SingleLinked<String>();
        linked.add("a");
        linked.add("b");
        linked.add("c");
        System.out.println(linked.toString());
        System.out.println(linked.size());
        linked.delete(0);
        System.out.println(linked.toString());
        System.out.println(linked.size());

        linked.add("dd");
        System.out.println(linked.toString());
        System.out.println(linked.size());
        linked.insert(2,"cc");
        System.out.println(linked.toString());
        System.out.println(linked.size());

        linked.reverse();;

        System.out.println(linked);

    }

}
