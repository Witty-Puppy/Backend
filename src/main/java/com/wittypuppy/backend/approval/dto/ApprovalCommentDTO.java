package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalCommentDTO {
    private Long approvalCommentCode;
    private ApprovalDocDTO approvalDocDTO;
    private EmployeeDTO employeeDTO;
    private String approvalCommentContent;
    private LocalDateTime approvalCommentDate;
}