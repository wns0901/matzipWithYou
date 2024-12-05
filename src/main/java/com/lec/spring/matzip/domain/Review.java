package com.lec.spring.matzip.domain;

import com.lec.spring.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private String content;
    private LocalDateTime regdate;
    private int starRating;
}
