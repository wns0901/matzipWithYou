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
    public Long id;
    public String name;
    public Long kindId;
    public String address;
    public Double lat;
    public Double lng;
    public String imgUrl;
    public String gu;
    public String kakaoMapUrl;
    public LocalDateTime regdate;
}
