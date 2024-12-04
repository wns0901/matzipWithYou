package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Tag;
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

        //(7)번 태그기 usermatziptagstatus에 존재함
        // 7번 맛집을 제외하고 2번 9번 태그 중 랜덤으로 1개 부여
        // 만약 랜덤으로 정해진 태그를 2번이라고 하자
        // 2번을 usermatziptagstatus에 넣고 저장한다.

        // 히든 맛집의 태그 리스트 불러오기 5번 맛집
        List<Tag> tagList = List.of(
                Tag.builder().id(2L).tagName("느낌 좋은").build(),
                Tag.builder().id(7L).tagName("양 많은").build(),
                Tag.builder().id(9L).tagName("가성비굿").build()
        );
        // user_matzip_tag_status에서 존재하는 태크 확인

        // (히든 맛집의 태그 - status에서 저장된 태그) 이중에서 램덤으로 하나 선정

        // UserMatzipTagStatus.setTagId(랜덤으로 선정된 태그)

        // 세이브
        userMatzipTagStatusRepository.tagSave(userMatzipTagStatus);

        // 랜덤으로 정한 하나의 태그 출력
        System.out.println();
    }

    public UserMatzipTagStatus findTagByMemberIdAndMatzipId(Long memberId, Long myMatzipId) {
        UserMatzipTagStatus singletags = userMatzipTagStatusRepository.findTagByMemberIdAndMatzipId(memberId, myMatzipId);

        return singletags;
    }

    @Override
    public List<UserMatzipTagStatus> findTagsAndMatzipIdByMember(Long memberId) {
        return userMatzipTagStatusRepository.findTagsAndMatzipIdByMember(memberId);
    }

    @Override
    public List<UserMatzipTagStatus> findMemberAndTagByMatzipId(Long myMatzipId) {
        return userMatzipTagStatusRepository.findMemberAndTagByMatzipId(myMatzipId);
    }


}
