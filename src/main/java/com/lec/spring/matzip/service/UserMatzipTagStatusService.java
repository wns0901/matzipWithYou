package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   List<UserMatzipTagStatus> deleteDuplicateByMyMatzipId(Long myMatzipId);

   // hintmanagement
   List<UserMatzipTagStatus> hintByMyMatzipId(Long myMatzipId);

   boolean deductPointsForHint(Long memberId, int pointsToDeduct);


//   @Transactional
   void hintTagSave(Long memberId, Long myMatzipId, Long tagId, int pointsToDeduct);

   List<UserMatzipTagStatus> shuffleTagNames(Long myMatzipId); ;




   List<UserMatzipTagStatus> purchasedTag(Long memberId);

   List<UserMatzipTagStatus> unpurchasedTag(Long memberId);

   Long getAuthenticatedMemberId();




}
