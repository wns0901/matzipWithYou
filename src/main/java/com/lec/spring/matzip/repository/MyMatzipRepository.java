package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.MyMatzipDTO;
import com.lec.spring.matzip.domain.DTO.SeoulMapDBDataDTO;


import java.util.List;

public interface MyMatzipRepository {
    List<MyMatzipDTO> findAll(Long id);

    int listCntByMemberId(Long id);

    boolean updateMyMatzipVisibility(Long id, String visibility);

    boolean deleteMyMatzip(Long id);

    List<SeoulMapDBDataDTO> findSeoulMapData(Long id);
}
