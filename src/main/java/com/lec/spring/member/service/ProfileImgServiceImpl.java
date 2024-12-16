package com.lec.spring.member.service;

import com.lec.spring.member.domain.ProfileImg;
import com.lec.spring.member.repository.ProfileImgRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileImgServiceImpl implements ProfileImgService {

    private final ProfileImgRepository profileImgRepository;
    private static final String DEFAULT_PROFILE_IMG = "defaultProfileImg.png";

    @Autowired
    public ProfileImgServiceImpl(ProfileImgRepository profileImgRepository) {
        this.profileImgRepository = profileImgRepository;
    }

    @Override
    public int addProfileImg(ProfileImg profileImg) {
        return profileImgRepository.save(profileImg);
    }

    @Override
    public boolean updateProfileImg(ProfileImg profileImg) {
        return 1 == profileImgRepository.update(profileImg);
    }

    @Override
    public boolean deleteProfileImg(Long id) {
        return 1 == profileImgRepository.deleteById(id);
    }

    @Override
    public ProfileImg getProfileImg(Long id) {
        return profileImgRepository.findById(id);
    }

    @Override
    public ProfileImg getMemberProfileImg(Long memberId) {
        ProfileImg profileImg = profileImgRepository.findByMemberId(memberId);
        if (profileImg == null) {
            // 프로필 이미지가 없으면 기본 이미지 정보를 가진 ProfileImg 객체 반환
            return ProfileImg.builder()
                    .memberId(memberId)
                    .sourcename(DEFAULT_PROFILE_IMG)
                    .filename(DEFAULT_PROFILE_IMG)  // static/IMG 폴더의 이미지를 사용
                    .build();
        }
        return profileImg;
    }
}