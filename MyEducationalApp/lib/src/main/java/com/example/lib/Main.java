package com.example.lib;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


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

        System.out.println(members);

        System.out.println(members.authoriseUser("Geun", "12345678"));
        System.out.println(members.authoriseUser("Geun", "123456783"));
        System.out.println(members.authoriseUser("geun", "12345678"));

        byte[] salt = members.generateSalt();
        for (byte b : salt) {
            System.out.println(b + " ");
        }
        System.out.println(salt);
        System.out.println(members.bytesToHex(salt));
        System.out.println(members.hexToByte(members.bytesToHex(salt)));


    }
}

