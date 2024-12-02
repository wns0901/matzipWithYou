package com.lec.spring.member.repository;

import com.lec.spring.member.domain.ProfileImg;

public interface ProfileImgRepository {
    // 프로필 이미지 저장
    int save(ProfileImg profileImg);

    // 프로필 이미지 수정
    int update(ProfileImg profileImg);

    // 프로필 이미지 삭제
    int deleteById(Long id);

    // 특정 id의 프로필 이미지 조회
    ProfileImg findById(Long id);

    // 특정 회원의 프로필 이미지 조회
    ProfileImg findByMemberId(Long memberId);
}