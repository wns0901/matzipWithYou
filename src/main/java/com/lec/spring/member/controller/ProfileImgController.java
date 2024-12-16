package com.lec.spring.member.controller;

import com.lec.spring.member.domain.ProfileImg;
import com.lec.spring.member.service.ProfileImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ProfileImgController {

    private final ProfileImgService profileImgService;
    private final String uploadDir = "upload";

    @Autowired
    public ProfileImgController(ProfileImgService profileImgService) {
        this.profileImgService = profileImgService;
    }

    // 프로필 이미지 페이지 (GET)
    @GetMapping("/members/{memberId}/profile-img")
    public String profileImagePage(@PathVariable Long memberId, Model model) {
        ProfileImg profileImg = profileImgService.getMemberProfileImg(memberId);
        model.addAttribute("profileImg", profileImg);
        model.addAttribute("memberId", memberId);
        return "member/profileImg";
    }

    // 프로필 이미지 업로드/수정 (POST)
    @PostMapping("/members/{memberId}/profile-img")
    public String updateProfileImage(
            @PathVariable Long memberId,
            @RequestParam("file") MultipartFile file,
            Model model
    ) {
        if (file != null && !file.isEmpty()) {
            ProfileImg currentImg = profileImgService.getMemberProfileImg(memberId);

            // 기존 업로드된 이미지가 있고 default 이미지가 아닌 경우에만 삭제
            if (currentImg != null && !currentImg.getFilename().equals("defaultProfileImg.png")) {
                File oldFile = new File(uploadDir + File.separator + currentImg.getFilename());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 새 파일 저장
            String sourcename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString();
            File destFile = new File(uploadDir + File.separator + filename);

            try {
                file.transferTo(destFile);

                ProfileImg profileImg = ProfileImg.builder()
                        .memberId(memberId)
                        .sourcename(sourcename)
                        .filename(filename)
                        .build();

                if (!currentImg.getFilename().equals("defaultProfileImg.png")) {
                    // 기존 이미지가 default가 아니었다면 update
                    profileImg.setId(currentImg.getId());
                    profileImgService.updateProfileImg(profileImg);
                } else {
                    // default였다면 새로 추가
                    profileImgService.addProfileImg(profileImg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/members/" + memberId + "/profile-img";
    }

    // 프로필 이미지 삭제 (DELETE) - 기본 이미지로 돌아감
    @DeleteMapping("/members/{memberId}/profile-img")
    public String deleteProfileImage(@PathVariable Long memberId) {
        ProfileImg currentImg = profileImgService.getMemberProfileImg(memberId);
        if (!currentImg.getFilename().equals("defaultProfileImg.png")) {
            File file = new File(uploadDir + File.separator + currentImg.getFilename());
            if (file.exists()) {
                file.delete();
            }
            profileImgService.deleteProfileImg(currentImg.getId());
        }
        return "redirect:/members/" + memberId + "/profile-img";
    }
}