package com.lec.spring.member.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EmailAuthServiceImpl implements EmailAuthService {
    private static final String PREFIX = "auth-code:";
    private final RedisTemplate redisTemplate;


    public EmailAuthServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

    }

    // 인증번호 저장
    @Override
    public void storeAuthCode(String email, String authCode) {
        // auth-code:cooconut0226@gmail.com <- 이런식으로 authCode가 작성이 된다.(key)

        try {
            redisTemplate.opsForValue().set(PREFIX + email, authCode, 3, TimeUnit.MINUTES);
            // 값이 성공적으로 설정됨
            System.out.println("성공적으로 저장 되었습니다 " + email + " 인증번호 : " + authCode);
        } catch (Exception e) {
            // 값 설정 실패
            System.out.println("실패: " + e.getMessage());
        }
    }

    // 인증번호 가져오기
    @Override
    public String getAuthCode(String email) {
        String result =(String) redisTemplate.opsForValue().get(PREFIX+email);
        System.out.println("############인증번호 가져오기 :" + result);
        return result;
    }

    // 인증번호 삭제
    @Override
    public void deleteAuthCode(String email) {
       Boolean result = redisTemplate.delete(PREFIX+email);
        System.out.println("##########인증번호가 삭제 됐나요 ? : " + result);

    }



    // email auth end
}
