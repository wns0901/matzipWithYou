package com.lec.spring.matzip.domain;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoPlaceDTO {
    private MatzipDTO kakao;
    private String kind;
}