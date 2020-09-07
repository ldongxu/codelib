package com.ldongxu.datastructure.linked;

/**
 * @author liudongxu06
 * @since 2020/9/4
 */
public interface Linked<E> {

    int size();

    boolean isEmpty();

    void add(E data);

    void insert(int p,E data);

    void set(int p,E data);

    void  delete(int p);

    E getFirst();

    E getLast();

    E get(int index);

    void clear();
}
