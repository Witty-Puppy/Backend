package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.calendar.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_LINE")
@Table(name = "tbl_approval_line")
public class ApprovalLine {
    @Id
    @Column(name = "approval_line_code")
    private Long approvalLineCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "approval_process_order")
    private Long approvalProcessOrder;

    @Column(name = "approval_process_status")
    private String ApprovalProcessStatus;

    @Column(name = "approval_process_date")
    private LocalDateTime approvalProcessDate;

    @Column(name = "approval_rejected_reason")
    private String approvalRejectedReason;
}