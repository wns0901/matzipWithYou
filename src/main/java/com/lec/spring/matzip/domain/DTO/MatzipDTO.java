package com.lec.spring.matzip.domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lec.spring.matzip.domain.Matzip;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatzipDTO extends Matzip {
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