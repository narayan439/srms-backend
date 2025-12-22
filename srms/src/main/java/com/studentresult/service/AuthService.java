package com.studentresult.service;

import com.studentresult.dto.LoginRequest;
import com.studentresult.dto.LoginResponse;
import com.studentresult.entity.Student;
import com.studentresult.entity.Teacher;
import com.studentresult.entity.User;
import com.studentresult.repository.StudentRepository;
import com.studentresult.repository.TeacherRepository;
import com.studentresult.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Login user with email and password
     * Returns login response with user role and redirect info
     */
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        // Trim whitespace
        email = email != null ? email.trim() : "";
        password = password != null ? password.trim() : "";
        
        System.out.println("🔐 Login attempt - Email: '" + email + "', Password: '" + password + "'");
        
        // Check if admin
        if (email.equals("admin@gmail.com") && password.equals("123456")) {
            System.out.println("✅ Admin login successful");
            return new LoginResponse(
                true,
                "Admin login successful",
                1L,
                "ADMIN",
                "Admin",
                "/admin/dashboard"
            );
        }
        
        // Check if teacher - authenticate from teacher table
        Optional<Teacher> teacher = teacherRepository.findByEmailIgnoreCase(email);
        
        if (teacher.isEmpty()) {
            System.out.println("❌ Teacher not found with email: " + email);
            return new LoginResponse(
                false,
                "Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        Teacher foundTeacher = teacher.get();
        String storedPassword = foundTeacher.getPassword();
        System.out.println("✓ Teacher found: " + foundTeacher.getName());
        System.out.println("  Email from DB: '" + foundTeacher.getEmail() + "'");
        System.out.println("  Stored password: '" + storedPassword + "'");
        System.out.println("  Submitted password: '" + password + "'");
        System.out.println("  is_active: " + foundTeacher.getIsActive());
        
        // Verify password - plain text comparison
        if (storedPassword == null) {
            System.out.println("❌ Password is NULL in database!");
            return new LoginResponse(
                false,
                "Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        if (!password.equals(storedPassword)) {
            System.out.println("❌ Password mismatch!");
            System.out.println("   Expected length: " + storedPassword.length() + ", Got length: " + password.length());
            return new LoginResponse(
                false,
                "Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        // Check if teacher is active
        if (!foundTeacher.getIsActive()) {
            System.out.println("❌ Teacher account is disabled");
            return new LoginResponse(
                false,
                "Teacher account is disabled",
                null,
                null,
                null,
                null
            );
        }
        
        // Return teacher login response
        System.out.println("✅ Teacher login successful: " + foundTeacher.getName());
        return new LoginResponse(
            true,
            "Login successful",
            foundTeacher.getTeacherId(),
            "TEACHER",
            foundTeacher.getName(),
            "/teacher/dashboard"
        );
    }
    
    /**
     * Register a new user
     * Password is stored as plain text (for development only)
     */
    public User registerUser(User user) {
        // Store password as plain text (development only)
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsActive(true);
        return userRepository.save(user);
    }
    
    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Teacher Login - dedicated endpoint for teacher authentication
     * @param loginRequest email and password
     * @return LoginResponse with teacher details if successful
     */
    public LoginResponse teacherLogin(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        // Trim whitespace
        email = email != null ? email.trim() : "";
        password = password != null ? password.trim() : "";
        
        System.out.println("🏫 Teacher Login attempt - Email: '" + email + "', Password: '" + password + "'");
        
        // Find teacher by email (case-insensitive)
        Optional<Teacher> teacher = teacherRepository.findByEmailIgnoreCase(email);
        
        if (teacher.isEmpty()) {
            System.out.println("❌ Teacher not found with email: " + email);
            return new LoginResponse(
                false,
                "Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        Teacher foundTeacher = teacher.get();
        String storedPassword = foundTeacher.getPassword();
        
        System.out.println("✓ Teacher found: " + foundTeacher.getName());
        System.out.println("  Email from DB: '" + foundTeacher.getEmail() + "'");
        System.out.println("  Stored password: '" + storedPassword + "'");
        System.out.println("  Submitted password: '" + password + "'");
        System.out.println("  is_active: " + foundTeacher.getIsActive());
        
        // Verify password
        if (storedPassword == null) {
            System.out.println("❌ Password is NULL in database!");
            return new LoginResponse(
                false,
                "Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        if (!password.equals(storedPassword)) {
            System.out.println("❌ Password mismatch!");
            System.out.println("   Expected length: " + storedPassword.length() + ", Got length: " + password.length());
            return new LoginResponse(
                false,
                "Invalid email or password",
                null,
                null,
                null,
                null
            );
        }
        
        // Check if teacher is active
        if (!foundTeacher.getIsActive()) {
            System.out.println("❌ Teacher account is disabled");
            return new LoginResponse(
                false,
                "Teacher account is disabled",
                null,
                null,
                null,
                null
            );
        }
        
        // Return teacher login response
        System.out.println("✅ Teacher login successful: " + foundTeacher.getName());
        return new LoginResponse(
            true,
            "Login successful",
            foundTeacher.getTeacherId(),
            "TEACHER",
            foundTeacher.getName(),
            "/teacher/dashboard"
        );
    }
    
    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
