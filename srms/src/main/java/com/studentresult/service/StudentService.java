package com.studentresult.service;

import com.studentresult.dto.StudentDTO;
import com.studentresult.entity.Student;
import com.studentresult.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all students
     */
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active students
     */
    public List<StudentDTO> getAllActiveStudents() {
        return studentRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get student by ID
     */
    public Optional<StudentDTO> getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(this::convertToDTO);
    }
    
    /**
     * Get students by class
     */
    public List<StudentDTO> getStudentsByClass(String className) {
        return studentRepository.findActiveStudentsByClass(className).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get student by email
     */
    public Optional<StudentDTO> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    /**
     * Get student by roll number
     */
    public Optional<StudentDTO> getStudentByRollNo(String rollNo) {
        return studentRepository.findByRollNo(rollNo)
                .map(this::convertToDTO);
    }
    
    /**
     * Search students by name or email
     */
    public List<StudentDTO> searchStudents(String searchTerm) {
        return studentRepository.searchStudents(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Add new student
     */
    public StudentDTO addStudent(Student student) {
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        student.setIsActive(true);
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }
    
    /**
     * Update student
     */
    public Optional<StudentDTO> updateStudent(Long studentId, Student studentDetails) {
        return studentRepository.findById(studentId).map(student -> {
            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            student.setClassName(studentDetails.getClassName());
            student.setRollNo(studentDetails.getRollNo());
            student.setPhone(studentDetails.getPhone());
            student.setDob(studentDetails.getDob());
            student.setIsActive(studentDetails.getIsActive());
            student.setUpdatedAt(LocalDateTime.now());
            Student updatedStudent = studentRepository.save(student);
            return convertToDTO(updatedStudent);
        });
    }
    
    /**
     * Delete student (soft delete - sets isActive to false)
     */
    public boolean deleteStudent(Long studentId) {
        return studentRepository.findById(studentId).map(student -> {
            student.setIsActive(false);
            student.setUpdatedAt(LocalDateTime.now());
            studentRepository.save(student);
            return true;
        }).orElse(false);
    }
    
    /**
     * Permanently delete student from database
     */
    public boolean permanentlyDeleteStudent(Long studentId) {
        return studentRepository.findById(studentId).map(student -> {
            studentRepository.delete(student);
            return true;
        }).orElse(false);
    }
    
    /**
     * Convert Student entity to DTO
     */
    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(
            student.getStudentId(),
            student.getName(),
            student.getEmail(),
            student.getClassName(),
            student.getRollNo(),
            student.getPhone(),
            student.getDob(),
            student.getIsActive(),
            student.getCreatedAt(),
            student.getUpdatedAt()
        );
    }
}
