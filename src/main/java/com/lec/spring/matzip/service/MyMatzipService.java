package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.DetailMapDataDTO;
import com.lec.spring.matzip.domain.DTO.FindingResultMyMatzipDTO;
import com.lec.spring.matzip.domain.DTO.FriendDataWithMatzipDTO;
import com.lec.spring.matzip.domain.DTO.SeoulMapDataDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MyMatzipService {

    FindingResultMyMatzipDTO findByMemberId(Long id);

    ResponseEntity<Map<String, String>> updateMyMatzipVisibility (Long id, String visibility);

    ResponseEntity<Map<String, String>> deleteMyMatzip (Long id);

    SeoulMapDataDTO findSeoulMapDataById(Long id);

    DetailMapDataDTO findGuMapDataById(Long id, String gu);
}
