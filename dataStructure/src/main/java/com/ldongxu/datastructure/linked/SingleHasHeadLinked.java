package com.ldongxu.datastructure.linked;

import java.util.NoSuchElementException;

/**
 * @author liudongxu06
 * @since 2020/9/4
 */
public class SingleHasHeadLinked<E> implements Linked<E> {

    int size = 0;
    Node<E> head;
    Node<E> tail;


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
            if (head != null)
                head = head.next;
        } else {
            Node<E> pre = node(p - 1);
            pre.next = pre.next.next;
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
        return null;
    }

    private void firstInsert(E data) {
        if (head == null) {
            head = new Node<E>(data, null);
            tail = head;
        } else {
            head.next = new Node<E>(data, head);
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
