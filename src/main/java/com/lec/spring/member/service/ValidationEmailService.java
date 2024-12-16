package com.lec.spring.member.service;

public interface ValidationEmailService {
    //Auth 관리
    boolean validateAuthCode(String email, String authCode);
}
