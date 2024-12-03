package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.MyMatzip;
import com.lec.spring.matzip.domain.MyMatzipDTO;


import java.util.List;

public interface MyMatzipRepository {
    List<MyMatzipDTO> findAll();

    List<MyMatzip> findAllOrderByNameAsc();

    List<MyMatzip> findAllOrderByFoodKindAsc();

    List<MyMatzip> findAllOrderByTagAsc();

    int updateMatzip(String visibility);

    int deletemyMatzip(MyMatzip myMatzip);
}
