package com.studentresult.repository;

import com.studentresult.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByEmail(String email);
    
    Optional<Student> findByRollNo(String rollNo);
    
    List<Student> findByClassName(String className);
    
    List<Student> findByIsActiveTrue();
    
    @Query("SELECT s FROM Student s WHERE s.className = :className AND s.isActive = true")
    List<Student> findActiveStudentsByClass(@Param("className") String className);
    
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Student> searchStudents(@Param("searchTerm") String searchTerm);
}
