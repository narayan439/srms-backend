package com.studentresult.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility class to generate BCrypt hashed passwords
 * Use this to generate encrypted passwords for sample data
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hashes for common passwords
        System.out.println("=== Password Hash Generator ===\n");
        
        String password123 = "password123";
        String admin123 = "admin123";
        
        System.out.println("Original Password: " + password123);
        System.out.println("Hashed Password: " + encoder.encode(password123));
        System.out.println();
        
        System.out.println("Original Password: " + admin123);
        System.out.println("Hashed Password: " + encoder.encode(admin123));
        System.out.println();
        
        // Verify passwords
        System.out.println("=== Password Verification ===\n");
        String hash1 = encoder.encode(password123);
        System.out.println("Original: " + password123);
        System.out.println("Hash: " + hash1);
        System.out.println("Matches: " + encoder.matches(password123, hash1));
        System.out.println();
        System.out.println("Wrong password matches: " + encoder.matches("wrongpassword", hash1));
    }
}
