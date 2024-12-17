package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPage {
    private String nickname;
    private Integer friendCnt;
    private Integer point;
    private String profileImage;

    private MyMatzipInfoDTO wishInfo;
    private MyMatzipInfoDTO matzipInfo;
    private MyReviewInfoDTO reviewInfo;
    private UpdateNickDTO newNickname;
}
