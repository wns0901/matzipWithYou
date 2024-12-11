package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPage {
    private String nickname;
    private Integer friendCnt;
    private Integer points;

    private Integer wishlistCnt;
    private Integer myMatzipCnt;
    private Integer myReviewCnt;

    // 미리보기 리스트
    private List<WishItem> wishList;
    private List<MyMatzipItem> myMatzipList;
    private List<MyReviewItem> myReviewList;

    @Data
    public static class WishItem {
        private String name;
        private String imgUrl;
    }

    @Data
    public static class MyMatzipItem {
        private String name;
        private String imgUrl;
    }

    @Data
    public static class MyReviewItem {
        private Long memberId;
        private double starRating;
        private String matzipName;
        private String content;
    }
}
