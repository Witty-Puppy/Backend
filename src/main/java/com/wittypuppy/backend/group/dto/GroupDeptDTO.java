package com.wittypuppy.backend.group.dto;

import com.wittypuppy.backend.group.entity.GroupEmp;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class GroupDeptDTO {

    private Long departmentCode;

    private String departmentName;

//    private Long parentDeptCode;
//
//    private List<GroupEmp> employee;

}

