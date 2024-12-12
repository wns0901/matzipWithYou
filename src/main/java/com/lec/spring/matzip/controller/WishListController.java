package com.lec.spring.matzip.controller;

import com.lec.spring.matzip.domain.DTO.WishListDTO;
import com.lec.spring.matzip.domain.WishList;
import com.lec.spring.matzip.service.WishListService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/matzips/wish-list/{memberId}")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getWishList(@PathVariable Long memberId) {
        return wishListService.findWishListByMemberId(memberId);
    }

    @PostMapping("")
    public ResponseEntity<Map<String,String>> addWishList(@PathVariable Long memberId, @RequestBody WishList wishList) {
        wishList.setMemberId(memberId);
        return wishListService.add(wishList);
    }

    @DeleteMapping("/{matzipId}")
    public ResponseEntity<Map<String,String>> deleteWishList(@PathVariable Long memberId, @PathVariable Long matzipId) {
        WishList wishList = WishList.builder()
                .memberId(memberId)
                .matzipId(matzipId)
                .build();
        return wishListService.delete(wishList);
    }
}
