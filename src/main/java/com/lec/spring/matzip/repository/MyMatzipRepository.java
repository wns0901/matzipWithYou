package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.MyMatzipDTO;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

public interface MyMatzipRepository {
    List<MyMatzipDTO> findAll(Long id);

    List<MyMatzipDTO> findAllOrderByNameAsc(Long id);

    List<MyMatzipDTO> findAllOrderByFoodKindAsc(Long id, String kindName);

    List<MyMatzipDTO> findAllOrderByTagAsc(@Param("id")Long id, @Param("array")List<String> tagName);

    int listCountAll();

    int updatemyMatzipvisibility(Long id, String visibility);

    int deletemyMatzip(Long id);
}
