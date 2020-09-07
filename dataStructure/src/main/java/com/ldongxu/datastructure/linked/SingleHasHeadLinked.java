package com.ldongxu.datastructure.linked;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * @author liudongxu06
 * @since 2020/9/4
 */
public class SingleHasHeadLinked<E> implements Linked<E>, Serializable {

    transient int size = 0;
    transient Node<E> head;
    transient Node<E> tail;


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E data) {
        lastInsert(data);
        size++;
    }

    public void insert(int p, E data) {
        checkPosition(p);
        if (p == 0) {
            firstInsert(data);
        } else if (p == size) {
            lastInsert(data);
        } else {
            Node<E> pre = node(p - 1);
            pre.next = new Node<E>(data, pre.next);
        }
        size++;
    }

    public void set(int p, E data) {
        checkPosition(p);
        Node<E> node = node(p);
        node.item = data;
    }

    public void delete(int p) {
        checkElementIndex(p);
        if (p == 0) {
            head.item = null;
            head = head.next;
        } else {
            Node<E> pre = node(p - 1);
            Node<E> cur = pre.next;
            cur.item = null;
            pre.next = cur.next;
        }
        size--;
    }

    public E getFirst() {
        if (head == null)
            throw new NoSuchElementException();
        return head.item;
    }

    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.item;
    }

    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    public void clear() {
        for (Node<E> x=head;x!=null;){
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    private void firstInsert(E data) {
        if (head == null) {
            head = new Node<E>(data, null);
            tail = head;
        } else {
            head = new Node<E>(data, head);
        }
    }

    private void lastInsert(E data) {
        final Node<E> last = tail;
        final Node<E> node = new Node<E>(data, null);
        if (last == null) {
            head = node;
        } else {
            last.next = node;
        }
        tail = node;
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


    Node<E> node(int index) {
        if (index >= size - 1) {
            return tail;
        } else {
            Node<E> x = head;
            for (int i = 0; i < index; i++) {
                x = head.next;
            }
            return x;
        }
    }

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        for (Node<E> x=head;x!=null;x=x.next){
            s.writeObject(x.item);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int l = s.readInt();
        for (int i=0;i<l;i++){
            lastInsert((E) s.readObject());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item.toString()).append(",");
            cur = cur.next;
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        SingleHasHeadLinked<String> linked = new SingleHasHeadLinked<String>();
        linked.add("aaa");
        linked.add("bb");
        linked.add("cc");
        System.out.println(linked.toString());
        linked.insert(1, "dd");
        System.out.println(linked.toString());
        linked.delete(1);
        System.out.println(linked.toString());

    }
}
