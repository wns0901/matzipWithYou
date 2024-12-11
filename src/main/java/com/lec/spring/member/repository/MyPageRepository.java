package com.lec.spring.member.repository;

import com.lec.spring.member.domain.MyPage;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPageRepository {

    // 닉, 친구수, 포인트
    MyPage myProfile();

    // 위시리스트 & 내맛집 & 내리뷰 개수
    int myActsCount(MyPage mypage);

    // 위시 미리보기
    String wishContents();

    // 내맛집 미리보기
    String myMatzipContents();

    // 내리뷰 미리보기
    String myReviewContents();

    // 닉네임 수정
    int updateNick();




}
