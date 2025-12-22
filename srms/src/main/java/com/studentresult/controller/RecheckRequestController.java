package com.studentresult.controller;

import com.studentresult.dto.ApiResponse;
import com.studentresult.dto.RecheckRequestDTO;
import com.studentresult.dto.RecheckRequestCreateRequest;
import com.studentresult.entity.RecheckRequest;
import com.studentresult.service.RecheckRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rechecks")
@CrossOrigin(origins = "http://localhost:4200")
public class RecheckRequestController {
    
    @Autowired
    private RecheckRequestService recheckRequestService;
    
    /**
     * Get all recheck requests
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RecheckRequestDTO>>> getAllRecheckRequests() {
        List<RecheckRequestDTO> requests = recheckRequestService.getAllRecheckRequests();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Recheck requests retrieved successfully", requests)
        );
    }
    
    /**
     * Get recheck request by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecheckRequestDTO>> getRecheckRequestById(
            @PathVariable Long id) {
        Optional<RecheckRequestDTO> request = recheckRequestService.getRecheckRequestById(id);
        if (request.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Recheck request retrieved successfully", request.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Recheck request not found", null));
    }
    
    /**
     * Get recheck requests for a student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<RecheckRequestDTO>>> getRecheckRequestsByStudentId(
            @PathVariable Long studentId) {
        System.out.println("\n📥 GET /rechecks/student/{" + studentId + "}");
        System.out.println("  Requesting recheck requests for student ID: " + studentId);
        
        List<RecheckRequestDTO> requests = 
            recheckRequestService.getRecheckRequestsByStudentId(studentId);
        
        System.out.println("  Retrieved recheck requests count: " + requests.size());
        if (requests.isEmpty()) {
            System.out.println("  ℹ️  No recheck requests found for student " + studentId);
        } else {
            System.out.println("  Recheck request details:");
            requests.forEach(r -> {
                System.out.println("    - ID: " + r.getRecheckId() + ", Subject: " + r.getSubject() + 
                                 ", Status: " + r.getStatus() + ", Date: " + r.getRequestDate());
            });
        }
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Recheck requests retrieved successfully", requests)
        );
    }
    
    /**
     * Get recheck requests by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<RecheckRequestDTO>>> getRecheckRequestsByStatus(
            @PathVariable String status) {
        try {
            RecheckRequest.RecheckStatus enumStatus = 
                RecheckRequest.RecheckStatus.valueOf(status.toUpperCase());
            List<RecheckRequestDTO> requests = 
                recheckRequestService.getRecheckRequestsByStatus(enumStatus);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Recheck requests retrieved successfully", requests)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Invalid status", null));
        }
    }
    
    /**
     * Create recheck request from frontend data
     * Accepts RecheckRequestCreateRequest and processes it
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<RecheckRequestDTO>> createRecheckRequest(
            @RequestBody RecheckRequestCreateRequest recheckRequest) {
        try {
            System.out.println("📥 Received recheck request from frontend: " + recheckRequest);
            
            if (recheckRequest == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Request body cannot be null", null));
            }
            
            RecheckRequestDTO newRequest = 
                recheckRequestService.createRecheckRequest(recheckRequest);
            
            System.out.println("✅ Recheck request created successfully with ID: " + newRequest.getRecheckId());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Recheck request created successfully", newRequest));
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Validation error: " + e.getMessage(), null));
        } catch (Exception e) {
            System.err.println("❌ Error creating recheck request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }
    
    /**
     * Update recheck request status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RecheckRequestDTO>> updateRecheckStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            RecheckRequest.RecheckStatus enumStatus = 
                RecheckRequest.RecheckStatus.valueOf(status.toUpperCase());
            Optional<RecheckRequestDTO> updatedRequest = 
                recheckRequestService.updateRecheckRequestStatus(id, enumStatus);
            
            if (updatedRequest.isPresent()) {
                return ResponseEntity.ok(
                    new ApiResponse<>(true, "Status updated successfully", updatedRequest.get())
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "Recheck request not found", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Invalid status", null));
        }
    }
    
    /**
     * Update recheck request with admin notes
     */
    @PutMapping("/{id}/notes")
    public ResponseEntity<ApiResponse<RecheckRequestDTO>> updateWithAdminNotes(
            @PathVariable Long id,
            @RequestBody String notes) {
        Optional<RecheckRequestDTO> updatedRequest = 
            recheckRequestService.updateWithAdminNotes(id, notes);
        
        if (updatedRequest.isPresent()) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Notes updated successfully", updatedRequest.get())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Recheck request not found", null));
    }
    
    /**
     * Delete recheck request
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecheckRequest(@PathVariable Long id) {
        if (recheckRequestService.deleteRecheckRequest(id)) {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Recheck request deleted successfully", null)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, "Recheck request not found", null));
    }
}
