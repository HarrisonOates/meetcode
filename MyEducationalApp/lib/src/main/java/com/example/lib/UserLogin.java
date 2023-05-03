package com.example.lib;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class UserLogin {
    // <Username, Password> pair
    public HashMap<String, String[]>  userLogins;

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
        byte[] salt = generateSalt();
        String hashed = hashPassword(password, salt);
        userLogins.put(username, new String[]{hashed, bytesToHex(salt)});
    }

    public String hashPassword(String password, byte[] salt) {
        try {
            // Uses the SHA-256 algorithm, which the hashed value is fixed to 256 bits or 32 bytes
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashed = md.digest(password.getBytes());
            return bytesToHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return Generate salt using SecureRandom (cryptographically strong random number generator).
     */
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Return a string consisting of hexadecimal values convert from a given bytes
     * @param bytes that is to be converted to hexadecimal
     * @return string of the hexadecimal values
     */
    public String bytesToHex(byte[] bytes) {
        StringBuilder hexValues = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            // Add empty 0 for single figure hex as 2 bytes were used to make that hex
            if (hex.length() == 1) {
                hexValues.append('0');
            }
            hexValues.append(hex);
        }
        return hexValues.toString();
    }

    public byte[] hexToByte(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int val = Integer.parseInt(hex.substring(i*2, i*2 + 2), 16);
            bytes[i] = (byte) val;
        }
        return bytes;
    }

    public boolean authoriseUser(String username, String password) {
        if (!userLogins.containsKey(username)) {
            return false;
        }
        // The String[] will never be null, but just added Objects.requireNonNull to remove the warning
        String storedPassword = Objects.requireNonNull(userLogins.get(username))[0];
        byte[] salt = hexToByte(Objects.requireNonNull(userLogins.get(username))[1]);
        String hashedPassword = hashPassword(password, salt);
        return storedPassword.equals(hashedPassword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> usernamesSet = userLogins.keySet();
        ArrayList<String> usernames = new ArrayList<>(usernamesSet);
        Collection<String[]> pwAndSalts = userLogins.values();
        ArrayList<String> passwords = new ArrayList<>();
        ArrayList<String> salts = new ArrayList<>();
        for (String[] pwAndSalt : pwAndSalts) {
            passwords.add(pwAndSalt[0]);
            salts.add(pwAndSalt[1]);
        }
        for (int i = 0; i < usernames.size(); i++) {
            String userInfo = usernames.get(i) + "," + passwords.get(i) + "," + salts.get(i) + "\n";
            sb.append(userInfo);
        }
        return String.valueOf(sb);
    }
}
