package com.studentresult.service;

import com.studentresult.dto.MarksDTO;
import com.studentresult.dto.MarksCreateRequest;
import com.studentresult.entity.Marks;
import com.studentresult.entity.RecheckRequest;
import com.studentresult.entity.Student;
import com.studentresult.entity.Subject;
import com.studentresult.repository.MarksRepository;
import com.studentresult.repository.RecheckRequestRepository;
import com.studentresult.repository.StudentRepository;
import com.studentresult.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarksService {
    
    @Autowired
    private MarksRepository marksRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private RecheckRequestRepository recheckRequestRepository;
    
    /**
     * Get all marks
     */
    public List<MarksDTO> getAllMarks() {
        return marksRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get mark by ID
     */
    public Optional<MarksDTO> getMarkById(Long marksId) {
        return marksRepository.findById(marksId)
                .map(this::convertToDTO);
    }
    
    /**
     * Get marks for a student
     */
    public List<MarksDTO> getMarksByStudentId(Long studentId) {
        System.out.println("\n📖 MarksService.getMarksByStudentId(" + studentId + ")");
        
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            System.out.println("  ❌ Student not found with ID: " + studentId);
            return List.of();
        }
        
        System.out.println("  ✓ Student found: " + student.get().getName());
        
        List<Marks> marksList = marksRepository.findByStudent(student.get());
        System.out.println("  📊 Query returned " + marksList.size() + " marks records");
        
        if (marksList.isEmpty()) {
            System.out.println("  ⚠️  No marks found in database for student");
            return List.of();
        }
        
        System.out.println("  Converting to DTOs...");
        List<MarksDTO> result = marksList.stream()
                .peek(m -> {
                    System.out.println("    - Mark ID: " + m.getMarksId());
                    System.out.println("      Student: " + (m.getStudent() != null ? m.getStudent().getName() : "NULL"));
                    System.out.println("      Subject: " + (m.getSubject() != null ? m.getSubject().getSubjectName() : "NULL"));
                    System.out.println("      Marks: " + m.getMarksObtained() + "/" + m.getMaxMarks());
                    
                    // Validate relationships before conversion
                    if (m.getSubject() == null) {
                        System.err.println("      ❌ ERROR: Subject is NULL!");
                    }
                    if (m.getStudent() == null) {
                        System.err.println("      ❌ ERROR: Student is NULL!");
                    }
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        System.out.println("  ✅ Converted to " + result.size() + " DTOs");
        return result;
    }
    
    /**
     * Get marks by class name
     */
    public List<MarksDTO> getMarksByClassName(String className) {
        return marksRepository.findByClassName(className).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get marks by student, term and year
     */
    public List<MarksDTO> getMarksByStudentAndTerm(Long studentId, String term, Integer year) {
        return marksRepository.findByStudentIdAndTermAndYear(studentId, term, year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Add new mark
     */
    public MarksDTO addMark(Marks mark) {
        // Validate student exists
        if (mark.getStudent() == null || mark.getStudent().getStudentId() == null) {
            throw new IllegalArgumentException("Student ID is required");
        }
        
        // Validate subject exists
        if (mark.getSubject() == null || mark.getSubject().getSubjectId() == null) {
            throw new IllegalArgumentException("Subject ID is required");
        }
        
        // If Student object only has ID, fetch full student
        if (mark.getStudent().getStudentId() != null && mark.getStudent().getName() == null) {
            Optional<Student> student = studentRepository.findById(mark.getStudent().getStudentId());
            if (student.isEmpty()) {
                throw new IllegalArgumentException("Student not found with ID: " + mark.getStudent().getStudentId());
            }
            mark.setStudent(student.get());
        }
        
        // If Subject object only has ID, fetch full subject
        if (mark.getSubject().getSubjectId() != null && mark.getSubject().getSubjectName() == null) {
            Optional<Subject> subject = subjectRepository.findById(mark.getSubject().getSubjectId());
            if (subject.isEmpty()) {
                throw new IllegalArgumentException("Subject not found with ID: " + mark.getSubject().getSubjectId());
            }
            mark.setSubject(subject.get());
        }
        
        if (mark.getMaxMarks() == null) {
            mark.setMaxMarks(100);
        }
        mark.setCreatedAt(LocalDateTime.now());
        mark.setUpdatedAt(LocalDateTime.now());
        mark.setIsRecheckRequested(false);
        
        System.out.println("✓ Saving mark: Student=" + mark.getStudent().getStudentId() + 
                          ", Subject=" + mark.getSubject().getSubjectId() + 
                          ", Marks=" + mark.getMarksObtained());
        
        Marks savedMark = marksRepository.save(mark);
        return convertToDTO(savedMark);
    }
    
    /**
     * Add new mark from request DTO
     */
    public MarksDTO addMarkFromRequest(MarksCreateRequest request) {
        // Fetch student from database
        Optional<Student> student = studentRepository.findById(request.getStudentId());
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Student not found with ID: " + request.getStudentId());
        }
        
        // Fetch subject from database
        Optional<Subject> subject = subjectRepository.findById(request.getSubjectId());
        if (subject.isEmpty()) {
            throw new IllegalArgumentException("Subject not found with ID: " + request.getSubjectId());
        }
        
        // Check if marks already exist for this student-subject combination
        String term = request.getTerm() != null ? request.getTerm() : "Term 1";
        Integer year = request.getYear() != null ? request.getYear() : 2024;
        
        List<Marks> existingMarks = marksRepository.findByStudentAndSubjectAndTermAndYear(
            student.get(),
            subject.get(),
            term,
            year
        );
        
        if (!existingMarks.isEmpty()) {
            System.out.println("⚠️ Marks already exist for Student=" + student.get().getStudentId() + 
                              ", Subject=" + subject.get().getSubjectId() + 
                              ", Term=" + term + ", Year=" + year);
            throw new IllegalArgumentException("Marks already added for " + subject.get().getSubjectName() + ". Cannot add duplicate marks.");
        }
        
        // Create marks entity
        Marks mark = new Marks();
        mark.setStudent(student.get());
        mark.setSubject(subject.get());
        mark.setMarksObtained(request.getMarksObtained());
        mark.setMaxMarks(request.getMaxMarks() != null ? request.getMaxMarks() : 100);
        mark.setTerm(term);
        mark.setYear(year);
        mark.setIsRecheckRequested(request.getIsRecheckRequested() != null ? request.getIsRecheckRequested() : false);
        mark.setCreatedAt(LocalDateTime.now());
        mark.setUpdatedAt(LocalDateTime.now());
        
        System.out.println("✓ Creating new mark: Student=" + student.get().getStudentId() + 
                          ", Subject=" + subject.get().getSubjectId() + 
                          ", Marks=" + mark.getMarksObtained());
        
        Marks savedMark = marksRepository.save(mark);
        return convertToDTO(savedMark);
    }
    public Optional<MarksDTO> updateMark(Long marksId, Marks markDetails) {
        return marksRepository.findById(marksId).map(mark -> {
            mark.setMarksObtained(markDetails.getMarksObtained());
            mark.setMaxMarks(markDetails.getMaxMarks());
            mark.setTerm(markDetails.getTerm());
            mark.setYear(markDetails.getYear());
            mark.setIsRecheckRequested(markDetails.getIsRecheckRequested());
            mark.setUpdatedAt(LocalDateTime.now());
            Marks updatedMark = marksRepository.save(mark);
            return convertToDTO(updatedMark);
        });
    }
    
    /**
     * Delete mark - also deletes associated recheck requests
     */
    public boolean deleteMark(Long marksId) {
        return marksRepository.findById(marksId).map(mark -> {
            try {
                // First, delete all recheck requests associated with this mark
                List<RecheckRequest> recheckRequests = recheckRequestRepository.findByMarksId(marksId);
                if (!recheckRequests.isEmpty()) {
                    System.out.println("🗑️ Deleting " + recheckRequests.size() + " recheck requests for marks " + marksId);
                    recheckRequestRepository.deleteAll(recheckRequests);
                }
                
                // Then delete the mark itself
                System.out.println("🗑️ Deleting mark " + marksId);
                marksRepository.delete(mark);
                System.out.println("✓ Mark " + marksId + " deleted successfully");
                return true;
            } catch (Exception e) {
                System.err.println("❌ Error deleting mark " + marksId + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error deleting mark: " + e.getMessage());
            }
        }).orElse(false);
    }
    
    /**
     * Update recheck status for a mark
     */
    public Optional<MarksDTO> updateRecheckStatus(Long marksId, Boolean recheckRequested) {
        return marksRepository.findById(marksId).map(mark -> {
            mark.setIsRecheckRequested(recheckRequested);
            mark.setUpdatedAt(LocalDateTime.now());
            Marks updatedMark = marksRepository.save(mark);
            return convertToDTO(updatedMark);
        });
    }
    
    /**
     * Calculate total marks for a student
     */
    public Integer calculateTotalMarks(Long studentId) {
        List<Marks> marks = marksRepository.findAllByStudentId(studentId);
        return marks.stream()
                .mapToInt(Marks::getMarksObtained)
                .sum();
    }
    
    /**
     * Calculate percentage for a student
     */
    public Double calculatePercentage(Long studentId) {
        List<Marks> marks = marksRepository.findAllByStudentId(studentId);
        if (marks.isEmpty()) return 0.0;
        
        int totalObtained = marks.stream().mapToInt(Marks::getMarksObtained).sum();
        int totalMaxMarks = marks.stream().mapToInt(Marks::getMaxMarks).sum();
        
        return totalMaxMarks > 0 ? (totalObtained * 100.0) / totalMaxMarks : 0.0;
    }
    
    /**
     * Calculate average marks for a student
     */
    public Double calculateAverageMarks(Long studentId) {
        List<Marks> marks = marksRepository.findAllByStudentId(studentId);
        if (marks.isEmpty()) return 0.0;
        return marks.stream()
                .mapToInt(Marks::getMarksObtained)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Get grade based on percentage
     */
    public String getGrade(Double percentage) {
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }
    
    /**
     * Convert Marks entity to DTO
     */
    private MarksDTO convertToDTO(Marks marks) {
        return new MarksDTO(
            marks.getMarksId(),
            marks.getStudent().getStudentId(),
            marks.getStudent().getName(),
            marks.getSubject().getSubjectName(),
            marks.getMarksObtained(),
            marks.getMaxMarks(),
            marks.getTerm(),
            marks.getYear(),
            marks.getIsRecheckRequested(),
            marks.getCreatedAt(),
            marks.getUpdatedAt()
        );
    }
}
