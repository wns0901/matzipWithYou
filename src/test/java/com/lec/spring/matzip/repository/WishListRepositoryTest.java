package com.lec.spring.matzip.repository;

import com.lec.spring.matzip.domain.WishList;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WishListRepositoryTest {
    @Autowired
    SqlSession sqlSession;

    @Test
    void test() {
        WishListRepository wishListRepository = sqlSession.getMapper(WishListRepository.class);

        WishList wishList = WishList.builder()
                .memberId(1L)
                .matzipId(6L)
                .build();

        wishListRepository.save(wishList);
        System.out.println(wishList);
        wishListRepository.getByMemberId(wishList.getMemberId()).forEach(System.out::println);
        wishListRepository.delete(wishList);
        wishListRepository.getByMemberId(wishList.getMemberId()).forEach(System.out::println);
    }

}