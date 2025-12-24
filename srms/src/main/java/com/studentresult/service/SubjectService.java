package com.studentresult.service;

import com.studentresult.dto.SubjectDTO;
import com.studentresult.entity.Subject;
import com.studentresult.entity.SchoolClass;
import com.studentresult.repository.ClassRepository;
import com.studentresult.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassRepository classRepository;
    
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
        if (className == null || className.trim().isEmpty()) {
            return List.of();
        }

        final String trimmed = className.trim();

        // Try resolve by classNumber first (handles: "10", "Class 10", "class 10")
        Integer classNumber = null;
        try {
            String digits = trimmed.replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) {
                classNumber = Integer.parseInt(digits);
            }
        } catch (Exception ignored) {
            classNumber = null;
        }

        Optional<SchoolClass> classOpt = (classNumber != null)
                ? classRepository.findByClassNumber(classNumber)
                : Optional.empty();

        if (classOpt.isEmpty()) {
            // Fallback: use provided className exactly (frontend sends "Class 10")
            classOpt = classRepository.findByClassName(trimmed);
        }

        if (classOpt.isEmpty()) {
            return List.of();
        }

        String subjectList = classOpt.get().getSubjectList();
        if (subjectList == null || subjectList.trim().isEmpty()) {
            return List.of();
        }

        // Parse comma-separated subject names, preserving order
        List<String> subjectNames = new ArrayList<>();
        for (String raw : subjectList.split(",")) {
            String name = raw == null ? "" : raw.trim();
            if (!name.isEmpty()) {
                subjectNames.add(name);
            }
        }

        if (subjectNames.isEmpty()) {
            return List.of();
        }

        // Build a lookup map of active subjects by lower-cased name
        Map<String, Subject> activeByName = new HashMap<>();
        for (Subject s : subjectRepository.findAllActiveSubjects()) {
            if (s.getSubjectName() != null) {
                activeByName.putIfAbsent(s.getSubjectName().trim().toLowerCase(), s);
            }
        }

        // Map in the same order as in class.subjectList
        List<SubjectDTO> results = new ArrayList<>();
        for (String name : subjectNames) {
            Subject match = activeByName.get(name.toLowerCase());
            if (match != null) {
                results.add(convertToDTO(match));
            } else {
                // If a Subject row is missing, still return the subject name so frontend can display/filter
                results.add(new SubjectDTO(
                        null,
                        name,
                        null,
                        null,
                        true,
                        null,
                        null
                ));
            }
        }

        return results;
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
