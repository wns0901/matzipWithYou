package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.FindingResultMyMatzipDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MyMatzipService {

    FindingResultMyMatzipDTO findByMemberId(Long id);

    ResponseEntity<Map<String, String>> updateMyMatzipVisibility (Long id, String visibility);

    ResponseEntity<Map<String, String>> deleteMyMatzip (Long id);
}
