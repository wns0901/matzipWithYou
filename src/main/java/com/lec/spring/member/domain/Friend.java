package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friend {
    private Long senderId;
    private Long receiverId;
    private Integer intimacy;
    private Boolean isAccept;
    private LocalDateTime regdate;
}
