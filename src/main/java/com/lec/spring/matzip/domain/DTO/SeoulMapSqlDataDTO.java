package com.lec.spring.matzip.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeoulMapSqlDataDTO {
    private Long friendId;
    private String nickname;
    private String profileImg;
    private List<String> publicGu;
    private List<String> hiddenGu;
}
