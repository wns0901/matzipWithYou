package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;

import java.util.List;
import java.util.Optional;


public interface UserMatzipTagStatusRepository {

    // 저장
    int tagSave(UserMatzipTagStatus userMatzipTagStatus);
    //조회(id와 member를 참고하여 tag를 찾는다..)
    Optional<UserMatzipTagStatus> findTagByIdAndMember(
            Long memberId,
            Long myMatzipId  );
    //단일 태그 조회
    UserMatzipTagStatus findTagByMemberIdAndMatzipId(Long memberId, Long myMatzipId);

    // 유저 태그 조회
    List<UserMatzipTagStatus> findTagsAndMatzipIdByMember(Long memberId);

    //가게태그 조회
    List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId);

    //히든태그 조회
    List<UserMatzipTagStatus> listHiddenMatzipTagIds(Long myMatzipId);

    //kindId 조회
    List<UserMatzipTagStatus> listKindName(Long myMatzipId);

} // end HintPurchaseRepository





