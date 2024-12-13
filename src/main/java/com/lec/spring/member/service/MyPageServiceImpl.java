package com.lec.spring.member.service;

import com.lec.spring.member.domain.*;
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
    public int updateNick(UpdateNickDTO updateNickDTO) {
        return myPageRepository.updateNick(updateNickDTO.getNewNickname());
    }



    @Override
    public MyPage getFullMyPageInfo(Long memberId) {
        MyPage myPage = myPageRepository.getMyPageInfo(memberId);

        // 위시리스트 데이터
        List<MatzipInfoDTO> wishPreview = myPageRepository.getWishPreview(memberId);
        MyMatzipInfoDTO wishInfo = MyMatzipInfoDTO.builder()
                .count(wishPreview.size())
                .preview(wishPreview)
                .build();
        myPage.setWishInfo(wishInfo);

        // 나의 맛집 데이터
        List<MatzipInfoDTO> matzipPreview = myPageRepository.getMyMatzipPreview(memberId);
        MyMatzipInfoDTO matzipInfo = MyMatzipInfoDTO.builder()
                .count(matzipPreview.size())
                .preview(matzipPreview)
                .build();
        myPage.setMatzipInfo(matzipInfo);

        // 나의 리뷰 데이터
        List<ReviewInfoDTO> reviewPreview = myPageRepository.getMyReviewPreview(memberId);
        MyReviewInfoDTO reviewInfo = MyReviewInfoDTO.builder()
                .count(reviewPreview.size())
                .preview(reviewPreview)
                .build();
        myPage.setReviewInfo(reviewInfo);

        return myPage;
    }


}
