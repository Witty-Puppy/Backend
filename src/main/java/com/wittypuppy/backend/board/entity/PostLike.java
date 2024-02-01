package com.wittypuppy.backend.board.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "BOARD_POST_LIKE")
@Table(name = "tbl_post_like")
public class PostLike {
    @Id
    @Column(name = "post_like_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeCode;

    @Column(name = "post_code", columnDefinition = "BIGINT")
    private Long postCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;
}
