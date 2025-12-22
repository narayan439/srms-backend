package com.studentresult.service;

import com.studentresult.dto.SubjectDTO;
import com.studentresult.entity.Subject;
import com.studentresult.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    /**
     * Get all subjects
     */
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active subjects
     */
    public List<SubjectDTO> getAllActiveSubjects() {
        return subjectRepository.findAllActiveSubjects().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get subject by ID
     */
    public Optional<SubjectDTO> getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .map(this::convertToDTO);
    }
    
    /**
     * Get subject by name
     */
    public Optional<SubjectDTO> getSubjectByName(String subjectName) {
        return subjectRepository.findBySubjectName(subjectName)
                .map(this::convertToDTO);
    }
    
    /**
     * Get subject by code
     */
    public Optional<SubjectDTO> getSubjectByCode(String code) {
        return subjectRepository.findByCode(code)
                .map(this::convertToDTO);
    }
    
    /**
     * Add new subject
     */
    public SubjectDTO addSubject(Subject subject) {
        subject.setCreatedAt(LocalDateTime.now());
        subject.setUpdatedAt(LocalDateTime.now());
        subject.setIsActive(true);
        Subject savedSubject = subjectRepository.save(subject);
        return convertToDTO(savedSubject);
    }
    
    /**
     * Update subject
     */
    public Optional<SubjectDTO> updateSubject(Long subjectId, Subject subjectDetails) {
        return subjectRepository.findById(subjectId).map(subject -> {
            subject.setSubjectName(subjectDetails.getSubjectName());
            subject.setDescription(subjectDetails.getDescription());
            subject.setCode(subjectDetails.getCode());
            subject.setIsActive(subjectDetails.getIsActive());
            subject.setUpdatedAt(LocalDateTime.now());
            Subject updatedSubject = subjectRepository.save(subject);
            return convertToDTO(updatedSubject);
        });
    }
    
    /**
     * Delete subject (soft delete)
     */
    public boolean deleteSubject(Long subjectId) {
        return subjectRepository.findById(subjectId).map(subject -> {
            subject.setIsActive(false);
            subject.setUpdatedAt(LocalDateTime.now());
            subjectRepository.save(subject);
            return true;
        }).orElse(false);
    }
    
    /**
     * Search subjects
     */
    public List<SubjectDTO> searchSubjects(String searchTerm) {
        return subjectRepository.searchSubjects(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get subjects by class name
     */
    public List<SubjectDTO> getSubjectsByClass(String className) {
        // Remove 'Class' prefix if present and handle different formats
        String classNum = className.replace("Class ", "").trim();
        
        // Query subjects for this class
        // For now, return all active subjects (backend may need to store class mapping)
        return subjectRepository.findAllActiveSubjects().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Subject entity to DTO
     */
    private SubjectDTO convertToDTO(Subject subject) {
        return new SubjectDTO(
            subject.getSubjectId(),
            subject.getSubjectName(),
            subject.getDescription(),
            subject.getCode(),
            subject.getIsActive(),
            subject.getCreatedAt(),
            subject.getUpdatedAt()
        );
    }
}
