package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Member;

import java.util.List;

public interface MemberRepository {
    int save(Member member);

    int update(Member member);

    int deleteById(Long id);

    List<Member> findAll();

    Member findById(Long id);

    Member findByUsername(String username);

    Member findByNickname(String nickname);

    Member findByEmail(String email);

    int updatePoint(Long id, Integer point);

    String findMemberIdByEmail(String email);

    int updatePassword(String email, String newPassword);


}//end MemberRepository

