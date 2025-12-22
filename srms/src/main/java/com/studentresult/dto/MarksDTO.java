package com.studentresult.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksDTO {
    private Long marksId;
    private Long studentId;
    private String studentName;
    private String subject;
    private Integer marksObtained;
    private Integer maxMarks;
    private String term;
    private Integer year;
    private Boolean isRecheckRequested;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
