package com.studentresult.service;

import com.studentresult.dto.RecheckRequestDTO;
import com.studentresult.dto.RecheckRequestCreateRequest;
import com.studentresult.entity.RecheckRequest;
import com.studentresult.entity.Student;
import com.studentresult.entity.Marks;
import com.studentresult.repository.RecheckRequestRepository;
import com.studentresult.repository.StudentRepository;
import com.studentresult.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecheckRequestService {
    
    @Autowired
    private RecheckRequestRepository recheckRequestRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private MarksRepository marksRepository;
    
    /**
     * Get all recheck requests
     */
    public List<RecheckRequestDTO> getAllRecheckRequests() {
        return recheckRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get recheck request by ID
     */
    public Optional<RecheckRequestDTO> getRecheckRequestById(Long recheckId) {
        return recheckRequestRepository.findById(recheckId)
                .map(this::convertToDTO);
    }
    
    /**
     * Get recheck requests for a student
     */
    public List<RecheckRequestDTO> getRecheckRequestsByStudentId(Long studentId) {
        System.out.println("📖 RecheckRequestService.getRecheckRequestsByStudentId(" + studentId + ")");
        
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            System.out.println("  ⚠️  Student not found with ID: " + studentId);
            return List.of();
        }
        
        System.out.println("  ✓ Student found: " + student.get().getName());
        
        List<RecheckRequest> requests = recheckRequestRepository.findByStudent(student.get());
        System.out.println("  Retrieved " + requests.size() + " recheck requests");
        
        requests.forEach(r -> {
            System.out.println("    - ID: " + r.getRecheckId() + ", Subject: " + r.getSubject() + 
                             ", Status: " + r.getStatus() + ", Date: " + r.getRequestDate());
        });
        
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get recheck requests by status
     */
    public List<RecheckRequestDTO> getRecheckRequestsByStatus(RecheckRequest.RecheckStatus status) {
        return recheckRequestRepository.findByStatusOrderByRequestDateDesc(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create new recheck request
     */
    /**
     * Create recheck request from frontend request DTO
     * Fetches related Student and Marks entities and creates the RecheckRequest
     */
    public RecheckRequestDTO createRecheckRequest(RecheckRequestCreateRequest request) {
        System.out.println("📥 Processing recheck request: " + request);
        
        // Validate required fields
        if (request.getStudentId() == null || request.getStudentId() <= 0) {
            throw new IllegalArgumentException("Student ID is required and must be valid");
        }
        
        if (request.getMarksId() == null || request.getMarksId() <= 0) {
            throw new IllegalArgumentException("Marks ID is required and must be valid");
        }
        
        if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject is required");
        }
        
        if (request.getReason() == null || request.getReason().trim().length() < 10) {
            throw new IllegalArgumentException("Reason must be at least 10 characters");
        }
        
        // Fetch Student
        Optional<Student> student = studentRepository.findById(request.getStudentId());
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Student not found with ID: " + request.getStudentId());
        }
        
        // Fetch Marks
        Optional<Marks> marks = marksRepository.findById(request.getMarksId());
        if (marks.isEmpty()) {
            throw new IllegalArgumentException("Marks not found with ID: " + request.getMarksId());
        }
        
        // Create RecheckRequest entity
        RecheckRequest recheckRequest = new RecheckRequest();
        recheckRequest.setStudent(student.get());
        recheckRequest.setMarks(marks.get());
        recheckRequest.setSubject(request.getSubject());
        recheckRequest.setReason(request.getReason());
        recheckRequest.setStatus(RecheckRequest.RecheckStatus.PENDING);
        recheckRequest.setRequestDate(LocalDateTime.now());
        recheckRequest.setAdminNotes(request.getAdminNotes() != null ? request.getAdminNotes() : "");
        
        System.out.println("✓ Saving recheck request for student: " + student.get().getName() + 
                          ", Subject: " + request.getSubject());
        
        RecheckRequest savedRequest = recheckRequestRepository.save(recheckRequest);
        return convertToDTO(savedRequest);
    }
    
    /**
     * Create recheck request from entity (legacy method for backward compatibility)
     */
    public RecheckRequestDTO createRecheckRequest(RecheckRequest recheckRequest) {
        recheckRequest.setRequestDate(LocalDateTime.now());
        recheckRequest.setStatus(RecheckRequest.RecheckStatus.PENDING);
        RecheckRequest savedRequest = recheckRequestRepository.save(recheckRequest);
        return convertToDTO(savedRequest);
    }
    
    /**
     * Update recheck request status
     */
    public Optional<RecheckRequestDTO> updateRecheckRequestStatus(
            Long recheckId, 
            RecheckRequest.RecheckStatus status) {
        return recheckRequestRepository.findById(recheckId).map(recheckRequest -> {
            recheckRequest.setStatus(status);
            if (!status.equals(RecheckRequest.RecheckStatus.PENDING)) {
                recheckRequest.setResolvedDate(LocalDateTime.now());
            }
            RecheckRequest updatedRequest = recheckRequestRepository.save(recheckRequest);
            return convertToDTO(updatedRequest);
        });
    }
    
    /**
     * Update recheck request with admin notes
     */
    public Optional<RecheckRequestDTO> updateWithAdminNotes(Long recheckId, String notes) {
        return recheckRequestRepository.findById(recheckId).map(recheckRequest -> {
            recheckRequest.setAdminNotes(notes);
            RecheckRequest updatedRequest = recheckRequestRepository.save(recheckRequest);
            return convertToDTO(updatedRequest);
        });
    }
    
    /**
     * Delete recheck request
     */
    public boolean deleteRecheckRequest(Long recheckId) {
        return recheckRequestRepository.findById(recheckId).map(recheckRequest -> {
            recheckRequestRepository.delete(recheckRequest);
            return true;
        }).orElse(false);
    }
    
    /**
     * Convert RecheckRequest entity to DTO
     */
    private RecheckRequestDTO convertToDTO(RecheckRequest recheckRequest) {
        return new RecheckRequestDTO(
            recheckRequest.getRecheckId(),
            recheckRequest.getStudent().getStudentId(),
            recheckRequest.getStudent().getName(),
            recheckRequest.getMarks().getMarksId(),
            recheckRequest.getSubject(),
            recheckRequest.getReason(),
            recheckRequest.getStatus().toString(),
            recheckRequest.getRequestDate(),
            recheckRequest.getResolvedDate(),
            recheckRequest.getAdminNotes()
        );
    }
}
