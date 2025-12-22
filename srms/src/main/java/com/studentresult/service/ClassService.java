package com.studentresult.service;

import com.studentresult.entity.SchoolClass;
import com.studentresult.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClassService {
    
    @Autowired
    private ClassRepository classRepository;
    
    /**
     * Get all classes
     */
    public List<SchoolClass> getAllClasses() {
        return classRepository.findAll();
    }
    
    /**
     * Get all active classes
     */
    public List<SchoolClass> getAllActiveClasses() {
        return classRepository.findAllActiveClasses();
    }
    
    /**
     * Get class by ID
     */
    public Optional<SchoolClass> getClassById(Long classId) {
        return classRepository.findById(classId);
    }
    
    /**
     * Get class by class name
     */
    public Optional<SchoolClass> getClassByName(String className) {
        return classRepository.findByClassName(className);
    }
    
    /**
     * Get class by class number
     */
    public Optional<SchoolClass> getClassByNumber(Integer classNumber) {
        return classRepository.findByClassNumber(classNumber);
    }
    
    /**
     * Create new class
     */
    public SchoolClass createClass(SchoolClass schoolClass) {
        schoolClass.setCreatedAt(LocalDateTime.now());
        schoolClass.setUpdatedAt(LocalDateTime.now());
        schoolClass.setIsActive(true);
        return classRepository.save(schoolClass);
    }
    
    /**
     * Update class
     */
    public SchoolClass updateClass(Long classId, SchoolClass schoolClass) {
        Optional<SchoolClass> existingClass = classRepository.findById(classId);
        if (existingClass.isPresent()) {
            SchoolClass existing = existingClass.get();
            existing.setClassName(schoolClass.getClassName());
            existing.setClassNumber(schoolClass.getClassNumber());
            existing.setMaxCapacity(schoolClass.getMaxCapacity());
            existing.setIsActive(schoolClass.getIsActive());
            existing.setSubjectList(schoolClass.getSubjectList());
            existing.setUpdatedAt(LocalDateTime.now());
            return classRepository.save(existing);
        }
        return null;
    }
    
    /**
     * Delete class (soft delete)
     */
    public boolean deleteClass(Long classId) {
        Optional<SchoolClass> schoolClass = classRepository.findById(classId);
        if (schoolClass.isPresent()) {
            SchoolClass existing = schoolClass.get();
            existing.setIsActive(false);
            existing.setUpdatedAt(LocalDateTime.now());
            classRepository.save(existing);
            return true;
        }
        return false;
    }
    
    /**
     * Delete class permanently (hard delete)
     */
    public boolean deleteClassPermanently(Long classId) {
        if (classRepository.existsById(classId)) {
            classRepository.deleteById(classId);
            return true;
        }
        return false;
    }
}
