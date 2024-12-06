package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.MyMatzipDTO;
import com.lec.spring.matzip.repository.MyMatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyMatzipServiceImpl implements MyMatzipService {


    private final MyMatzipRepository myMatzipRepository;

    public MyMatzipServiceImpl(SqlSession sqlSession) {
        this.myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
    }


    @Override
    public List<MyMatzipDTO> findSeries(Long id, String query, String kindName, List<String> tagName) {


        return switch (query) {
            case "nameAsc" -> myMatzipRepository.findAllOrderByNameAsc(id);
            case "foodKindAsc" -> myMatzipRepository.findAllOrderByFoodKindAsc(id, kindName);
            case "tagAsc" -> myMatzipRepository.findAllOrderByTagAsc(id, tagName);
            default -> myMatzipRepository.findAll(id);
        };

    }

    @Override
    public int myMatzipvisibilityUpdate(Long id, String visibility) {
        int result = 0;
        result = myMatzipRepository.updatemyMatzipvisibility(id, visibility);
        return result;
    }

    @Override
    public int myMatzipDelete(Long id) {
        int result = 0;
        result = myMatzipRepository.deletemyMatzip(id);
        return result;
    }

    @Override
    public int myMatzipListAll() {
        int result = 0;
        result = myMatzipRepository.listCountAll();
        return result;
    }
}
