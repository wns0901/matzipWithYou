package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.FriendDataWithMatzipDTO;
import com.lec.spring.matzip.domain.DTO.MatzipListDataDTO;
import com.lec.spring.matzip.domain.DTO.MyMatzipDTO;
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
        System.out.println("#".repeat(20) + "test" + "#".repeat(20));
        myMatzipRepository.findSeoulMapData(1L).forEach(System.out::println);
        System.out.println("#".repeat(20) + "test" + "#".repeat(20));
    }

    @Test void testSeoulService() {
        myMatzipService.findSeoulMapDataById(1L);
    }

    @Test void testGuData() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        MatzipListDataDTO test = MatzipListDataDTO.builder()
                .name("오늘런치뷔페")
                .build();

        System.out.println("#".repeat(20) + "test" + "#".repeat(20));
        List<FriendDataWithMatzipDTO> result = myMatzipRepository.findGuMapData(1L, "강남구");
        result.forEach(System.out::println);
        System.out.println(result.get(0).getMatzipList().get(0).equals(test));
        System.out.println("#".repeat(20) + "test" + "#".repeat(20));
    }
}