package com.studentresult.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecheckRequestDTO {
    private Long recheckId;
    private Long studentId;
    private String studentName;
    private Long marksId;
    private String subject;
    private String reason;
    private String status;
    private LocalDateTime requestDate;
    private LocalDateTime resolvedDate;
    private String adminNotes;
}
