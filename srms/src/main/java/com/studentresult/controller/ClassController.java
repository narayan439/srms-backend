package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.entity.SchoolClass;
import com.studentresult.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClassController {
    
    @Autowired
    private ClassService classService;
    
    /**
     * Get all classes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SchoolClass>>> getAllClasses() {
        List<SchoolClass> classes = classService.getAllClasses();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Classes retrieved successfully", classes)
        );
    }
    
    /**
     * Get all active classes
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SchoolClass>>> getAllActiveClasses() {
        List<SchoolClass> classes = classService.getAllActiveClasses();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Active classes retrieved successfully", classes)
        );
    }
    
    /**
     * Get class by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolClass>> getClassById(@PathVariable Long id) {
        Optional<SchoolClass> schoolClass = classService.getClassById(id);
        if (schoolClass.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Class retrieved successfully", schoolClass.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Class not found", null));
    }
    
    /**
     * Get class by name
     */
    @GetMapping("/name/{className}")
    public ResponseEntity<ApiResponse<SchoolClass>> getClassByName(@PathVariable String className) {
        Optional<SchoolClass> schoolClass = classService.getClassByName(className);
        if (schoolClass.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Class retrieved successfully", schoolClass.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Class not found", null));
    }
    
    /**
     * Get class by number
     */
    @GetMapping("/number/{classNumber}")
    public ResponseEntity<ApiResponse<SchoolClass>> getClassByNumber(@PathVariable Integer classNumber) {
        Optional<SchoolClass> schoolClass = classService.getClassByNumber(classNumber);
        if (schoolClass.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Class retrieved successfully", schoolClass.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Class not found", null));
    }
    
    /**
     * Create new class
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SchoolClass>> createClass(@RequestBody SchoolClass schoolClass) {
        SchoolClass createdClass = classService.createClass(schoolClass);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Class created successfully", createdClass));
    }
    
    /**
     * Update class
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolClass>> updateClass(
            @PathVariable Long id,
            @RequestBody SchoolClass schoolClass) {
        SchoolClass updatedClass = classService.updateClass(id, schoolClass);
        if (updatedClass != null) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Class updated successfully", updatedClass)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Class not found", null));
    }
    
    /**
     * Delete class (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(@PathVariable Long id) {
        boolean deleted = classService.deleteClass(id);
        if (deleted) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Class deleted successfully", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Class not found", null));
    }
    
    /**
     * Delete class permanently (hard delete)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Void>> deleteClassPermanently(@PathVariable Long id) {
        boolean deleted = classService.deleteClassPermanently(id);
        if (deleted) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Class deleted permanently", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Class not found", null));
    }
}
