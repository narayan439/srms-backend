package com.studentresult.repository;

import com.studentresult.entity.Marks;
import com.studentresult.entity.Student;
import com.studentresult.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    
    List<Marks> findByStudent(Student student);
    
    List<Marks> findByStudentAndSubject(Student student, Subject subject);
    
    @Query("SELECT m FROM Marks m WHERE m.student.className = :className")
    List<Marks> findByClassName(@Param("className") String className);
    
    @Query("SELECT m FROM Marks m WHERE m.student.studentId = :studentId AND m.term = :term AND m.year = :year")
    List<Marks> findByStudentIdAndTermAndYear(
        @Param("studentId") Long studentId,
        @Param("term") String term,
        @Param("year") Integer year
    );
    
    @Query("SELECT m FROM Marks m WHERE m.student.studentId = :studentId")
    List<Marks> findAllByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT m FROM Marks m WHERE m.student = :student AND m.subject = :subject AND m.term = :term AND m.year = :year")
    List<Marks> findByStudentAndSubjectAndTermAndYear(
        @Param("student") Student student,
        @Param("subject") Subject subject,
        @Param("term") String term,
        @Param("year") Integer year
    );
}
