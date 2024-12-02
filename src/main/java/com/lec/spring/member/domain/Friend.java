package com.lec.spring.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friend {

    private Long senderId;
    private Long receiverId;
    private int intimacy = 0;
    private boolean isAccept = false;
    private LocalDateTime regdate;
}
