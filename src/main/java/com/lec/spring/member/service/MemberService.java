package com.lec.spring.member.service;

import com.lec.spring.member.domain.Member;

import java.util.List;

public interface MemberService {

    int register(Member member);

    //int registerWithReferral(Member member, String referrerNickname);

    boolean isExist(String username);

    boolean isExistNickname(String nickname);

    Member findByUsername(String username);

    List<Member> findAll();

    int updateMember(Member member);
}