package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Matzip;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Map;

public interface MatzipService {
    ResponseEntity<Map<String, String>>  saveMatzip(Matzip matzip, String kind);

    String getImgUrlFromKakao(String kakaoPageUrl);

    Matzip getMatzipById(Long id, Model model);
}
