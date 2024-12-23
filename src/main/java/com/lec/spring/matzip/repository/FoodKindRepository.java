package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.FoodKind;

import java.util.List;

public interface FoodKindRepository {
    int save(FoodKind foodKind);

    FoodKind findByKindName(String kindName);

    FoodKind findByKindId(Long id);

    List<FoodKind> findAll();

    int deleteById(Long id);
}
