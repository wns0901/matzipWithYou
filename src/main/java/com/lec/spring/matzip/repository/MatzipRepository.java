package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Matzip;

import java.util.List;

public interface MatzipRepository {
    int save(Matzip matzip);

    List<Matzip> findAll();

    Matzip findById(Long id);

    int deleteById(Long id);
}
