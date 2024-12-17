package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.FoodKind;
import com.lec.spring.matzip.repository.FoodKindRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodKindServiceImpl implements FoodKindService {
    private final FoodKindRepository foodKindRepository;

    @Autowired
    public FoodKindServiceImpl(SqlSession sqlSession) {
        this.foodKindRepository = sqlSession.getMapper(FoodKindRepository.class);
    }

    @Override
    public List<FoodKind> getAllFoodKinds() {
        return foodKindRepository.findAll();
    }
}
