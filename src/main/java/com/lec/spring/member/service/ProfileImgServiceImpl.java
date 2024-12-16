package com.lec.spring.member.service;

import com.lec.spring.member.domain.ProfileImg;
import com.lec.spring.member.repository.ProfileImgRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileImgServiceImpl implements ProfileImgService {

    private static final String DEFAULT_PROFILE_IMG = "defaultProfileImg.png";
    private ProfileImgRepository profileImgRepository;

    @Autowired
    public ProfileImgServiceImpl(SqlSession sqlSession) {
        profileImgRepository = sqlSession.getMapper(ProfileImgRepository.class);
    }

    @Override
    public int addProfileImg(ProfileImg profileImg) {
        return profileImgRepository.save(profileImg);
    }

    @Override
    public boolean updateProfileImg(ProfileImg profileImg) {
        return profileImgRepository.update(profileImg) > 0;
    }

    @Override
    public boolean deleteProfileImg(Long id) {
        return profileImgRepository.deleteById(id) > 0;
    }

//    @Override
//    public ProfileImg getProfileImg(Long id) {
//
//        return profileImgRepository.findById(id);
//    }
//
//    @Override
//    public ProfileImg getMemberProfileImg(Long memberId) {
//
//        return profileImgRepository.findByMemberId(memberId);
//    }

    @Override
    public ProfileImg getProfileImg(Long id) {
        ProfileImg profileImg = profileImgRepository.findById(id);
        if (profileImg == null) {
            return ProfileImg.builder()
                    .sourcename(DEFAULT_PROFILE_IMG)
                    .filename(DEFAULT_PROFILE_IMG)
                    .isImage(true)
                    .build();
        }
        return profileImg;
    }

    @Override
    public ProfileImg getMemberProfileImg(Long memberId) {
        ProfileImg profileImg = profileImgRepository.findByMemberId(memberId);
        if (profileImg == null) {
            return ProfileImg.builder()
                    .memberId(memberId)
                    .sourcename(DEFAULT_PROFILE_IMG)
                    .filename(DEFAULT_PROFILE_IMG)
                    .isImage(true)
                    .build();
        }
        return profileImg;
    }
}