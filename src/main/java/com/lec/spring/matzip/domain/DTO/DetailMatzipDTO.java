package com.lec.spring.matzip.domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lec.spring.matzip.domain.Matzip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DetailMatzipDTO extends Matzip {
    private int starRating;
    private List<String> tagList;

    @JsonIgnore
    private Long kindId;
    @JsonIgnore
    private String imgUrl;
    @JsonIgnore
    private String tagName;
    @JsonIgnore
    private Double lat;
    @JsonIgnore
    private Double lng;
    @JsonIgnore
    private String gu;
    @JsonIgnore
    private LocalDateTime regdate;
}
