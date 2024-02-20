package com.wittypuppy.backend.approval.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SoftwareUseDTO {
    private Long softwareUseCode;
    private Long approvalDocCode;
    private String softwareTitle;
    private String kindOfSoftware;
    private String softwareReason;
    private LocalDateTime softwareStartDate;
    private String approvalTitle;
}
