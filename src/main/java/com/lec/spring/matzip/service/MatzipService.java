package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Matzip;

public interface MatzipService {
    int saveMatzip(Matzip matzip, String kind);

    String getImgUrlFromKakao(String kakaoPageUrl);
}
