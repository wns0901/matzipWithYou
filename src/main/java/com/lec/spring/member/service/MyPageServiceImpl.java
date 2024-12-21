package com.lec.spring.member.service;

import com.lec.spring.member.domain.*;
import com.lec.spring.member.repository.MyPageRepository;
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
    public int updateNick(Long memberId, UpdateNickDTO updateNickDTO) {
        String newNickname = updateNickDTO.getNewNickname();

        // 1. 닉네임 형식 검증 (20자 이하, 숫자/영어/한글만 허용)
        if (!newNickname.matches("^[a-zA-Z0-9가-힣\\s]{1,20}$")) {
            throw new IllegalArgumentException("닉네임은 20자 이하의 숫자, 영어, 한글만 허용됩니다.");
        }

        // 2. 닉네임 중복 확인
        boolean nicknameExists = myPageRepository.existsByNickname(newNickname);
        if (nicknameExists) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 3. 현재 포인트 확인
        int currentPoint = myPageRepository.getCurrentPoint(memberId);
        if (currentPoint < 5000) {
            throw new IllegalArgumentException("포인트가 부족합니다. (소지 포인트: " + currentPoint +"pt)");
        }

        // 포인트 차감 및 닉네임 업데이트
        int rowsUpdated = myPageRepository.updateNicknameAndDeductPoints(memberId, newNickname, 5000);
        if (rowsUpdated <= 0) {
            throw new RuntimeException("닉네임 변경에 실패했습니다.");
        }

        return rowsUpdated;
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
