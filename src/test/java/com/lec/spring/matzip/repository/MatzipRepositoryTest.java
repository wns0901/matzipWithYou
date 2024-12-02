package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Matzip;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatzipRepositoryTest {

    @Autowired
    SqlSession sqlSession;

    @Test
    void test1() {
        MatzipRepository matzipRepository = sqlSession.getMapper(MatzipRepository.class);
//        List<Matzip> list = matzipRepository.findAll();
//        System.out.println(list);
        System.out.println(matzipRepository.deleteById(1L));
    }
}