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
public class MyReviewInfoDTO {
    // 나의 리뷰
    private Integer myReviewCnt;
    private List<MyReviewInfoDTO> reviewPreview;

    private String matzipName;
    private String matzipImage;
    private String reviewContent;
    private Integer starRating;
}
