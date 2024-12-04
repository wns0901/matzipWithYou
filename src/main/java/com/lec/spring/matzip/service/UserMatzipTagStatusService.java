package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserMatzipTagStatusService {


//    // 저장
   void tagSave(UserMatzipTagStatus userMatzipTagStatus);

//    단일태그조회
   UserMatzipTagStatus findTagByIdAndMember(Long memberId, Long myMatzipId);
   // 새로운 다중 태그 (회원별태그) 조회
   public List<UserMatzipTagStatus> findTagsByMemberAndMatzip(Long memberId);
   // 가게별 다중태그 조회
   public List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId);



}
