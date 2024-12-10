package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Tag;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagRepositoryTest {
    @LocalServerPort
    int port;

    @Autowired
    SqlSession sqlSession;

    @Test
    void test() {
        TagRepository tagRepository = sqlSession.getMapper(TagRepository.class);
        List<Tag> tags = tagRepository.findNonMatchingTags(List.of(7L, 9L), 5L);

        System.out.println(tags);
    }
}