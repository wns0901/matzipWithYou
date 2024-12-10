package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImg {
    private Long id;        // 프로필 사진 일련번호
    private Long memberId;  // 회원 일련번호
    private String sourcename;  // 원본 이름
    private String filename;    // 저장 이름

    private boolean isImage;    // 이미지 파일 여부
}