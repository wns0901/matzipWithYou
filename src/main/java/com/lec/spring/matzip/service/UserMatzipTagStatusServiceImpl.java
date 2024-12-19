package com.lec.spring.matzip.service;


import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import com.lec.spring.member.repository.MyPageRepository;
import com.lec.spring.member.service.MyPageService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserMatzipTagStatusServiceImpl implements UserMatzipTagStatusService {
    private final UserMatzipTagStatusRepository userMatzipTagStatusRepository;
    private final TagRepository tagRepository;


    @Autowired
    public UserMatzipTagStatusServiceImpl(SqlSession sqlSession) {
        this.userMatzipTagStatusRepository = sqlSession.getMapper(UserMatzipTagStatusRepository.class);
        this.tagRepository = sqlSession.getMapper(TagRepository.class);

    }

    @Override
    public List<UserMatzipTagStatus> userMatzipTagStatus() {
        return userMatzipTagStatusRepository.userMatzipTagStatus();
    }

    @Override
    public List<UserMatzipTagStatus> listWholeHiddenListByMyMatzipId(Long myMatzipId) {
        List<UserMatzipTagStatus> result = userMatzipTagStatusRepository.listWholeHiddenListByMyMatzipId(myMatzipId);
        System.out.println("#############listWholeHidden:" + result);
        return result;
    }

    ////////////////////// my Matzip 기준 중복 찾고 제거하기
    @Override
    public List<UserMatzipTagStatus> findDuplicateMyMatzipId(Long myMatzipID) {
        List<UserMatzipTagStatus> duplicateId = userMatzipTagStatusRepository.findDuplicateMyMatzipId(myMatzipID);
        System.out.println("################중복된 id " + duplicateId.size());
        return duplicateId;
    }

    @Override
    public List<UserMatzipTagStatus> deleteDuplicateByMyMatzipId(Long myMatzipID) {
        // 숨겨진 맛집 리스트
        List<UserMatzipTagStatus> hiddenList = userMatzipTagStatusRepository.listWholeHiddenListByMyMatzipId(myMatzipID);

        // 중복된 맛집 리스트
        List<UserMatzipTagStatus> duplicateList = userMatzipTagStatusRepository.findDuplicateMyMatzipId(myMatzipID);


        // 중복 제거 로직
        List<UserMatzipTagStatus> resultList = new ArrayList<>(hiddenList);
        resultList.removeAll(duplicateList);

        System.out.println("****************중복 제거된 결과: " + resultList.size());
        return resultList;
    }

    @Override
    public List<UserMatzipTagStatus> hintByMyMatzipId(Long myMatzipId) {
        // 숨겨진 맛집 리스트
        List<UserMatzipTagStatus> hiddenList = userMatzipTagStatusRepository.listWholeHiddenListByMyMatzipId(myMatzipId);

        // 중복된 맛집 리스트
        List<UserMatzipTagStatus> duplicateList = userMatzipTagStatusRepository.findDuplicateMyMatzipId(myMatzipId);

        // 중복 제거 로직
        List<UserMatzipTagStatus> resultList = new ArrayList<>(hiddenList);
        resultList.removeAll(duplicateList);

      System.out.println("****************중복 제거된 결과: " + resultList.size());
        return resultList;
    }


    public boolean deductPointsForHint(Long memberId, int pointsToDeduct) {
        // 포인트 차감 쿼리 호출 (int 반환)
        int rowsAffected = userMatzipTagStatusRepository.deductPointsForHint(memberId, pointsToDeduct);
        // 차감된 행 수가 0이면 포인트가 부족하여 차감되지 않은 경우
        return rowsAffected > 0;
    }



    @Override
    @Transactional
    public void hintTagSave(Long memberId, Long myMatzipId, Long tagId, int pointsToDeduct) {
        // 현재 포인트 조회
        int currentPoint = userMatzipTagStatusRepository.getCurrentPoint(memberId);
        System.out.println("현재 포인트 : " + currentPoint);

        // 포인트 차감 처리
        boolean isPointsDeducted = deductPointsForHint(memberId, pointsToDeduct);

        // 포인트 차감 성공 여부 확인
        if (!isPointsDeducted) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }

        // 포인트 차감 후 업데이트된 포인트 확인
        int updatedPoint = userMatzipTagStatusRepository.getCurrentPoint(memberId);
        System.out.println("차감 후 포인트 : " + updatedPoint);

        // 힌트 저장
        UserMatzipTagStatus status = new UserMatzipTagStatus();
        status.setMemberId(memberId);
        status.setMyMatzipId(myMatzipId);
        status.setTagId(tagId);

        // 힌트 정보를 데이터베이스에 저장
        userMatzipTagStatusRepository.tagSave(status);
    }




    @Override
    public List<UserMatzipTagStatus> shuffleTagNames(Long myMatzipId) {
        List<UserMatzipTagStatus> tagNames = hintByMyMatzipId(myMatzipId); // ID로 리스트 가져오기
        Collections.shuffle(tagNames);  // 리스트 섞기
        return tagNames;


}




    //구매된 태그(userMatzipTagStatus)에 등록된 태그
    // 형식(tagId, tagName, myMatzipID)
    @Override
    public List<UserMatzipTagStatus> purchasedTag(Long memberId) {
        return userMatzipTagStatusRepository.listpurchasedTagByMemberId(memberId);
    }

    // 힌트태그(구매 안된태그)
    @Override
    public List<UserMatzipTagStatus> unpurchasedTag(Long memberId) {
        List<UserMatzipTagStatus> hidden = userMatzipTagStatusRepository.listWholeHiddenMatizpByMemberId();
        System.out.println("hidden:" + hidden);
        List<UserMatzipTagStatus> purchasedList = userMatzipTagStatusRepository.listpurchasedTagByMemberId(memberId);
        System.out.println("purchasedList:" + purchasedList);
        // 중복 제거 로직
        List<UserMatzipTagStatus> resultList = new ArrayList<>(hidden);
        resultList.removeAll(purchasedList);

        System.out.println("****************중복 제거된 결과: " + resultList.size());

        return resultList;
    }

    @Override
    public Long getAuthenticatedMemberId() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // authentication이 null이 아니고, PrincipalDetails가 로그인된 사용자 정보인 경우
        if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            // memberId 가져오기
            Long memberId = principalDetails.getMember().getId();
            System.out.println("##########memberId:" + memberId);
            return memberId;
        }

        // 인증되지 않은 경우나 다른 예외 처리
        throw new IllegalStateException("로그인된 사용자 정보가 없습니다.");
    }



    // 태그 저장
    @Override
    @Transactional
    public void tagSave(UserMatzipTagStatus userMatzipTagStatus) {
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);
    }// end tagSave





}// userMatzipTagStatusImple
