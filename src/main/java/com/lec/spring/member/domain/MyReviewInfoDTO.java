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

    // 나의 리뷰 개수와 미리보기
    private Integer count;
    private List<ReviewInfoDTO> preview;

}
