package com.wittypuppy.backend.mypage.dto;

import com.wittypuppy.backend.group.entity.GroupDept;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyPageUpdateDTO {

//    @NotNull(message = "이 컬럼에 데이터 무조건 들어와야됨")
    private Long empCode;

    private String empName;

    private GroupDept department;

    private String empEmail;

    private String phone;

    private Date retirementDate;

    private Date empBirth;

    private String address;


}