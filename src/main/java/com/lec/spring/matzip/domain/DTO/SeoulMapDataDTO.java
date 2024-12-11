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
public class SeoulMapDataDTO {
    private ToTalDataDTO toTalData;
    private List<FriendDataDTO> friendData;
}
