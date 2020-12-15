package com.ldongxu.datastructure.linked;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liudongxu06
 * @since 2020/12/15
 */
public class LRUCache {

    private Node head;
    private Node tail;
    private Map<String,Node> cache;
    private int size;
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity=capacity;
        cache = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.right=tail;
        tail.left=head;
    }

    public void put(String k, int v){
        Node node = cache.get(k);
        if (node==null){
            node = new Node(k,v);
            if (size<capacity){
                cache.put(k,node);
                addHead(node);
                size++;
            }else {
                //移除尾节点
                Node t= removeTail();
                cache.remove(t.key);
                //放到头节点
                addHead(node);
            }
        }else {
            node.val=v;
        }
    }

    public int get(String k){
        Node node = cache.get(k);
        if (node==null){
            return -1;
        }
        removeNode(node);
        addHead(node);
        return node.val;
    }

    private Node removeTail(){
        Node t = tail.left;
        removeNode(t);
        return t;
    }

    private void removeNode(Node node){
        node.left.right=node.right;
        node.right.left = node.left;
    }
    private void addHead(Node node){
        Node hnext = head.right;
        head.right=node;
        hnext.left = node;
        node.left=head;
        node.right=hnext;
    }


    static class Node{
        String key;
        int val;
        Node left;
        Node right;

        public Node() {
        }

        public Node(String key, int val) {
            this.key=key;
            this.val = val;
        }
    }
}
