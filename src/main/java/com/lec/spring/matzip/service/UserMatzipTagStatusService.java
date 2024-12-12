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

   //히든태그 조회
   List<Long> listHiddenMatzipTagIds(Long myMatzipId);

   //kindName 조회
   String listKindName(Long myMatzipId);

//중복삭제
   List<UserMatzipTagStatus> finddeleteDuplicateMyMatzipId();

   //wholeHiddenList 조회
   List<UserMatzipTagStatus> listWholeHiddenList();

   // 중복된 내용 삭제
   List<UserMatzipTagStatus> deleteDuplicateMyMatzipId();

   List<UserMatzipTagStatus> userMatzipTagStatus();


}
