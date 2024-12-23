package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.domain.Tag;
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
    private Long memberId;
    private String profileImg;
    private String nickname;
    private List<MyMatzipDTO> list;
    private List<Tag> allTagList;
    private List<FoodKind> allKindList;
}
