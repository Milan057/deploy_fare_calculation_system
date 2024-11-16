package com.mdbackend.mdbackend.utilis;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class Generator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    private static final Set<String> generatedKeys = new HashSet<>();
    private static final int KEY_LENGTH = 10;
    private static final String word = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public  String generateUniqueRandomKey() {
        String key;

        do {
            key = generateRandomKey(KEY_LENGTH);
        } while (generatedKeys.contains(key)); 

        generatedKeys.add(key); 
        return key;
    }

    private static String generateRandomKey(int length) {
        StringBuilder key = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            key.append(word.charAt(RANDOM.nextInt(word.length())));
        }
        return key.toString();
    }
}
