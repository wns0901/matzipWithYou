package com.lec.spring.matzip.domain.DTO;

import com.lec.spring.member.domain.FriendDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSubmitModalDTO {
    private List<FriendDetailsDTO> hiddenFriends;
    private String topFriendName;
    private int friendCount;
    private int intimacyIncrease;
    private int rewardPoints;
}
