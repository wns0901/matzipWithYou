package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.MyMatzipDTO;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class  MyMatzipRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void findAll() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
        List<MyMatzipDTO> list = myMatzipRepository.findAll();

        list.forEach(System.out::println);
    }

    @Test
    void findAllOrderByNameAsc() {
        MyMatzipRepository myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
      List<MyMatzip> list = myMatzipRepository.findAllOrderByNameAsc();

      list.forEach(System.out::println);
    }

    @Test
    void findAllOrderByFoodKindAsc() {
    }

    @Test
    void findAllOrderByTagAsc() {
    }

    @Test
    void updateMatzip() {
    }

    @Test
    void deletemyMatzip() {
    }
}