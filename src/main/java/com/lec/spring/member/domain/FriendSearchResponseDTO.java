package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendSearchResponseDTO {
    private Long id;
    private String username;
    private String nickname;
    private String profileImg;
    private int publicCount;
    private int hiddenCount;
    private boolean isAlreadyFriend;
}
