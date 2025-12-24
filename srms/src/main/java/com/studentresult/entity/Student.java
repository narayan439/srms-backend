package com.studentresult.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, length = 50)
    private String className;
    
    @Column(nullable = false, unique = true, length = 20)
    private String rollNo;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 100)
    private String dob;
    
    @Column(nullable = false)
    private Boolean isActive;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
        // Normalize DOB format to DD/MM/YYYY
        if (this.dob != null) {
            this.dob = normalizeDob(this.dob);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        // Normalize DOB format to DD/MM/YYYY
        if (this.dob != null) {
            this.dob = normalizeDob(this.dob);
        }
    }
    
    /**
     * Normalize DOB to DD/MM/YYYY format.
     * Handles formats like: 2002-02-19T18:30:00.000Z, 2002-02-19, 19/02/2002, 19-02-2002.
     */
    private static String normalizeDob(String dob) {
        if (dob == null) {
            return null;
        }

        dob = dob.trim();
        if (dob.isEmpty()) {
            return dob;
        }

        try {
            // Remove timezone and time if present (ISO format)
            if (dob.contains("T")) {
                dob = dob.split("T")[0];
            }

            final DateTimeFormatter out = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // YYYY-MM-DD -> DD/MM/YYYY
            if (dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate date = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);
                return date.format(out);
            }

            // DD/MM/YYYY -> normalize zero padding
            if (dob.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                LocalDate date = LocalDate.parse(dob, DateTimeFormatter.ofPattern("d/M/yyyy"));
                return date.format(out);
            }

            // DD-MM-YYYY -> DD/MM/YYYY
            if (dob.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
                LocalDate date = LocalDate.parse(dob, DateTimeFormatter.ofPattern("d-M-yyyy"));
                return date.format(out);
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error normalizing DOB: " + dob);
        }

        return dob;
    }
}
