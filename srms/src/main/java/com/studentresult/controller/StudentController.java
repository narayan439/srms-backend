package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.dto.StudentDTO;
import com.studentresult.entity.Student;
import com.studentresult.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * Get all students
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Students retrieved successfully", students)
        );
    }
    
    /**
     * Get all active students
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllActiveStudents() {
        List<StudentDTO> students = studentService.getAllActiveStudents();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Active students retrieved successfully", students)
        );
    }
    
    /**
     * Get student by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentById(@PathVariable Long id) {
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Student retrieved successfully", student.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Student not found", null));
    }
    
    /**
     * Get students by class
     */
    @GetMapping("/class/{className}")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getStudentsByClass(
            @PathVariable String className) {
        List<StudentDTO> students = studentService.getStudentsByClass(className);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Students retrieved successfully", students)
        );
    }
    
    /**
     * Get student by roll number
     */
    @GetMapping("/rollno/{rollNo}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByRollNo(
            @PathVariable String rollNo) {
        Optional<StudentDTO> student = studentService.getStudentByRollNo(rollNo);
        if (student.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Student retrieved successfully", student.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Student not found", null));
    }
    
    /**
     * Search students
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> searchStudents(
            @RequestParam String searchTerm) {
        List<StudentDTO> students = studentService.searchStudents(searchTerm);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Students found", students)
        );
    }
    
    /**
     * Add new student
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StudentDTO>> addStudent(@RequestBody Student student) {
        try {
            StudentDTO newStudent = studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Student added successfully", newStudent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Update student
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable Long id,
            @RequestBody Student studentDetails) {
        Optional<StudentDTO> updatedStudent = studentService.updateStudent(id, studentDetails);
        if (updatedStudent.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Student updated successfully", updatedStudent.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Student not found", null));
    }
    
    /**
     * Delete student (soft delete - marks as inactive)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        if (studentService.deleteStudent(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Student marked as inactive", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Student not found", null));
    }
    
    /**
     * Permanently delete student from database
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteStudent(@PathVariable Long id) {
        if (studentService.permanentlyDeleteStudent(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Student permanently deleted from database", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Student not found", null));
    }
}
