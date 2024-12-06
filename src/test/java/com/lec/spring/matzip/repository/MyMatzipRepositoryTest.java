package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.MyMatzipDTO;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class  MyMatzipRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void findAll() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<MyMatzipDTO> list = myMatzipRepository.findAll(4L);

        list.forEach(System.out::println);
    }

    @Test
    void findAllOrderByNameAsc() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<MyMatzipDTO> list = myMatzipRepository.findAllOrderByNameAsc(2L);

        System.out.println(list);
    }

    @Test
    void findAllOrderByFoodKindAsc() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<MyMatzipDTO> list = myMatzipRepository.findAllOrderByFoodKindAsc(4L, "일식");

        System.out.println(list);
    }

    @Test
    void findAllOrderByTagAsc() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<MyMatzipDTO> list = myMatzipRepository.findAllOrderByTagAsc(4L, List.of("가성비굿"));

        System.out.println(list);
    }

    @Test
    void updateMatzip() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        int cnt = myMatzipRepository.updatemyMatzipvisibility(4L, "PUBLIC");

        System.out.println(cnt + "개 수정");
        List<MyMatzipDTO> list = myMatzipRepository.findAll(4L);

        System.out.println(list);

    }

    @Test
    void deletemyMatzip() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);

        int cnt = myMatzipRepository.deletemyMatzip(4L);
        System.out.println(cnt + "개 삭제");
        List<MyMatzipDTO> list = myMatzipRepository.findAll(4L);
        System.out.println(list);

    }
}