package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMatzipTagStatusServiceImpl implements UserMatzipTagStatusService {
    private final UserMatzipTagStatusRepository userMatzipTagStatusRepository;

    @Autowired
    public UserMatzipTagStatusServiceImpl(SqlSession sqlSession) {
        this.userMatzipTagStatusRepository = sqlSession.getMapper(UserMatzipTagStatusRepository.class);
    }

    @Override
    public void tagSave(UserMatzipTagStatus userMatzipTagStatus) {
        // 히든 맛집의 태그 리스트 불러오기

        // user_matzip_tag_status에서 존재하는 태크 확인

        // (히든 맛집의 태그 - status에서 저장된 태그) 이중에서 램덤으로 하나 선정

        // UserMatzipTagStatus.setTagId(랜덤으로 선정된 태그)
//        userMatzipTagStatus.setTagId(7L);
        // 세이브
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);


    }

    public UserMatzipTagStatus findTagByIdAndMember(Long memberId, Long myMatzipId) {
        UserMatzipTagStatus singletags = userMatzipTagStatusRepository.isExistTag(memberId, myMatzipId);

        return singletags;
    }

    @Override
    public List<UserMatzipTagStatus> findTagsByMemberAndMatzip(Long memberId) {
        return userMatzipTagStatusRepository.findTagsByMemberAndMatzip(memberId);
    }

    @Override
    public List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId) {
        return userMatzipTagStatusRepository.findMemberAndTagByMatzipId(myMatzipId);
    }


}
