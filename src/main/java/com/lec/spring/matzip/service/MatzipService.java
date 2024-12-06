package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.Matzip;
import org.springframework.ui.Model;

import java.util.List;

public interface MatzipService {
    int saveMatzip(Matzip matzip, String kind);

    String getImgUrlFromKakao(String kakaoPageUrl);

    Matzip getMatzipById(Long id, Model model);
    //지윤: listKindName
    List<String> listTagName(Long id, Model model);

    //지윤: listKindName
    List<String> listKindName(Long id, Model model);
}
