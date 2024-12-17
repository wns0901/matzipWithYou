package com.lec.spring.member.service;

import org.springframework.stereotype.Service;


public interface EmailAuthService {

    //Redis 저장
    void storeAuthCode(String email,String authCode);

    String getAuthCode(String email);

    void deleteAuthCode(String email);



}
