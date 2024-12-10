package com.lec.spring.matzip.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindingResultMyMatzipDTO {
    private int cnt;
    private List<MyMatzipDTO> list;
}
