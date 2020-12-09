package com.ldongxu.datastructure.linked;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 已知一个完全二叉树的广度优先遍历顺序为ABCDEFGHK,求还原这颗完全二叉树。
 *                A
 *             /    \
 *            B      C
 *           /  \   /  \
 *          D   E  F   G
 *         / \
 *        H   K
 *
 *  深度优先遍历：前序、中序、后续，可以用递归或者栈Stack ABDHKECFG。
 *  广度优先遍历：从根节点开始，沿着树的宽度依次遍历树的每个节点，可以用队列Queue
 * @author liudongxu06
 * @since 2020/12/9
 */
public class LinkedCreate {

    public static void main(String[] args) {
        char[] arr = new char[]{'A','B','C','D','E','F','G','H','K','J'};
        TreeNode p = new LinkedCreate().createTreeNode(arr);
        System.out.println(p);
    }

    public TreeNode createTreeNode(char[] charArr){
        int index=0;
        TreeNode head = new TreeNode(charArr[index++]);
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(head);
        while (!queue.isEmpty() && index<charArr.length){
            TreeNode p = queue.remove();
            TreeNode left = new TreeNode(charArr[index++]);
            p.left =left;
            queue.add(left);
            if (index<charArr.length){
                TreeNode right = new TreeNode(charArr[index++]);
                p.right=right;
                queue.add(right);
            }
        }
        return head;
    }

    static class TreeNode{
        char val;
        TreeNode left;
        TreeNode right;

        public TreeNode(char val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "TreeNode{" +
                    "val=" + val +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
