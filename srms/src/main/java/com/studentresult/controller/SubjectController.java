package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.dto.SubjectDTO;
import com.studentresult.entity.Subject;
import com.studentresult.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subjects")
@CrossOrigin(origins = "http://localhost:4200")
public class SubjectController {
    
    @Autowired
    private SubjectService subjectService;
    
    /**
     * Get all subjects
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Subjects retrieved successfully", subjects)
        );
    }
    
    /**
     * Get all active subjects
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> getAllActiveSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllActiveSubjects();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Active subjects retrieved successfully", subjects)
        );
    }
    
    /**
     * Get subject by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDTO>> getSubjectById(@PathVariable Long id) {
        Optional<SubjectDTO> subject = subjectService.getSubjectById(id);
        if (subject.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Subject retrieved successfully", subject.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Subject not found", null));
    }
    
    /**
     * Get subject by name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<SubjectDTO>> getSubjectByName(@PathVariable String name) {
        Optional<SubjectDTO> subject = subjectService.getSubjectByName(name);
        if (subject.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Subject retrieved successfully", subject.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Subject not found", null));
    }
    
    /**
     * Get subject by code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SubjectDTO>> getSubjectByCode(@PathVariable String code) {
        Optional<SubjectDTO> subject = subjectService.getSubjectByCode(code);
        if (subject.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Subject retrieved successfully", subject.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Subject not found", null));
    }
    
    /**
     * Add new subject
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<SubjectDTO>> addSubject(@RequestBody Subject subject) {
        try {
            if (subject.getSubjectName() == null || subject.getSubjectName().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Subject name is required", null));
            }
            if (subject.getCode() == null || subject.getCode().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Subject code is required", null));
            }
            
            SubjectDTO newSubject = subjectService.addSubject(subject);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Subject added successfully", newSubject));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Update subject
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectDTO>> updateSubject(
            @PathVariable Long id,
            @RequestBody Subject subjectDetails) {
        Optional<SubjectDTO> updatedSubject = subjectService.updateSubject(id, subjectDetails);
        if (updatedSubject.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Subject updated successfully", updatedSubject.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Subject not found", null));
    }
    
    /**
     * Delete subject (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        if (subjectService.deleteSubject(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Subject deleted successfully", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Subject not found", null));
    }
    
    /**
     * Search subjects
     */
    @GetMapping("/search/{term}")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> searchSubjects(@PathVariable String term) {
        List<SubjectDTO> subjects = subjectService.searchSubjects(term);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Search results retrieved successfully", subjects)
        );
    }

    /**
     * Get subjects by class name
     */
    @GetMapping("/class/{className}")
    public ResponseEntity<ApiResponse<List<SubjectDTO>>> getSubjectsByClass(@PathVariable String className) {
        try {
            List<SubjectDTO> subjects = subjectService.getSubjectsByClass(className);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Subjects for class retrieved successfully", subjects)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
}
