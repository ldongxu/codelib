package com.ldongxu.datastructure.linked;

/**
 * @author liudongxu06
 * @since 2020/9/7
 */
public class SingleLinked<E> implements Linked<E> {
    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public void add(E data) {

    }

    public void insert(int p, E data) {

    }

    public void set(int p, E data) {

    }

    public void delete(int p) {

    }

    public E getFirst() {
        return null;
    }

    public E getLast() {
        return null;
    }

    public E get(int index) {
        return null;
    }

    public void clear() {

    }

    private static class Node<E>{
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }
}
