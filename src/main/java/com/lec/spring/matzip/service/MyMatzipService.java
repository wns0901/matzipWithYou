package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FindingResultMyMatzipDTO;
import com.lec.spring.matzip.domain.MyMatzipDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MyMatzipService {

    FindingResultMyMatzipDTO findByMemberId(Long id);

    ResponseEntity<Map<String, String>> updateMyMatzipVisibility (Long id, String visibility);

    ResponseEntity<Map<String, String>> deleteMyMatzip (Long id);
}
