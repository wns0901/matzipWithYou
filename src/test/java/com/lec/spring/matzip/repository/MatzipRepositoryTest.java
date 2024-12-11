package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.KakaoPlaceDTO;
import com.lec.spring.matzip.domain.Matzip;
import com.lec.spring.matzip.domain.MatzipDTO;
import com.lec.spring.matzip.service.MatzipService;
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

    @Autowired
    private MatzipService matzipService;
    @Test
    void test1() {
        MatzipDTO matzipDTO = MatzipDTO.builder()
                .lng(37.5308561175718)
                .lat(126.990949104616)
                .name("명동교자 이태원점")
                .kakaoMapUrl("http://place.map.kakao.com/1952478679")
                .address("서울 용산구 녹사평대로 136")
                .gu("용산구")
                .build();

        KakaoPlaceDTO kakaoPlaceDTO = KakaoPlaceDTO.builder()
                .kakao(matzipDTO)
                .kind("중식")
                .build();

        System.out.println(kakaoPlaceDTO.getKakao());

        matzipService.saveMatzip(kakaoPlaceDTO.getKakao(), kakaoPlaceDTO.getKind());

        System.out.println(kakaoPlaceDTO.getKakao());
    }
}