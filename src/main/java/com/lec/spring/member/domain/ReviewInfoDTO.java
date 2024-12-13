package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewInfoDTO {
    private String matzipName;
    private String matzipImage;
    private String reviewContent;
    private Integer starRating;
}