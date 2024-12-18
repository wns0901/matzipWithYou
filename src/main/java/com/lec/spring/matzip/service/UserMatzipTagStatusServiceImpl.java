package com.lec.spring.matzip.service;


import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public void hintTagSave(Long memberId, Long myMatzipId, Long tagId) {
        // 데이터베이스에 저장하는 로직
        UserMatzipTagStatus status = new UserMatzipTagStatus();
        status.setMemberId(memberId);
        status.setMyMatzipId(myMatzipId);
        status.setTagId(tagId);

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
        return userMatzipTagStatusRepository.listUnpurchasedTagByMemberId(memberId);
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
    public void tagSave(UserMatzipTagStatus userMatzipTagStatus) {
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);
    }// end tagSave





}// userMatzipTagStatusImple
