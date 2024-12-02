package com.lec.spring.member.service;

import com.lec.spring.member.domain.ProfileImg;

public interface ProfileImgService {
    // 프로필 이미지 등록
    int addProfileImg(ProfileImg profileImg);

    // 프로필 이미지 수정
    boolean updateProfileImg(ProfileImg profileImg);

    // 프로필 이미지 삭제
    boolean deleteProfileImg(Long id);

    // 프로필 이미지 조회
    ProfileImg getProfileImg(Long id);

    // 회원의 프로필 이미지 조회
    ProfileImg getMemberProfileImg(Long memberId);
}