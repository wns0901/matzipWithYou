package com.lec.spring.matzip.domain;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodKind {
    private Long id;
    private String kindName;
    private LocalDateTime regdate;
}
