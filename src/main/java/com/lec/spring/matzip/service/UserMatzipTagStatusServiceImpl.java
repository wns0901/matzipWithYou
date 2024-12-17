package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.TagRepository;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println("result:" + result);
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
    public List<UserMatzipTagStatus> hintByMyMatzipId(Long myMatzipID) {
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
    public void hintTagSave(Long memberId, Long myMatzipId, Long tagId) {
        // 데이터베이스에 저장하는 로직
        UserMatzipTagStatus status = new UserMatzipTagStatus();
        status.setMemberId(memberId);
        status.setMyMatzipId(myMatzipId);
        status.setTagId(tagId);

        userMatzipTagStatusRepository.tagSave(status);
    }


    @Override
    public List<UserMatzipTagStatus> shuffleTagNames(List<UserMatzipTagStatus> tagNames) {
        Collections.shuffle(tagNames);  // 리스트 섞기
        return tagNames;
    }

    // 태그 저장
    @Override
    public void tagSave(UserMatzipTagStatus userMatzipTagStatus) {
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);
    }// end tagSave





}// userMatzipTagStatusImple
