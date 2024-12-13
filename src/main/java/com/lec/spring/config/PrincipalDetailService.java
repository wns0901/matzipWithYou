package com.lec.spring.config;

import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.AuthorityRepository;
import com.lec.spring.member.repository.MemberRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;


    public PrincipalDetailService(SqlSession sqlsession){
        this.memberRepository = sqlsession.getMapper(MemberRepository.class);
        this.authorityRepository = sqlsession.getMapper(AuthorityRepository.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);

        if (member != null) {
            PrincipalDetails memberDetails = new PrincipalDetails(member);
            memberDetails.setAuthorityRepository(authorityRepository);
            return memberDetails;
        }

        throw new UsernameNotFoundException(username);
    }
}
