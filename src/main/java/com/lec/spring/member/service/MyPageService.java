package com.lec.spring.member.service;

import com.lec.spring.member.domain.MyPage;
import com.lec.spring.member.domain.UpdateNickDTO;

public interface MyPageService {

    // 전체 정보
    MyPage getFullMyPageInfo(Long memberId);

    // 닉네임 변경
    int updateNick(Long memberId, UpdateNickDTO updateNickDTO);

}