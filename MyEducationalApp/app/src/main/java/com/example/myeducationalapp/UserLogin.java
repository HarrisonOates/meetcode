package com.example.myeducationalapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> usernamesSet = userLogins.keySet();
        ArrayList<String> usernames = new ArrayList<>(usernamesSet);
        Collection<String> pwCollection = userLogins.values();
        ArrayList<String> passwords = new ArrayList<>(pwCollection);
        for (int i = 0; i < usernames.size(); i++) {
            sb.append(usernames.get(i) + ": " + passwords.get(i) + "\n");
        }
        return String.valueOf(sb);
    }
}
