package com.wittypuppy.backend.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectDTO {
    private String projectTitle;
    private String projectDescription;
    private LocalDateTime projectDeadline;
    private String projectLockedStatus;
}
