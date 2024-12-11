package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.MyMatzipDTO;
import com.lec.spring.matzip.domain.DTO.SeoulMapDBDataDTO;
import com.lec.spring.matzip.service.MyMatzipService;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class  MyMatzipRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private MyMatzipService myMatzipService;

    @Test
    void findAll() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<MyMatzipDTO> list = myMatzipRepository.findAll(4L);

        list.forEach(System.out::println);
    }

    @Test void testSeoul() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<SeoulMapDBDataDTO> result = myMatzipRepository.findSeoulMapData(1L);

        result.forEach(System.out::println);
    }

    @Test void testSeoulService() {
        myMatzipService.findSeoulMapDataById(1L);
    }
}