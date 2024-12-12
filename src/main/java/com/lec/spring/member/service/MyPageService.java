package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;

import java.util.List;

public interface MyPageService {

    // 닉네임, 친구수, 포인트
//    MyPage getMyProfile(MyPage mypage);
//
//    // 위시리스트 & 내맛집 & 내리뷰 개수
//    MyPage wishlistCnt(MyPage mypage);
//    MyPage myMatzipCnt(MyPage mypage);
//    MyPage myReviewCnt(MyPage mypage);
//
//    // 위시리스트 & 내맛집 & 내리뷰 미리보기
//    MyPage getWish(MyPage mypage);
//    MyPage getMyMatzip(MyPage mypage);
//    MyPage getMyReview(MyPage mypage);

    // 닉네임 변경
    int updateNick(String nickname);

    MyPage getFullMyPageInfo(Long memberId);


}