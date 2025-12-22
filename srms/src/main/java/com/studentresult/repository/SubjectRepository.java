package com.studentresult.repository;

import com.studentresult.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    Optional<Subject> findBySubjectName(String subjectName);
    
    Optional<Subject> findBySubjectNameIgnoreCase(String subjectName);
    
    Optional<Subject> findByCode(String code);
    
    @Query("SELECT s FROM Subject s WHERE s.isActive = true")
    List<Subject> findAllActiveSubjects();
    
    @Query("SELECT s FROM Subject s WHERE s.subjectName LIKE %:searchTerm% OR s.code LIKE %:searchTerm%")
    List<Subject> searchSubjects(@Param("searchTerm") String searchTerm);
}
