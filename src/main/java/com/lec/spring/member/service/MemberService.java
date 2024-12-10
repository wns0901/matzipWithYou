package com.lec.spring.member.service;

import com.lec.spring.member.domain.Member;

import java.util.List;

public interface MemberService {

    int register(Member member);

    int registerWithReferral(Member member, String referrerNickname);

    boolean isExist(String username);

    boolean isExistNickname(String nickname);

    Member findByUsername(String username);

    Member findByNickname(String nickname);

    List<Member> findAll();

    int updateMember(Member member);

    int updateAdditionalInfo(Long id, String name, String nickname, String email);
}