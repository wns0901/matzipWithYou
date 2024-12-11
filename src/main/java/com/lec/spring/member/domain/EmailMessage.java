package com.lec.spring.member.domain;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class EmailMessage {
    //수신자
    private String to;
    // 메일 제목
    private String subject;
    // 메일 내용
    private String message;
}
