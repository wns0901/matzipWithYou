package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserMatzipTagStatusService {

   // 저장
   void tagSave(UserMatzipTagStatus userMatzipTagStatus);

   // 단일 태그 조회
   UserMatzipTagStatus findTagByMemberIdAndMatzipId(Long memberId, Long myMatzipId);

   // 새로운 다중 태그 (회원별 태그) 조회
   List<UserMatzipTagStatus> findTagsAndMatzipIdByMember(Long memberId);

   // 가게별 다중 태그 조회
   List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId);

   //List<UserMatzipTagStatus> findTagsByMemberAndMatzip(Long memberId, String tagName, Long tagId);

   //List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId, String tagName, Long tagId);
}
