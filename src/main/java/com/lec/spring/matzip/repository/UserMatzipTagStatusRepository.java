package com.lec.spring.matzip.repository;


import com.lec.spring.matzip.domain.UserMatzipTagStatus;

import java.util.List;
import java.util.Optional;


public interface UserMatzipTagStatusRepository {

    // 저장
    int tagSave(UserMatzipTagStatus userMatzipTagStatus);
    // myMatzipId를 기준으로 hiddentag불러오기
    List<UserMatzipTagStatus> listWholeHiddenListByMyMatzipId(Long myMatzipId);
    // myMatzipId를 기준으로 중복된 태그 찾기
    List<UserMatzipTagStatus> findDuplicateMyMatzipId(Long myMatzipId);

    List<UserMatzipTagStatus> userMatzipTagStatus();

    List<UserMatzipTagStatus> userMatzipTagStatusByMemberID(Long memberId);


    // memberID기준으로 member가 구매한 태그와 구매하지 않은 태그가져오기 (visiblity = "hidden")이여야함
    List<UserMatzipTagStatus> listpurchasedTagByMemberId(Long memberId);

    List <UserMatzipTagStatus> listWholeHiddenMatizpByMemberId(Long memberId);
//    List<UserMatzipTagStatus> listUnpurchasedTagByMemberId(Long memberId);


} // end HintPurchaseRepository





