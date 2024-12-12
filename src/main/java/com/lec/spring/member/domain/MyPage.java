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

//    private String nickname;
//    private Integer friendCnt;
//    private Integer points;

//    private Integer wishlistCnt;
//    private Integer myMatzipCnt;
//    private Integer myReviewCnt;
//
//    private Integer wish;
//    private Integer myMatzip;
//    private Integer myReview;

    private MyInfo myInfo;
    private WishSection wish;
    private MatzipSection myMatzip;
    private ReviewSection myReview;

    @Data
    public static class MyInfo {
        private String nickname;
        private int friendCount;
        private int point;
        private String profileImage;
    }

    @Data
    public static class WishSection {
        private int count;
        private List<MatzipInfo> matzipInfos;
    }

    @Data
    public static class MatzipSection {
        private int count;
        private List<MatzipInfo> matzipInfos;
    }

    @Data
    public static class ReviewSection {
        private int count;
        private List<ReviewInfo> reviewInfos;
    }

    @Data
    public static class MatzipInfo {
        private String matzipName;
        private String matzipImage;
    }

    @Data
    public static class ReviewInfo {
        private String matzipName;
        private String matzipImage;
        private String reviewContent;
        private int starRating;
    }

}
