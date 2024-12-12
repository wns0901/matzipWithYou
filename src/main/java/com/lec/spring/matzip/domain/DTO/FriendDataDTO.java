package com.lec.spring.matzip.domain.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FriendDataDTO extends ToTalDataDTO{
    private Long firendId;
    private String nickname;
    private String profileImg;
}
