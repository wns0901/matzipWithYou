package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FoodKindService {
    List<FoodKind> getAllFoodKinds();

    int deleteById(Long id);
}
