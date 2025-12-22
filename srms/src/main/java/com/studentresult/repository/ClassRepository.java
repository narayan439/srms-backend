package com.studentresult.repository;

import com.studentresult.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
    
    /**
     * Find class by class name
     */
    Optional<SchoolClass> findByClassName(String className);
    
    /**
     * Find all active classes
     */
    @Query("SELECT c FROM SchoolClass c WHERE c.isActive = true ORDER BY c.classNumber ASC")
    List<SchoolClass> findAllActiveClasses();
    
    /**
     * Find class by class number
     */
    Optional<SchoolClass> findByClassNumber(Integer classNumber);
}
