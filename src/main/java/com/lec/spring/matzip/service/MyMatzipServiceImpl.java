package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.FindingResultMyMatzipDTO;
import com.lec.spring.matzip.repository.MyMatzipRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MyMatzipServiceImpl implements MyMatzipService {


    private final MyMatzipRepository myMatzipRepository;

    public MyMatzipServiceImpl(SqlSession sqlSession) {
        this.myMatzipRepository = sqlSession.getMapper(MyMatzipRepository.class);
    }


    @Override
    public FindingResultMyMatzipDTO findByMemberId(Long id) {
        return FindingResultMyMatzipDTO.builder()
                .cnt(myMatzipRepository.listCntByMemberId(id))
                .list(myMatzipRepository.findAll(id))
                .build();
    }

    @Override
    public ResponseEntity<Map<String, String>> updateMyMatzipVisibility(Long id, String visibility) {
        if(myMatzipRepository.updateMyMatzipVisibility(id, visibility)) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg","수정에 실패했습니다."
                    ));
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteMyMatzip(Long id) {
        if(myMatzipRepository.deleteMyMatzip(id)) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg","삭제에 실패했습니다."
            ));
        }
    }
}
