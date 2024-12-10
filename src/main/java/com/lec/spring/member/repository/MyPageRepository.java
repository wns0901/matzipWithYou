package com.lec.spring.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface MyPageRepository {

    int myProfile();

    int myActsCount();

    int updateNick();




}
