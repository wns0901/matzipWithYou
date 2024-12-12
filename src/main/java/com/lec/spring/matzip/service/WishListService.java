package com.lec.spring.matzip.service;

import com.lec.spring.matzip.domain.WishList;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface WishListService {
    ResponseEntity<Map<String, String>> add(WishList wishList);

    ResponseEntity<Map<String, String>> delete(WishList wishList);

    ResponseEntity<Map<String, Object>> findWishListByMemberId(Long id);
}
