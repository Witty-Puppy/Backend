package com.wittypuppy.backend.approval.entity;

import com.wittypuppy.backend.calendar.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "APPROVAL_REPRESENT")
@Table(name = "tbl_approval_represent")
public class ApprovalRepresent {
    @Id
    @Column(name = "approval_represent_code")
    private Long approvalRepresentCode;

    @JoinColumn(name = "approval_document_code")
    @ManyToOne
    private ApprovalDoc approvalDoc;

    @JoinColumn(name = "assignee_code")
    @ManyToOne
    private Employee representativeCode;

    @JoinColumn(name = "representative_code")
    @ManyToOne
    private Employee assigneeCode;
}
