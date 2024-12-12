package com.lec.spring.member.repository;

import com.lec.spring.matzip.domain.Tag;
import com.lec.spring.member.domain.Member;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface MemberRepository {
    int save(Member member);

    int update(Member member);

    int deleteById(Long id);

    List<Member> findAll();

    Member findById(Long id);

    List<Member> findByIds(List<Long> ids);

    Member findByUsername(String username);

    Member findByNickname(String nickname);

    Member findByEmail(String email);
  
    int updatePoint(@Param("id") Long id, @Param("point") Integer point);

    int updateAdditionalInfo(@Param("id") Long id,
                             @Param("name") String name,
                             @Param("nickname") String nickname,
                             @Param("email") String email);


    int updatePassword(Long id, String newPassword);


}//end MemberRepository




