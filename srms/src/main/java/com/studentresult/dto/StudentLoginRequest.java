package com.studentresult.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for student login request
 * Student login uses email and DOB-based password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginRequest {
    private String email;
    private String password;
}
