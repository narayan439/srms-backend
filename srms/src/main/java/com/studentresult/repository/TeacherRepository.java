package com.studentresult.repository;

import com.studentresult.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByEmail(String email);
    
    @Query("SELECT t FROM Teacher t WHERE LOWER(t.email) = LOWER(:email)")
    Optional<Teacher> findByEmailIgnoreCase(@Param("email") String email);
    
    List<Teacher> findByIsActiveTrue();
    
    @Query("SELECT t FROM Teacher t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Teacher> searchTeachers(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT t FROM Teacher t WHERE t.subjects LIKE CONCAT('%', :subject, '%')")
    List<Teacher> findBySubject(@Param("subject") String subject);
}
