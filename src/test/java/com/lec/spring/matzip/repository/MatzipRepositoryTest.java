package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.KakaoPlaceDTO;
import com.lec.spring.matzip.domain.Matzip;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MatzipRepositoryTest {
    @LocalServerPort
    int port;

    @Autowired
    SqlSession sqlSession;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void test1() {


    }
}