package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.*;
import com.lec.spring.matzip.domain.MyMatzip;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MyMatzipService {

    FindingResultMyMatzipDTO findByMemberId(Long id);

    ResponseEntity<Map<String, String>> updateMyMatzipVisibility (UpdateMyMatzipVisibility updateMyMatzipVisibility);

    ResponseEntity<Map<String, String>> deleteMyMatzip (Long id);

    SeoulMapDataDTO findSeoulMapDataById(Long id);

    DetailMapDataDTO findGuMapDataById(Long id, String gu);

    boolean saveMyMatzip(MyMatzip myMatzip);
}
