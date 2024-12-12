package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
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
    // sorry...
    private String tagName;//(지윤)
    private String kindName;
}