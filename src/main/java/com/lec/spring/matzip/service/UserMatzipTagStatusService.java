package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserMatzipTagStatusService {


   // 저장
   void tagSave(UserMatzipTagStatus userMatzipTagStatus);

   List<UserMatzipTagStatus> userMatzipTagStatus();

   //wholeHiddenList 조회
   List<UserMatzipTagStatus> listWholeHiddenListByMyMatzipId(Long myMatzipId);
   //중복 찾기
   List<UserMatzipTagStatus> findDuplicateMyMatzipId(Long myMatzipID);

   // 중복된 내용 삭제
   List<UserMatzipTagStatus> deleteDuplicateByMyMatzipId(Long myMatzipID);

   // hintmanagement
   List<UserMatzipTagStatus> hintByMyMatzipId(Long myMatzipID);

   void hintTagSave(Long memberId, Long myMatzipId, Long tagId);

   List<UserMatzipTagStatus> shuffleTagNames(List<UserMatzipTagStatus> tagNames) ;




}
