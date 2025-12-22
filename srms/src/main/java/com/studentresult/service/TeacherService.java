package com.studentresult.service;

import com.studentresult.dto.TeacherDTO;
import com.studentresult.entity.Teacher;
import com.studentresult.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    /**
     * Get all teachers
     */
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active teachers
     */
    public List<TeacherDTO> getAllActiveTeachers() {
        return teacherRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get teacher by ID
     */
    public Optional<TeacherDTO> getTeacherById(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .map(this::convertToDTO);
    }
    
    /**
     * Get teacher by email
     */
    public Optional<TeacherDTO> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    /**
     * Search teachers by name or email
     */
    public List<TeacherDTO> searchTeachers(String searchTerm) {
        return teacherRepository.searchTeachers(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get teachers by subject
     */
    public List<TeacherDTO> getTeachersBySubject(String subject) {
        return teacherRepository.findBySubject(subject).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Add new teacher
     * Auto-generates password based on name and phone number
     */
    public TeacherDTO addTeacher(Teacher teacher) {
        // Auto-generate password: first 3 letters of name (uppercase) + last 4 digits of phone
        String generatedPassword = generatePassword(teacher.getName(), teacher.getPhone());
        teacher.setPassword(generatedPassword);
        
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        teacher.setIsActive(true);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDTO(savedTeacher);
    }
    
    /**
     * Generate password based on teacher name and phone number
     * Format: First 3 letters of name (uppercase) + Last 4 digits of phone
     * Example: Name "Sharma" + Phone "9876543210" → "SHA3210"
     */
    private String generatePassword(String name, String phone) {
        // Get first 3 letters of name (uppercase)
        String namePart = name != null && name.length() >= 3 
            ? name.substring(0, 3).toUpperCase() 
            : (name != null ? name.toUpperCase() : "XXX");
        
        // Get last 4 digits of phone
        String phonePart = phone != null && phone.replaceAll("\\D", "").length() >= 4
            ? phone.replaceAll("\\D", "").substring(phone.replaceAll("\\D", "").length() - 4)
            : "0000";
        
        return namePart + phonePart;
    }
    
    /**
     * Update teacher
     */
    public Optional<TeacherDTO> updateTeacher(Long teacherId, Teacher teacherDetails) {
        return teacherRepository.findById(teacherId).map(teacher -> {
            teacher.setName(teacherDetails.getName());
            teacher.setEmail(teacherDetails.getEmail());
            teacher.setPhone(teacherDetails.getPhone());
            teacher.setSubjects(teacherDetails.getSubjects());
            teacher.setExperience(teacherDetails.getExperience());
            teacher.setIsActive(teacherDetails.getIsActive());
            teacher.setUpdatedAt(LocalDateTime.now());
            Teacher updatedTeacher = teacherRepository.save(teacher);
            return convertToDTO(updatedTeacher);
        });
    }
    
    /**
     * Delete teacher (soft delete - sets isActive to false)
     */
    public boolean deleteTeacher(Long teacherId) {
        return teacherRepository.findById(teacherId).map(teacher -> {
            teacher.setIsActive(false);
            teacher.setUpdatedAt(LocalDateTime.now());
            teacherRepository.save(teacher);
            return true;
        }).orElse(false);
    }
    
    /**
     * Permanently delete teacher from database
     */
    public boolean permanentlyDeleteTeacher(Long teacherId) {
        return teacherRepository.findById(teacherId).map(teacher -> {
            teacherRepository.delete(teacher);
            return true;
        }).orElse(false);
    }
    
    /**
     * Convert Teacher entity to DTO
     */
    private TeacherDTO convertToDTO(Teacher teacher) {
        return new TeacherDTO(
            teacher.getTeacherId(),
            teacher.getName(),
            teacher.getEmail(),
            teacher.getPhone(),
            teacher.getPassword(),
            teacher.getSubjects(),
            teacher.getExperience(),
            teacher.getIsActive(),
            teacher.getCreatedAt(),
            teacher.getUpdatedAt()
        );
    }
}
