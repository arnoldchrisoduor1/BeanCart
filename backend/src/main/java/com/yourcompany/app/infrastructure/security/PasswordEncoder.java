package com.yourcompany.app.infrastructure.security;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordEncoder {
    
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "SHA-256";
    
    public String encode(String rawPassword) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(rawPassword.getBytes());
            
            // Combine salt and password
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
            
            // Encode as Base64 string
            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }
    
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            // Decode from Base64
            byte[] combined = Base64.getDecoder().decode(encodedPassword);
            
            // Extract salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            
            // Hash password with extracted salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(rawPassword.getBytes());
            
            // Compare with stored hash
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);
            
            return MessageDigest.isEqual(hashedPassword, storedHash);
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            return false;
        }
    }
}