package com.lec.spring.matzip.domain;

import com.lec.spring.matzip.domain.DTO.MatzipDTO;
import lombok.AllArgsConstructor;
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

    public Matzip(MatzipDTO other) {
        this.id = other.getId();
        this.name = other.getName();
        this.kindId = other.getKindId();
        this.address = other.getAddress();
        this.lat = other.getLat();
        this.lng = other.getLng();
        this.imgUrl = other.getImgUrl();
        this.gu = other.getGu();
        this.kakaoMapUrl = other.getKakaoMapUrl();
        this.regdate = other.getRegdate();
    }
}