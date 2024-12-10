package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.Matzip;

import java.util.List;

public interface MatzipRepository {
    boolean save(Matzip matzip);

    List<Matzip> findAll();

    Matzip findByName(String name);

    Matzip findById(Long id);

    int deleteById(Long id);
}
