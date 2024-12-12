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
public class MyMatzip {
    private Long id;
    private Long matzipId;
    private Long memberId;
    private LocalDateTime regdate;
    private String visibility;
    private String content;
    private Integer starRating;
}




