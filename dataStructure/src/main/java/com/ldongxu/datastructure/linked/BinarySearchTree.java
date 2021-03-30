package com.ldongxu.datastructure.linked;

/**
 * @author liudongxu06
 * @since 2020/12/21
 */
public class BinarySearchTree {

    private TreeNode root;

    public void insert(int data){
        if (root==null){
            root = new TreeNode(data);
            return;
        }
        TreeNode p = root;
        while (p!=null){
            if (data<p.val){
                if (p.left==null){
                    p.left= new TreeNode(data);
                    return;
                }
                p = p.left;
            }else {
                if (p.right==null){
                    p.right=new TreeNode(data);
                    return;
                }
                p=p.right;
            }
        }
    }

    public void del(int data){
        TreeNode father = null;
        TreeNode p = root;
        while (p!=null && p.val!=data){
            father = p;
            if (data<p.val){
                p= p.left;
            }else {
                p=p.right;
            }
        }
        if (p==null){
            return;
        }
        if (p.left!=null && p.right!=null){
            TreeNode rmin = p.right;
            TreeNode rfMin = p;
            while (rmin.left!=null){
                rfMin =rmin;
                rmin = rmin.left;
            }
            p.val=rmin.val;
            p=rmin;
            father = rfMin;
        }
        TreeNode child;
        if (p.left!=null){
            child = p.left;
        }else if (p.right!=null){
            child = p.right;
        }else {
            child=null;
        }

        if (father==null){
            root=child;
        }else if (father.left==p){
            father.left = child;
        }else {
            father.right =child;
        }
        System.out.println(root);
    }

    public int query(int data){
        TreeNode node = queryNode(data);
        if (node==null){
            return -1;
        }
        return node.val;
    }

    public TreeNode queryNode(int data){
       TreeNode p=root;
       while (p!=null){
           if (p.val==data){
               return p;
           }
           if (data<p.val){
               p = p.left;
           }else {
               p = p.right;
           }
       }
       return p;
    }

    public void printMin(){
        TreeNode node = root;
        while (node!=null && node.left!=null){
            node = node.left;
        }
        System.out.println(node.val);
    }

    public void printMax(){
        TreeNode node = root;
        while (node!=null && node.right!=null){
            node = node.right;
        }
        System.out.println(node.val);
    }

    public void printBySort(){
        iteratorS(root);
    }

    public void beforeIterator(){
        iteratorB(root);
    }

    private void iteratorS(TreeNode node){
        if (node==null) return;
        iteratorS(node.left);
        System.out.println(node.val);
        iteratorS(node.right);
    }

    private void iteratorB(TreeNode node){
        if (node==null) return;
        System.out.println(node.val);
        iteratorB(node.left);
        iteratorB(node.right);
    }

    static class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
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

    public static void main(String[] args) {
        int[] arr = {8,4,6,7,10,2,5,9};
        BinarySearchTree sbt = new BinarySearchTree();
        for (int i=0;i<arr.length;i++){
            sbt.insert(arr[i]);
        }
        System.out.println(sbt.root);
        sbt.printBySort();
        TreeNode a = sbt.queryNode(8);
        System.out.println(a);

        sbt.printMin();
        sbt.printMax();
        sbt.del(5);
        System.out.println(sbt.root);;
    }
}
