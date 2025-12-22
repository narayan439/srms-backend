package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.dto.TeacherDTO;
import com.studentresult.entity.Teacher;
import com.studentresult.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    /**
     * Get all teachers
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TeacherDTO>>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Teachers retrieved successfully", teachers)
        );
    }
    
    /**
     * Get all active teachers
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TeacherDTO>>> getAllActiveTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllActiveTeachers();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Active teachers retrieved successfully", teachers)
        );
    }
    
    /**
     * Get teacher by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherDTO>> getTeacherById(@PathVariable Long id) {
        Optional<TeacherDTO> teacher = teacherService.getTeacherById(id);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Teacher retrieved successfully", teacher.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Teacher not found", null));
    }
    
    /**
     * Get teacher by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<TeacherDTO>> getTeacherByEmail(
            @PathVariable String email) {
        Optional<TeacherDTO> teacher = teacherService.getTeacherByEmail(email);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Teacher retrieved successfully", teacher.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Teacher not found", null));
    }
    
    /**
     * Search teachers
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TeacherDTO>>> searchTeachers(
            @RequestParam String searchTerm) {
        List<TeacherDTO> teachers = teacherService.searchTeachers(searchTerm);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Teachers found", teachers)
        );
    }
    
    /**
     * Get teachers by subject
     */
    @GetMapping("/subject/{subject}")
    public ResponseEntity<ApiResponse<List<TeacherDTO>>> getTeachersBySubject(
            @PathVariable String subject) {
        List<TeacherDTO> teachers = teacherService.getTeachersBySubject(subject);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Teachers retrieved successfully", teachers)
        );
    }
    
    /**
     * Add new teacher
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<TeacherDTO>> addTeacher(@RequestBody Teacher teacher) {
        try {
            TeacherDTO newTeacher = teacherService.addTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Teacher added successfully", newTeacher));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Update teacher
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherDTO>> updateTeacher(
            @PathVariable Long id,
            @RequestBody Teacher teacherDetails) {
        Optional<TeacherDTO> updatedTeacher = teacherService.updateTeacher(id, teacherDetails);
        if (updatedTeacher.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Teacher updated successfully", updatedTeacher.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Teacher not found", null));
    }
    
    /**
     * Delete teacher (soft delete - marks as inactive)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id) {
        if (teacherService.deleteTeacher(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Teacher marked as inactive", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Teacher not found", null));
    }
    
    /**
     * Permanently delete teacher from database
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteTeacher(@PathVariable Long id) {
        if (teacherService.permanentlyDeleteTeacher(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Teacher permanently deleted from database", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Teacher not found", null));
    }
}
