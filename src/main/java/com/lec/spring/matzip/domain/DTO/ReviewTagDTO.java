package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.ReviewTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReviewTagDTO extends ReviewTag {
    private String tagName;
}
