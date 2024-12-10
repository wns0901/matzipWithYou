package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.MyMatzipDTO;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

public interface MyMatzipRepository {
    List<MyMatzipDTO> findAll(Long id);

    int listCntByMemberId(Long id);

    boolean updateMyMatzipVisibility(Long id, String visibility);

    boolean deleteMyMatzip(Long id);
}
