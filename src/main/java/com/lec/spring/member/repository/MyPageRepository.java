package com.lec.spring.member.repository;

import com.lec.spring.member.domain.MyPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPageRepository {

    // 닉, 친구수, 포인트
    MyPage getMyPageInfo(Long memberId);

//    // 위시리스트 & 내맛집 & 내리뷰 개수
//    MyPage wishlistCnt(MyPage mypage);
//    MyPage myMatzipCnt(MyPage mypage);
//    MyPage myReviewCnt(MyPage mypage);

    // 위시 & 내맛집 & 내리뷰 미리보기
    List<MyPage.MatzipInfo> getWishPreview(Long memberId);
    List<MyPage.MatzipInfo> getMyMatzipPreview(Long memberId);
    List<MyPage.ReviewInfo> getMyReviewPreview(Long memberId);

    // 닉네임 수정
    int updateNick(String nickname);

}
