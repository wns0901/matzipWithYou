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
@RequestMapping("/profileImg")
public class ProfileImgController {

    private final ProfileImgService profileImgService;
    private final String uploadDir = "upload";
    private final String DEFAULT_PROFILE_IMG = "defaultProfileImg.png";

    @Autowired
    public ProfileImgController(ProfileImgService profileImgService) {

        this.profileImgService = profileImgService;

        File defaultImg = new File(uploadDir + File.separator + DEFAULT_PROFILE_IMG);

    }

    // 프로필 이미지 업로드 페이지
    @GetMapping("/upload")
    public void upload() {}

    // 프로필 이미지 업로드 처리
    @PostMapping("/upload")
    public String uploadOk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("memberId") Long memberId,
            Model model
    ) {
        // 업로드된 파일이 있다면 처리 시작
        if (file != null && !file.isEmpty()) {
            String sourcename = file.getOriginalFilename();  // 원본 파일명
            String filename = UUID.randomUUID().toString();  // 저장될 파일명

            // 물리적 파일 저장
            File destFile = new File(uploadDir + File.separator + filename);
            try {
                file.transferTo(destFile);  // 파일 저장

                // DB 에 프로필 이미지 정보 저장
                ProfileImg profileImg = ProfileImg.builder()
                        .memberId(memberId)
                        .sourcename(sourcename)
                        .filename(filename)
                        .build();

                model.addAttribute("result", profileImgService.addProfileImg(profileImg));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "profileImg/uploadOk";
    }

    // 프로필 이미지 삭제
    @PostMapping("/delete")
    public String delete(Long id, Model model) {
        // DB 에서 프로필 이미지 정보 조회
        ProfileImg profileImg = profileImgService.getProfileImg(id);
        if (profileImg != null) {
            // 물리적 파일 삭제
            File file = new File(uploadDir + File.separator + profileImg.getFilename());
            if (file.exists()) {
                file.delete();
            }
            // DB 에서 프로필 이미지 정보 삭제
            model.addAttribute("result", profileImgService.deleteProfileImg(id));
        }

        return "profileImg/deleteOk";
    }

    // 프로필 이미지 보여주기
    @GetMapping("/view/{memberId}")
    public String view(@PathVariable Long memberId, Model model) {
        ProfileImg profileImg = profileImgService.getMemberProfileImg(memberId);
        model.addAttribute("profileImg", profileImg);
        return "profileImg/view";
    }
}