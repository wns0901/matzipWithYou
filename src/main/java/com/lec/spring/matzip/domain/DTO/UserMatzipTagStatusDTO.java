package com.lec.spring.matzip.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMatzipTagStatusDTO {
    private Long myMatzipId;// 맛집 태그 일련 번호(가게)
    private Long memberId; // 회원 일련번호
    private Long tagId;
}
