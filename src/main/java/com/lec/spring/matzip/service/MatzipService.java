package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.DetailMatzipDTO;
import com.lec.spring.matzip.domain.Matzip;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface MatzipService {
    ResponseEntity<Map<String, Object>> saveMatzip(Matzip matzip);

    String getImgUrlFromKakao(String kakaoPageUrl);

    Matzip getMatzipById(Long id);

    ResponseEntity<DetailMatzipDTO> getDetailMatzip(Long matzipId, Long memberId);

    List<String> listTagName(Long id);

    List<String> listKindName(Long id);

    List<Matzip> getAllMatzips();

    int deleteById(Long id);

    void updateMatzipFoodKind(Long matzipId, Long foodKindId);

}
