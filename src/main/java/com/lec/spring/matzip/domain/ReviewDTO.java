package com.lec.spring.matzip.domain;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO extends Review {
    private Long matzipId;
    private Long memberId;
    private List<Long> tagIds;
}