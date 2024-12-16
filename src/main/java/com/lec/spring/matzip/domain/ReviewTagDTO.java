package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReviewTagDTO extends ReviewTag {
    private Long tagId;
    private String tagName;
    private Long reviewId;

    public ReviewTagDTO(Long id, String tagName) {
        this.tagName = tagName;
        this.tagId = id;
        this.reviewId = id;
    }

    public Long getTagId() { return tagId; }
    public String getName() { return tagName; }
}
