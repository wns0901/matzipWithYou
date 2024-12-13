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

    // 닉네임 수정
    int updateNick(String nickname);

}
