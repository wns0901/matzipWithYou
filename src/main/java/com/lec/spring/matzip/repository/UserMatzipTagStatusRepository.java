package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    UserMatzipTagStatus isExistTag(Long memberId, Long myMatzipId);

    // 유저 태그 조회
    List<UserMatzipTagStatus> findTagsByMemberAndMatzip( Long memberId);

    //가게태그 조회
    List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId);

} // end HintPurchaseRepository





