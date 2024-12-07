package com.lec.spring.config;

import com.lec.spring.member.domain.Authority;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.repository.AuthorityRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    @Setter
    private AuthorityRepository authorityRepository;

    @Getter
    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public PrincipalDetails(Member member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();

        List<Authority> list = authorityRepository.findByMember(member);

        for(Authority authority : list){
            collect.add((GrantedAuthority) authority::getName);
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

    private Map<String, Object> attributes;

    @Override
    public String getName(){
        return null;
    }

    @Override
    public Map<String, Object> getAttributes(){
        return this.attributes;
    }
}
