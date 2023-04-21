package com.example.myeducationalapp;

public class AVLTree <T extends Comparable<T>> {
    // Keep a track of how many past questions have been deleted (e.g. wrong questions have been posted so need to be removed?)
    // delete() will be the last one to implement if we have time
    public int deletedNo = 0;
    public Node<T> root;
    public AVLTree() {this.root = null;}

    private void rightRotate(){}

    private void leftRotate(){}

    private void insertBeforeBalance(Node<T> root, Node<T> x) {
        if (root.value.compareTo(x.value) > 0) {
            if (root.left.value == null) {
                root.left = x;
                x.parent = root;
            } else {
                insertBeforeBalance(root.left, x);
            }
        } else if (root.value.compareTo(x.value) < 0) {
            if (root.right.value == null) {
                root.right = x;
                x.parent = root;
            } else {
                insertBeforeBalance(root.right, x);
            }
        }
        // Do nothing if the tree already has a node with the same value.
    }

    private void insert(Node<T> x) {
        if (this.root == null) {
            this.root = x;
        } else {
            insertBeforeBalance(this.root, x);
        }
    }

    public void insert(T value) {
        Node<T> node = new Node<T>(value);
        insert(node);
    }

    private Node<T> find(Node<T> x, T v) {
        if (x.value == null) {
            return null;
        }

        int cmp = v.compareTo(x.value);
        if (cmp < 0)
            return find(x.left, v);
        else if (cmp > 0)
            return find(x.right, v);
        else
            return x;
    }

    public Node<T> search(T key) {
        return find(this.root, key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringHelper(sb, "", "", root);
        return sb.toString();
    }

    private void toStringHelper(StringBuilder sb, String padding, String pointer, Node<T> node) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.value);
            sb.append("\n");

            String paddingForBoth = padding + "|  ";
            String pointerRight = "R---";
            String pointerLeft = "L---";

            toStringHelper(sb, paddingForBoth, pointerLeft, node.left);
            toStringHelper(sb, paddingForBoth, pointerRight, node.right);
        }
    }
    public class Node<E extends Comparable<E>> {

        E value; // Node value - hash value of past questions?
        Node<E> parent, left, right; // this parent, this left and right nodes

        public Node(E value) {
            this.value = value;
            this.parent = null;

            // Initialise children leaf nodes
            this.left = new Node<E>();
            this.right = new Node<E>();
            this.left.parent = this;
            this.right.parent = this;
        }

        // Leaf node
        public Node() {
            this.value = null;
        }

        public int getHeight(){
            // Check whether leftNode or rightNode are null
            int leftNodeHeight = left == null ? 0 : 1 + left.getHeight();
            int rightNodeHeight = right == null ? 0 : 1 + right.getHeight();
            return Math.max(leftNodeHeight, rightNodeHeight);
        }

        public int getBalanceFactor() {
            return left.getHeight() - right.getHeight();
        }
    }
}


