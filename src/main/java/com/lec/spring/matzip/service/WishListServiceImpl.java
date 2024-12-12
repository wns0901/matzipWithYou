package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.DTO.WishListDTO;
import com.lec.spring.matzip.domain.WishList;
import com.lec.spring.matzip.repository.WishListRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;

    public WishListServiceImpl(SqlSession sqlSession) {
        this.wishListRepository = sqlSession.getMapper(WishListRepository.class);
    }

    @Override
    public ResponseEntity<Map<String, String>> add(WishList wishList) {
        if(wishListRepository.save(wishList)) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg","추가에 실패했습니다."
            ));
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> delete(WishList wishList) {
        if(wishListRepository.delete(wishList)) {
            return ResponseEntity.ok(Map.of("status", "SUCCESS"));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "FAIL",
                    "msg","삭제에 실패했습니다."
            ));
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> findWishListByMemberId(Long id) {
        List<WishListDTO> result = wishListRepository.getByMemberId(id);
        Map<String, Object> res = new HashMap<>();
        if(result == null) {
            res.put("status", "FAIL");
            return ResponseEntity.notFound().build();
        }
        res.put("status", "SUCCESS");
        res.put("data", result);
        return ResponseEntity.ok(res);
    }
}
