package com.lec.spring.matzip.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReviewDTO extends Review {
    private Long matzipId;
    private Long memberId;
    private String kindName;
    private List<Long> memberIds;
    private List<Long> tagIds;
}