package com.studentresult.service;

import com.studentresult.dto.StudentLoginRequest;
import com.studentresult.dto.LoginResponse;
import com.studentresult.entity.Student;
import com.studentresult.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentAuthService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Authenticate student with email and DOB-based password
     * Password format: DDMMYYYY + "ok" (derived from DOB)
     * Example: If DOB is "09/04/2011" → Password is "09042011ok"
     */
    public LoginResponse studentLogin(StudentLoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        // Find student by email
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        
        if (studentOptional.isEmpty()) {
            return new LoginResponse(
                false,
                "❌ Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        Student student = studentOptional.get();
        
        // Check if student is active
        if (!student.getIsActive()) {
            return new LoginResponse(
                false,
                "❌ Student account is disabled",
                null,
                null,
                null,
                null
            );
        }
        
        // Generate expected password from student's DOB
        String expectedPassword = generatePasswordFromDOB(student.getDob());
        
        // Verify password
        if (!password.equals(expectedPassword)) {
            return new LoginResponse(
                false,
                "❌ Invalid email or password. Password should be DOB(DDMMYYYY). Example: 09042011 for DOB 09/04/2011",
                null,
                null,
                null,
                null
            );
        }
        
        // Login successful
        return new LoginResponse(
            true,
            "✓ Login successful",
            student.getStudentId(),
            "STUDENT",
            student.getName(),
            "/student/dashboard"
        );
    }
    
    /**
     * Generate password from student's DOB
     * Format: DDMMYYYY or YYYYMMDD (8 digits)
     * Examples:
     *   - If DOB is "27/05/2009" (DDMMYYYY) → Password is "27052009"
     *   - If DOB is "2009-05-27" (YYYYMMDD) → Password is "20090527"
     * 
     * @param dob Date of birth in any format (DD/MM/YYYY, YYYY-MM-DD, etc)
     * @return Generated password in format 8 digits (DDMMYYYY or YYYYMMDD)
     */
    private String generatePasswordFromDOB(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            return "";
        }
        
        // Remove all non-digit characters
        String dobDigits = dob.replaceAll("\\D", "");
        
        // Take first 8 digits (DDMMYYYY or YYYYMMDD)
        if (dobDigits.length() >= 8) {
            dobDigits = dobDigits.substring(0, 8);
        }
        
        // Return the 8-digit password
        return dobDigits;
    }
    
    /**
     * Validate student DOB and generate expected password
     * Useful for testing and debugging
     */
    public String getExpectedPassword(String dob) {
        return generatePasswordFromDOB(dob);
    }
    
    /**
     * Get student by email (for verification)
     */
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
