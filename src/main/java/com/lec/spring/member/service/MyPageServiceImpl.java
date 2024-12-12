package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.repository.MyPageRepository;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MyPageServiceImpl implements MyPageService {
    private MyPageRepository myPageRepository;

    public MyPageServiceImpl(SqlSession sqlSession) {
        this.myPageRepository = sqlSession.getMapper(MyPageRepository.class);
    }
//
//    // 닉네임, 친구수, 포인트
//    @Override
//    public MyPage getMyProfile(MyPage mypage) {
//        return myPageRepository.myProfile(mypage);
//    }
//
//    // 위시리스트 & 내맛집 & 내리뷰 개수
//    @Override
//    public MyPage wishlistCnt(MyPage mypage) {
//        return myPageRepository.wishlistCnt(mypage);
//    }
//    @Override
//    public MyPage myMatzipCnt(MyPage mypage) {
//        return myPageRepository.myMatzipCnt(mypage);
//    }
//    @Override
//    public MyPage myReviewCnt(MyPage mypage) {
//        return myPageRepository.myReviewCnt(mypage);
//    }
//
//    // 위시 미리보기
//    @Override
//    public MyPage getWishContents(MyPage mypage) {
//        MyPage wishPreview = myPageRepository.wishContents(mypage);
//        mypage.setWishPreview();
//        return mypage;
//    }
//
//    // 내맛집 미리보기
//    @Override
//    public MyPage getMyMatzipContents(MyPage mypage) {
//        MyPage matzipPreview = myPageRepository.myMatzipContents(mypage);
//        mypage.setMatzipPreview();
//        return mypage;
//    }
//
//    // 내리뷰 미리보기
//    @Override
//    public MyPage getMyReviewContents(MyPage mypage) {
//        MyPage reviewPreview = myPageRepository.myReviewContents(mypage);
//        mypage.setReviewPreview();
//        return mypage;
//    }



    // 닉네임 변경
    @Override
    public int updateNick(String nickname) {
        return myPageRepository.updateNick(nickname);
    }

    @Override
    public MyPage getFullMyPageInfo(Long memberId) {
        // 전체 정보 조회
        MyPage response = myPageRepository.getMyPageInfo(memberId);

        // 위시리스트 미리보기 추가
        response.getWish().setMatzipInfos(
                myPageRepository.getWishPreview(memberId)
        );

        // 내 맛집 미리보기 추가
        response.getMyMatzip().setMatzipInfos(
                myPageRepository.getMyMatzipPreview(memberId)
        );

        // 내 리뷰 미리보기 추가
        response.getMyReview().setReviewInfos(
                myPageRepository.getMyReviewPreview(memberId)
        );

        return response;
    }




}
