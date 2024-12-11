package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private Long id;
    private String username;
    private String password;
    private String re_password;
    private String email;
    private Integer point;
    private String nickname;
    private String name;

    private String referrerNickname;

    private String provider;
    private String providerId;
}