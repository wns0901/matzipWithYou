package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.UserMatzipTagStatus;
import com.lec.spring.matzip.repository.UserMatzipTagStatusRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserMatzipTagStatusServiceImpl implements UserMatzipTagStatusService {
    private final SqlSession sqlSession;
    private final UserMatzipTagStatusRepository userMatzipTagStatusRepository;

    @Autowired
    public UserMatzipTagStatusServiceImpl(SqlSession sqlSession, UserMatzipTagStatusRepository userMatzipTagStatusRepository) {
        this.sqlSession = sqlSession;
        this.userMatzipTagStatusRepository = userMatzipTagStatusRepository;
    }

    @Override
    public void save(UserMatzipTagStatus userMatzipTagStatus) {
        // 히든 맛집의 태그 리스트 불러오기

        // user_matzip_tag_status에서 존재하는 태크 확인

        // (히든 맛집의 태그 - status에서 저장된 태그) 이중에서 램덤으로 하나 선정

        // UserMatzipTagStatus.setTagId(랜덤으로 선정된 태그)

        // 세이브
        sqlSession.insert("save", userMatzipTagStatus);

        /*todo
        *  getMapper(userMatzipTagStatus.class)
        *  매퍼 인터페이스의 클래스 타입을 나타냄. */

    }

    public Optional<List<UserMatzipTagStatus>> findTagByIdAndMember(Long memberId, Long myMatzipId) {
        List<UserMatzipTagStatus> tags = sqlSession.selectList("isExistTag",
                new HashMap<String, Object>() {{
                    put("memberId", memberId);
                    put("myMatzipId", myMatzipId);
                }});

        return Optional.ofNullable(tags.isEmpty() ? null : tags);
    }



}
