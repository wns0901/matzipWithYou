package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestDTO {
    private Long senderId;
    private Long receiverId;
    private String nickname;
    private String username;
    private Integer intimacy;
    private String profileImg;
    private Integer publicCount;
    private Integer hiddenCount;
    private LocalDateTime regdate;
}