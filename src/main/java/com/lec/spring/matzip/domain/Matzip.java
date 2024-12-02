package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Matzip {
    private Long id;
    private String name;
    private Long kindId;
    private String address;
    private Double lat;
    private Double lng;
    private String imgUrl;
    private String gu;
    private String kakaoMapUrl;
    private LocalDateTime regdate;
}
