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
//        MatzipRepository matzipRepository = sqlSession.getMapper(MatzipRepository.class);
//        List<Matzip> list = matzipRepository.findAll();
//        System.out.println(list);
//
//        Matzip matzip = matzipRepository.findById(1L);
//        System.out.println(matzip);
//
//        matzip = matzipRepository.findByName("치킨공식");
//        System.out.println(matzip);

        KakaoPlaceDTO kakaoPlaceDTO = KakaoPlaceDTO.builder()
                .lng(37.5308561175718)
                .lat(126.990949104616)
                .name("명동교자 이태원점")
                .kakaoMapUrl("http://place.map.kakao.com/1952478679")
                .address("서울 용산구 녹사평대로 136")
                .build();


        ResponseEntity<String> res = restTemplate.postForEntity( "http://localhost:" + port +"/matzip/test", kakaoPlaceDTO,String.class);

        System.out.println(res.getBody());
    }
}