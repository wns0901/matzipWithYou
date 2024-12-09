package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.FoodKind;

import java.util.List;

public interface FoodKindRepository {
    int save();

    FoodKind findByKindName(String kindName);

    List<FoodKind> findAll();

    int deleteById(Long id);
}
