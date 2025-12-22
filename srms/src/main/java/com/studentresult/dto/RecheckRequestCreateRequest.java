package com.studentresult.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a recheck request
 * Accepts data from frontend with IDs and fetches related entities
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecheckRequestCreateRequest {
    private Long studentId;
    private Long marksId;
    private String studentEmail;
    private String studentName;
    private String rollNo;
    private String subject;
    private String reason;
    private Integer marksObtained;
    private Integer maxMarks;
    private String status;
    private String requestDate;
    private String adminNotes;
}
