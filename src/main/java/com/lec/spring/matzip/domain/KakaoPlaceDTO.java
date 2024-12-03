package com.lec.spring.matzip.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;



@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
class MatzipDTO extends Matzip{
    @JsonProperty("road_address_name")
    private String address;

    @JsonProperty("place_name")
    private String name;

    @JsonProperty("x")
    private Double lng;

    @JsonProperty("y")
    private Double lat;

    @JsonProperty("place_url")
    private String kakaoMapUrl;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoPlaceDTO {
    private MatzipDTO kakao;
    private String kind;
}