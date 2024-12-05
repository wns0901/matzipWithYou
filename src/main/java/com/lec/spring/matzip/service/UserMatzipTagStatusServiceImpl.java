package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import com.lec.spring.matzip.service.UserMatzipTagStatusService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserMatzipTagStatusServiceImpl implements UserMatzipTagStatusService {
    private final UserMatzipTagStatusRepository userMatzipTagStatusRepository;

    @Autowired
    public UserMatzipTagStatusServiceImpl(SqlSession sqlSession) {
        this.userMatzipTagStatusRepository = sqlSession.getMapper(UserMatzipTagStatusRepository.class);
    }



    @Override
    public void tagSave(UserMatzipTagStatus userMatzipTagStatus) {
        //1. 히든 맛집의 태그 리스트 불러오기
        //(5,6,1) -> 히든 맛집

        //2. 제가 만든 메소드에 찾은 태드 id 보내주기 (list<long>




        // 세이브
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);



    }// end tagSave

    public UserMatzipTagStatus findTagByMemberIdAndMatzipId(Long memberId, Long myMatzipId) {
        return userMatzipTagStatusRepository.findTagByMemberIdAndMatzipId(memberId, myMatzipId);
    }// end findTagByMemberIdAndMatzipId

    @Override
    public List<UserMatzipTagStatus> findTagsAndMatzipIdByMember(Long memberId) {
        return userMatzipTagStatusRepository.findTagsAndMatzipIdByMember(memberId);
    }// end findTAgsAndMatzipIdByMember

    @Override
    public List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId) {
        return userMatzipTagStatusRepository.findMemberAndTagByMatzipId(myMatzipId);
    }// end findMemberAndTagByMatzipId

    @Override
    public List<Long> listHiddenMatzipTagIds(Long myMatzipId) {
        // myMatzipId에 해당하는 히든 맛집의 태그 상태를 조회합니다.
        List<UserMatzipTagStatus> hiddenTags = userMatzipTagStatusRepository.listHiddenMatzipTagIds(myMatzipId, "HIDDEN");

        // UserMatzipTagStatus 객체에서 ID만 추출하여 리스트로 변환
        List<Long> tagIds = hiddenTags.stream()
                .map(UserMatzipTagStatus::getTagId) // getTagId()는 ID를 반환하는 메서드로 가정
                .collect(Collectors.toList());

        System.out.println("히든 맛집의 태그 ID: " + tagIds);

        return tagIds;


    }

}// userMatzipTagStatusImple
