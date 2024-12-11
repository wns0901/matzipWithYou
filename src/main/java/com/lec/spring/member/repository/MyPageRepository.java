package com.lec.spring.member.repository;

import com.lec.spring.member.domain.MyPage;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPageRepository {

    // 닉, 친구수, 포인트
    MyPage myProfile(MyPage mypage);

    // 위시리스트 & 내맛집 & 내리뷰 개수
    MyPage myActsCount(MyPage mypage);

    // 위시 미리보기
    String wishContents(MyPage mypage);

    // 내맛집 미리보기
    String myMatzipContents(MyPage mypage);

    // 내리뷰 미리보기
    String myReviewContents(MyPage mypage);

    // 닉네임 수정
    int updateNick(String nickname);

}
