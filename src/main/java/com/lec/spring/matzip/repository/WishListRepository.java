package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.DTO.WishListDTO;
import com.lec.spring.matzip.domain.WishList;

import java.util.List;

public interface WishListRepository {
    boolean save(WishList wishList);

    List<WishListDTO> getByMemberId(long id);

    List<WishList> getWishListByMemberId(long id);

    boolean delete(WishList wishList);
}
