package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.Review;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReviewDTO extends Review {
    private String kindName;
    private Long foodKindId;
    private List<Long> memberIds;
    private List<Long> tagIds;
    private Long memberId;
    private Long matzipId;
}