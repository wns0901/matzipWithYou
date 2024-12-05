package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Matzip;
import org.springframework.ui.Model;

public interface MatzipService {
    int saveMatzip(Matzip matzip, String kind);

    String getImgUrlFromKakao(String kakaoPageUrl);

    Matzip getMatzipById(Long id, Model model);
}
