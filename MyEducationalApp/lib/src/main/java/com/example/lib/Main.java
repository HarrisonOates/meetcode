package com.example.lib;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Integer[] tree1Values = new Integer[]{-3,-2,-1,0,1,2,3,4,5};
        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer value : tree1Values) {
            tree.insert(value);
        }
        System.out.println(tree);
        Integer rootToBeDeleted = tree.root.value;
        tree.delete(rootToBeDeleted);
        System.out.println(tree);



    }
}

