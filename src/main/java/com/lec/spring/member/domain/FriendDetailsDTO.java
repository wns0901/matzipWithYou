package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendDetailsDTO {
    private String nickname;
    private String username;
    private Integer intimacy;
    private Integer publicCount;
    private Integer hiddenCount;
    private String profileImg;

    private Long friendId;
}