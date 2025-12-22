package com.studentresult.repository;

import com.studentresult.entity.RecheckRequest;
import com.studentresult.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecheckRequestRepository extends JpaRepository<RecheckRequest, Long> {
    
    List<RecheckRequest> findByStudent(Student student);
    
    @Query("SELECT r FROM RecheckRequest r WHERE r.student.studentId = :studentId")
    List<RecheckRequest> findByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT r FROM RecheckRequest r WHERE r.status = :status")
    List<RecheckRequest> findByStatus(@Param("status") RecheckRequest.RecheckStatus status);
    
    List<RecheckRequest> findByStatusOrderByRequestDateDesc(RecheckRequest.RecheckStatus status);
    
    @Query("SELECT r FROM RecheckRequest r WHERE r.marks.marksId = :marksId")
    List<RecheckRequest> findByMarksId(@Param("marksId") Long marksId);
}
