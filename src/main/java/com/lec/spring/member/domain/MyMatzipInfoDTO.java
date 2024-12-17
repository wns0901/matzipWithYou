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
    // 나의 맛집, 위시리스트 개수
    private Integer count;

    // 미리보기 리스트
    private List<MatzipInfoDTO> preview;

}