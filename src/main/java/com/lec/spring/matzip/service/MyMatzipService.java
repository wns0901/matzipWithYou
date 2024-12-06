package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.MyMatzipDTO;

import java.util.List;

public interface MyMatzipService {

    List<MyMatzipDTO> findSeries(Long id, String query, String kindName, List<String> tagName );

    int myMatzipvisibilityUpdate (Long id, String visibility);

    int myMatzipDelete (Long id);

    int myMatzipListAll();

}
