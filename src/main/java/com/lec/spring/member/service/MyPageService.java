package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;

import java.util.List;

public interface MyPageService {

    // 닉네임 변경
    int updateNick(String nickname);

    MyPage getFullMyPageInfo(Long memberId);


}