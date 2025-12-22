package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.dto.LoginRequest;
import com.studentresult.dto.LoginResponse;
import com.studentresult.dto.StudentLoginRequest;
import com.studentresult.entity.User;
import com.studentresult.service.AuthService;
import com.studentresult.service.StudentAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private StudentAuthService studentAuthService;
    
    /**
     * Student Login endpoint
     * Uses DOB-based password (DDMMYYYY + "ok")
     * Example: For DOB 09/04/2011, password is "09042011ok"
     */
    @PostMapping("/student-login")
    public ResponseEntity<ApiResponse<LoginResponse>> studentLogin(@RequestBody StudentLoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email is required", null));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Password is required", null));
            }
            
            LoginResponse response = studentAuthService.studentLogin(loginRequest);
            
            if (response.getSuccess()) {
                return ResponseEntity.ok(
                    new ApiResponse<>(true, response.getMessage(), response)
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, response.getMessage(), null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Login endpoint
     * Takes email and password, returns user role and redirect path
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email is required", null));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Password is required", null));
            }
            
            LoginResponse response = authService.login(loginRequest);
            
            if (response.getSuccess()) {
                return ResponseEntity.ok(
                    new ApiResponse<>(true, response.getMessage(), response)
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, response.getMessage(), null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Teacher Login endpoint (Dedicated)
     * Uses email and password stored in teacher table
     * Format: email + auto-generated password (first 3 letters of name + last 4 digits of phone)
     * Example: sharma@teacher.com / SHA3210
     */
    @PostMapping("/teachers-login")
    public ResponseEntity<ApiResponse<LoginResponse>> teachersLogin(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email is required", null));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Password is required", null));
            }
            
            LoginResponse response = authService.teacherLogin(loginRequest);
            
            if (response.getSuccess()) {
                return ResponseEntity.ok(
                    new ApiResponse<>(true, response.getMessage(), response)
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, response.getMessage(), null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Register endpoint
     * Creates a new user account
     * Password is automatically encrypted using BCrypt
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User user) {
        try {
            // Validation
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Username is required", null));
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email is required", null));
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Password is required", null));
            }
            if (user.getRole() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Role is required", null));
            }
            
            User newUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully. Password has been encrypted.", newUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Check if user exists by email
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@PathVariable String email) {
        Optional<User> user = authService.getUserByEmail(email);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Email check completed", user.isPresent())
        );
    }
    
    /**
     * Check if username exists
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@PathVariable String username) {
        Optional<User> user = authService.getUserByUsername(username);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Username check completed", user.isPresent())
        );
    }
    
    /**
     * Test endpoint - Debug login
     */
    @PostMapping("/test-login")
    public ResponseEntity<ApiResponse<?>> testLogin(@RequestBody LoginRequest request) {
        Map<String, Object> debug = new HashMap<>();
        debug.put("email", request.getEmail());
        debug.put("password", request.getPassword());
        
        Optional<User> user = authService.getUserByEmail(request.getEmail());
        
        if (user.isEmpty()) {
            debug.put("user_found", false);
            debug.put("message", "User not found");
        } else {
            debug.put("user_found", true);
            debug.put("database_password", user.get().getPassword());
            debug.put("password_match", request.getPassword().equals(user.get().getPassword()));
            debug.put("is_active", user.get().getIsActive());
            debug.put("role", user.get().getRole().toString());
        }
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Debug test login", debug));
    }
}
