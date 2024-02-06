package com.wittypuppy.backend.board.dto;

import com.wittypuppy.backend.common.entity.PostAlert;
import com.wittypuppy.backend.common.entity.PostComment;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardMemberDTO {

    private Long boardMemberCode;

    private Long employeeCode;

    private Long boardCode;

    private String createPostStatus;

//    private List<PostCommentDTO> postCommentList;

//    private List<PostAlertDTO> postAlertList;

}
