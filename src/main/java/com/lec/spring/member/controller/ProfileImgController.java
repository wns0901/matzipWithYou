package com.lec.spring.member.controller;

import com.lec.spring.member.domain.ProfileImg;
import com.lec.spring.member.service.ProfileImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
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

    @PatchMapping("/members/{memberId}/profile-img")
    @Transactional
    @ResponseBody
    public ResponseEntity<?> updateProfileImage(
            @PathVariable Long memberId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file provided");
            }

            // 현재 이미지 정보 가져오기
            ProfileImg profileImg = profileImgService.getMemberProfileImg(memberId);

            // 새 파일 정보 준비
            String sourcename = file.getOriginalFilename();
            String fileExtension = sourcename.substring(sourcename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + fileExtension;


            // ID 유무와 상관없이 파일이 defaultProfileImg인 경우는 새로 생성
            if (profileImg != null && profileImg.getId() != null && !profileImg.getFilename().equals("defaultProfileImg.png")) {
                // 기존 파일 삭제 (실제 파일이 있는 경우에만)
                File oldFile = new File(uploadDir, profileImg.getFilename());
                if (oldFile.exists()) {
                    oldFile.delete();
                }

                // 기존 레코드 업데이트
                profileImg.setSourcename(sourcename);
                profileImg.setFilename(newFilename);
                profileImgService.updateProfileImg(profileImg);
            } else {
                // defaultProfileImg이거나 ID가 없는 경우는 새로 생성
                ProfileImg newProfileImg = ProfileImg.builder()
                        .memberId(memberId)
                        .sourcename(sourcename)
                        .filename(newFilename)
                        .build();
                profileImgService.addProfileImg(newProfileImg);
            }


            // 새 파일 저장
            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile.toPath());

            return ResponseEntity.ok(newFilename);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    // 프로필 이미지 삭제 (DELETE)
    @DeleteMapping("/members/{memberId}/profile-img")
    @ResponseBody
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long memberId) {
        try {
            ProfileImg currentImg = profileImgService.getMemberProfileImg(memberId);
            if (currentImg != null && !currentImg.getFilename().equals("defaultProfileImg.png")) {
                File file = new File(uploadDir, currentImg.getFilename());
                if (file.exists()) {
                    file.delete();
                }
                profileImgService.deleteProfileImg(currentImg.getId());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("No image to delete");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete image: " + e.getMessage());
        }
    }
}
