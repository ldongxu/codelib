package com.ldongxu.datastructure.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树遍历
 * @author liudongxu06
 * @since 2021/4/4
 */
public class BinaryTree {

    private TreeNode root;

    public BinaryTree(int[] array) {
        this.root = createBinaryTree(array,0);
    }

    private TreeNode createBinaryTree(int[] array, int index){
        TreeNode node = null;
        if (index<array.length){
            node = new TreeNode(array[index]);
            node.left = createBinaryTree(array,2*index+1);
            node.right = createBinaryTree(array,2*index+2);
        }
        return node;
    }

    public void  preOrder(){
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode node = this.root;
        while (node!=null || !stack.isEmpty()){
            while (node!=null){
                System.out.print(node.val+",");
                stack.addFirst(node);
                node = node.left;
            }
            node = stack.removeFirst();
            node = node.right;
        }
        System.out.println();
    }

    public void inOrder(){
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode node = this.root;
        while (node!=null || !stack.isEmpty()){
            while (node!=null){
                stack.addFirst(node);
                node = node.left;
            }
            node = stack.removeFirst();
            System.out.print(node.val+",");
            node = node.right;
        }
        System.out.println();
    }

    public void postOrder(){
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode node = this.root;
        TreeNode pre = null;
        while (node!=null || !stack.isEmpty()){
            while (node!=null){
                stack.addFirst(node);
                node = node.left;
            }
            node = stack.removeFirst();
            if (node.right==null || node.right==pre){
                System.out.print(node.val+",");
                pre = node;
                node = null;
            }else {
                stack.addFirst(node);
                node = node.right;
            }
        }
        System.out.println();
    }

    public void levelOrder(){
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(this.root);
        while (!queue.isEmpty()){
            for (int i=0;i<queue.size();i++){
                TreeNode node = queue.remove();
                System.out.print(node.val+",");
                if (node.left!=null){
                    queue.add(node.left);
                }
                if (node.right!=null){
                    queue.add(node.right);
                }
            }
        }
        System.out.println();
    }

    static class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    /*
            2
           / \
          3   4
         /\   /\
        6  5 7  8
     */

    public static void main(String[] args) {
        int[] array = new int[]{2,3,4,6,5,7,8};
        BinaryTree tree = new BinaryTree(array);
        tree.preOrder();
        tree.inOrder();
        tree.postOrder();
        tree.levelOrder();
    }
}
