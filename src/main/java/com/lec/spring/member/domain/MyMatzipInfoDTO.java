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
public class MyMatzipInfoDTO {
    // 위시리스트
    private Integer wishCnt;
    private List<MyMatzipInfoDTO> wishPreview;

    // 나의 맛집
    private Integer myMatzipCnt;
    private List<MyMatzipInfoDTO> matzipPreview;

    private String matzipName;
    private String matzipImage;
}
