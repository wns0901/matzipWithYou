package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Matzip {
    Long id;
    String name;
    Long kindId;
    String address;
    Double lat;
    Double lng;
    String imgUrl;
    String gu;
    String kakaoMapUrl;
}
