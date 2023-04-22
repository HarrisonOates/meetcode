package com.example.lib;

import java.util.HashMap;

public class UserLogin {
    // <Username, Password> pair
    public HashMap<String, String>  userLogins;

    public UserLogin() {
        this.userLogins = new HashMap<>();
    }

    public void addUser(String username, String password) {
        if (userLogins.containsKey(username)) {
            throw new IllegalArgumentException("This username already exists!");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long!");
        }
        userLogins.put(username, password);
    }

    public int hash(String username) {
        String password = userLogins.get(username);
        String hash = username+password;
        return hash.hashCode();
    }

    public boolean authoriseUser(String username, String password) {
        String actual = username + password;
        return actual.hashCode() == hash(username);
    }
}
