package com.lec.spring.member.repository;

import com.lec.spring.member.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPageRepository {

    // 닉, 친구수, 포인트 + 위시 & 내맛집 & 내리뷰 count
    MyPage getMyPageInfo(Long memberId);

    // 위시 & 내맛집 & 내리뷰 미리보기
    List<MatzipInfoDTO> getWishPreview(Long memberId);
    List<MatzipInfoDTO> getMyMatzipPreview(Long memberId);
    List<ReviewInfoDTO> getMyReviewPreview(Long memberId);

    // 닉네임 중복 확인
    boolean existsByNickname(String newNickname);

    // 현재 소지 포인트
    int getCurrentPoint(Long memberId);

    // 닉 업데이트 및 포인트 차감
    int updateNicknameAndDeductPoints(Long memberId, String newNickname, int pointsToDeduct);

    //힌트 구매시 포인트차감
    int deductPoints(Long memberId, int pointsToDeduct);




}
