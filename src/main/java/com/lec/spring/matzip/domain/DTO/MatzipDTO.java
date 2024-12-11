package com.lec.spring.matzip.domain.DTO;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatzipDTO {
    private MatzipDataDTO data;
    private String kind;
}