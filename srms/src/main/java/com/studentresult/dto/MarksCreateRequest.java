package com.studentresult.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksCreateRequest {
    private Long studentId;
    private Long subjectId;
    private Integer marksObtained;
    private Integer maxMarks;
    private String term;
    private Integer year;
    private Boolean isRecheckRequested;
}
