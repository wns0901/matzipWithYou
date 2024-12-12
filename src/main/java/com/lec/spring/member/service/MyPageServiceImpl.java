package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.domain.MyMatzipInfoDTO;
import com.lec.spring.member.domain.MyReviewInfoDTO;
import com.lec.spring.member.repository.MyPageRepository;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MyPageServiceImpl implements MyPageService {
    private final MyPageRepository myPageRepository;

    public MyPageServiceImpl(SqlSession sqlSession) {
        this.myPageRepository = sqlSession.getMapper(MyPageRepository.class);
    }



    // 닉네임 변경
    @Override
    public int updateNick(String nickname) {
        return myPageRepository.updateNick(nickname);
    }

    @Override
    public MyPage getFullMyPageInfo(Long memberId) {
        // 전체 정보 조회
        MyPage myPage = myPageRepository.getMyPageInfo(memberId);

        // 위시리스트 관련 데이터 설정
        List<MyMatzipInfoDTO> wishPreview = myPageRepository.getWishPreview(memberId);
        MyMatzipInfoDTO wishInfo = MyMatzipInfoDTO.builder()
                .wishCnt(myPage.getWishCnt())
                .wishPreview(wishPreview)
                .build();

        // 내 맛집 관련 데이터 설정
        List<MyMatzipInfoDTO> matzipPreview = myPageRepository.getMyMatzipPreview(memberId);
        MyMatzipInfoDTO matzipInfo = MyMatzipInfoDTO.builder()
                .myMatzipCnt(myPage.getMyMatzipCnt())
                .matzipPreview(matzipPreview)
                .build();

        // 리뷰 관련 데이터 설정
        List<MyReviewInfoDTO> reviewPreview = myPageRepository.getMyReviewPreview(memberId);
        MyReviewInfoDTO reviewInfo = MyReviewInfoDTO.builder()
                .myReviewCnt(myPage.getMyReviewCnt())
                .reviewPreview(reviewPreview)
                .build();

        // MyPage 객체에 추가 데이터 설정
        myPage.setWishInfo(wishInfo);
        myPage.setMatzipInfo(matzipInfo);
        myPage.setReviewInfo(reviewInfo);

        return myPage;
    }


}

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
