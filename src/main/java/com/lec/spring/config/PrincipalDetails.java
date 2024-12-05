package com.lec.spring.config;

import com.lec.spring.member.domain.Authority;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.AuthorityRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PrincipalDetails implements UserDetails {

    private AuthorityRepository authorityRepository;

    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    private Member member;
    public Member getMember() {return member;}

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();

        List<Authority> list = authorityRepository.findByMember(member);

        for(Authority authority : list){
            collect.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return authority.getName();
                }

            });
        }

        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
