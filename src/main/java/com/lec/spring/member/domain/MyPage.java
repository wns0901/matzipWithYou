package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPage {


    private String newNickname;

    private String nickname;
    private Integer friendCnt;
    private Integer point;
    private String profileImage;

    private Integer wishCnt;
    private Integer myMatzipCnt;
    private Integer myReviewCnt;

    private MyMatzipInfoDTO wishInfo;
    private MyMatzipInfoDTO matzipInfo;
    private MyReviewInfoDTO reviewInfo;
}
