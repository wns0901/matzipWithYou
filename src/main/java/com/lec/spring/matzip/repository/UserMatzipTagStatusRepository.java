package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.service.UserMatzipTagStatusService;
import com.lec.spring.member.domain.Member;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Mapper
public interface UserMatzipTagStatusRepository {

    // 저장
    public void save (UserMatzipTagStatus userMatzipTagStatus);
    //조회(id와 member를 참고하여 tag를 찾는다..)
    Optional<UserMatzipTagStatus> findTagByIdAndMember(
            @Param("memberId") Long memberId,
            @Param("myMatzipId") Long myMatzipId  );



} // end HintPurchaseRepository





