package com.lec.spring.matzip.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FriendDataDTO extends ToTalDataDTO{
    private Long firendId;
    private String nickname;
    private String profileImg;
}
