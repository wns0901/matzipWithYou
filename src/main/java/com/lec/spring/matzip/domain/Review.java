package com.lec.spring.matzip.domain;

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
    private int id;
    private Text content;
    private LocalDateTime regdate;
    private int star_rating;
    private boolean is_deleted = false;
}
