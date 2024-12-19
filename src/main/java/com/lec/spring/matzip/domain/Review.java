package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Review {
    private Long id;
    private String content;
    private LocalDateTime regdate;
    private int starRating;

    private Long matzipId;
    private Long memberId;
}
