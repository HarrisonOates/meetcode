package com.example.lib;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Hard coded test to quickly check its implementation

        //Informal test for AVLTree
        AVLTree<Integer> tree = new AVLTree<>();
        Integer[] nodesValues = new Integer[]{4,2,3,0,5,6,10,9,7};
        for (Integer value : nodesValues) {
            tree.insert(value);
        }
        System.out.println(tree);

        ArrayList<AVLTree.Node<Integer>> nodes = tree.preOrderTraversal();
        for (AVLTree.Node<Integer> node : nodes) {
            System.out.println("Value: " + node.value + ", BF: " +  tree.getBalance(node));
        }

        //Informal test for UserLogin
        UserLogin members = new UserLogin();
        members.addUser("Alex", "!@#$%^&*");
        members.addUser("Geun", "12345678");
        members.addUser("Harrison", "WaHoOoOOO");
        members.addUser("Jayden", "qwertyuiop");
        try{
            members.addUser("Nikhlia", "hihihi");
        } catch (Exception e) {
            System.out.println(e);
        }
        try{
            members.addUser("Geun", "IamBack");
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(members.authoriseUser("Geun", "12345678"));
        System.out.println(members.authoriseUser("Geun", "123456783"));
        System.out.println(members.authoriseUser("geun", "12345678"));
    }
}

