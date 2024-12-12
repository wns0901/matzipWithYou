package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.MatzipDTO;
import com.lec.spring.matzip.domain.DTO.MatzipDataDTO;
import com.lec.spring.matzip.service.MatzipService;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

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
        MatzipDataDTO matzipDataDTO = MatzipDataDTO.builder()
                .lng(37.5308561175718)
                .lat(126.990949104616)
                .name("명동교자 이태원점")
                .kakaoMapUrl("http://place.map.kakao.com/1952478679")
                .address("서울 용산구 녹사평대로 136")
                .gu("용산구")
                .build();

        MatzipDTO kakaoPlaceDTO = MatzipDTO.builder()
                .data(matzipDataDTO)
                .kind("중식")
                .build();

        System.out.println(kakaoPlaceDTO.getData());

        matzipService.saveMatzip(kakaoPlaceDTO.getData(), kakaoPlaceDTO.getKind());

        System.out.println(kakaoPlaceDTO.getData());
    }
}