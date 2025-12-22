package com.studentresult.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Generate BCrypt hash for password: -12345
 * Run this to get the correct encrypted password
 */
public class GenerateBCryptHash {
    
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "-12345";
        String hash = encoder.encode(password);
        
        System.out.println("=== BCrypt Hash Generator ===");
        System.out.println("Original Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("");
        System.out.println("Use this hash in sample-data.sql:");
        System.out.println("'" + hash + "'");
        System.out.println("");
        
        // Verify it works
        System.out.println("Verification: " + encoder.matches(password, hash));
    }
}
