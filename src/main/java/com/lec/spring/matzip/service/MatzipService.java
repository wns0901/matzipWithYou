package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Matzip;

public interface MatzipService {
    int saveMatzip(Matzip matzip);

    String getImgUrlFromKakao(Long kakaoPlaceId);

    int deleteMatzip(Long id);

    int findById(Long id);

}
