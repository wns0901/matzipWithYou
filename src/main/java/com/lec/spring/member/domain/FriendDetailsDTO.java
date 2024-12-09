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
    private int intimacy;
    private int publicCount;
    private int hiddenCount;
    private String profileImg;
}