package com.lec.spring.matzip.domain;

import com.lec.spring.member.domain.Member;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMatzipTagStatus {
        private Long myMatzipId;// 맛집 태그 일련 번호(가게)
        private Long memberId; // 회원 일련번호
        private Long tagId; // 태그 일련번호
        // Tag 에서 가져오기
        private String tagName;
        //kind id
        private String kindName;

        //wholeHiddenList
        private Long matzipId;
        private String visibility;
        private Long id;


}// end hintPurchase


