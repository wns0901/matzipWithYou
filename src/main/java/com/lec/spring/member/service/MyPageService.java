package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;

public interface MyPageService {

    // 닉네임, 친구수, 포인트
    MyPage getMyProfile(MyPage mypage);

    // 위시리스트 & 내맛집 & 내리뷰 개수
    MyPage myActsCount(MyPage mypage);

    // 위시리스트 & 내맛집 & 내리뷰 미리보기
    MyPage getWishContents(MyPage mypage);
    MyPage getMyMatzipContents(MyPage mypage);
    MyPage getMyReviewContents(MyPage mypage);

    // 닉네임 변경
    int updateNick(String nickname);

}