package com.studentresult.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "recheck_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecheckRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recheckId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marks_id", nullable = false)
    private Marks marks;
    
    @Column(nullable = false, length = 100)
    private String subject;
    
    @Column(length = 500)
    private String reason;
    
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RecheckStatus status;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate;
    
    @Column
    private LocalDateTime resolvedDate;
    
    @Column(length = 500)
    private String adminNotes;
    
    @PrePersist
    protected void onCreate() {
        if (this.requestDate == null) {
            this.requestDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = RecheckStatus.PENDING;
        }
    }
    
    public enum RecheckStatus {
        PENDING, APPROVED, REJECTED
    }
}
