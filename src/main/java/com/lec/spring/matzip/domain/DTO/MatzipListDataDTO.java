package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.Matzip;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MatzipListDataDTO {
    private Long id;
    private String name;
    private String address;
    private Double lat;
    private Double lng;
    private String imgUrl;
    private String visibility;
    private String regdate;
}
