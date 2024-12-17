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


} // end HintPurchaseRepository





