package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Authority;
import com.lec.spring.member.domain.Member;

import java.util.List;

public interface AuthorityRepository {
    // 특정 이름(name)의 권한 정보 읽어오기
    Authority findByName(String name);

    // 특정 사용자(Member)의 권한(들) 읽어오기
    List<Authority> findByMember(Member member);

    // 특정 사용자(member_id)에 권한(authorities_id) 추가
    int addAuthority(Long member_id, Long authorities_id);
}