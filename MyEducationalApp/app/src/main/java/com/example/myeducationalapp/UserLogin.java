package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.Firebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class UserLogin {
    private static UserLogin instance;
    public static UserLogin getInstance() {
        if (instance == null) {
            instance = new UserLogin();
        }
        return instance;
    }

    // <Username, String[]{Hashed password, salt}> pair
    public HashMap<String, String[]>  userLogins;

    private UserLogin() {
        this.userLogins = new HashMap<>();

        Firebase.getInstance().readLoginDetails().then((obj) -> {
            String data = (String) obj;

            if (!data.isEmpty()) {
                String[] userLoginInfos = data.split("\n");
                for (String userInfo : userLoginInfos) {
                    String[] pair = userInfo.split(",");
                    userLogins.put(pair[0], new String[]{pair[1], pair[2]});
                }
            }

            return null;
        });

    }

    public void addUser(String username, String password) {
        // No duplicate username allowed
        if (userLogins.containsKey(username)) {
            throw new IllegalArgumentException("This username already exists!");
        }
        // Can adjust the minimum strength of the password later
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long!");
        }
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        userLogins.put(username, new String[]{hashedPassword, bytesToHex(salt)});
        Firebase.getInstance().writeLoginDetails(this.toString());
    }

    /**
     * Return hashed password given a password and random salt,
     * using SHA-256 algorithm, which is known for its strong cryptographic security.
     * @param password that is to be hashed
     * @param salt which is random 16 bytes
     */
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
        for (int i = 0; i < hex.length(); i += 2) {
            int val = Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[i / 2] = (byte) val;
        }
        return bytes;
    }

    public String getCurrentUsername() {
        return "TODO: IMPLEMENT ME!";
    }

    public boolean isUserLoggedIn(String username) {
        return getCurrentUsername().equals(username);
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
