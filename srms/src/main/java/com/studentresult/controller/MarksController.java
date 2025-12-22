package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.dto.MarksDTO;
import com.studentresult.dto.MarksCreateRequest;
import com.studentresult.entity.Marks;
import com.studentresult.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/marks")
@CrossOrigin(origins = "http://localhost:4200")
public class MarksController {
    
    @Autowired
    private MarksService marksService;
    
    /**
     * Get all marks
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MarksDTO>>> getAllMarks() {
        List<MarksDTO> marks = marksService.getAllMarks();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Marks retrieved successfully", marks)
        );
    }
    
    /**
     * Get mark by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MarksDTO>> getMarkById(@PathVariable Long id) {
        Optional<MarksDTO> mark = marksService.getMarkById(id);
        if (mark.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mark retrieved successfully", mark.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Mark not found", null));
    }
    
    /**
     * Get marks for a student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<MarksDTO>>> getMarksByStudentId(
            @PathVariable Long studentId) {
        System.out.println("\n📥 GET /marks/student/{" + studentId + "}");
        System.out.println("  Requesting marks for student ID: " + studentId);
        
        List<MarksDTO> marks = marksService.getMarksByStudentId(studentId);
        
        System.out.println("  Retrieved marks count: " + marks.size());
        if (marks.isEmpty()) {
            System.out.println("  ⚠️  WARNING: No marks found for student " + studentId);
        } else {
            System.out.println("  Marks details:");
            marks.forEach(m -> {
                System.out.println("    - ID: " + m.getMarksId() + ", Subject: " + m.getSubject() + 
                                 ", Obtained: " + m.getMarksObtained() + ", Max: " + m.getMaxMarks());
            });
        }
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Student marks retrieved successfully", marks)
        );
    }
    
    /**
     * Get marks by class
     */
    @GetMapping("/class/{className}")
    public ResponseEntity<ApiResponse<List<MarksDTO>>> getMarksByClassName(
            @PathVariable String className) {
        List<MarksDTO> marks = marksService.getMarksByClassName(className);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Class marks retrieved successfully", marks)
        );
    }
    
    /**
     * Get marks by student, term and year
     */
    @GetMapping("/student/{studentId}/term/{term}/year/{year}")
    public ResponseEntity<ApiResponse<List<MarksDTO>>> getMarksByStudentAndTerm(
            @PathVariable Long studentId,
            @PathVariable String term,
            @PathVariable Integer year) {
        List<MarksDTO> marks = marksService.getMarksByStudentAndTerm(studentId, term, year);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Marks retrieved successfully", marks)
        );
    }
    
    /**
     * Add new mark (generic endpoint without /add)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MarksDTO>> createMark(@RequestBody MarksCreateRequest request) {
        try {
            System.out.println("📤 Received mark creation request: " + request);
            
            // Validation
            if (request.getStudentId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Student ID is required", null));
            }
            if (request.getSubjectId() == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Subject ID is required", null));
            }
            if (request.getMarksObtained() == null || request.getMarksObtained() < 0 || request.getMarksObtained() > 100) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Marks must be between 0 and 100", null));
            }
            
            System.out.println("   Student ID: " + request.getStudentId());
            System.out.println("   Subject ID: " + request.getSubjectId());
            System.out.println("   Marks: " + request.getMarksObtained());
            
            MarksDTO newMark = marksService.addMarkFromRequest(request);
            System.out.println("✓ Mark created successfully: " + newMark);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Mark added successfully", newMark));
        } catch (Exception e) {
            System.err.println("❌ Error creating mark: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }

    /**
     * Add new mark (legacy endpoint with /add)
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<MarksDTO>> addMark(@RequestBody Marks mark) {
        try {
            // Validation
            if (mark.getMarksObtained() < 0 || mark.getMarksObtained() > 100) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Marks must be between 0 and 100", null));
            }
            
            MarksDTO newMark = marksService.addMark(mark);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Mark added successfully", newMark));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Update mark
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MarksDTO>> updateMark(
            @PathVariable Long id,
            @RequestBody Marks markDetails) {
        Optional<MarksDTO> updatedMark = marksService.updateMark(id, markDetails);
        if (updatedMark.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mark updated successfully", updatedMark.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Mark not found", null));
    }
    
    /**
     * Delete mark
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMark(@PathVariable Long id) {
        if (marksService.deleteMark(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mark deleted successfully", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Mark not found", null));
    }
    
    /**
     * Get mark statistics for a student
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMarkStatistics(
            @PathVariable Long studentId) {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            Integer total = marksService.calculateTotalMarks(studentId);
            Double percentage = marksService.calculatePercentage(studentId);
            Double average = marksService.calculateAverageMarks(studentId);
            String grade = marksService.getGrade(percentage);
            String status = percentage >= 40 ? "PASS" : "FAIL";
            
            statistics.put("total", total);
            statistics.put("percentage", String.format("%.2f", percentage));
            statistics.put("average", String.format("%.2f", average));
            statistics.put("grade", grade);
            statistics.put("status", status);
            
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Statistics retrieved successfully", statistics)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
}
