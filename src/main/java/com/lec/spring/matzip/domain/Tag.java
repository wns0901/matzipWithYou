package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    public Long id;
    public String tagName;
    public LocalDateTime regdate;
}
